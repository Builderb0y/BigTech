package builderb0y.bigtech.api;

import java.util.Objects;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.damageTypes.BigTechDamageTypes;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;
import builderb0y.bigtech.lightning.LightningPulseInteractors;
import builderb0y.bigtech.util.BlockApiLookups;
import builderb0y.bigtech.util.Directions;

/**
a block which does something special when hit by a lightning pulse.
the primary method in this class is {@link #onPulse(ServerWorld, LinkedBlockPos, BlockState, LightningPulse)},
but other secondary methods are also available to fine-tune
how the pulse spreads into and out of this block.
register blocks with {@link #LOOKUP}.

register blocks with {@link #LOOKUP} OR implement
this interface on your Block class. either works.
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
		1: fromInteractor {@link #canConductOut(WorldView, BlockPos, BlockState, Direction)} of the provided direction,
		2: toInteractor {@link #canConductIn(WorldView, BlockPos, BlockState, Direction)} from the opposite of the provided direction, and
		3: fromState's {@link #getConductionShape(WorldView, BlockPos, BlockState, Direction)} touches toState's conduction state.
	which is a fancy of saying the two blocks can conduct with each other, and touch each other.
	*/
	public static boolean canConductThrough(
		WorldView world,
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
			toInteractor  .canConductIn (world,   toPos,   toState, fromFromToTo.getOpposite()) &&
			VoxelShapes.matchesAnywhere(
				fromInteractor.getConductionShape(world, fromPos, fromState, fromFromToTo),
				toInteractor  .getConductionShape(world,   toPos, toState, fromFromToTo.getOpposite()),
				BooleanBiFunction.AND
			)
		);
	}

	/**
	returns the shape used for checking whether or not this block touches an adjacent one.
	*/
	public default VoxelShape getConductionShape(WorldView world, BlockPos pos, BlockState state, Direction face) {
		return state.getOutlineShape(world, pos).getFace(face);
	}

	/**
	every block traversed will subtract this number from the number of remaining traversable blocks.
	when the number of remaining traversable blocks reaches 0, the pulse stops spreading.
	*/
	public default float getResistance(WorldView world, BlockPos pos, BlockState state, Direction side) {
		return 1.0F;
	}

	/**
	returns true if a lightning pulse can spread to this block, false otherwise.

	the provided side may be null if the pulse is not coming from an adjacent block.
	for example, lightning beams use a null direction when hitting something at a diagonal angle.

	use {@link #onPulse(ServerWorld, LinkedBlockPos, BlockState, LightningPulse)} for that instead.
	*/
	public default boolean canConductIn(WorldView world, BlockPos pos, BlockState state, @Nullable Direction side) {
		return true;
	}

	/**
	returns true if this block can spread to an adjacent block, false otherwise.

	use {@link #onPulse(ServerWorld, LinkedBlockPos, BlockState, LightningPulse)} for that instead.
	*/
	public default boolean canConductOut(WorldView world, BlockPos pos, BlockState state, Direction side) {
		return true;
	}

	/**
	returns true if this block is intended to actively consume lightning energy.
	this implies 2 things:
		1: only the lightning paths that lead to sinks will receive the pulse
			see {@link #onPulse(ServerWorld, LinkedBlockPos, BlockState, LightningPulse)}.
		2: this block will not attempt to spread to other adjacent blocks by default.
			see {@link #spreadOut(ServerWorld, LinkedBlockPos, BlockState, LightningPulse)}.

	use {@link #onPulse(ServerWorld, LinkedBlockPos, BlockState, LightningPulse)} for that instead.
	*/
	public default boolean isSink(WorldView world, BlockPos pos, BlockState state) {
		return false;
	}

	/**
	called when an adjacent block is actively attempting to spread into this block.

	this method should not modify the provided world.
	use {@link #onPulse(ServerWorld, LinkedBlockPos, BlockState, LightningPulse)} for that instead.
	*/
	public default void spreadIn(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		pulse.addNode(pos);
		if (this.isSink(world, pos, state)) {
			pulse.addSink(pos);
		}
	}

	/**
	called when this block is scheduled to spread to adjacent blocks.
	this method can be overridden to delegate to
	{@link #forceSpreadOut(ServerWorld, LinkedBlockPos, BlockState, LightningPulse)}
	if this block is a sink that wishes to continue spreading instead of being an "end point".

	this method should not modify the provided world.
	use {@link #onPulse(ServerWorld, LinkedBlockPos, BlockState, LightningPulse)} for that instead.
	*/
	public default void spreadOut(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		if (!this.isSink(world, pos, state)) {
			this.forceSpreadOut(world, pos, state, pulse);
		}
	}

	/**
	called by {@link #spreadOut(ServerWorld, LinkedBlockPos, BlockState, LightningPulse)}
	if this block should spread to adjacent blocks.
	this method performs the spreading logic for all directions which
	{@link #canConductOut(WorldView, BlockPos, BlockState, Direction)}.

	this method should not modify the provided world.
	use {@link #onPulse(ServerWorld, LinkedBlockPos, BlockState, LightningPulse)} for that instead.
	*/
	public default void forceSpreadOut(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		for (Direction direction : Directions.ALL) {
			if (!this.canConductOut(world, pos, state, direction)) continue;
			float resistance = this.getResistance(world, pos, state, direction);
			if (!(resistance <= pos.distanceRemaining)) continue;
			LinkedBlockPos adjacentPos = pos.offset(direction, resistance);
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
	{@link LightningPulse#originPos source} to {@link #isSink(WorldView, BlockPos, BlockState) sink},
	including on the source and sink themselves.
	alternatively, if, after spreading, no sinks have been found,
	all explored blocks will have a pulse spread through them.
	this method may modify the provided world however it wants to.
	*/
	public abstract void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse);

	public default ActionResult interactWithBattery(
		World world,
		BlockPos pos,
		BlockState state,
		PlayerEntity player,
		ItemStack stack,
		LightningStorageItem battery
	) {
		int charge = battery.getCharge(stack);
		if (charge > 0) {
			if (world instanceof ServerWorld serverWorld) {
				new LightningPulse(serverWorld, pos, state, this, charge, battery.getDefaultSpreadEvents(stack)).run();
				if (player == null || !player.isCreative()) {
					battery.setCharge(stack, 0);
				}
			}
			return ActionResult.SUCCESS;
		}
		else {
			return ActionResult.PASS;
		}
	}

	/**
	damages all entities within 1 block of the provided position with lightning damage.
	the damage dealt to each entity is the pulse's {@link LightningPulse#totalEnergy} divided by 1000.
	*/
	public static void shockEntitiesAround(ServerWorld world, BlockPos pos, BlockState state, LightningPulse pulse) {
		for (
			Entity entity : world.getNonSpectatingEntities(
				Entity.class,
				new Box(
					pos.getX() - 1,
					pos.getY() - 1,
					pos.getZ() - 1,
					pos.getX() + 2,
					pos.getY() + 2,
					pos.getZ() + 2
				)
			)
		) {
			LightningPulse.shockEntity(
				world,
				entity,
				pulse.totalEnergy / 1000.0F,
				new DamageSource(
					world
					.getRegistryManager()
					.getOrThrow(RegistryKeys.DAMAGE_TYPE)
					.getOrThrow(BigTechDamageTypes.SHOCKING)
				)
			);
		}
	}

	public static void spawnLightningParticles(World world, BlockPos pos, BlockState state, LightningPulse pulse) {
		world.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, pos, -1);
	}

	public static final Object INITIALIZER = new Object() {{
		LOOKUP.registerForBlocks(
			BlockApiLookups.constant(LightningPulseInteractors.TNT),
			Blocks.TNT
		);
		LOOKUP.registerForBlocks(
			BlockApiLookups.constant(LightningPulseInteractors.LIGHTNING_ROD),
			Blocks.LIGHTNING_ROD
		);
		LOOKUP.registerForBlocks(
			BlockApiLookups.constant(LightningPulseInteractors.MOB_SPAWNER),
			Blocks.SPAWNER
		);
		LOOKUP.registerFallback(
			(World world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Void context) -> {
				if (state.getBlock() instanceof LightningPulseInteractor interactor) {
					return interactor;
				}
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