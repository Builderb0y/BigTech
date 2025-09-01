package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;

import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.MicroProcessorBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.gui.screenHandlers.CircuitDebuggerScreenHandler;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.util.WorldHelper;

public class MicroProcessorBlock extends Block implements BlockEntityProvider {

	@SuppressWarnings("rawtypes")
	public static final MapCodec CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public static final VoxelShape SHAPE = VoxelShapes.union(
		VoxelShapes.cuboidUnchecked(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D),
		VoxelShapes.cuboidUnchecked(0.1875D, 0.125D, 0.1875D, 0.8125D, 0.1875D, 0.8125D)
	);

	public MicroProcessorBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (stack.isOf(FunctionalItems.MAGNIFYING_GLASS)) {
			if (!world.isClient) {
				MicroProcessorBlockEntity processor = WorldHelper.getBlockEntity(world, pos, MicroProcessorBlockEntity.class);
				if (processor != null && processor.circuit != null) {
					player.openHandledScreen(
						new SimpleNamedScreenHandlerFactory(
							(
								int syncId,
								PlayerInventory playerInventory,
								PlayerEntity player_
							)
							-> new CircuitDebuggerScreenHandler(
								syncId,
								() -> processor.circuit,
								playerInventory
							),
							processor.getName()
						)
					);
				}
			}
			return ActionResult.SUCCESS;
		}
		return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
	}

	@Override
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		MicroProcessorBlockEntity processor = WorldHelper.getBlockEntity(world, pos, MicroProcessorBlockEntity.class);
		return processor != null ? processor.getRedstoneOutput(direction.getOpposite()) : 0;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		MicroProcessorBlockEntity processor = WorldHelper.getBlockEntity(world, pos, MicroProcessorBlockEntity.class);
		return processor != null ? processor.getRedstoneOutput(direction.getOpposite()) : 0;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
		super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
		MicroProcessorBlockEntity processor = WorldHelper.getBlockEntity(world, pos, MicroProcessorBlockEntity.class);
		if (processor != null) processor.wakeup();
	}

	@Override
	public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
		ItemStack stack = super.getPickStack(world, pos, state, includeData);
		MicroProcessorBlockEntity processor = WorldHelper.getBlockEntity(world, pos, MicroProcessorBlockEntity.class);
		if (processor != null) {
			stack.applyComponentsFrom(processor.createComponentMap());
		}
		return stack;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MicroProcessorBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type == BigTechBlockEntityTypes.MICRO_PROCESSOR ? MicroProcessorBlockEntity.TICKER.as() : null;
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.HORIZONTAL_FACING, context.getHorizontalPlayerFacing());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
	}
}