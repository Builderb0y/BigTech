package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import org.joml.Vector3f;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.Beam;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.base.PulseBeam;

public interface BeamInteractor {

	public static final BlockApiLookup<BeamInteractor, Beam> LOOKUP = BlockApiLookup.get(BigTechMod.modID("beam_interactor"), BeamInteractor.class, Beam.class);

	/**
	called while the beam is still in its spreading phase, before it is added to the world.
	this method may be used to modify a beam segment passing through it.
	for example, to change its color or direction or distance remaining.

	the {@link Beam} instance can be obtained via {@link BeamSegment#beam}.
	this can be used to add new segments to the beam's queue
	via {@link Beam#addSegment(BlockPos, BeamSegment)}.

	the world may be obtained via {@link BeamSegment#beam} and {@link Beam#world},
	but obtaining the world this way should be reserved for querying other
	blocks or block entities only. this method should NOT modify the world.
	the reason for this is that the beam may not be added to the world after spreading finishes,
	and it is not desirable to have the world be modified by "phantom" beams.

	if this block wishes to do something to the world when a beam passes
	through it, consider registering a {@link BeamCallback} to {@link #LOOKUP},
	instead of just a BeamInteractor.
	*/
	public abstract boolean spreadOut(BlockPos pos, BlockState state, BeamSegment inputSegment);

	/**
	an extension of BeamInteractor with additional callback
	methods for what to do when a beam passes through this block.
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
		LOOKUP.registerFallback((world, pos, state, blockEntity, beam) -> {
			BeaconBeamColorProvider provider = BeaconBeamColorProvider.LOOKUP.find(world, pos, state, blockEntity, null);
			if (provider != null) {
				float[] color = provider.getBeaconColor(world, pos, state);
				if (color != null) {
					Vector3f actualColor = new Vector3f(color[0], color[1], color[2]);
					return (pos1, state1, inputSegment) -> {
						if (inputSegment.color == null || inputSegment.color.equals(actualColor)) {
							BeamSegment extension = inputSegment.extend();
							if (extension != null) {
								inputSegment.beam.addSegment(pos1, extension.withColor(actualColor));
							}
							return true;
						}
						else {
							return false;
						}
					};
				}
			}
			return null;
		});
	}};
}