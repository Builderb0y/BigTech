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
import builderb0y.bigtech.blockEntities.IgnitorBlockEntity;
import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.util.WorldHelper;

public class IgnitorBeam extends PersistentBeam {

	public static final Vector3f COLOR = new Vector3f(1.0F, 0.5F, 0.25F);

	public IgnitorBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	@Override
	public void onEntityCollision(ServerWorld world, BlockPos pos, Entity entity) {
		super.onEntityCollision(world, pos, entity);
		IgnitorBlockEntity ignitor = WorldHelper.getBlockEntity(world, this.origin, IgnitorBlockEntity.class);
		if (ignitor != null) ignitor.onEntityCollision(world, entity);
	}

	@Override
	public BeamType getType() {
		return BeamTypes.IGNITOR;
	}

	@Override
	public Vector3fc getInitialColor() {
		return COLOR;
	}

	@Override
	public void onBlockChanged(ServerWorld world, BlockPos pos, BlockState oldState, BlockState newState) {
		BlockState originState = world.getBlockState(this.origin);
		if (originState.isOf(FunctionalBlocks.IGNITOR_BEAM)) {
			world.addSyncedBlockEvent(this.origin, FunctionalBlocks.IGNITOR_BEAM, 0, 0);
		}
		else {
			this.removeFromWorld(world);
		}
	}
}