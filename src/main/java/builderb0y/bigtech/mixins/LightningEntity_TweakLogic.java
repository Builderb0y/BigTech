package builderb0y.bigtech.mixins;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;

@Mixin(LightningEntity.class)
public abstract class LightningEntity_TweakLogic extends Entity {

	public LightningEntity_TweakLogic() {
		super(null, null);
	}

	/**
	extend the lifespan of lightning bolts,
	to give them time for my fancy animation to play.
	*/
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LightningEntity;discard()V"))
	private void bigtech_stayAlive(LightningEntity instance) {
		if (instance.age >= 16) instance.discard();
	}

	/**
	prevent my renderer from drastically changing the shape of the lightning mid-animation.
	*/
	@Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LightningEntity;seed:J", opcode = Opcodes.PUTFIELD))
	private void bigtech_dontChangeSeed(LightningEntity instance, long value) {
		//don't do anything.
	}
}