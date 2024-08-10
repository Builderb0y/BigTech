package builderb0y.bigtech.beams.base;

import java.util.UUID;

import it.unimi.dsi.fastutil.longs.LongIterator;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;

import builderb0y.bigtech.api.BeamInteractor;
import builderb0y.bigtech.api.BeamInteractor.BeamCallback;
import builderb0y.bigtech.networking.PulseBeamPacket;

/**
a "single use" laser beam.

these beams will simply spawn some particles when added to the world,
and then disappear forever.
*/
public abstract class PulseBeam extends Beam {

	public PulseBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	@Override
	public void addToWorld() {
		this.onAdded();
		PulseBeamPacket.INSTANCE.send(this);
	}

	public void onAdded() {
		for (BlockPos pos : this.callbacks) {
			BlockState state = this.world.getBlockState(pos);
			BeamInteractor interactor = BeamInteractor.LOOKUP.find(this.world, pos, state, null, this);
			if (interactor instanceof BeamCallback callback) {
				callback.onBeamPulse(pos, state, this);
			}
		}
	}
}