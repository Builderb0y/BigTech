package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;

@Mixin(ItemEntity.class)
public abstract class ItemEntity_DontMergeDuringTransport extends Entity {

	public ItemEntity_DontMergeDuringTransport(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "canMerge()Z", at = @At("HEAD"), cancellable = true)
	private void bigtech_dontMergeDuringTransport(CallbackInfoReturnable<Boolean> callback) {
		if (this.pos.squaredDistanceTo(this.prevX, this.prevY, this.prevZ) > 0.00001D) {
			callback.setReturnValue(Boolean.FALSE);
		}
	}
}