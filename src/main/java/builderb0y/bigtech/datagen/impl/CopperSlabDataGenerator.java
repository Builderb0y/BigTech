package builderb0y.bigtech.datagen.impl;

import java.util.Map;

import net.minecraft.block.Oxidizable.OxidationLevel;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.blocks.DecoBlocks;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

@Dependencies(CommonCopperSlabDataGenerator.class)
public class CopperSlabDataGenerator extends BasicBlockDataGenerator {

	public final CopperRegistrableCollection.Type type;

	public CopperSlabDataGenerator(BlockItem blockItem, CopperRegistrableCollection.Type type) {
		super(blockItem);
		this.type = type;
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.block)
				.map(state -> new BlockStateJsonVariant(
					state,
					context.prefixSuffixPath("block/", this.id, '_' + state.get(Properties.SLAB_TYPE).asString()).toString(),
					null,
					null
				))
				::iterator
			)
			.toString()
		);
	}

	public Identifier vanillaTexture() {
		//inconsistent naming schemes go brrrrrr!
		return new Identifier("minecraft", this.type.level == OxidationLevel.UNAFFECTED ? "copper_block" : this.type.notWaxed().lowerCaseName);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_double")),
			new RetexturedModelBuilder()
			.blockParent(new Identifier("minecraft", "cube_bottom_top"))
			.blockTexture("bottom", this.vanillaTexture())
			.blockTexture("top", this.vanillaTexture())
			.blockTexture("side", BigTechMod.modID(this.type.notWaxed().prefix + "slab"))
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_bottom")),
			new RetexturedModelBuilder()
			.blockParent(new Identifier("minecraft", "slab"))
			.blockTexture("bottom", this.vanillaTexture())
			.blockTexture("top", this.vanillaTexture())
			.blockTexture("side", BigTechMod.modID(this.type.notWaxed().prefix + "slab"))
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_top")),
			new RetexturedModelBuilder()
			.blockParent(new Identifier("minecraft", "slab_top"))
			.blockTexture("bottom", this.vanillaTexture())
			.blockTexture("top", this.vanillaTexture())
			.blockTexture("side", BigTechMod.modID(this.type.notWaxed().prefix + "slab"))
			.toString()
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.id, "_bottom");
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		//handled by dependency.
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//handled by dependency.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.COPPER_SLABS).addElement(this.id);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.COPPER_SLABS).addElement(this.id);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.id, "_from_blocks")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.BUILDING)
			.group(BigTechMod.modID("copper_slabs"))
			.pattern("bbb")
			.where('b', BigTechBlocks.VANILLA_COPPER_BLOCKS.get(this.type))
			.result(this.id)
			.count(6)
			.toString()
		);
		if (this.type.waxed) {
			context.writeToFile(
				context.recipePath(context.suffixPath(this.id, "_from_honeycomb")),
				new ShapelessRecipeBuilder()
				.group(BigTechMod.modID("copper_slabs"))
				.ingredient(DecoBlocks.COPPER_SLABS.get(this.type.notWaxed()))
				.ingredient(DecoBlocks.COPPER_SLABS.get(this.type.notWaxed()))
				.ingredient(Items.HONEYCOMB)
				.result(this.id)
				.count(2)
				.toString()
			);
		}
		context.writeToFile(
			context.recipePath(context.suffixPath(this.id, "_from_stonecutting")),
			context.replace(
				//language=json
				"""
				{
					"type": "minecraft:stonecutting",
					"count": 2,
					"ingredient": { "item": "%INPUT" },
					"result": "%OUTPUT"
				}""",
				Map.of(
					"INPUT", Registries.BLOCK.getId(BigTechBlocks.VANILLA_COPPER_BLOCKS.get(this.type)).toString(),
					"OUTPUT", this.id.toString()
				)
			)
		);
	}
}