package builderb0y.bigtech.beams.base;

import java.util.UUID;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.api.BeamInteractor;
import builderb0y.bigtech.api.BeamInteractor.BeamCallback;
import builderb0y.bigtech.networking.PulseBeamPacket;

/**
a "single use" laser beam.

these beams will be displayed for 10 ticks,
getting more transparent during this time,
before disappearing forever.
*/
public abstract class PulseBeam extends Beam {

	public PulseBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	@Override
	public void addToWorld(ServerWorld world) {
		this.onAdded(world);
		PulseBeamPacket.INSTANCE.send(world, this);
	}

	public void onAdded(ServerWorld world) {
		for (BlockPos pos : this.callbacks) {
			BlockState state = world.getBlockState(pos);
			BeamInteractor interactor = BeamInteractor.LOOKUP.find(world, pos, state, null, this);
			if (interactor instanceof BeamCallback callback) {
				callback.onBeamPulse(world, pos, state, this);
			}
		}
	}
}