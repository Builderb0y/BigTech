package builderb0y.bigtech.datagen.base;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandlerType;

import builderb0y.bigtech.blocks.DecoBlocks;
import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.blocks.MaterialBlocks;
import builderb0y.bigtech.damageTypes.BigTechDamageTypes;
import builderb0y.bigtech.datagen.formats.TableFormats.LangEntry;
import builderb0y.bigtech.datagen.formats.TableFormats.TagElement;
import builderb0y.bigtech.datagen.impl.ConfigDataGenerator;
import builderb0y.bigtech.datagen.impl.VanillaTweaksDataGenerator;
import builderb0y.bigtech.datagen.tables.Table;
import builderb0y.bigtech.entities.BigTechEntityTypes;
import builderb0y.bigtech.items.BigTechItemGroups;
import builderb0y.bigtech.items.DecoItems;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.items.MaterialItems;
import builderb0y.bigtech.particles.BigTechParticles;
import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;

public class DataGen {

	public static final Logger LOGGER = LoggerFactory.getLogger("${builderb0y.bigtech.BigTechMod.MODNAME}/DataGen");

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
		context.generators.add(new ConfigDataGenerator());
		context.generators.add(new VanillaTweaksDataGenerator());
		context.collectGenerators(MaterialBlocks           .class, Block            .class);
		context.collectGenerators(DecoBlocks               .class, Block            .class);
		context.collectGenerators(FunctionalBlocks         .class, Block            .class);
		context.collectGenerators(MaterialItems            .class, Item             .class);
		context.collectGenerators(DecoItems                .class, Item             .class);
		context.collectGenerators(FunctionalItems          .class, Item             .class);
		context.collectGenerators(BigTechEntityTypes       .class, EntityType       .class);
		context.collectGenerators(BigTechScreenHandlerTypes.class, ScreenHandlerType.class);
		context.collectGenerators(BigTechDamageTypes       .class, RegistryKey      .class);
		context.collectGenerators(BigTechItemGroups        .class, ItemGroup        .class);
		context.collectGenerators(BigTechParticles         .class, ParticleType     .class);
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
			entry.getValue().stream().map(TagElement::new).forEachOrdered(table.rows::add);
			context.writeToFile(context.tagPath(entry.getKey()), table.toString());
		}
		LOGGER.info("Done running data gen.");
		if (context.errored) try {
			Thread.sleep(5000L); //give me time to see the errors before more stuff gets printed.
		}
		catch (InterruptedException ignored) {}
	}
}