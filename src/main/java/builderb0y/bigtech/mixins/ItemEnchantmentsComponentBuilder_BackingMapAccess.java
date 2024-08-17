package builderb0y.bigtech.mixins;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;

/**
I don't want to iterate through the key set and call getLevel()
each time when the entry set will give both at the same time.
*/
@Mixin(ItemEnchantmentsComponent.Builder.class)
public interface ItemEnchantmentsComponentBuilder_BackingMapAccess {

	@Accessor("enchantments")
	public abstract Object2IntOpenHashMap<RegistryEntry<Enchantment>> bigtech_getEnchantments();
}