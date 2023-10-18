package builderb0y.bigtech.blocks.belts;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class RedstoneReceivingBeltBlock extends DirectionalBeltBlock {

	public RedstoneReceivingBeltBlock(Settings settings) {
		super(settings);
		this.defaultState = this.defaultState.with(Properties.POWERED, Boolean.FALSE);
	}

	/**
	doesn't actually emit redstone, but this method controls
	whether or not redstone dust will connect to this block.
	*/
	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	public boolean shouldBePowered(World world, BlockPos pos, BlockState state) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		if (world.getEmittedRedstonePower(mutable.set(pos, Direction.DOWN), Direction.DOWN) > 0) return true;
		Direction forward = state.get(Properties.HORIZONTAL_FACING);
		if (world.getEmittedRedstonePower(mutable.set(pos, forward), forward) > 0) return true;
		if (world.getEmittedRedstonePower(mutable.set(pos, forward.rotateYClockwise()), forward.rotateYClockwise()) > 0) return true;
		if (world.getEmittedRedstonePower(mutable.set(pos, forward.rotateYCounterclockwise()), forward.rotateYCounterclockwise()) > 0) return true;
		return world.getEmittedRedstonePower(mutable.set(pos, forward.opposite), forward.opposite) > 0;
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		BlockState state = super.getPlacementState(context);
		return state.with(Properties.POWERED, this.shouldBePowered(context.world, context.blockPos, state));
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
		BlockState newState = state.with(Properties.POWERED, this.shouldBePowered(world, pos, state));
		if (state != newState) {
			world.setBlockState(pos, newState);
		}
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}
}