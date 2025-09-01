package builderb0y.bigtech.datagen.impl.functional;

import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blocks.BigTechProperties;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.DecoItems;
import builderb0y.bigtech.items.FunctionalItems;

public class AssemblerDataGenerator extends BasicBlockDataGenerator {

	public AssemblerDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupLang(DataGenContext context) {
		super.setupLang(context);
		context.lang.put("gui.bigtech.assembler.width", "Width: %d");
		context.lang.put("gui.bigtech.assembler.height", "Height: %d");
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
	public void writeLootTableJson(DataGenContext context) {
		this.writeBlockEntityComponentCopyingLootTableJson(context, DataComponentTypes.CUSTOM_NAME);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		for (boolean active : new boolean[] { true, false }) {
			context.writeToFile(
				context.blockModelPath(context.suffixPath(this.getId(), active ? "_on" : "_off")),
				context.replace(
					//language=json
					"""
					{
						"credit": "Made with Blockbench",
						"parent": "minecraft:block/block",
						"textures": {
							"frame_outer": "bigtech:block/steel_frame_outer",
							"frame_inner": "bigtech:block/steel_frame_inner",
							"top_bottom":  "bigtech:block/assembler_top",
							"inner_core":  "bigtech:block/assembler_inner",
							"glow":        "bigtech:block/assembler_glow",
							"particle":    "bigtech:block/steel_frame_outer"
						},
						"elements": [
							{
								"from": [  0,  0, 0 ],
								"to":   [ 16, 16, 3 ],
								"faces": {
									"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#frame_outer", "cullface": "north" },
									"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#frame_inner" }
								}
							},
							{
								"from": [  0,  0, 13 ],
								"to":   [ 16, 16, 16 ],
								"faces": {
									"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#frame_inner" },
									"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#frame_outer", "cullface": "south" }
								}
							},
							{
								"from": [ 13,  0,  0 ],
								"to":   [ 16, 16, 16 ],
								"faces": {
									"east": { "uv": [ 0, 0, 16, 16 ], "texture": "#frame_outer", "cullface": "east" },
									"west": { "uv": [ 0, 0, 16, 16 ], "texture": "#frame_inner" }
								}
							},
							{
								"from": [ 0,  0,  0 ],
								"to":   [ 3, 16, 16 ],
								"faces": {
									"east": { "uv": [ 0, 0, 16, 16 ], "texture": "#frame_inner" },
									"west": { "uv": [ 0, 0, 16, 16 ], "texture": "#frame_outer", "cullface": "west" }
								}
							},
							{
								"from": [  0, 13,  0 ],
								"to":   [ 16, 16, 16 ],
								"faces": {
									"up":   { "uv": [ 0, 0, 16, 16 ], "texture": "#top_bottom", "cullface": "up" },
									"down": { "uv": [ 0, 0, 16, 16 ], "texture": "#frame_inner" }
								}
							},
							{
								"from": [  0, 0,  0 ],
								"to":   [ 16, 3, 16 ],
								"faces": {
									"up":   { "uv": [ 0, 0, 16, 16 ], "texture": "#frame_inner" },
									"down": { "uv": [ 0, 0, 16, 16 ], "texture": "#top_bottom", "cullface": "down" }
								}
							},
							{
								"from": [  4,  4, 3 ],
								"to":   [ 12, 12, 2 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"east": { "uv": [  3,  4,  4, 12 ], "texture": "#glow", "cullface": "north", "tintindex": 0 },
									"west": { "uv": [ 12,  4, 13, 12 ], "texture": "#glow", "cullface": "north", "tintindex": 0 },
									"up":   { "uv": [  4,  3, 12,  4 ], "texture": "#glow", "cullface": "north", "tintindex": 0, "rotation": 180 },
									"down": { "uv": [  4, 12, 12, 13 ], "texture": "#glow", "cullface": "north", "tintindex": 0, "rotation": 180 }
								}
							},
							{
								"from": [ 7, 7, 3 ],
								"to":   [ 9, 9, 2 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"east": { "uv": [ 6, 7,  7,  9 ], "texture": "#glow", "cullface": "north", "tintindex": 0 },
									"west": { "uv": [ 9, 7, 10,  9 ], "texture": "#glow", "cullface": "north", "tintindex": 0 },
									"up":   { "uv": [ 7, 6,  9,  7 ], "texture": "#glow", "cullface": "north", "tintindex": 0, "rotation": 180 },
									"down": { "uv": [ 7, 9,  9, 10 ], "texture": "#glow", "cullface": "north", "tintindex": 0, "rotation": 180 }
								}
							},
							{
								"from": [  6,  6, 2 ],
								"to":   [ 10, 10, 3 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"east": { "uv": [ 6, 6,  7, 10 ], "texture": "#glow", "cullface": "north", "tintindex": 0 },
									"west": { "uv": [ 9, 6, 10, 10 ], "texture": "#glow", "cullface": "north", "tintindex": 0 },
									"up":   { "uv": [ 6, 6, 10,  7 ], "texture": "#glow", "cullface": "north", "tintindex": 0, "rotation": 180 },
									"down": { "uv": [ 6, 9, 10, 10 ], "texture": "#glow", "cullface": "north", "tintindex": 0, "rotation": 180 }
								}
							},
							{
								"from": [ 14,  4,  4 ],
								"to":   [ 13, 12, 12 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"north": { "uv": [ 12,  4, 13, 12 ], "texture": "#glow", "cullface": "east", "tintindex": 0 },
									"south": { "uv": [  3,  4,  4, 12 ], "texture": "#glow", "cullface": "east", "tintindex": 0 },
									"up":    { "uv": [  4,  3, 12,  4 ], "texture": "#glow", "cullface": "east", "tintindex": 0, "rotation": 270 },
									"down":  { "uv": [  4, 12, 12, 13 ], "texture": "#glow", "cullface": "east", "tintindex": 0, "rotation":  90 }
								}
							},
							{
								"from": [ 14, 7, 7 ],
								"to":   [ 13, 9, 9 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"north": { "uv": [ 9, 7, 10,  9 ], "texture": "#glow", "cullface": "east", "tintindex": 0 },
									"south": { "uv": [ 6, 7,  7,  9 ], "texture": "#glow", "cullface": "east", "tintindex": 0 },
									"up":    { "uv": [ 7, 6,  9,  7 ], "texture": "#glow", "cullface": "east", "tintindex": 0, "rotation": 270 },
									"down":  { "uv": [ 7, 9,  9, 10 ], "texture": "#glow", "cullface": "east", "tintindex": 0, "rotation":  90 }
								}
							},
							{
								"from": [ 13,  6,  6 ],
								"to":   [ 14, 10, 10 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"north": { "uv": [ 9, 6, 10, 10 ], "texture": "#glow", "cullface": "east", "tintindex": 0 },
									"south": { "uv": [ 6, 6,  7, 10 ], "texture": "#glow", "cullface": "east", "tintindex": 0 },
									"up":    { "uv": [ 6, 6, 10,  7 ], "texture": "#glow", "cullface": "east", "tintindex": 0, "rotation": 270 },
									"down":  { "uv": [ 6, 9, 10, 10 ], "texture": "#glow", "cullface": "east", "tintindex": 0, "rotation":  90 }
								}
							},
							{
								"from": [  4,  4, 14 ],
								"to":   [ 12, 12, 13 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"east": { "uv": [ 12,  4, 13, 12 ], "texture": "#glow", "cullface": "south", "tintindex": 0 },
									"west": { "uv": [  3,  4,  4, 12 ], "texture": "#glow", "cullface": "south", "tintindex": 0 },
									"up":   { "uv": [  4,  3, 12,  4 ], "texture": "#glow", "cullface": "south", "tintindex": 0 },
									"down": { "uv": [  4, 12, 12, 13 ], "texture": "#glow", "cullface": "south", "tintindex": 0 }
								}
							},
							{
								"from": [ 7, 7, 14 ],
								"to":   [ 9, 9, 13 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"east": { "uv": [ 9, 7, 10,  9 ], "texture": "#glow", "cullface": "south", "tintindex": 0 },
									"west": { "uv": [ 6, 7,  7,  9 ], "texture": "#glow", "cullface": "south", "tintindex": 0 },
									"up":   { "uv": [ 7, 6,  9,  7 ], "texture": "#glow", "cullface": "south", "tintindex": 0 },
									"down": { "uv": [ 7, 9,  9, 10 ], "texture": "#glow", "cullface": "south", "tintindex": 0 }
								}
							},
							{
								"from": [  6,  6, 13 ],
								"to":   [ 10, 10, 14 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"east": { "uv": [ 9, 6, 10, 10 ], "texture": "#glow", "cullface": "south", "tintindex": 0 },
									"west": { "uv": [ 6, 6,  7, 10 ], "texture": "#glow", "cullface": "south", "tintindex": 0 },
									"up":   { "uv": [ 6, 6, 10,  7 ], "texture": "#glow", "cullface": "south", "tintindex": 0 },
									"down": { "uv": [ 6, 9, 10, 10 ], "texture": "#glow", "cullface": "south", "tintindex": 0 }
								}
							},
							{
								"from": [ 3,  4,  4 ],
								"to":   [ 2, 12, 12 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"north": { "uv": [  3,  4,  4, 12 ], "texture": "#glow", "cullface": "west", "tintindex": 0 },
									"south": { "uv": [ 12,  4, 13, 12 ], "texture": "#glow", "cullface": "west", "tintindex": 0 },
									"up":    { "uv": [  4,  3, 12,  4 ], "texture": "#glow", "cullface": "west", "tintindex": 0, "rotation":  90 },
									"down":  { "uv": [  4, 12, 12, 13 ], "texture": "#glow", "cullface": "west", "tintindex": 0, "rotation": 270 }
								}
							},
							{
								"from": [ 3, 7, 7 ],
								"to":   [ 2, 9, 9 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"north": { "uv": [ 6, 7,  7,  9 ], "texture": "#glow", "cullface": "west", "tintindex": 0 },
									"south": { "uv": [ 9, 7, 10,  9 ], "texture": "#glow", "cullface": "west", "tintindex": 0 },
									"up":    { "uv": [ 7, 6,  9,  7 ], "texture": "#glow", "cullface": "west", "tintindex": 0, "rotation":  90 },
									"down":  { "uv": [ 7, 9,  9, 10 ], "texture": "#glow", "cullface": "west", "tintindex": 0, "rotation": 270 }
								}
							},
							{
								"from": [ 2,  6,  6 ],
								"to":   [ 3, 10, 10 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"north": { "uv": [ 6, 6,  7, 10 ], "texture": "#glow", "cullface": "west", "tintindex": 0 },
									"south": { "uv": [ 9, 6, 10, 10 ], "texture": "#glow", "cullface": "west", "tintindex": 0 },
									"up":    { "uv": [ 6, 6, 10,  7 ], "texture": "#glow", "cullface": "west", "tintindex": 0, "rotation":  90 },
									"down":  { "uv": [ 6, 9, 10, 10 ], "texture": "#glow", "cullface": "west", "tintindex": 0, "rotation": 270 }
								}
							},
							{
								"from": [  2,  3,  2 ],
								"to":   [ 14, 13, 14 ],
								"shade": %SHADE,
								"light_emission": %LIGHT,
								"faces": {
									"north": { "uv": [ 2, 3, 14, 13 ], "texture": "#glow", "cullface": "north", "tintindex": 0 },
									"east":  { "uv": [ 2, 3, 14, 13 ], "texture": "#glow", "cullface": "east",  "tintindex": 0 },
									"south": { "uv": [ 2, 3, 14, 13 ], "texture": "#glow", "cullface": "south", "tintindex": 0 },
									"west":  { "uv": [ 2, 3, 14, 13 ], "texture": "#glow", "cullface": "west",  "tintindex": 0 }
								}
							},
							{
								"from": [  3,  4,  3 ],
								"to":   [ 13, 12, 13 ],
								"faces": {
									"north": { "uv": [ 3, 4, 13, 12 ], "texture": "#inner_core", "cullface": "north" },
									"east":  { "uv": [ 3, 4, 13, 12 ], "texture": "#inner_core", "cullface": "east"  },
									"south": { "uv": [ 3, 4, 13, 12 ], "texture": "#inner_core", "cullface": "south" },
									"west":  { "uv": [ 3, 4, 13, 12 ], "texture": "#inner_core", "cullface": "west"  }
								}
							}
						],
						"groups": [
							{
								"name": "outer",
								"origin": [ 0, 0, 0 ],
								"color": 0,
								"children": [ 0, 1, 2, 3, 4, 5 ]
							},
							{
								"name": "inner",
								"origin": [ 8, 8, 8 ],
								"color": 0,
								"children": [
									{
										"name": "north",
										"origin": [ 8, 8, 8 ],
										"color": 0,
										"shade": %SHADE,
										"children": [ 6, 7, 8 ]
									},
									{
										"name": "east",
										"origin": [ 8, 8, 8 ],
										"color": 0,
										"shade": %SHADE,
										"children": [ 9, 10, 11 ]
									},
									{
										"name": "south",
										"origin": [ 8, 8, 8 ],
										"color": 0,
										"shade": %SHADE,
										"children": [ 12, 13, 14 ]
									},
									{
										"name": "west",
										"origin": [ 8, 8, 8 ],
										"color": 0,
										"shade": %SHADE,
										"children": [ 15, 16, 17 ]
									},
									18,
									19
								]
							}
						]
					}""",
					Map.of(
						"SHADE", Boolean.toString(!active),
						"LIGHT", active ? "15" : "0"
					)
				)
			);
		}
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath("block/", this.getId(), state.get(BigTechProperties.ACTIVE) ? "_on" : "_off").toString(),
			null,
			null
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.getId(), "_off");
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
			.pattern("fsf", "lcl", "frf")
			.where('f', DecoItems.STEEL_FRAME)
			.where('s', BigTechItemTags.SILICON)
			.where('l', FunctionalItems.LENS)
			.where('c', BigTechItemTags.CRYSTAL_CLUSTERS)
			.where('r', BigTechItemTags.REDSTONE_ALLOY_INGOTS)
			.result(this.getId())
			.toString()
		);
	}
}