package builderb0y.bigtech.mixins;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
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

	@Shadow private int spawnCount;
	@Shadow private int maxNearbyEntities;
	@Shadow private int spawnRange;

	@Shadow protected abstract MobSpawnerEntry getSpawnEntry(@Nullable World world, Random random, BlockPos pos);

	@Shadow protected abstract void updateSpawns(World world, BlockPos pos);

	@Override
	public void bigtech_spawnMobs(ServerWorld world, BlockPos pos) {
		boolean bl = false;
		Random random = world.getRandom();
		MobSpawnerEntry mobSpawnerEntry = this.getSpawnEntry(world, random, pos);
		for (int i = 0; i < this.spawnCount; ++i) {
			MobSpawnerEntry.CustomSpawnRules customSpawnRules;
			double f;
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
			double d2 = f = j >= 3 ? nbtList.getDouble(2) : (double)pos.getZ() + (random.nextDouble() - random.nextDouble()) * (double)this.spawnRange + 0.5;
			if (!world.isSpaceEmpty(optional.get().createSimpleBoundingBox(d, e, f))) continue;
			BlockPos blockPos = BlockPos.ofFloored(d, e, f);
			if (!mobSpawnerEntry.getCustomSpawnRules().isPresent() ? !SpawnRestriction.canSpawn(optional.get(), world, SpawnReason.SPAWNER, blockPos, world.getRandom()) : !optional.get().getSpawnGroup().isPeaceful() && world.getDifficulty() == Difficulty.PEACEFUL || !(customSpawnRules = mobSpawnerEntry.getCustomSpawnRules().get()).blockLightLimit().contains(world.getLightLevel(LightType.BLOCK, blockPos)) || !customSpawnRules.skyLightLimit().contains(world.getLightLevel(LightType.SKY, blockPos))) continue;
			Entity entity2 = EntityType.loadEntityWithPassengers(nbtCompound, world, entity -> {
				entity.refreshPositionAndAngles(d, e, f, entity.getYaw(), entity.getPitch());
				return entity;
			});
			if (entity2 == null) {
				this.updateSpawns(world, pos);
				return;
			}
			int k = world.getNonSpectatingEntities(entity2.getClass(), new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(this.spawnRange)).size();
			if (k >= this.maxNearbyEntities) {
				this.updateSpawns(world, pos);
				return;
			}
			entity2.refreshPositionAndAngles(entity2.getX(), entity2.getY(), entity2.getZ(), random.nextFloat() * 360.0f, 0.0f);
			if (entity2 instanceof MobEntity) {
				MobEntity mobEntity = (MobEntity)entity2;
				if (mobSpawnerEntry.getCustomSpawnRules().isEmpty() && !mobEntity.canSpawn(world, SpawnReason.SPAWNER) || !mobEntity.canSpawn(world)) continue;
				if (mobSpawnerEntry.getNbt().getSize() == 1 && mobSpawnerEntry.getNbt().contains("id", NbtElement.STRING_TYPE)) {
					((MobEntity)entity2).initialize(world, world.getLocalDifficulty(entity2.getBlockPos()), SpawnReason.SPAWNER, null, null);
				}
			}
			if (!world.spawnNewEntityAndPassengers(entity2)) {
				this.updateSpawns(world, pos);
				return;
			}
			world.syncWorldEvent(WorldEvents.SPAWNER_SPAWNS_MOB, pos, 0);
			world.emitGameEvent(entity2, GameEvent.ENTITY_PLACE, blockPos);
			if (entity2 instanceof MobEntity) {
				((MobEntity)entity2).playSpawnEffects();
			}
			bl = true;
		}
		if (bl) {
			this.updateSpawns(world, pos);
		}
	}
}