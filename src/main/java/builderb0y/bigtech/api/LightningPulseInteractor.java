package builderb0y.bigtech.api;

import java.util.Objects;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.entity.BlockEntity;
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

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;
import builderb0y.bigtech.lightning.LightningPulseInteractors;
import builderb0y.bigtech.util.Enums;

/**
a block which does something special when hit by a lightning pulse.
the primary method in this class is {@link #onPulse(World, LinkedBlockPos, BlockState, LightningPulse)},
but other secondary methods are also available to fine-tune
how the pulse spreads into and out of this block.
register blocks with {@link #LOOKUP}.
*/
public interface LightningPulseInteractor {

	public static final BlockApiLookup<LightningPulseInteractor, Void> LOOKUP = BlockApiLookup.get(BigTechMod.modID("lightning_pulse_interactor"), LightningPulseInteractor.class, Void.class);

	public static LightningPulseInteractor get(World world, BlockPos pos) {
		return Objects.requireNonNullElse(LOOKUP.find(world, pos, null), LightningPulseInteractors.INSULATOR);
	}

	public static LightningPulseInteractor get(World world, BlockPos pos, BlockState state) {
		return Objects.requireNonNullElse(LOOKUP.find(world, pos, state, null, null), LightningPulseInteractors.INSULATOR);
	}

	public static LightningPulseInteractor get(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		return Objects.requireNonNullElse(LOOKUP.find(world, pos, state, blockEntity, null), LightningPulseInteractors.INSULATOR);
	}

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
			LightningPulseInteractor adjacentInteractor = get(world, adjacentPos, adjacentState);
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
	the damage dealt to each entity is the pulse's {@link LightningPulse#totalEnergy} divided by 1000.
	*/
	public default void shockEntitiesAround(World world, BlockPos pos, BlockState state, LightningPulse pulse) {
		for (Entity entity : world.getNonSpectatingEntities(Entity.class, new Box(pos.x - 1, pos.y - 1, pos.z - 1, pos.x + 2, pos.y + 2, pos.z + 2))) {
			entity.damage(entity.damageSources.lightningBolt(), pulse.totalEnergy / 1000.0F);
		}
	}

	public default void spawnLightningParticles(World world, BlockPos pos, BlockState state, LightningPulse pulse) {
		world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, pos, -1);
	}

	public static final Object INITIALIZER = new Object() {{
		LOOKUP.registerForBlocks(
			(world, pos, state, blockEntity, context) -> LightningPulseInteractors.TNT,
			Blocks.TNT
		);
		LOOKUP.registerForBlocks(
			(world, pos, state, blockEntity, context) -> LightningPulseInteractors.LIGHTNING_ROD,
			Blocks.LIGHTNING_ROD
		);
		LOOKUP.registerFallback(
			(world, pos, state, blockEntity, context) -> {
				if (state.getBlock() instanceof Oxidizable) {
					return LightningPulseInteractors.OXIDIZABLE;
				}
				if (state.isIn(BigTechBlockTags.CONDUCTS_LIGHTNING)) {
					return (
						state.isIn(BigTechBlockTags.SHOCKS_ENTITIES)
						? LightningPulseInteractors.SHOCKING_CONDUCTOR
						: LightningPulseInteractors.INSULATED_CONDUCTOR
					);
				}
				return null;
			}
		);
	}};
}