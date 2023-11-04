package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.Beam;
import builderb0y.bigtech.beams.BeamSegment;
import builderb0y.bigtech.beams.PersistentBeam;
import builderb0y.bigtech.beams.PulseBeam;

public interface BeamInteractor {

	public static final BlockApiLookup<BeamInteractor, Beam> LOOKUP = BlockApiLookup.get(BigTechMod.modID("beam_interactor"), BeamInteractor.class, Beam.class);

	/**
	called while the beam is still in its spreading phase, before it is added to the world.
	this method may be used to modify a beam segment passing through it.
	for example, to change its color or direction or distance remaining.

	the world may be obtained via {@link BeamSegment#beam} and {@link Beam#world},
	but obtaining the world this way should be reserved for querying other
	blocks or block entities only. this method should NOT modify the world.
	the reason for this is that the beam may not be added to the world,
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

		/** called after a persistent beam is added to the world. */
		public abstract void onBeamAdded(BlockPos pos, BlockState state, PersistentBeam beam);

		/** called after a persistent beam is removed from the world. */
		public abstract void onBeamRemoved(BlockPos pos, BlockState state, PersistentBeam beam);

		/** called after a pulse beam has finished calculating its spread. */
		public abstract void onBeamPulse(BlockPos pos, BlockState state, PulseBeam beam);
	}
}