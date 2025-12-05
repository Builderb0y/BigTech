package builderb0y.bigtech.datagen.impl.functional;

import java.util.Map;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blocks.BigTechProperties;
import builderb0y.bigtech.blocks.BuildingBlock.BuildingBlockMode;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Models;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.DecoItems;

public class BuildingBlockDataGenerator extends BasicBlockDataGenerator {

	public BuildingBlockDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		BuildingBlockMode mode = state.get(BigTechProperties.BUILDING_BLOCK_MODE);
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath("block/", this.getId(), '_' + mode.lowerCaseName).toString(),
			null,
			null
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		for (BuildingBlockMode mode : BuildingBlockMode.VALUES) {
			Identifier path = context.suffixPath(this.getId(), '_' + mode.lowerCaseName);
			context.writeToFile(
				context.blockModelPath(path),
				new Models.block.cube_column()
				.side(context.suffixPath(this.getId(), "_side"))
				.end(path)
				.toString()
			);
		}
	}

	@Override
	public void writeItemDefinitions(DataGenContext context) {
		StringBuilder builder = new StringBuilder(1024);
		builder.append(
			"""
			{
				"model": {
					"type": "minecraft:select",
					"property": "minecraft:block_state",
					"block_state_property": "mode",
					"fallback": {
						"type": "minecraft:model",
						"model": "bigtech:block/building_block_stack"
					},
					"cases": [
			"""
		);
		for (BuildingBlockMode mode : BuildingBlockMode.VALUES) {
			builder.append(
				context.replace(
					"""
					{
						"when": "%MODE",
						"model": {
							"type": "minecraft:model",
							"model": "bigtech:block/building_block_%MODE"
						}
					},""",
					Map.of("MODE", mode.lowerCaseName)
				)
				.indent('\t', 3)
			)
			.append('\n');
		}
		builder.setLength(builder.length() - 2);
		builder.append(
			"""

					]
				}
			}"""
		);
		context.writeToFile(context.itemDefinitionPath(this.getId()), builder.toString());
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.getId(), "_stack");
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.IRON).addElement(this.getId());
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("fmf", "ece", "fmf")
			.where('f', DecoItems.STEEL_FRAME)
			.where('m', BigTechItemTags.MAGNETITE_INGOTS)
			.where('e', ConventionalItemTags.ENDER_PEARLS)
			.where('c', BigTechItemTags.CRYSTAL_CLUSTERS)
			.result(this.getId())
			.toString()
		);
	}
}