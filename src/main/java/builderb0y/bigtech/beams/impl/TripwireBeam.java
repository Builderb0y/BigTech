package builderb0y.bigtech.beams.impl;

import java.util.UUID;

import org.joml.Vector3f;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.blockEntities.TripwireBlockEntity;
import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.util.WorldHelper;

public class TripwireBeam extends PersistentBeam {

	public static final Vector3f COLOR = new Vector3f(0.75F, 0.5F, 1.0F);

	public TripwireBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	@Override
	public BeamType getType() {
		return BeamTypes.TRIPWIRE;
	}

	@Override
	public Vector3f getInitialColor() {
		return COLOR;
	}

	@Override
	public void onEntityCollision(BlockPos pos, Entity entity) {
		TripwireBlockEntity tripwire = WorldHelper.getBlockEntity(this.world, this.origin, TripwireBlockEntity.class);
		if (tripwire != null && tripwire.delay != 20) {
			tripwire.setDelay(10);
			tripwire.markDirty();
		}
	}

	@Override
	public void onBlockChanged(BlockPos pos, BlockState oldState, BlockState newState) {
		if (
			pos.equals(this.origin) &&
			oldState.isOf(FunctionalBlocks.TRIPWIRE) &&
			newState.isOf(FunctionalBlocks.TRIPWIRE) &&
			newState.get(Properties.HORIZONTAL_FACING) == oldState.get(Properties.HORIZONTAL_FACING)
		) {
			return;
		}
		this.world.addSyncedBlockEvent(pos, FunctionalBlocks.TRIPWIRE, 0, 0);
	}
}