package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKey;

@Mixin(AbstractBlock.Settings.class)
public interface AbstractBlockSettings_RegistryKeyAccessor {

	@Accessor("registryKey")
	public abstract RegistryKey<Block> bigtech_getRegistryKey();
}