package builderb0y.bigtech.lightning;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;

import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;
import builderb0y.bigtech.mixinterfaces.ForceableMobSpawnerLogic;
import builderb0y.bigtech.util.WorldHelper;

public class LightningPulseInteractors {

	public static final LightningPulseInteractor
		TNT = new LightningPulseInteractor() {

			@Override
			public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				TntBlock.primeTnt(world, pos);
				world.removeBlock(pos, false);
				LightningPulseInteractor.spawnLightningParticles(world, pos, state, pulse);
			}

			@Override
			public String toString() {
				return "LightningPulseInteractors.TNT";
			}
		},
		OXIDIZABLE = new LightningPulseInteractor() {

			@Override
			public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				BlockState newState = Oxidizable.getDecreasedOxidationState(state).orElse(null);
				if (newState != null) world.setBlockState(pos, newState);
				LightningPulseInteractor.shockEntitiesAround(world, pos, state, pulse);
				LightningPulseInteractor.spawnLightningParticles(world, pos, state, pulse);
			}

			@Override
			public String toString() {
				return "LightningPulseInteractors.OXIDIZABLE";
			}
		},
		INSULATED_CONDUCTOR = new LightningPulseInteractor() {

			@Override
			public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {}

			@Override
			public String toString() {
				return "LightningPulseInteractors.INSULATED_CONDUCTOR";
			}
		},
		SHOCKING_CONDUCTOR = new LightningPulseInteractor() {

			@Override
			public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				LightningPulseInteractor.shockEntitiesAround(world, pos, state, pulse);
				LightningPulseInteractor.spawnLightningParticles(world, pos, state, pulse);
			}

			@Override
			public String toString() {
				return "LightningPulseInteractors.SHOCKING_CONDUCTOR";
			}
		},
		INSULATOR = new LightningPulseInteractor() {

			@Override
			public boolean canConductIn(WorldView world, BlockPos pos, BlockState state, @Nullable Direction side) {
				return false;
			}

			@Override
			public boolean canConductOut(WorldView world, BlockPos pos, BlockState state, Direction side) {
				return false;
			}

			@Override
			public void spreadIn(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {}

			@Override
			public void spreadOut(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {}

			@Override
			public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				//no-op.
			}

			@Override
			public String toString() {
				return "LightningPulseInteractors.INSULATOR";
			}
		},
		LIGHTNING_ROD = new LightningPulseInteractor() {

			@Override
			public boolean canConductOut(WorldView world, BlockPos pos, BlockState state, Direction side) {
				return side == state.get(Properties.FACING).getOpposite();
			}

			@Override
			public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				LightningPulseInteractor.shockEntitiesAround(world, pos, state, pulse);
				if (!pos.equals(pulse.originPos)) {
					LightningPulseInteractor.spawnLightningParticles(world, pos, state, pulse);
				}
			}

			@Override
			public String toString() {
				return "LightningPulseInteractors.LIGHTNING_ROD";
			}
		},
		MOB_SPAWNER = new LightningPulseInteractor() {

			@Override
			public boolean isSink(WorldView world, BlockPos pos, BlockState state) {
				return true;
			}

			@Override
			public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
				MobSpawnerBlockEntity spawner = WorldHelper.getBlockEntity(world, pos, MobSpawnerBlockEntity.class);
				if (spawner != null) {
					spawner.getLogic().<ForceableMobSpawnerLogic>as().bigtech_spawnMobs(world.as(), pos);
					LightningPulseInteractor.spawnLightningParticles(world, pos, state, pulse);
				}
			}
		};
}