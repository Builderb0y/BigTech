package builderb0y.bigtech.lightning;

import net.minecraft.block.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;

public class LightningPulseInteractors {

	public static LightningPulseInteractor forBlock(WorldAccess world, BlockPos pos, BlockState state) {
		Block block = state.getBlock();
		if (block instanceof LightningPulseInteractor interactor) return interactor;
		if (block instanceof TntBlock) return TNT;
		if (block instanceof Oxidizable) return OXIDIZABLE;
		if (block instanceof LightningRodBlock) return LIGHTNING_ROD;
		if (state.isIn(BigTechBlockTags.CONDUCTS_LIGHTNING)) {
			return state.isIn(BigTechBlockTags.SHOCKS_ENTITIES) ? SHOCKING_CONDUCTOR : INSULATED_CONDUCTOR;
		}
		return INSULATOR;
	}

	public static final LightningPulseInteractor
		TNT = new LightningPulseInteractor() {

			@Override
			public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				TntBlock.primeTnt(world, pos);
				world.removeBlock(pos, false);
				this.spawnLightningParticles(world, pos, state, pulse);
			}
		},
		OXIDIZABLE = new LightningPulseInteractor() {

			@Override
			public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				BlockState newState = Oxidizable.getDecreasedOxidationState(state).orElse(null);
				if (newState != null) world.setBlockState(pos, newState);
				this.shockEntitiesAround(world, pos, state, pulse);
				this.spawnLightningParticles(world, pos, state, pulse);
			}
		},
		INSULATED_CONDUCTOR = new LightningPulseInteractor() {

			@Override
			public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				this.spawnLightningParticles(world, pos, state, pulse);
			}
		},
		SHOCKING_CONDUCTOR = new LightningPulseInteractor() {

			@Override
			public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				this.shockEntitiesAround(world, pos, state, pulse);
				this.spawnLightningParticles(world, pos, state, pulse);
			}
		},
		INSULATOR = new LightningPulseInteractor() {

			@Override
			public boolean canConductIn(WorldAccess world, BlockPos pos, BlockState state, Direction side) {
				return false;
			}

			@Override
			public boolean canConductOut(WorldAccess world, BlockPos pos, BlockState state, Direction side) {
				return false;
			}

			@Override
			public void spreadIn(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {}

			@Override
			public void spreadOut(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {}

			@Override
			public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				//no-op.
			}
		},
		LIGHTNING_ROD = new LightningPulseInteractor() {

			@Override
			public boolean canConductOut(WorldAccess world, BlockPos pos, BlockState state, Direction side) {
				return side == state.get(Properties.FACING).opposite;
			}

			@Override
			public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				this.shockEntitiesAround(world, pos, state, pulse);
				if (!pos.equals(pulse.originPos)) {
					this.spawnLightningParticles(world, pos, state, pulse);
				}
			}
		};
}