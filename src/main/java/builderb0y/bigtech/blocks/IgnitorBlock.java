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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.IgnitorBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.WorldHelper;

public class IgnitorBlock extends Block implements BlockEntityProvider {

	/**
	entity collision logic skips the ground, so we need to slightly
	contract the bounding box for entities to be able to collide properly.
	*/
	public static final VoxelShape COLLISION_SHAPE = VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 1.0D, 0.999D, 1.0D);

	public static final MapCodec<IgnitorBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public IgnitorBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.LIT, Boolean.FALSE)
		);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return COLLISION_SHAPE;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
		super.onEntityCollision(state, world, pos, entity, handler);
		if (world instanceof ServerWorld serverWorld) {
			IgnitorBlockEntity ignitor = WorldHelper.getBlockEntity(world, pos, IgnitorBlockEntity.class);
			if (ignitor != null) ignitor.onEntityCollision(serverWorld, entity);
		}
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
	public @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return world.getBlockEntity(pos) instanceof IgnitorBlockEntity blockEntity ? blockEntity : null;
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.HORIZONTAL_FACING, context.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new IgnitorBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient ? null : IgnitorBlockEntity.SERVER_TICKER.as();
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(Properties.HORIZONTAL_FACING, mirror.apply(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING, Properties.LIT);
	}
}