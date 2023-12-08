package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.entity.BeaconBlockEntity;

@Mixin(BeaconBlockEntity.class)
public interface BeaconBlockEntity_LevelGetter {

	@Accessor("level")
	public abstract int bigtech_getLevel();
}