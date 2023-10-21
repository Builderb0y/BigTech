package builderb0y.bigtech;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.util.Identifier;

import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.DataGen;
import builderb0y.bigtech.items.BigTechItemGroups;
import builderb0y.bigtech.items.BigTechItems;
import builderb0y.bigtech.networking.BigTechClientNetwork;
import builderb0y.bigtech.screenHandlers.BigTechScreenHandlerTypes;

public class BigTechMod implements ModInitializer {

	public static final String
		MODNAME = "Big Tech",
		MODID = "bigtech";

	public static final Logger LOGGER = LoggerFactory.getLogger(MODNAME);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing...");
		BigTechBlocks.init();
		BigTechItems.init();
		BigTechItemGroups.init();
		BigTechBlockEntityTypes.init();
		BigTechClientNetwork.init();
		BigTechScreenHandlerTypes.init();
		if (DataGen.isEnabled) DataGen.run();
		LOGGER.info("Done initializing.");
	}

	public static Identifier modID(String path) {
		return new Identifier(MODID, path);
	}
}