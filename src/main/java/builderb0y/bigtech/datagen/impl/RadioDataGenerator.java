package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Formatting;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;

public class RadioDataGenerator extends BasicBlockDataGenerator {

	public RadioDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixPath("block/", this.getId()).toString(),
			null,
			BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			//language=json
			"""
			{
				"parent": "block/block",
				"textures": {
					"radio":    "bigtech:block/radio",
					"particle": "bigtech:block/radio"
				},
				"elements": [
					{
						"from": [  4, 0,  6 ],
						"to":   [ 12, 4, 10 ],
						"faces": {
							"up":    { "uv": [ 8, 12, 16, 16 ], "texture": "#radio" },
							"down":  { "uv": [ 8, 12, 16, 16 ], "texture": "#radio", "cullface": "down" },
							"north": { "uv": [ 8, 12, 16, 16 ], "texture": "#radio" },
							"east":  { "uv": [ 0,  8,  4, 12 ], "texture": "#radio" },
							"south": { "uv": [ 0, 12,  8, 16 ], "texture": "#radio" },
							"west":  { "uv": [ 0,  8,  4, 12 ], "texture": "#radio" }
						}
					},
					{
						"from": [ 10, 4, 8 ],
						"to":   [ 11, 9, 9 ],
						"faces": {
							"up":    { "uv": [ 6, 7, 7,  8 ], "texture": "#radio" },
							"north": { "uv": [ 6, 7, 7, 12 ], "texture": "#radio" },
							"east":  { "uv": [ 6, 7, 7, 12 ], "texture": "#radio" },
							"south": { "uv": [ 6, 7, 7, 12 ], "texture": "#radio" },
							"west":  { "uv": [ 6, 7, 7, 12 ], "texture": "#radio" }
						}
					}
				]
			}"""
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {

	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {

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
			.pattern(" t", "ii")
			.where('t', Items.REDSTONE_TORCH)
			.where('i', ConventionalItemTags.IRON_INGOTS)
			.result(this.getId())
			.toString()
		);
	}

	@Override
	public void setupLang(DataGenContext context) {
		super.setupLang(context);
		context.lang.put("tooltip.bigtech.radio.usage", "Attach to a ComputerCraft computer as a peripheral to control miners");
		context.lang.put("tooltip.bigtech.radio.computercraft_not_installed", Formatting.RED + "This block is useless without ComputerCraft installed!");
	}
}