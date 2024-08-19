package builderb0y.bigtech.datagen.impl;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

public class WoodenHopperDataGenerator extends BasicBlockDataGenerator {

	public WoodenHopperDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.getBlock())
				.map((BlockState state) -> new BlockStateJsonVariant(
					state,
					context
					.prefixSuffixPath(
						"block/",
						this.getId(),
						state.get(Properties.HOPPER_FACING) == Direction.DOWN ? "_down" : "_north"
					)
					.toString(),
					null,
					BlockStateJsonVariant.yFromNorth(state.get(Properties.HOPPER_FACING))
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_down")),
			//language=json
			"""
			{
				"parent": "minecraft:block/block",
				"ambientocclusion": false,
				"textures": {
					"base_top":    "bigtech:block/wooden_hopper_base_top",
					"base_bottom": "bigtech:block/wooden_hopper_base_bottom",
					"base_side":   "bigtech:block/wooden_hopper_base_side",
					"base_inside": "bigtech:block/wooden_hopper_base_inside",
					"extension":   "bigtech:block/wooden_hopper_extension",
					"particle":    "bigtech:block/wooden_hopper_extension"
				},
				"elements": [
					{
						"from": [  0, 10,  0 ],
						"to":   [ 16, 16, 16 ],
						"faces": {
							"up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#base_top", "cullface": "up" },
							"down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#base_bottom" },
							"north": { "uv": [ 0, 0, 16,  6 ], "texture": "#base_side"   },
							"east":  { "uv": [ 0, 0, 16,  6 ], "texture": "#base_side"   },
							"south": { "uv": [ 0, 0, 16,  6 ], "texture": "#base_side"   },
							"west":  { "uv": [ 0, 0, 16,  6 ], "texture": "#base_side"   }
						}
					},
					{
						"from": [  2, 16,  2 ],
						"to":   [ 14, 11, 14 ],
						"faces": {
							"up":    { "uv": [ 2,  2, 14, 14 ], "texture": "#base_inside", "cullface": "up" },
							"north": { "uv": [ 2, 11, 14,  6 ], "texture": "#base_side",   "cullface": "up" },
							"east":  { "uv": [ 2, 11, 14,  6 ], "texture": "#base_side",   "cullface": "up" },
							"south": { "uv": [ 2, 11, 14,  6 ], "texture": "#base_side",   "cullface": "up" },
							"west":  { "uv": [ 2, 11, 14,  6 ], "texture": "#base_side",   "cullface": "up" }
						}
					},
					{
						"from": [  4,  4,  4 ],
						"to":   [ 12, 10, 12 ],
						"faces": {
							"down":  { "uv": [ 8, 0, 16, 8 ], "texture": "#extension" },
							"north": { "uv": [ 0, 0,  8, 6 ], "texture": "#extension" },
							"east":  { "uv": [ 0, 0,  8, 6 ], "texture": "#extension" },
							"south": { "uv": [ 0, 0,  8, 6 ], "texture": "#extension" },
							"west":  { "uv": [ 0, 0,  8, 6 ], "texture": "#extension" }
						}
					},
					{
						"from": [  6,  0, 6 ],
						"to":   [ 10, 4, 10 ],
						"faces": {
							"down":  { "uv": [ 0, 12, 4, 16 ], "texture": "#extension", "cullface": "down" },
							"north": { "uv": [ 0, 12, 4, 16 ], "texture": "#extension" },
							"east":  { "uv": [ 0, 12, 4, 16 ], "texture": "#extension" },
							"south": { "uv": [ 0, 12, 4, 16 ], "texture": "#extension" },
							"west":  { "uv": [ 0, 12, 4, 16 ], "texture": "#extension" }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_north")),
			//language=json
			"""
			{
				"parent": "minecraft:block/block",
				"ambientocclusion": false,
				"textures": {
					"base_top":    "bigtech:block/wooden_hopper_base_top",
					"base_bottom": "bigtech:block/wooden_hopper_base_bottom",
					"base_side":   "bigtech:block/wooden_hopper_base_side",
					"base_inside": "bigtech:block/wooden_hopper_base_inside",
					"extension":   "bigtech:block/wooden_hopper_extension",
					"particle":    "bigtech:block/wooden_hopper_extension"
				},
				"elements": [
					{
						"from": [  0, 10,  0 ],
						"to":   [ 16, 16, 16 ],
						"faces": {
							"up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#base_top", "cullface": "up" },
							"down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#base_bottom" },
							"north": { "uv": [ 0, 0, 16,  6 ], "texture": "#base_side"   },
							"east":  { "uv": [ 0, 0, 16,  6 ], "texture": "#base_side"   },
							"south": { "uv": [ 0, 0, 16,  6 ], "texture": "#base_side"   },
							"west":  { "uv": [ 0, 0, 16,  6 ], "texture": "#base_side"   }
						}
					},
					{
						"from": [  2, 16,  2 ],
						"to":   [ 14, 11, 14 ],
						"faces": {
							"up":    { "uv": [ 2,  2, 14, 14 ], "texture": "#base_inside", "cullface": "up" },
							"north": { "uv": [ 2, 11, 14,  6 ], "texture": "#base_side",   "cullface": "up" },
							"east":  { "uv": [ 2, 11, 14,  6 ], "texture": "#base_side",   "cullface": "up" },
							"south": { "uv": [ 2, 11, 14,  6 ], "texture": "#base_side",   "cullface": "up" },
							"west":  { "uv": [ 2, 11, 14,  6 ], "texture": "#base_side",   "cullface": "up" }
						}
					},
					{
						"from": [  4,  4,  4 ],
						"to":   [ 12, 10, 12 ],
						"faces": {
							"down":  { "uv": [ 8, 8, 16, 16 ], "texture": "#extension" },
							"north": { "uv": [ 0, 6,  8, 12 ], "texture": "#extension" },
							"east":  { "uv": [ 0, 0,  8,  6 ], "texture": "#extension" },
							"south": { "uv": [ 0, 0,  8,  6 ], "texture": "#extension" },
							"west":  { "uv": [ 0, 0,  8,  6 ], "texture": "#extension" }
						}
					},
					{
						"from": [  6, 4, 0 ],
						"to":   [ 10, 8, 4 ],
						"faces": {
							"up":    { "uv": [ 0, 12, 4, 16 ], "texture": "#extension" },
							"down":  { "uv": [ 4, 12, 8, 16 ], "texture": "#extension" },
							"north": { "uv": [ 0, 12, 4, 16 ], "texture": "#extension", "cullface": "north" },
							"east":  { "uv": [ 0, 12, 4, 16 ], "texture": "#extension" },
							"south": { "uv": [ 0, 12, 4, 16 ], "texture": "#extension" },
							"west":  { "uv": [ 0, 12, 4, 16 ], "texture": "#extension" }
						}
					}
				]
			}"""
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.getId(), "_down");
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.AXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//wood is fine.
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
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern("p p", "p p", " p ")
			.where('p', ItemTags.PLANKS)
			.result(this.getId())
			.toString()
		);
	}
}