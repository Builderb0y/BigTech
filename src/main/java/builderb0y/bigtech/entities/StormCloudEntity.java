package builderb0y.bigtech.entities;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import builderb0y.bigtech.mixins.ServerWorld_GetLightningPosInvoker;
import builderb0y.bigtech.util.BigTechMath;

public class StormCloudEntity extends Entity {

	public static Map<World, Set<StormCloudEntity>> ACTIVE = Collections.synchronizedMap(new WeakHashMap<>());

	public static final int
		ADDED_RADIUS_SQUARED_BY_SILVER_IODIDE_CANNON = 256,
		REQUIRED_RADIUS_SQUARED_FOR_ONE_LIGHTNING_BOLT_PER_TICK = 65536,
		REQUIRED_RADIUS_SQUARED_FOR_HALF_BRIGHTNESS_IN_CENTER = 1024;

	public int radiusSquared;
	public int seed;

	public StormCloudEntity(EntityType<?> type, World world) {
		super(type, world);
		this.seed = this.getRandom().nextInt();
	}

	@Override
	public void tick() {
		if (this.firstUpdate) {
			ACTIVE.computeIfAbsent(this.getWorld(), (World world_) -> (
				Collections.synchronizedSet(
					Collections.newSetFromMap(
						new WeakHashMap<>()
					)
				)
			))
			.add(this);
		}
		super.tick();
		if (--this.radiusSquared <= 0) {
			this.discard();
		}
		else if (this.getWorld() instanceof ServerWorld world && this.random.nextFloat() < this.getLightningChancePerTick()) {
			Vec3d startPos = this.getRandomLightningBoltPosition();
			BlockPos blockPos = ((ServerWorld_GetLightningPosInvoker)(world)).bigtech_getLightningPos(
				BlockPos.ofFloored(startPos)
			);
			if (world.shouldTickEntityAt(blockPos)) {
				StormLightningEntity lightning = BigTechEntityTypes.STORM_LIGHTNING.create(world, SpawnReason.EVENT);
				if (lightning != null) {
					lightning.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
					lightning.setStartX(startPos.x);
					lightning.setStartY(startPos.y);
					lightning.setStartZ(startPos.z);
					world.spawnEntity(lightning);
				}
			}
		}
	}

	public float getLightningChancePerTick() {
		return ((float)(this.radiusSquared)) / ((float)(REQUIRED_RADIUS_SQUARED_FOR_ONE_LIGHTNING_BOLT_PER_TICK));
	}

	public Vec3d getRandomLightningBoltPosition() {
		double angle  = this.random.nextDouble() * Math.TAU;
		double radius = this.random.nextDouble() * Math.sqrt(this.radiusSquared);
		double x = Math.cos(angle) * radius + this.getX();
		double z = Math.sin(angle) * radius + this.getZ();
		return new Vec3d(x, this.getY(), z);
	}

	public float accumulateRaininess(double x, double y, double z, float raininess) {
		if (y < this.getY()) {
			double radiusSquared = BigTechMath.square(x - this.getX()) + BigTechMath.square(z - this.getZ());
			radiusSquared = 1.0D - radiusSquared / this.radiusSquared;
			if (radiusSquared > 0.0D) {
				radiusSquared *= radiusSquared;
				return MathHelper.lerp((float)(radiusSquared), raininess, 1.0F);
			}
		}
		return raininess;
	}

	@Override
	public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
		return new EntitySpawnS2CPacket(this, entityTrackerEntry, this.radiusSquared);
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		this.radiusSquared = packet.getEntityData();
	}

	@Override
	public boolean canUsePortals(boolean allowVehicles) {
		return false;
	}

	@Override
	public void initDataTracker(DataTracker.Builder builder) {}

	@Override
	public boolean damage(ServerWorld world, DamageSource source, float amount) {
		return false;
	}

	@Override
	public void onRemove(Entity.RemovalReason reason) {
		super.onRemove(reason);
		Set<StormCloudEntity> set = ACTIVE.get(this.getWorld());
		if (set != null) {
			set.remove(this);
			if (set.isEmpty()) {
				ACTIVE.remove(this.getWorld());
			}
		}
	}

	@Override
	public void readCustomData(ReadView view) {
		this.radiusSquared = view.getInt("r2", 0);
	}

	@Override
	public void writeCustomData(WriteView view) {
		view.putInt("r2", this.radiusSquared);
	}
}