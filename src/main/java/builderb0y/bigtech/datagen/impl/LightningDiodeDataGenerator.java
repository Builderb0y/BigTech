package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItemTags;

public class LightningDiodeDataGenerator extends BasicBlockDataGenerator {

	public LightningDiodeDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			//language=json
			"""
			{
				"parent": "minecraft:block/block",
				"ambientocclusion": false,
				"textures": {
					"diode": "bigtech:block/lightning_diode",
					"particle": "bigtech:block/lightning_diode"
				},
				"elements": [
					{
						"from": [  4,  0,  4 ],
						"to":   [ 12, 16, 12 ],
						"faces": {
							"north": { "uv": [ 0, 0,  8, 16 ], "texture": "#diode" },
							"east":  { "uv": [ 0, 0,  8, 16 ], "texture": "#diode" },
							"south": { "uv": [ 0, 0,  8, 16 ], "texture": "#diode" },
							"west":  { "uv": [ 0, 0,  8, 16 ], "texture": "#diode" },
							"up":    { "uv": [ 8, 0, 16,  8 ], "texture": "#diode" },
							"down":  { "uv": [ 8, 0, 16,  8 ], "texture": "#diode" }
						}
					}
				]
			}"""
		);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		Direction direction = state.get(Properties.FACING);
		if (state.get(Properties.POWERED)) {
			direction = direction.getOpposite();
		}
		return new BlockStateJsonVariant(
			state,
			context.prefixPath("block/", this.getId()).toString(),
			BlockStateJsonVariant.xFromUp(direction),
			BlockStateJsonVariant.yFromNorth(direction)
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.SHEARS).addElement(this.getId());
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
			.category(CraftingRecipeCategory.MISC)
			.pattern("crc")
			.where('c', BigTechItemTags.LIGHTNING_CABLES)
			.where('r', ConventionalItemTags.REDSTONE_DUSTS)
			.result(this.getId())
			.toString()
		);
	}
}