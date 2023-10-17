package builderb0y.bigtech.datagen.base;

import java.io.File;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.items.BigTechItems;

public class DataGen {

	public static final Logger LOGGER = LoggerFactory.getLogger("${BigTechMod.MODNAME}/DataGen");

	public static final File MANUAL_RESOURCES;
	static {
		String property = System.getProperty("bigtech.dev.dataGenInput");
		MANUAL_RESOURCES = property == null ? null : new File(property);
	}
	public static final File[] ROOT_DIRECTORIES;
	static {
		String property = System.getProperty("bigtech.dev.dataGenOutputs");
		ROOT_DIRECTORIES = (
			property == null
			? null
			: Pattern.compile(File.pathSeparator, Pattern.LITERAL)
			.splitAsStream(property)
			.map(File::new)
			.toArray(File[]::new)
		);
	}
	static {
		if ((MANUAL_RESOURCES == null) != (ROOT_DIRECTORIES == null)) {
			throw new IllegalStateException("Must specify both dataGenInput and dataGenOutputs, or neither of them.");
		}
	}

	public static boolean isEnabled() {
		return ROOT_DIRECTORIES != null;
	}

	public static void run() {
		if (!isEnabled()) throw new IllegalStateException("DataGen not enabled.");
		LOGGER.info("Running data gen...");
		DataGenContext context = new DataGenContext();
		context.collectGenerators(BigTechBlocks.class, Block.class);
		context.collectGenerators(BigTechItems .class, Item .class);
		context.empty("assets");
		context.empty("data");
		context.copy(new File(MANUAL_RESOURCES, "assets"), "assets");
		context.copy(new File(MANUAL_RESOURCES, "data"), "data");
		LOGGER.info("Done running data gen.");
	}
}