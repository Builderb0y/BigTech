package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.ArmorMaterials;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.armorMaterials.ArmorMaterialTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;

public class VanillaTweaksDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(MiningToolTags.AXE).add(BlockTags.LEAVES);
		context.getTags(ArmorMaterialTags.SHOCK_PROTECTION).addAll(ArmorMaterials.IRON, ArmorMaterials.GOLD, ArmorMaterials.CHAIN);
		context.writeToFile(
			context.blockModelPath(Identifier.ofVanilla("ladder")),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_ladder"))
			.blockTexture("ladder", Identifier.ofVanilla("ladder"))
			.toString()
		);
		for (String suffix : new String[] { "_center", "_north", "_east", "_south", "_west" }) {				context.writeToFile(
			context.blockModelPath(context.suffixPath(BigTechMod.modID("vanilla_iron_bars"), suffix)),
				new RetexturedModelBuilder()
				.blockParent(BigTechMod.modID("template_bars${suffix}"))
				.blockTexture("bars", Identifier.ofVanilla("iron_bars"))
				.toString()
			);
		}
		context.writeToFile(
			context.blockstatePath(Identifier.ofVanilla("iron_bars")),
			//language=json
			"""
			{
				"multipart": [
					{ "apply": { "model": "bigtech:block/vanilla_iron_bars_center" } },
					{ "apply": { "model": "bigtech:block/vanilla_iron_bars_north"  }, "when": { "north": "true" } },
					{ "apply": { "model": "bigtech:block/vanilla_iron_bars_east"   }, "when": { "east":  "true" } },
					{ "apply": { "model": "bigtech:block/vanilla_iron_bars_south"  }, "when": { "south": "true" } },
					{ "apply": { "model": "bigtech:block/vanilla_iron_bars_west"   }, "when": { "west":  "true" } }
				]
			}"""
		);
	}
}