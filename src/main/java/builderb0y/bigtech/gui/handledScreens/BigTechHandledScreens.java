package builderb0y.bigtech.gui.handledScreens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;

@Environment(EnvType.CLIENT)
public class BigTechHandledScreens {

	public static void initClient() {
		HandledScreens.register(BigTechScreenHandlerTypes.SORTER_BELT,                        SorterBeltHandledScreen::new);
		HandledScreens.register(BigTechScreenHandlerTypes.TRANSMUTER,                         TransmuterHandledScreen::new);
		HandledScreens.register(BigTechScreenHandlerTypes.DESTROYER,                           DestroyerHandledScreen::new);
		HandledScreens.register(BigTechScreenHandlerTypes.MINER,                                   MinerHandledScreen::new);
		HandledScreens.register(BigTechScreenHandlerTypes.IGNITOR,                               IgnitorHandledScreen::new);
		HandledScreens.register(BigTechScreenHandlerTypes.SILVER_IODIDE_CANNON,       SilverIodideCannonHandledScreen::new);
		HandledScreens.register(BigTechScreenHandlerTypes.STONE_CRAFTING_TABLE,                        CraftingScreen::new);
		HandledScreens.register(BigTechScreenHandlerTypes.SPAWNER_INTERCEPTOR,        SpawnerInterceptorHandledScreen::new);
		HandledScreens.register(BigTechScreenHandlerTypes.CONDUCTIVE_ANVIL,              ConductiveAnvilHandledScreen::new);
		HandledScreens.register(BigTechScreenHandlerTypes.PLACED_TECHNO_CRAFTER,     PlacedTechnoCrafterHandledScreen::new);
		HandledScreens.register(BigTechScreenHandlerTypes.PORTABLE_TECHNO_CRAFTER, PortableTechnoCrafterHandledScreen::new);
		HandledScreens.register(BigTechScreenHandlerTypes.DISLOCATOR,                         DislocatorHandledScreen::new);
	}
}