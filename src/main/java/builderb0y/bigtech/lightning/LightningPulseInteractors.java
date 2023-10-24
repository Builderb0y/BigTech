package builderb0y.bigtech.lightning;

import net.minecraft.block.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;

public class LightningPulseInteractors {

	public static final LightningPulseInteractor
		TNT = new LightningPulseInteractor() {

			@Override
			public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				TntBlock.primeTnt(world, pos);
				world.removeBlock(pos, false);
				this.spawnLightningParticles(world, pos, state, pulse);
			}

			@Override
			public String toString() {
				return "LightningPulseInteractors.TNT";
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

			@Override
			public String toString() {
				return "LightningPulseInteractors.OXIDIZABLE";
			}
		},
		INSULATED_CONDUCTOR = new LightningPulseInteractor() {

			@Override
			public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {}

			@Override
			public String toString() {
				return "LightningPulseInteractors.INSULATED_CONDUCTOR";
			}
		},
		SHOCKING_CONDUCTOR = new LightningPulseInteractor() {

			@Override
			public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				this.shockEntitiesAround(world, pos, state, pulse);
				this.spawnLightningParticles(world, pos, state, pulse);
			}

			@Override
			public String toString() {
				return "LightningPulseInteractors.SHOCKING_CONDUCTOR";
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

			@Override
			public String toString() {
				return "LightningPulseInteractors.SHOCKING_CONDUCTOR";
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

			@Override
			public String toString() {
				return "LightningPulseInteractors.LIGHTNING_ROD";
			}
		};
}