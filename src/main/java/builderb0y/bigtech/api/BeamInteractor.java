package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.*;
import builderb0y.bigtech.mixins.BeaconBlockEntity_LevelGetter;
import builderb0y.bigtech.util.BlockApiLookups;

/**
a block which does something when a beam hits it,
or modifies a beam passing through it.

register blocks with {@link #LOOKUP} OR implement
this interface on your Block class. either works.
*/
public interface BeamInteractor {

	public static final BlockApiLookup<BeamInteractor, Beam> LOOKUP = BlockApiLookup.get(BigTechMod.modID("beam_interactor"), BeamInteractor.class, Beam.class);

	/**
	a simple implementation of BeamInteractor which
	allows beams to simply pass through the block,
	even if they normally wouldn't. this interactor is used
	for {@link Blocks#GLASS} and {@link Blocks#GLASS_PANE}.
	*/
	public static final BeamInteractor TRANSPARENT_BLOCK = (ServerWorld world, BlockPos pos, BlockState state, SpreadingBeamSegment inputSegment) -> {
		inputSegment.beam().addSegment(world, inputSegment.extend());
		return true;
	};

	/**
	called while the beam is still in its spreading phase, before it is added to the world.
	this method may be used to modify a beam segment passing through it.
	for example, to change its color or direction or distance remaining.

	the {@link Beam} instance can be obtained via {@link SpreadingBeamSegment#beam}.
	this can be used to add new segments to the beam's queue
	via {@link Beam#addSegment(ServerWorld, SpreadingBeamSegment)}.

	the world should be used for querying other blocks or block entities only.
	this method should NOT modify the world.
	the reason for this is that the beam may not be added to the world after spreading finishes,
	and it is not desirable to have the world be modified by "phantom" beams.

	if this block wishes to do something to the world when a beam passes
	through it, consider registering a {@link BeamCallback} to {@link #LOOKUP},
	instead of just a BeamInteractor.

	return true if this interceptor did its job and added all the segments it wishes to add.
	return false to fallback on the beam's default logic that it would use for non-interceptor blocks.
	*/
	public abstract boolean spreadOut(ServerWorld world, BlockPos pos, BlockState state, SpreadingBeamSegment segment);

	/**
	an extension of BeamInteractor with additional callback
	methods for what to do when a beam which passes through
	this block is added to the world or removed from the world.

	registration works the same way as with BeamInteractor.
	since this interface extends BeamInteractor,
	beams will check for this interface via instanceof.
	*/
	public static interface BeamCallback extends BeamInteractor {

		/**
		called after a persistent beam is added to the world.
		the world can be modified in any way the callback wants.
		*/
		public abstract void onBeamAdded(ServerWorld world, BlockPos pos, BlockState state, PersistentBeam beam);

		/**
		called after a persistent beam is removed from the world.
		the world can be modified in any way the callback wants.
		*/
		public abstract void onBeamRemoved(ServerWorld world, BlockPos pos, BlockState state, PersistentBeam beam);

		/**
		called after a pulse beam has finished calculating its spread.
		the world can be modified in any way the callback wants.
		*/
		public abstract void onBeamPulse(ServerWorld world, BlockPos pos, BlockState state, PulseBeam beam);
	}

	public static final Object INITIALIZER = new Object() {{
		LOOKUP.registerForBlocks(BlockApiLookups.constant(TRANSPARENT_BLOCK), Blocks.GLASS, Blocks.GLASS_PANE);
		LOOKUP.registerForBlocks(
			(World world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Beam context) -> {
				if (blockEntity instanceof BeaconBlockEntity_LevelGetter beacon) {
					int extraDistance = beacon.bigtech_getLevel() << 5;
					return (ServerWorld serverWorld, BlockPos pos1, BlockState state1, SpreadingBeamSegment inputSegment) -> {
						if (extraDistance == 0) {
							inputSegment.beam().addSegment(serverWorld, inputSegment.terminate());
						}
						else {
							inputSegment.beam().addSegment(serverWorld, inputSegment.extend(inputSegment.distanceRemaining() + extraDistance, BeamDirection.UP));
						}
						return true;
					};
				}
				return null;
			},
			Blocks.BEACON
		);
		LOOKUP.registerFallback((World world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Beam beam) -> {
			if (state.getBlock() instanceof BeamInteractor interactor) {
				return interactor;
			}
			BeaconBeamColorProvider provider = BeaconBeamColorProvider.LOOKUP.find(world, pos, state, blockEntity, null);
			if (provider != null) {
				int color = provider.getBeaconColor(world, pos, state);
				if (color != 0) {
					Vector3f actualColor = new Vector3f(
						BeaconBeamColorProvider.getRed(color),
						BeaconBeamColorProvider.getGreen(color),
						BeaconBeamColorProvider.getBlue(color)
					)
					.div(255.0F);
					return (ServerWorld serverWorld, BlockPos pos1, BlockState state1, SpreadingBeamSegment inputSegment) -> {
						if (inputSegment.segment().color() == null || inputSegment.segment().color().equals(actualColor)) {
							inputSegment.beam().addSegment(serverWorld, inputSegment.withColor(actualColor).extend());
						}
						else {
							inputSegment.beam().addSegment(serverWorld, inputSegment.terminate());
						}
						return true;
					};
				}
			}
			return null;
		});
	}};
}