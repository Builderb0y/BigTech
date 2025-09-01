package builderb0y.bigtech.beams.impl;

import java.util.UUID;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.blockEntities.IgniterBlockEntity;
import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.util.WorldHelper;

public class IgniterBeam extends PersistentBeam {

	public static final Vector3f COLOR = new Vector3f(1.0F, 0.5F, 0.25F);

	public IgniterBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	@Override
	public void onEntityCollision(ServerWorld world, BlockPos pos, Entity entity) {
		super.onEntityCollision(world, pos, entity);
		IgniterBlockEntity igniter = WorldHelper.getBlockEntity(world, this.origin, IgniterBlockEntity.class);
		if (igniter != null) igniter.onEntityCollision(world, entity);
	}

	@Override
	public BeamType getType() {
		return BeamTypes.IGNITER;
	}

	@Override
	public Vector3fc getInitialColor() {
		return COLOR;
	}

	@Override
	public void onBlockChanged(ServerWorld world, BlockPos pos, BlockState oldState, BlockState newState) {
		BlockState originState = world.getBlockState(this.origin);
		if (originState.isOf(FunctionalBlocks.IGNITER_BEAM)) {
			world.addSyncedBlockEvent(this.origin, FunctionalBlocks.IGNITER_BEAM, 0, 0);
		}
		else {
			this.removeFromWorld(world);
		}
	}
}