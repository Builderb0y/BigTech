package builderb0y.bigtech.beams.impl;

import java.util.UUID;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.blocks.FunctionalBlocks;

public class RedstoneBeam extends PersistentBeam {

	public static final Vector3fc COLOR = new Vector3f(1.0F, 0.25F, 0.0F);

	public RedstoneBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	@Override
	public BeamType getType() {
		return BeamTypes.REDSTONE;
	}

	@Override
	public Vector3fc getInitialColor() {
		return COLOR;
	}

	@Override
	public void onBlockChanged(BlockPos pos, BlockState oldState, BlockState newState) {
		BlockState originState = this.world.getBlockState(this.origin);
		if (originState.isOf(FunctionalBlocks.REDSTONE_TRANSMITTER)) {
			this.world.addSyncedBlockEvent(this.origin, FunctionalBlocks.REDSTONE_TRANSMITTER, 0, 0);
		}
		else {
			this.removeFromWorld();
		}
	}
}