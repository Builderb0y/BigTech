package builderb0y.bigtech.items;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import builderb0y.bigtech.entities.BigTechEntityTypes;
import builderb0y.bigtech.entities.MagneticArrowEntity;

public class MagneticArrowItem extends ArrowItem {

	public MagneticArrowItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
		return new MagneticArrowEntity(BigTechEntityTypes.MAGNETIC_ARROW, shooter, world, stack.copyWithCount(1), shotFrom);
	}

	@Override
	public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
		MagneticArrowEntity entity = new MagneticArrowEntity(BigTechEntityTypes.MAGNETIC_ARROW, pos.getX(), pos.getY(), pos.getZ(), world, stack.copyWithCount(1), null);
		entity.pickupType = PickupPermission.ALLOWED;
		return entity;
	}
}