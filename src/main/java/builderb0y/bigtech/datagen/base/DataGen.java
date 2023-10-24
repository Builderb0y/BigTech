package builderb0y.bigtech.datagen.base;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.tag.TagKey;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.formats.TableFormats.LangEntry;
import builderb0y.bigtech.datagen.formats.TableFormats.TagElement;
import builderb0y.bigtech.datagen.tables.Table;
import builderb0y.bigtech.items.BigTechItemGroups;
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
		return ROOT_DIRECTORIES != null && ROOT_DIRECTORIES.length != 0;
	}

	public static void run() {
		if (!isEnabled()) throw new IllegalStateException("DataGen not enabled.");
		LOGGER.info("Running data gen...");
		DataGenContext context = new DataGenContext();
		context.collectGenerators(BigTechBlocks          .class, Block          .class);
		context.collectGenerators(BigTechItems           .class, Item           .class);
		context.collectGenerators(BigTechBlockEntityTypes.class, BlockEntityType.class);
		context.collectGenerators(BigTechItemGroups      .class, ItemGroup      .class);
		context.empty("assets");
		context.empty("data");
		context.copy(new File(MANUAL_RESOURCES, "assets"), "assets");
		context.copy(new File(MANUAL_RESOURCES, "data"), "data");
		for (DataGenerator dataGenerator : context.generators) {
			dataGenerator.run(context);
		}
		{
			Table<LangEntry> table = new Table<>(LangEntry.FORMAT);
			context.lang.entrySet().stream().map(LangEntry::new).forEachOrdered(table.rows::add);
			context.writeToFile("assets/bigtech/lang/en_us.json", table.toString());
		}
		for (Map.Entry<TagKey<?>, TagBuilder> entry : context.tags.entrySet()) {
			Table<TagElement> table = new Table<>(TagElement.FORMAT);
			entry.value.stream().map(TagElement::new).forEachOrdered(table.rows::add);
			context.writeToFile(context.tagPath(entry.key), table.toString());
		}
		LOGGER.info("Done running data gen.");
		if (context.errored) try {
			Thread.sleep(5000L); //give me time to see the errors before more stuff gets printed.
		}
		catch (InterruptedException ignored) {}
	}
}