package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
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
	public static final BeamInteractor TRANSPARENT_BLOCK = (SpreadingBeamSegment inputSegment, BlockState state) -> {
		inputSegment.beam.addSegment(inputSegment.extend());
		return true;
	};

	/**
	called while the beam is still in its spreading phase, before it is added to the world.
	this method may be used to modify a beam segment passing through it.
	for example, to change its color or direction or distance remaining.

	the {@link Beam} instance can be obtained via {@link BeamSegment#beam}.
	this can be used to add new segments to the beam's queue
	via {@link Beam#addSegment(SpreadingBeamSegment)}.

	the world may be obtained via {@link SpreadingBeamSegment#beam} and {@link Beam#world},
	but obtaining the world this way should be reserved for querying other
	blocks or block entities only. this method should NOT modify the world.
	the reason for this is that the beam may not be added to the world after spreading finishes,
	and it is not desirable to have the world be modified by "phantom" beams.

	if this block wishes to do something to the world when a beam passes
	through it, consider registering a {@link BeamCallback} to {@link #LOOKUP},
	instead of just a BeamInteractor.
	*/
	public abstract boolean spreadOut(SpreadingBeamSegment segment, BlockState state);

	/**
	an extension of BeamInteractor with additional callback
	methods for what to do when a beam which passes through this block
	is added to the world or removed from the world.

	registration works the same way as with BeamInteractor.
	since this interface extends BeamInteractor,
	beams will check for this interface via instanceof.
	*/
	public static interface BeamCallback extends BeamInteractor {

		/**
		called after a persistent beam is added to the world.
		the world reference may be obtained via {@link Beam#world},
		and the world can be modified in any way the callback wants.
		*/
		public abstract void onBeamAdded(BlockPos pos, BlockState state, PersistentBeam beam);

		/**
		called after a persistent beam is removed from the world.
		the world reference may be obtained via {@link Beam#world}.
		and the world can be modified in any way the callback wants.
		*/
		public abstract void onBeamRemoved(BlockPos pos, BlockState state, PersistentBeam beam);

		/**
		called after a pulse beam has finished calculating its spread.
		the world reference may be obtained via {@link Beam#world}.
		and the world can be modified in any way the callback wants.
		*/
		public abstract void onBeamPulse(BlockPos pos, BlockState state, PulseBeam beam);
	}

	public static final Object INITIALIZER = new Object() {{
		LOOKUP.registerForBlocks(BlockApiLookups.constant(TRANSPARENT_BLOCK), Blocks.GLASS, Blocks.GLASS_PANE);
		LOOKUP.registerForBlocks(
			(World world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Beam context) -> {
				if (blockEntity instanceof BeaconBlockEntity_LevelGetter beacon) {
					int extraDistance = beacon.bigtech_getLevel() << 5;
					return (SpreadingBeamSegment inputSegment, BlockState state1) -> {
						if (extraDistance == 0) {
							inputSegment.beam.addSegment(inputSegment.terminate());
						}
						else {
							inputSegment.beam.addSegment(inputSegment.extend(inputSegment.distanceRemaining + extraDistance, BeamDirection.UP));
						}
						return true;
					};
				}
				return null;
			},
			Blocks.BEACON
		);
		LOOKUP.registerFallback((World world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Beam beam) -> {
			if (state.block instanceof BeamInteractor interactor) {
				return interactor;
			}
			BeaconBeamColorProvider provider = BeaconBeamColorProvider.LOOKUP.find(world, pos, state, blockEntity, null);
			if (provider != null) {
				float[] color = provider.getBeaconColor(world, pos, state);
				if (color != null) {
					Vector3f actualColor = new Vector3f(color[0], color[1], color[2]);
					return (SpreadingBeamSegment inputSegment, BlockState state1) -> {
						if (inputSegment.segment.color == null || inputSegment.segment.color.equals(actualColor)) {
							inputSegment.beam.addSegment(inputSegment.withColor(actualColor).extend());
						}
						else {
							inputSegment.beam.addSegment(inputSegment.terminate());
						}
						return true;
					};
				}
			}
			return null;
		});
	}};
}