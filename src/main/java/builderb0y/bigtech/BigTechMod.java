package builderb0y.bigtech;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.util.Identifier;

import builderb0y.bigtech.api.BigTechInitializer;
import builderb0y.bigtech.api.ProficiencyCategory;
import builderb0y.bigtech.api.ProficiencyItem;
import builderb0y.bigtech.api.ProficiencyManager;
import builderb0y.bigtech.beams.impl.BeamTypes;
import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.commands.BigTechCommands;
import builderb0y.bigtech.compat.computercraft.ComputercraftCompat;
import builderb0y.bigtech.config.BigTechConfig;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.entities.BigTechEntityTypes;
import builderb0y.bigtech.items.BigTechItemGroups;
import builderb0y.bigtech.items.BigTechItems;
import builderb0y.bigtech.networking.BigTechNetwork;
import builderb0y.bigtech.particles.BigTechParticles;
import builderb0y.bigtech.recipes.BigTechRecipeBookCategories;
import builderb0y.bigtech.recipes.BigTechRecipeSerializers;
import builderb0y.bigtech.recipes.BigTechRecipeTypes;
import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.worldgen.BigTechFeatures;
import builderb0y.bigtech.worldgen.BigTechWorldgen;

public class BigTechMod implements ModInitializer {

	public static final String
		MODNAME = "Big Tech",
		MODID   = "bigtech";

	public static final Logger LOGGER = LoggerFactory.getLogger(MODNAME);
	public static final boolean AUDIT = Boolean.getBoolean("bigtech.mixinAudit");

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing...");
		BigTechConfig.init();
		BigTechDataComponents.init();
		BigTechBlocks.init();
		BigTechItems.init();
		BigTechItemGroups.init();
		BigTechBlockEntityTypes.init();
		BigTechEntityTypes.init();
		BeamTypes.init();
		BigTechNetwork.init();
		BigTechScreenHandlerTypes.init();
		BigTechParticles.init();
		BigTechRecipeTypes.init();
		BigTechRecipeSerializers.init();
		BigTechRecipeBookCategories.init();
		BigTechCommands.init();
		BigTechFeatures.init();
		BigTechWorldgen.init();
		ComputercraftCompat.init();
		ProficiencyManager.init();
		LOGGER.info("Running other mods' initializers...");
		FabricLoader.getInstance().getEntrypoints(MODID, BigTechInitializer.class).forEach(BigTechInitializer::init);
		ProficiencyCategory.init();
		ProficiencyItem.init();
		LOGGER.info("Done initializing.");
	}

	public static Identifier modID(String path) {
		return Identifier.of(MODID, path);
	}
}