package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(value = PlayerEntity.class, priority = 2000)
public abstract class PlayerEntity_NoClimbingWhileFlying extends LivingEntity {

	@Shadow @Final private PlayerAbilities abilities;

	public PlayerEntity_NoClimbingWhileFlying(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Dynamic("isClimbing() is not, by default, overridden by PlayerEntity. A different mixin overrides it.")
	@Inject(method = "isClimbing()Z", at = @At("HEAD"), cancellable = true)
	private void bigtech_ladderTweaks(CallbackInfoReturnable<Boolean> callback) {
		if (this.abilities.flying) {
			callback.setReturnValue(Boolean.FALSE);
		}
	}
}