package builderb0y.bigtech.beams.impl;

import java.util.UUID;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PulseBeam;
import builderb0y.bigtech.beams.base.SpreadingBeamSegment;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;

public class LightningBeam extends PulseBeam {

	public static final Vector3fc COLOR = new Vector3f(0.875F, 0.9375F, 1.0F);

	public LightningPulse pulse;

	public LightningBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	@Override
	public void handleIntersection(ServerWorld world, BlockPos pos, BlockState state, SpreadingBeamSegment segment, BlockHitResult hitResult) {
		super.handleIntersection(world, pos, state, segment, hitResult);
		LinkedBlockPos linked = new LinkedBlockPos(pos, (LinkedBlockPos)(this.origin));
		if (!this.pulse.hasNode(linked)) {
			LightningPulseInteractor interactor = LightningPulseInteractor.get(world, linked, state);
			if (interactor.canConductIn(world, linked, state, segment.direction().toVanilla())) {
				interactor.spreadIn(world, linked, state, this.pulse);
			}
		}
	}

	@Override
	public BeamType getType() {
		return BeamTypes.LIGHTNING;
	}

	@Override
	public Vector3fc getInitialColor() {
		return COLOR;
	}
}