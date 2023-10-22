package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(value = PlayerEntity.class, priority = 1000)
public abstract class PlayerEntity_OverrideIsClimbing extends LivingEntity {

	public PlayerEntity_OverrideIsClimbing(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	@Unique(silent = true)
	public boolean isClimbing() {
		return super.isClimbing();
	}
}