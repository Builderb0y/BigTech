package builderb0y.bigtech.datagen.impl;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

public class SpawnerInterceptorDataGenerator extends BasicBlockDataGenerator {

	public SpawnerInterceptorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_on")),
			//language=json
			"""
			{
				"jmxl": true,
				"ambientocclusion": false,
				"parent": "minecraft:block/thin_block",
				"textures": {
					"particle": "bigtech:block/spawner_interceptor_top_bottom_on",
					"main":     "bigtech:block/spawner_interceptor_top_bottom_on",
					"deco":     "bigtech:block/spawner_interceptor_sides_deco"
				},
				"elements": [
					{
						"from": [  1, 0,  1 ],
						"to":   [ 15, 3, 15 ],
						"faces": {
							"up":    { "uv": [ 1, 1, 15, 15 ], "texture": "#main" },
							"down":  { "uv": [ 1, 1, 15, 15 ], "texture": "#main" },
							"north": { "uv": [ 1, 0, 15,  3 ], "texture": "#deco" },
							"east":  { "uv": [ 1, 0, 15,  3 ], "texture": "#deco" },
							"south": { "uv": [ 1, 0, 15,  3 ], "texture": "#deco" },
							"west":  { "uv": [ 1, 0, 15,  3 ], "texture": "#deco" }
						}
					},
					{
						"shade": false,
						"jmxl_emissive": true,
						"from": [ 3, -4, 3 ],
						"to":   [ 5,  4, 5 ],
						"faces": {
							"up":    { "uv": [ 5, 12, 7, 14 ], "texture": "#deco" },
							"down":  { "uv": [ 5, 12, 7, 14 ], "texture": "#deco" },
							"north": { "uv": [ 4,  4, 6, 12 ], "texture": "#deco" },
							"east":  { "uv": [ 6,  4, 8, 12 ], "texture": "#deco" },
							"south": { "uv": [ 4,  4, 6, 12 ], "texture": "#deco" },
							"west":  { "uv": [ 6,  4, 8, 12 ], "texture": "#deco" }
						}
					},
					{
						"shade": false,
						"jmxl_emissive": true,
						"from": [ 3, -4, 11 ],
						"to":   [ 5,  4, 13 ],
						"faces": {
							"up":    { "uv": [ 5, 12, 7, 14 ], "texture": "#deco" },
							"down":  { "uv": [ 5, 12, 7, 14 ], "texture": "#deco" },
							"north": { "uv": [ 4,  4, 6, 12 ], "texture": "#deco" },
							"east":  { "uv": [ 6,  4, 8, 12 ], "texture": "#deco" },
							"south": { "uv": [ 4,  4, 6, 12 ], "texture": "#deco" },
							"west":  { "uv": [ 6,  4, 8, 12 ], "texture": "#deco" }
						}
					},
					{
						"shade": false,
						"jmxl_emissive": true,
						"from": [ 11, -4, 11 ],
						"to":   [ 13,  4, 13 ],
						"faces": {
							"up":    { "uv": [ 5, 12, 7, 14 ], "texture": "#deco" },
							"down":  { "uv": [ 5, 12, 7, 14 ], "texture": "#deco" },
							"north": { "uv": [ 4,  4, 6, 12 ], "texture": "#deco" },
							"east":  { "uv": [ 6,  4, 8, 12 ], "texture": "#deco" },
							"south": { "uv": [ 4,  4, 6, 12 ], "texture": "#deco" },
							"west":  { "uv": [ 6,  4, 8, 12 ], "texture": "#deco" }
						}
					},
					{
						"shade": false,
						"jmxl_emissive": true,
						"from": [ 11, -4, 3 ],
						"to":   [ 13,  4, 5 ],
						"faces": {
							"up":    { "uv": [ 5, 12, 7, 14 ], "texture": "#deco" },
							"down":  { "uv": [ 5, 12, 7, 14 ], "texture": "#deco" },
							"north": { "uv": [ 4,  4, 6, 12 ], "texture": "#deco" },
							"east":  { "uv": [ 6,  4, 8, 12 ], "texture": "#deco" },
							"south": { "uv": [ 4,  4, 6, 12 ], "texture": "#deco" },
							"west":  { "uv": [ 6,  4, 8, 12 ], "texture": "#deco" }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_off")),
			//language=json
			"""
			{
				"ambientocclusion": false,
				"parent": "minecraft:block/thin_block",
				"textures": {
					"particle": "bigtech:block/spawner_interceptor_top_bottom_off",
					"main":     "bigtech:block/spawner_interceptor_top_bottom_off",
					"deco":     "bigtech:block/spawner_interceptor_sides_deco"
				},
				"elements": [
					{
						"from": [  1, 0,  1 ],
						"to":   [ 15, 3, 15 ],
						"faces": {
							"up":    { "uv": [ 1, 1, 15, 15 ], "texture": "#main" },
							"down":  { "uv": [ 1, 1, 15, 15 ], "texture": "#main" },
							"north": { "uv": [ 1, 0, 15,  3 ], "texture": "#deco" },
							"east":  { "uv": [ 1, 0, 15,  3 ], "texture": "#deco" },
							"south": { "uv": [ 1, 0, 15,  3 ], "texture": "#deco" },
							"west":  { "uv": [ 1, 0, 15,  3 ], "texture": "#deco" }
						}
					},
					{
						"from": [ 3, -4, 3 ],
						"to":   [ 5,  4, 5 ],
						"faces": {
							"up":    { "uv": [  9, 12, 11, 14 ], "texture": "#deco" },
							"down":  { "uv": [  9, 12, 11, 14 ], "texture": "#deco" },
							"north": { "uv": [  8,  4, 10, 12 ], "texture": "#deco" },
							"east":  { "uv": [ 10,  4, 12, 12 ], "texture": "#deco" },
							"south": { "uv": [  8,  4, 10, 12 ], "texture": "#deco" },
							"west":  { "uv": [ 10,  4, 12, 12 ], "texture": "#deco" }
						}
					},
					{
						"from": [ 3, -4, 11 ],
						"to":   [ 5,  4, 13 ],
						"faces": {
							"up":    { "uv": [  9, 12, 11, 14 ], "texture": "#deco" },
							"down":  { "uv": [  9, 12, 11, 14 ], "texture": "#deco" },
							"north": { "uv": [  8,  4, 10, 12 ], "texture": "#deco" },
							"east":  { "uv": [ 10,  4, 12, 12 ], "texture": "#deco" },
							"south": { "uv": [  8,  4, 10, 12 ], "texture": "#deco" },
							"west":  { "uv": [ 10,  4, 12, 12 ], "texture": "#deco" }
						}
					},
					{
						"from": [ 11, -4, 11 ],
						"to":   [ 13,  4, 13 ],
						"faces": {
							"up":    { "uv": [  9, 12, 11, 14 ], "texture": "#deco" },
							"down":  { "uv": [  9, 12, 11, 14 ], "texture": "#deco" },
							"north": { "uv": [  8,  4, 10, 12 ], "texture": "#deco" },
							"east":  { "uv": [ 10,  4, 12, 12 ], "texture": "#deco" },
							"south": { "uv": [  8,  4, 10, 12 ], "texture": "#deco" },
							"west":  { "uv": [ 10,  4, 12, 12 ], "texture": "#deco" }
						}
					},
					{
						"from": [ 11, -4, 3 ],
						"to":   [ 13,  4, 5 ],
						"faces": {
							"up":    { "uv": [  9, 12, 11, 14 ], "texture": "#deco" },
							"down":  { "uv": [  9, 12, 11, 14 ], "texture": "#deco" },
							"north": { "uv": [  8,  4, 10, 12 ], "texture": "#deco" },
							"east":  { "uv": [ 10,  4, 12, 12 ], "texture": "#deco" },
							"south": { "uv": [  8,  4, 10, 12 ], "texture": "#deco" },
							"west":  { "uv": [ 10,  4, 12, 12 ], "texture": "#deco" }
						}
					}
				]
			}"""
		);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			new Table<>(BlockStateJsonVariant.FORMAT).addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.getBlock())
				.map((BlockState state) -> new BlockStateJsonVariant(
					state,
					context.prefixSuffixPath("block/", this.getId(), state.get(Properties.POWERED) ? "_on" : "_off").toString(),
					null,
					null
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.getId(), "_on");
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.DIAMOND).addElement(this.getId());
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
			.pattern(" r ", "ooo", "b b")
			.where('r', ConventionalItemTags.REDSTONE_DUSTS)
			.where('o', Items.OBSIDIAN)
			.where('b', ConventionalItemTags.BLAZE_RODS)
			.result(this.getId())
			.toString()
		);
	}
}