package builderb0y.bigtech.screenHandlers;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.datagen.impl.InventoryDataGenerator;
import builderb0y.bigtech.datagen.impl.MinerDataGenerator;

public class BigTechScreenHandlerTypes {

	@UseDataGen(InventoryDataGenerator.class)
	public static final ScreenHandlerType<SorterBeltScreenHandler> SORTER_BELT = register(
		"sorter_belt",
		SorterBeltScreenHandler::new
	);
	@UseDataGen(InventoryDataGenerator.class)
	public static final ScreenHandlerType<TransmuterScreenHandler> TRANSMUTER = register(
		"transmuter",
		TransmuterScreenHandler::new
	);
	@UseDataGen(InventoryDataGenerator.class)
	public static final ScreenHandlerType<DestroyerScreenHandler> DESTROYER = register(
		"destroyer",
		DestroyerScreenHandler::new
	);
	@UseDataGen(MinerDataGenerator.class)
	public static final ScreenHandlerType<MinerScreenHandler> MINER = register(
		"miner",
		MinerScreenHandler::new
	);
	@UseDataGen(InventoryDataGenerator.class)
	public static final ScreenHandlerType<IgnitorScreenHandler> IGNITOR = register(
		"ignitor",
		IgnitorScreenHandler::new
	);
	@UseDataGen(InventoryDataGenerator.class)
	public static final ScreenHandlerType<SilverIodideCannonScreenHandler> SILVER_IODIDE_CANNON = registerExtended(
		"silver_iodide_cannon",
		SilverIodideCannonScreenHandler::create
	);
	@UseDataGen(void.class)
	public static final ScreenHandlerType<StoneCraftingTableScreenHandler> STONE_CRAFTING_TABLE = register(
		"stone_crafting_table",
		StoneCraftingTableScreenHandler::new
	);

	public static <H extends ScreenHandler> ScreenHandlerType<H> register(String name, ScreenHandlerType.Factory<H> factory) {
		return Registry.register(Registries.SCREEN_HANDLER, BigTechMod.modID(name), new ScreenHandlerType<>(factory, FeatureSet.empty()));
	}

	public static <H extends ScreenHandler> ScreenHandlerType<H> registerExtended(String name, ExtendedScreenHandlerType.ExtendedFactory<H> factory) {
		return Registry.register(Registries.SCREEN_HANDLER, BigTechMod.modID(name), new ExtendedScreenHandlerType<>(factory));
	}

	public static void init() {}
}