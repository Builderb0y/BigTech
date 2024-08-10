package builderb0y.bigtech.mixins;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.spawner.MobSpawnerEntry;
import net.minecraft.block.spawner.MobSpawnerLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;

import builderb0y.bigtech.mixinterfaces.ForceableMobSpawnerLogic;

/**
vanilla logic is inside the serverTick() method,
with no way to call only part of the method.
so, I copy-pasted it here.
*/
@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogic_MakeForceable implements ForceableMobSpawnerLogic {

	@Shadow
	private int spawnCount;
	@Shadow
	private int maxNearbyEntities;
	@Shadow
	private int spawnRange;

	@Shadow
	protected abstract MobSpawnerEntry getSpawnEntry(@Nullable World world, Random random, BlockPos pos);

	@Shadow
	protected abstract void updateSpawns(World world, BlockPos pos);

	@Override
	public void bigtech_spawnMobs(ServerWorld world, BlockPos pos) {
		boolean bl = false;
		Random random = world.getRandom();
		MobSpawnerEntry mobSpawnerEntry = this.getSpawnEntry(world, random, pos);

		for (int i = 0; i < this.spawnCount; i++) {
			NbtCompound nbtCompound = mobSpawnerEntry.getNbt();
			Optional<EntityType<?>> optional = EntityType.fromNbt(nbtCompound);
			if (optional.isEmpty()) {
				this.updateSpawns(world, pos);
				return;
			}

			NbtList nbtList = nbtCompound.getList("Pos", NbtElement.DOUBLE_TYPE);
			int j = nbtList.size();
			double d = j >= 1 ? nbtList.getDouble(0) : (double)pos.getX() + (random.nextDouble() - random.nextDouble()) * (double)this.spawnRange + 0.5;
			double e = j >= 2 ? nbtList.getDouble(1) : (double)(pos.getY() + random.nextInt(3) - 1);
			double f = j >= 3 ? nbtList.getDouble(2) : (double)pos.getZ() + (random.nextDouble() - random.nextDouble()) * (double)this.spawnRange + 0.5;
			if (world.isSpaceEmpty(optional.get().getSpawnBox(d, e, f))) {
				BlockPos blockPos = BlockPos.ofFloored(d, e, f);
				if (mobSpawnerEntry.getCustomSpawnRules().isPresent()) {
					if (!optional.get().getSpawnGroup().isPeaceful() && world.getDifficulty() == Difficulty.PEACEFUL) {
						continue;
					}

					MobSpawnerEntry.CustomSpawnRules customSpawnRules = mobSpawnerEntry.getCustomSpawnRules().get();
					if (!customSpawnRules.canSpawn(blockPos, world)) {
						continue;
					}
				}
				else if (!SpawnRestriction.canSpawn((EntityType)optional.get(), world, SpawnReason.SPAWNER, blockPos, world.getRandom())) {
					continue;
				}

				Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, entityx -> {
					entityx.refreshPositionAndAngles(d, e, f, entityx.getYaw(), entityx.getPitch());
					return entityx;
				});
				if (entity == null) {
					this.updateSpawns(world, pos);
					return;
				}

				int k = world.getEntitiesByType(
					TypeFilter.equals(entity.getClass()),
					new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(this.spawnRange),
					EntityPredicates.EXCEPT_SPECTATOR
				)
				.size();
				if (k >= this.maxNearbyEntities) {
					this.updateSpawns(world, pos);
					return;
				}

				entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), random.nextFloat() * 360.0F, 0.0F);
				if (entity instanceof MobEntity mobEntity) {
					if (mobSpawnerEntry.getCustomSpawnRules().isEmpty() && !mobEntity.canSpawn(world, SpawnReason.SPAWNER) || !mobEntity.canSpawn(world)) {
						continue;
					}

					boolean bl2 = mobSpawnerEntry.getNbt().getSize() == 1 && mobSpawnerEntry.getNbt().contains("id", NbtElement.STRING_TYPE);
					if (bl2) {
						((MobEntity)entity).initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWNER, null);
					}

					mobSpawnerEntry.getEquipment().ifPresent(mobEntity::setEquipmentFromTable);
				}

				if (!world.spawnNewEntityAndPassengers(entity)) {
					this.updateSpawns(world, pos);
					return;
				}

				world.syncWorldEvent(WorldEvents.SPAWNER_SPAWNS_MOB, pos, 0);
				world.emitGameEvent(entity, GameEvent.ENTITY_PLACE, blockPos);
				if (entity instanceof MobEntity) {
					((MobEntity)entity).playSpawnEffects();
				}

				bl = true;
			}
		}

		if (bl) {
			this.updateSpawns(world, pos);
		}
	}
}