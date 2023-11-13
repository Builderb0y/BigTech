package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.throwables.MixinError;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;

@Mixin(CreeperEntity.class)
public interface CreeperEntity_MakeChargeableByTeslaCoil {

	@Accessor("CHARGED")
	public static TrackedData<Boolean> getCharged() {
		throw new MixinError("Accessor not applied");
	}
}