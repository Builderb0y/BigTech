package builderb0y.bigtech.datagen.impl;

import java.util.Arrays;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.formats.TableFormats.ModelFace;
import builderb0y.bigtech.datagen.tables.Table;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.registrableCollections.LedRegistrableCollection.LedColor;

@Dependencies(CommonLedDataGenerator.class)
public class LedDataGenerator extends BasicBlockDataGenerator {

	public final LedColor color;

	public LedDataGenerator(BlockItem blockItem, LedColor color) {
		super(blockItem);
		this.color = color;
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(
			state,
			context.prefixSuffixPath("block/", this.getId(), state.get(Properties.LIT) ? "_on" : "_off").toString(),
			BlockStateJsonVariant.xFromDown(state.get(Properties.FACING)),
			BlockStateJsonVariant.yFromNorth(state.get(Properties.FACING))
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_off")),
			//language=json
			"""
			{
				"ambientocclusion": false,
				"parent": "minecraft:block/thin_block",
				"textures": {
					"led": "bigtech:block/led",
					"particle": "bigtech:block/led"
				},
				"elements": [
					{
						"from": [  4, 0,  4 ],
						"to":   [ 12, 2, 12 ],
						"faces": {
							"up":    { "uv": [ 0, 12, 4, 16 ], "texture": "#led" },
							"north": { "uv": [ 4, 15, 8, 16 ], "texture": "#led" },
							"east":  { "uv": [ 4, 15, 8, 16 ], "texture": "#led" },
							"south": { "uv": [ 4, 15, 8, 16 ], "texture": "#led" },
							"west":  { "uv": [ 4, 15, 8, 16 ], "texture": "#led" }
						}
					},
					{
						"from": [  6, 2,  6 ],
						"to":   [ 10, 4, 10 ],
						"faces": {
			"""
			+
			new Table<>(ModelFace.FORMAT)
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.FACING_ORDER)
				.filter((Direction face) -> face != Direction.DOWN)
				.map((Direction face) -> {
					int startX = this.color.startX(0) >>> 1;
					int startY = this.color.startY(0) >>> 1;
					if (face == Direction.UP) {
						return new ModelFace("up", startX, startY, startX + 2, startY + 2, "#led", null, null);
					}
					else {
						return new ModelFace(face.getName(), startX, startY + 2, startX + 2, startY + 3, "#led", null, null);
					}
				})
				::iterator
			)
			.toString()
			+
			"""
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_on")),
			//language=json
			"""
			{
				"jmxl": true,
				"ambientocclusion": false,
				"parent": "minecraft:block/thin_block",
				"textures": {
					"led": "bigtech:block/led",
					"particle": "bigtech:block/led"
				},
				"elements": [
					{
						"from": [  4, 0,  4 ],
						"to":   [ 12, 2, 12 ],
						"faces": {
							"up":    { "uv": [ 0, 12, 4, 16 ], "texture": "#led" },
							"north": { "uv": [ 4, 15, 8, 16 ], "texture": "#led" },
							"east":  { "uv": [ 4, 15, 8, 16 ], "texture": "#led" },
							"south": { "uv": [ 4, 15, 8, 16 ], "texture": "#led" },
							"west":  { "uv": [ 4, 15, 8, 16 ], "texture": "#led" }
						}
					},
					{
						"shade": false,
						"jmxl_emissive": true,
						"from": [  6, 2,  6 ],
						"to":   [ 10, 4, 10 ],
						"faces": {
			"""
			+
			new Table<>(ModelFace.FORMAT)
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.FACING_ORDER)
				.filter((Direction face) -> face != Direction.DOWN)
				.map((Direction face) -> {
					int startX = this.color.startX(1) >>> 1;
					int startY = this.color.startY(1) >>> 1;
					if (face == Direction.UP) {
						return new ModelFace("up", startX, startY, startX + 2, startY + 2, "#led", null, null);
					}
					else {
						return new ModelFace(face.getName(), startX, startY + 2, startX + 2, startY + 3, "#led", null, null);
					}
				})
				::iterator
			)
			.toString()
			+
			"""
						}
					}
				]
			}"""
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.getId(), "_on");
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		//hand is fine.
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//wood is fine.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.LEDS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.LEDS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_1")),
			new ShapelessRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:leds")
			.ingredient(BigTechItemTags.LEDS)
			.ingredient(this.color.dyeTag())
			.result(this.getId())
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_8")),
			new ShapelessRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:leds")
			.ingredient(BigTechItemTags.LEDS)
			.ingredient(BigTechItemTags.LEDS)
			.ingredient(BigTechItemTags.LEDS)
			.ingredient(BigTechItemTags.LEDS)
			.ingredient(this.color.dyeTag())
			.ingredient(BigTechItemTags.LEDS)
			.ingredient(BigTechItemTags.LEDS)
			.ingredient(BigTechItemTags.LEDS)
			.ingredient(BigTechItemTags.LEDS)
			.result(this.getId())
			.toString()
		);
	}
}