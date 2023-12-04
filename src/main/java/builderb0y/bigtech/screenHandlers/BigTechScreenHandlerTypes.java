package builderb0y.bigtech.screenHandlers;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import builderb0y.bigtech.BigTechMod;

public class BigTechScreenHandlerTypes {

	public static final ScreenHandlerType<SorterBeltScreenHandler> SORTER_BELT = register(
		"sorter_belt",
		SorterBeltScreenHandler::new
	);
	public static final ScreenHandlerType<TransmuterScreenHandler> TRANSMUTER = register(
		"transmuter",
		TransmuterScreenHandler::new
	);
	public static final ScreenHandlerType<DestroyerScreenHandler> DESTROYER = register(
		"destroyer",
		DestroyerScreenHandler::new
	);

	public static <H extends ScreenHandler> ScreenHandlerType<H> register(String name, ScreenHandlerType.Factory<H> factory) {
		return Registry.register(Registries.SCREEN_HANDLER, BigTechMod.modID(name), new ScreenHandlerType<>(factory, FeatureSet.empty()));
	}

	public static void init() {}
}