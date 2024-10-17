package builderb0y.bigtech.mixins;

import me.shedaniel.autoconfig.ConfigData;
import org.spongepowered.asm.mixin.Mixin;

import builderb0y.bigtech.config.BigTechConfig;

@Mixin(BigTechConfig.class)
public abstract class BigTechConfig_ImplementConfigData implements ConfigData {}