package builderb0y.bigtech.items;

import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

import net.minecraft.item.BlockItem;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.MaterialBlocks;
import builderb0y.bigtech.circuits.*;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.material.*;
import builderb0y.bigtech.datagen.impl.material.circuits.*;
import builderb0y.bigtech.datagen.impl.material.electrum.ElectrumBlockDataGenerator;
import builderb0y.bigtech.datagen.impl.material.electrum.ElectrumIngotDataGenerator;
import builderb0y.bigtech.datagen.impl.material.electrum.ElectrumNuggetDataGenerator;
import builderb0y.bigtech.datagen.impl.material.glowstone.GlowstoneAlloyBlockDataGenerator;
import builderb0y.bigtech.datagen.impl.material.glowstone.GlowstoneAlloyIngotDataGenerator;
import builderb0y.bigtech.datagen.impl.material.glowstone.GlowstoneAlloyNuggetDataGenerator;
import builderb0y.bigtech.datagen.impl.material.lapis.LapisAlloyBlockDataGenerator;
import builderb0y.bigtech.datagen.impl.material.lapis.LapisAlloyIngotDataGenerator;
import builderb0y.bigtech.datagen.impl.material.lapis.LapisAlloyNuggetDataGenerator;
import builderb0y.bigtech.datagen.impl.material.photovoltaic.PhotovoltaicEmitterDataGenerator;
import builderb0y.bigtech.datagen.impl.material.photovoltaic.PhotovoltaicReceiverDataGenerator;
import builderb0y.bigtech.datagen.impl.material.redstone.RedstoneAlloyBlockDataGenerator;
import builderb0y.bigtech.datagen.impl.material.redstone.RedstoneAlloyIngotDataGenerator;
import builderb0y.bigtech.datagen.impl.material.redstone.RedstoneAlloyNuggetDataGenerator;
import builderb0y.bigtech.datagen.impl.material.silver.*;
import builderb0y.bigtech.datagen.impl.material.steel.SteelBlockDataGenerator;
import builderb0y.bigtech.datagen.impl.material.steel.SteelIngotDataGenerator;
import builderb0y.bigtech.datagen.impl.material.steel.SteelNuggetDataGenerator;
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
	@UseDataGen(SiliconBlockDataGenerator.class)
	public static final BlockItem SILICON_BLOCK = registerPlacer(
		MaterialBlocks.SILICON_BLOCK
	);
	@UseDataGen(SiliconDataGenerator.class)
	public static final Item SILICON = register(
		new Item(settings("silicon", false))
	);
	@UseDataGen(SourceCircuitDataGenerator.class)
	public static final Item SOURCE_CIRCUIT = register(
		new Item(
			settings("source_circuit", false).component(
				BigTechDataComponents.CIRCUIT,
				SourceCircuitComponent.getInstance(0)
			)
		)
	);
	@UseDataGen(WireCircuitDataGenerator.class)
	public static final Item WIRE_CIRCUIT = register(
		new Item(
			settings("wire_circuit", false).component(
				BigTechDataComponents.CIRCUIT,
				WireCircuitComponent.getInstance(0)
			)
		)
	);
	@UseDataGen(CrossoverCircuitDataGenerator.class)
	public static final Item CROSSOVER_CIRCUIT = register(
		new Item(
			settings("crossover_circuit", false).component(
				BigTechDataComponents.CIRCUIT,
				CrossoverCircuitComponent.getInstance(0, 0)
			)
		)
	);
	@UseDataGen(NotGateCircuitDataGenerator.class)
	public static final Item NOT_GATE_CIRCUIT = register(
		new Item(
			settings("not_gate_circuit", false).component(
				BigTechDataComponents.CIRCUIT,
				NotGateCircuitComponent.getInstance((byte)(0))
			)
		)
	);
	@UseDataGen(DiodeCircuitDataGenerator.class)
	public static final Item DIODE_CIRCUIT = register(
		new Item(
			settings("diode_circuit", false).component(
				BigTechDataComponents.CIRCUIT,
				DiodeCircuitComponent.getInstance((byte)(0))
			)
		)
	);
	@UseDataGen(ComparatorCircuitDataGenerator.class)
	public static final Item COMPARATOR_CIRCUIT = register(
		new Item(
			settings("comparator_circuit", false).component(
				BigTechDataComponents.CIRCUIT,
				ComparatorCircuitComponent.getInstance((byte)(0))
			)
		)
	);
	@UseDataGen(SubtracterCircuitDataGenerator.class)
	public static final Item SUBTRACTER_CIRCUIT = register(
		new Item(
			settings("subtracter_circuit", false).component(
				BigTechDataComponents.CIRCUIT,
				SubtracterCircuitComponent.getInstance((byte)(0))
			)
		)
	);
	@UseDataGen(RedstoneAlloyBlockDataGenerator.class)
	public static final BlockItem REDSTONE_ALLOY_BLOCK = registerPlacer(
		MaterialBlocks.REDSTONE_ALLOY_BLOCK
	);
	@UseDataGen(RedstoneAlloyIngotDataGenerator.class)
	public static final Item REDSTONE_ALLOY_INGOT = register(
		new Item(settings("redstone_alloy_ingot", false))
	);
	@UseDataGen(RedstoneAlloyNuggetDataGenerator.class)
	public static final Item REDSTONE_ALLOY_NUGGET = register(
		new Item(settings("redstone_alloy_nugget", false))
	);
	@UseDataGen(LapisAlloyBlockDataGenerator.class)
	public static final BlockItem LAPIS_ALLOY_BLOCK = registerPlacer(
		MaterialBlocks.LAPIS_ALLOY_BLOCK
	);
	@UseDataGen(LapisAlloyIngotDataGenerator.class)
	public static final Item LAPIS_ALLOY_INGOT = register(
		new Item(settings("lapis_alloy_ingot", false))
	);
	@UseDataGen(LapisAlloyNuggetDataGenerator.class)
	public static final Item LAPIS_ALLOY_NUGGET = register(
		new Item(settings("lapis_alloy_nugget", false))
	);
	@UseDataGen(GlowstoneAlloyBlockDataGenerator.class)
	public static final BlockItem GLOWSTONE_ALLOY_BLOCK = registerPlacer(
		MaterialBlocks.GLOWSTONE_ALLOY_BLOCK
	);
	@UseDataGen(GlowstoneAlloyIngotDataGenerator.class)
	public static final Item GLOWSTONE_ALLOY_INGOT = register(
		new Item(settings("glowstone_alloy_ingot", false))
	);
	@UseDataGen(GlowstoneAlloyNuggetDataGenerator.class)
	public static final Item GLOWSTONE_ALLOY_NUGGET = register(
		new Item(settings("glowstone_alloy_nugget", false))
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
	@UseDataGen(PhotovoltaicEmitterDataGenerator.class)
	public static final Item PHOTOVOLTAIC_EMITTER = register(
		new Item(settings("photovoltaic_emitter", false))
	);
	@UseDataGen(PhotovoltaicReceiverDataGenerator.class)
	public static final Item PHOTOVOLTAIC_RECEIVER = register(
		new Item(settings("photovoltaic_receiver", false))
	);

	public static void init() {
		FuelRegistryEvents.BUILD.register((FuelRegistry.Builder builder, FuelRegistryEvents.Context context) -> {
			int total = context.baseSmeltTime();
			builder.add(CRYSTAL_DEBRIS, total * 6);
		});
	}
}