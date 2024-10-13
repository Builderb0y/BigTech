package builderb0y.bigtech.entities;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import builderb0y.bigtech.items.FunctionalItems;

public class MagneticArrowEntity extends PersistentProjectileEntity {

	public MagneticArrowEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
		super(type, owner, world, stack, shotFrom);
	}

	public MagneticArrowEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
		super(type, x, y, z, world, stack, weapon);
	}

	public MagneticArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public ItemStack getDefaultItemStack() {
		return new ItemStack(FunctionalItems.MAGNETIC_ARROW);
	}

	@Override
	public void tick() {
		List<Entity> list = this.getWorld().getOtherEntities(
			this,
			new Box(
				this.getX() - 16.0D,
				this.getY() - 16.0D,
				this.getZ() - 16.0D,
				this.getX() + 16.0D,
				this.getY() + 16.0D,
				this.getZ() + 16.0D
			),
			(Entity entity) -> (
				!entity.isSpectator() &&
				entity != this.getOwner() &&
				entity.canBeHitByProjectile()
			)
		);
		if (!list.isEmpty()) {
			Entity target = null;
			double targetRank = Double.POSITIVE_INFINITY;
			double
				newVX = this.getVelocity().x,
				newVY = this.getVelocity().y,
				newVZ = this.getVelocity().z,
				speed = Math.sqrt(newVX * newVX + newVY * newVY + newVZ * newVZ),
				rcp   = 1.0D  / speed,
				oldVX = newVX * rcp,
				oldVY = newVY * rcp,
				oldVZ = newVZ * rcp;
			for (Entity entity : list) {
				double
					dx = entity.getX() - this.getX(),
					dy = entity.getY() - this.getY() + entity.getHeight() * 0.5D,
					dz = entity.getZ() - this.getZ(),
					squaredDistance = dx * dx + dy * dy + dz * dz;
				if (squaredDistance < 256.0D) {
					if (target == null) {
						target = entity;
					}
					else {
						double
							dotProduct = dx * oldVX + dy * oldVY + dz * oldVZ,
							targetX = dx - oldVX * dotProduct,
							targetY = dy - oldVY * dotProduct,
							targetZ = dz - oldVZ * dotProduct,
							newRank = targetX * targetX + targetY * targetY + targetZ * targetZ;
						if (newRank < targetRank) {
							target = entity;
							targetRank = newRank;
						}
					}
				}
			}
			if (target != null) {
				double
					dx = target.getX() - this.getX(),
					dy = target.getY() - this.getY() + target.getHeight() * 0.5D,
					dz = target.getZ() - this.getZ(),
					squaredDistance = dx * dx + dy * dy + dz * dz,
					dotProduct = dx * oldVX + dy * oldVY + dz * oldVZ,
					targetX = dx - oldVX * dotProduct,
					targetY = dy - oldVY * dotProduct,
					targetZ = dz - oldVZ * dotProduct,
					multiplier = 1.0D - squaredDistance * (1.0D / 256.0D);
				multiplier *= speed * 0.125D;
				multiplier /= targetX * targetX + targetY * targetY + targetZ * targetZ + 1.0D;
				newVX += targetX * multiplier;
				newVY += targetY * multiplier;
				newVZ += targetZ * multiplier;
				this.setVelocity(newVX, newVY, newVZ);
			}
		}
		super.tick();
	}
}