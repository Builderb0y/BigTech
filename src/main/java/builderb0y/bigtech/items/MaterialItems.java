package builderb0y.bigtech.items;

import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

import net.minecraft.item.BlockItem;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.MaterialBlocks;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.*;
import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection.CrystalColor;

import static builderb0y.bigtech.items.BigTechItems.*;

@SuppressWarnings("unused")
public class MaterialItems {

	@UseDataGen(CrystalClusterDataGenerator.class)
	public static final CrystalItemCollection CRYSTAL_CLUSTERS = new CrystalItemCollection(
		true,
		(CrystalColor color) -> new BlockItem(
			MaterialBlocks.CRYSTAL_CLUSTERS.get(color),
			settings(color.prefix + "crystal_cluster", true)
		)
	);
	@UseDataGen(CrystalDebrisDataGenerator.class)
	public static final Item CRYSTAL_DEBRIS = register(
		new Item(settings("crystal_debris", false))
	);
	@UseDataGen(CrystallineSandDataGenerator.class)
	public static final BlockItem CRYSTALLINE_SAND = registerPlacer(
		MaterialBlocks.CRYSTALLINE_SAND
	);
	@UseDataGen(SmoothObsidianDataGenerator.class)
	public static final BlockItem SMOOTH_OBSIDIAN = registerPlacer(
		MaterialBlocks.SMOOTH_OBSIDIAN
	);
	@UseDataGen(SteelBlockDataGenerator.class)
	public static final BlockItem STEEL_BLOCK = registerPlacer(
		MaterialBlocks.STEEL_BLOCK
	);
	@UseDataGen(SteelIngotDataGenerator.class)
	public static final Item STEEL_INGOT = register(
		new Item(settings("steel_ingot", false))
	);
	@UseDataGen(SteelNuggetDataGenerator.class)
	public static final Item STEEL_NUGGET = register(
		new Item(settings("steel_nugget", false))
	);
	@UseDataGen(MagnetiteIngotDataGenerator.class)
	public static final Item FERROMAGNETIC_INGOT = register(
		new Item(settings("ferromagnetic_ingot", false))
	);
	static {
		Registries.ITEM.addAlias(BigTechMod.modID("magnetite_ingot"), BigTechMod.modID("ferromagnetic_ingot"));
	}
	@UseDataGen(MagnetiteNuggetDataGenerator.class)
	public static final Item FERROMAGNETIC_NUGGET = register(
		new Item(settings("ferromagnetic_nugget", false))
	);
	static {
		Registries.ITEM.addAlias(BigTechMod.modID("magnetite_nugget"), BigTechMod.modID("ferromagnetic_nugget"));
	}
	@UseDataGen(CastIronBlockDataGenerator.class)
	public static final BlockItem CAST_IRON_BLOCK = registerPlacer(
		MaterialBlocks.CAST_IRON_BLOCK
	);
	@UseDataGen(CastIronIngotDataGenerator.class)
	public static final Item CAST_IRON_INGOT = register(
		new Item(settings("cast_iron_ingot", false))
	);
	@UseDataGen(CopperNuggetDataGenerator.class)
	public static final Item COPPER_NUGGET = register(
		new Item(settings("copper_nugget", false))
	);
	@UseDataGen(SilverOreDataGenerator.class)
	public static final BlockItem SILVER_ORE = registerPlacer(
		MaterialBlocks.SILVER_ORE
	);
	@UseDataGen(DeepslateSilverOreDataGenerator.class)
	public static final BlockItem DEEPSLATE_SILVER_ORE = registerPlacer(
		MaterialBlocks.DEEPSLATE_SILVER_ORE
	);
	@UseDataGen(RawSilverBlockDataGenerator.class)
	public static final BlockItem RAW_SILVER_BLOCK = registerPlacer(
		MaterialBlocks.RAW_SILVER_BLOCK
	);
	@UseDataGen(RawSilverDataGenerator.class)
	public static final Item RAW_SILVER = register(
		new Item(settings("raw_silver", false))
	);
	@UseDataGen(SilverBlockDataGenerator.class)
	public static final BlockItem SILVER_BLOCK = registerPlacer(
		MaterialBlocks.SILVER_BLOCK
	);
	@UseDataGen(SilverIngotDataGenerator.class)
	public static final Item SILVER_INGOT = register(
		new Item(settings("silver_ingot", false))
	);
	@UseDataGen(SilverNuggetDataGenerator.class)
	public static final Item SILVER_NUGGET = register(
		new Item(settings("silver_nugget", false))
	);
	@UseDataGen(ElectrumBlockDataGenerator.class)
	public static final BlockItem ELECTRUM_BLOCK = registerPlacer(
		MaterialBlocks.ELECTRUM_BLOCK
	);
	@UseDataGen(ElectrumIngotDataGenerator.class)
	public static final Item ELECTRUM_INGOT = register(
		new Item(settings("electrum_ingot", false))
	);
	@UseDataGen(ElectrumNuggetDataGenerator.class)
	public static final Item ELECTRUM_NUGGET = register(
		new Item(settings("electrum_nugget", false))
	);

	public static void init() {
		FuelRegistryEvents.BUILD.register((FuelRegistry.Builder builder, FuelRegistryEvents.Context context) -> {
			int total = context.baseSmeltTime();
			builder.add(CRYSTAL_DEBRIS, total * 6);
		});
	}
}