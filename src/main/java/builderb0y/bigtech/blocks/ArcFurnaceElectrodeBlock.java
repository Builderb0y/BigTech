package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;

import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.blockEntities.CrucibleBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;

public class ArcFurnaceElectrodeBlock extends Block implements LightningPulseInteractor {

	@SuppressWarnings("rawtypes")
	public static final MapCodec CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public static final VoxelShape
		X_RAYTRACE_SHAPE = VoxelShapes.union(
			VoxelShapes.cuboid(0.25D,    0.0D,  0.0D,   0.75D,   0.75D, 1.0D   ),
			VoxelShapes.cuboid(0.25D,    0.75D, 0.25D,  0.75D,   1.0D,  0.75D  )
		),
		Z_RAYTRACE_SHAPE = VoxelShapes.union(
			VoxelShapes.cuboid(0.0D,     0.0D,  0.25D,  1.0D,    0.75D, 0.75D  ),
			VoxelShapes.cuboid(0.25D,    0.75D, 0.25D,  0.75D,   1.0D,  0.75D  )
		),
		X_SHAPE = VoxelShapes.union(
			VoxelShapes.cuboid(0.25D,    0.0D,  0.0D,   0.75D,   0.25D, 0.125D ),
			VoxelShapes.cuboid(0.25D,    0.0D,  0.875D, 0.75D,   0.25D, 1.0D   ),
			VoxelShapes.cuboid(0.25D,    0.25D, 0.0D,   0.75D,   0.75D, 1.0D   ),
			VoxelShapes.cuboid(0.25D,    0.75D, 0.25D,  0.75D,   1.0D,  0.75D  ),
			VoxelShapes.cuboid(0.4375D, -0.5D,  0.4375, 0.5625D, 0.25D, 0.5625D)
		),
		Z_SHAPE = VoxelShapes.union(
			VoxelShapes.cuboid(0.0D,    0.0D,  0.25D,   0.125D,  0.25D, 0.75D  ),
			VoxelShapes.cuboid(0.875D,  0.0D,  0.25D,   1.0D,    0.25D, 0.75D  ),
			VoxelShapes.cuboid(0.0D,    0.25D, 0.25D,   1.0D,    0.75D, 0.75D  ),
			VoxelShapes.cuboid(0.25D,   0.75D, 0.25D,   0.75D,   1.0D,  0.75D  ),
			VoxelShapes.cuboid(0.4375, -0.5D,  0.4375D, 0.5625D, 0.25D, 0.5625D)
		);

	public ArcFurnaceElectrodeBlock(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch (state.get(Properties.HORIZONTAL_FACING).getAxis()) {
			case X -> X_SHAPE;
			case Z -> Z_SHAPE;
			case Y -> throw new AssertionError();
		};
	}

	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return switch (state.get(Properties.HORIZONTAL_FACING).getAxis()) {
			case X -> X_RAYTRACE_SHAPE;
			case Z -> Z_RAYTRACE_SHAPE;
			case Y -> throw new AssertionError();
		};
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.HORIZONTAL_FACING, context.getHorizontalPlayerFacing());
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moved) {
		super.onBlockAdded(state, world, pos, oldState, moved);
		BlockPos downPos = pos.down();
		if (world.getBlockState(downPos).isOf(Blocks.CAULDRON)) {
			world.setBlockState(downPos, FunctionalBlocks.CRUCIBLE.getDefaultState());
		}
	}

	@Override
	public void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
		super.onStateReplaced(state, world, pos, moved);
		BlockPos downPos = pos.down();
		if (world.getBlockState(downPos).isOf(FunctionalBlocks.CRUCIBLE)) {
			world.setBlockState(downPos, Blocks.CAULDRON.getDefaultState());
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean moved) {
		super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, moved);
		BlockPos downPos = pos.down();
		if (world.getBlockState(downPos).isOf(Blocks.CAULDRON)) {
			world.setBlockState(downPos, FunctionalBlocks.CRUCIBLE.getDefaultState());
		}
	}

	@Override
	public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		LightningPulseInteractor.spawnLightningParticles(world, pos, state, pulse);
		LightningPulseInteractor.shockEntitiesAround(world, pos, state, pulse);
		BlockPos down = pos.down();
		if (world.getBlockEntity(down) instanceof CrucibleBlockEntity crucible) {
			crucible.onLightningPulse(world, pulse);
			LightningPulseInteractor.spawnLightningParticles(world, down, state, pulse);
			LightningPulseInteractor.shockEntitiesAround(world, down, state, pulse);
		}
	}

	@Override
	public boolean canConductIn(WorldView world, BlockPos pos, BlockState state, @Nullable Direction side) {
		Direction forward = state.get(Properties.HORIZONTAL_FACING);
		return side == Direction.UP || side == forward.rotateYClockwise() || side == forward.rotateYCounterclockwise();
	}

	@Override
	public boolean canConductOut(WorldView world, BlockPos pos, BlockState state, Direction side) {
		Direction forward = state.get(Properties.HORIZONTAL_FACING);
		return side == Direction.UP || side == forward.rotateYClockwise() || side == forward.rotateYCounterclockwise();
	}

	@Override
	public void spreadOut(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		this.forceSpreadOut(world, pos, state, pulse);
	}

	@Override
	public boolean isSink(WorldView world, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING);
	}
}