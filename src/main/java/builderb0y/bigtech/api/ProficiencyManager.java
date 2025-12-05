package builderb0y.bigtech.api;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.jetbrains.annotations.ApiStatus.Internal;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;

import builderb0y.bigtech.BigTechMod;

public class ProficiencyManager {

	public static final StatType<ProficiencyCategory> PROFICIENCY_STAT_TYPE = Registry.register(Registries.STAT_TYPE, BigTechMod.modID("proficiency"), new StatType<>(ProficiencyCategory.REGISTRY, Text.translatable("stat_type.bigtech.proficiency")));

	@Internal
	public static void init() {}

	public static double getProficiency(ServerPlayerEntity player, Item item, boolean justCrafted) {
		//the stat for "item crafted" gets incremented before this is called,
		//so we want to get the value just before it got incremented.
		double base = Math.sqrt(Math.max(player.getStatHandler().getStat(Stats.CRAFTED, item) - (justCrafted ? 1 : 0), 0 /* ??? */)) * ProficiencyCategory.ALL_ITEMS.value().multiplier();
		for (ProficiencyCategory category : ProficiencyItem.CACHE.get(item).keySet()) {
			base += Math.sqrt(player.getStatHandler().getStat(PROFICIENCY_STAT_TYPE, category)) * category.multiplier();
		}
		return base;
	}

	public static void onCrafted(ServerPlayerEntity player, Item item) {
		//Stats.CRAFTED is incremented automatically.
		for (ObjectIterator<Object2IntMap.Entry<ProficiencyCategory>> iterator = Object2IntMaps.fastIterator(ProficiencyItem.CACHE.get(item)); iterator.hasNext();) {
			Object2IntMap.Entry<ProficiencyCategory> entry = iterator.next();
			//int oldValue = player.getStatHandler().getStat(PROFICIENCY_STAT_TYPE, entry.getKey());
			player.getStatHandler().increaseStat(player, PROFICIENCY_STAT_TYPE.getOrCreateStat(entry.getKey()), entry.getIntValue());
			//int newValue = player.getStatHandler().getStat(PROFICIENCY_STAT_TYPE, entry.getKey());
		}
	}
}