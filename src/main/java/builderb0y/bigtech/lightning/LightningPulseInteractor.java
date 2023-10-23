package builderb0y.bigtech.lightning;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;

import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;
import builderb0y.bigtech.util.Enums;

public interface LightningPulseInteractor {

	/**
	returns true if all 3 of the following conditions are met:
		1: fromInteractor {@link #canConductOut(WorldAccess, BlockPos, BlockState, Direction)} of the provided direction,
		2: toInteractor {@link #canConductIn(WorldAccess, BlockPos, BlockState, Direction)} from the opposite of the provided direction, and
		3: fromState's {@link BlockState#getCullingFace(BlockView, BlockPos, Direction)} touches toSTate's culling face.
	which is a fancy of saying the two blocks can conduct with each other, and touch each other.
	*/
	public static boolean canConductThrough(
		WorldAccess world,
		LightningPulseInteractor fromInteractor,
		BlockPos fromPos,
		BlockState fromState,
		LightningPulseInteractor toInteractor,
		BlockPos toPos,
		BlockState toState,
		Direction fromFromToTo
	) {
		return (
			fromInteractor.canConductOut(world, fromPos, fromState, fromFromToTo) &&
			toInteractor  .canConductIn (world,   toPos,   toState, fromFromToTo.opposite) &&
			VoxelShapes.matchesAnywhere(
				fromState.getCullingFace(world, fromPos, fromFromToTo),
				toState  .getCullingFace(world,   toPos, fromFromToTo.opposite),
				BooleanBiFunction.AND
			)
		);
	}

	/**
	returns true if an adjacent block can spread to this block, false otherwise.

	this method should not modify the provided world.
	use {@link #onPulse(World, LinkedBlockPos, BlockState, LightningPulse)} for that instead.
	*/
	public default boolean canConductIn(WorldAccess world, BlockPos pos, BlockState state, Direction side) {
		return true;
	}

	/**
	returns true if this block can spread to an adjacent block, false otherwise.

	this method should not modify the provided world.
	use {@link #onPulse(World, LinkedBlockPos, BlockState, LightningPulse)} for that instead.
	*/
	public default boolean canConductOut(WorldAccess world, BlockPos pos, BlockState state, Direction side) {
		return true;
	}

	/**
	returns true if this block is intended to actively consume lightning energy.
	this implies 2 things:
		1: only the lightning paths that lead to sinks will receive the pulse
			see {@link #onPulse(World, LinkedBlockPos, BlockState, LightningPulse)}.
		2: this block will not attempt to spread to other adjacent blocks by default.
			see {@link #spreadOut(World, LinkedBlockPos, BlockState, LightningPulse)}.

	this method should not modify the provided world.
	use {@link #onPulse(World, LinkedBlockPos, BlockState, LightningPulse)} for that instead.
	*/
	public default boolean isSink(World world, BlockPos pos, BlockState state) {
		return false;
	}

	/**
	called when an adjacent block is actively attempting to spread into this block.

	this method should not modify the provided world.
	use {@link #onPulse(World, LinkedBlockPos, BlockState, LightningPulse)} for that instead.
	*/
	public default void spreadIn(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		pulse.addNode(pos);
		if (this.isSink(world, pos, state)) {
			pulse.addSink(pos);
		}
	}

	/**
	called when this block is scheduled to spread to adjacent blocks.
	this method can be overridden to delegate to
	{@link #forceSpreadOut(World, LinkedBlockPos, BlockState, LightningPulse)}
	if this block is a sink that wishes to continue spreading instead of being an "end point".

	this method should not modify the provided world.
	use {@link #onPulse(World, LinkedBlockPos, BlockState, LightningPulse)} for that instead.
	*/
	public default void spreadOut(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		if (!this.isSink(world, pos, state)) {
			this.forceSpreadOut(world, pos, state, pulse);
		}
	}

	/**
	called by {@link #spreadOut(World, LinkedBlockPos, BlockState, LightningPulse)}
	if this block should spread to adjacent blocks.
	this method performs the spreading logic for all directions which
	{@link #canConductOut(WorldAccess, BlockPos, BlockState, Direction)}.

	this method should not modify the provided world.
	use {@link #onPulse(World, LinkedBlockPos, BlockState, LightningPulse)} for that instead.
	*/
	public default void forceSpreadOut(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		for (Direction direction : Enums.DIRECTIONS) {
			LinkedBlockPos adjacentPos = pos.offset(direction);
			if (pulse.hasNode(adjacentPos)) continue;
			BlockState adjacentState = world.getBlockState(adjacentPos);
			LightningPulseInteractor adjacentInteractor = LightningPulseInteractors.forBlock(world, adjacentPos, adjacentState);
			if (canConductThrough(world, this, pos, state, adjacentInteractor, adjacentPos, adjacentState, direction)) {
				adjacentInteractor.spreadIn(world, adjacentPos, adjacentState, pulse);
			}
		}
	}

	/**
	called after spreading logic has finished for all blocks that are along a path from
	{@link LightningPulse#originPos source} to {@link #isSink(World, BlockPos, BlockState) sink},
	including on the source and sink themselves.
	alternatively, if, after spreading, no sinks have been found,
	all explored blocks will have a pulse spread through them.
	this method may modify the provided world however it wants to.
	*/
	public abstract void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse);

	/**
	damages all entities within 1 block of the provided position with lightning damage.
	the damage dealt to each entity is the pulse's {@link LightningPulse#totalEnergy} divided by 100.
	*/
	public default void shockEntitiesAround(World world, BlockPos pos, BlockState state, LightningPulse pulse) {
		for (Entity entity : world.getNonSpectatingEntities(Entity.class, new Box(pos.x - 1, pos.y - 1, pos.z - 1, pos.x + 2, pos.y + 2, pos.z + 2))) {
			entity.damage(entity.damageSources.lightningBolt(), pulse.totalEnergy / 100.0F);
		}
	}

	public default void spawnLightningParticles(World world, BlockPos pos, BlockState state, LightningPulse pulse) {
		world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, pos, -1);
	}
}