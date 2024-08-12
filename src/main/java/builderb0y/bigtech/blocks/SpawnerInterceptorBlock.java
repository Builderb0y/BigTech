package builderb0y.bigtech.blocks;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.SpawnerInterceptorBlockEntity;
import builderb0y.bigtech.util.WorldHelper;

public class SpawnerInterceptorBlock extends Block implements BlockEntityProvider {

	public static final VoxelShape SHAPE = VoxelShapes.union(
		VoxelShapes.cuboidUnchecked(0.0625D, 0.0D,    0.0625D, 0.9375D, 0.1875D, 0.9375D),
		VoxelShapes.cuboidUnchecked(0.1875D, 0.1875D, 0.1875D, 0.3125D, 0.25D,   0.3125D),
		VoxelShapes.cuboidUnchecked(0.6875D, 0.1875D, 0.1875D, 0.8125D, 0.25D,   0.3125D),
		VoxelShapes.cuboidUnchecked(0.1875D, 0.1875D, 0.6875D, 0.3125D, 0.25D,   0.8125D),
		VoxelShapes.cuboidUnchecked(0.6875D, 0.1875D, 0.6875D, 0.8125D, 0.25D,   0.8125D)
	);

	static {
		ItemStorage.SIDED.registerForBlocks(
			(
				World world,
				BlockPos pos,
				BlockState state,
				@Nullable BlockEntity blockEntity,
				@Nullable Direction side
			)
			-> {
				if (world.getBlockEntity(pos.up()) instanceof SpawnerInterceptorBlockEntity interceptor) {
					return InventoryStorage.of(interceptor.inventory, side);
				}
				else {
					return null;
				}
			},
			Blocks.SPAWNER
		);
		UseBlockCallback.EVENT.register(
			(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) -> {
				BlockPos upPos;
				BlockState upState;
				if (
					!player.shouldCancelInteraction() &&
					player.getMainHandStack().isEmpty() &&
					world.getBlockState(hitResult.getBlockPos()).isOf(Blocks.SPAWNER) &&
					(upState = world.getBlockState(upPos = hitResult.getBlockPos().up())).isOf(FunctionalBlocks.SPAWNER_INTERCEPTOR)
				) {
					return upState.onUse(world, player, hitResult.withBlockPos(upPos));
				}
				else {
					return ActionResult.PASS;
				}
			}
		);
	}

	public SpawnerInterceptorBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(Properties.POWERED, Boolean.FALSE));
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean moved) {
		super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, moved);
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = (
			world.getBlockState(mutable.set(pos, Direction.DOWN)).getBlock() == Blocks.SPAWNER && (
				world.getStrongRedstonePower(mutable.set(pos, Direction.DOWN).move(Direction.NORTH), Direction.NORTH) > 0 ||
				world.getStrongRedstonePower(mutable.set(pos, Direction.DOWN).move(Direction.SOUTH), Direction.SOUTH) > 0 ||
				world.getStrongRedstonePower(mutable.set(pos, Direction.DOWN).move(Direction.EAST ), Direction.EAST ) > 0 ||
				world.getStrongRedstonePower(mutable.set(pos, Direction.DOWN).move(Direction.WEST ), Direction.WEST ) > 0 ||
				world.getStrongRedstonePower(mutable.set(pos, Direction.DOWN).move(Direction.DOWN ), Direction.DOWN ) > 0
			)
		);
		if (powered != shouldBePowered) world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient) {
			NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
			if (factory != null) player.openHandledScreen(factory);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!newState.isOf(this)) {
			SpawnerInterceptorBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, SpawnerInterceptorBlockEntity.class);
			if (blockEntity != null) ItemScatterer.spawn(world, pos, blockEntity.inventory);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SpawnerInterceptorBlockEntity(pos, state);
	}

	@Override
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return world.getBlockEntity(pos) instanceof NamedScreenHandlerFactory factory ? factory : null;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}
}