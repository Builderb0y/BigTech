package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.util.Formatting;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class SilverIodideCannonDataGenerator extends BasicBlockDataGenerator {

	public SilverIodideCannonDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupLang(DataGenContext context) {
		super.setupLang(context);
		context.lang.put("bigtech.silver_iodide_cannon.cant_see_the_sky", Formatting.RED   + "Cannon is not outside.");
		context.lang.put("bigtech.silver_iodide_cannon.no_ammunition",    Formatting.RED   + "No ammunition.");
		context.lang.put("bigtech.silver_iodide_cannon.not_a_firework",   Formatting.RED   + "Not a firework rocket.");
		context.lang.put("bigtech.silver_iodide_cannon.no_stars",         Formatting.RED   + "No stars.");
		context.lang.put("bigtech.silver_iodide_cannon.ready_to_launch",  Formatting.GREEN + "Ready to launch!");
		context.lang.put("container.bigtech.silver_iodide_cannon.make_less_rainy", "Make less rainy");
		context.lang.put("container.bigtech.silver_iodide_cannon.make_more_rainy", "Make more rainy");
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			//language=json
			"""
			{
				"parent": "minecraft:block/block",
				"textures": {
					"exterior": "bigtech:block/silver_iodide_cannon_exterior",
					"interior": "bigtech:block/silver_iodide_cannon_interior",
					"particle": "bigtech:block/silver_iodide_cannon_exterior"
				},
				"elements": [
					{
						"from": [  4,  0,  4 ],
						"to":   [ 12, 16, 12 ],
						"faces": {
							"north": { "uv": [ 0, 0,  8, 16 ], "texture": "exterior" },
							"east":  { "uv": [ 0, 0,  8, 16 ], "texture": "exterior" },
							"south": { "uv": [ 0, 0,  8, 16 ], "texture": "exterior" },
							"west":  { "uv": [ 0, 0,  8, 16 ], "texture": "exterior" },
							"up":    { "uv": [ 8, 0, 16,  8 ], "texture": "exterior", "cullface": "up"   },
							"down":  { "uv": [ 8, 8, 16, 16 ], "texture": "exterior", "cullface": "down" }
						}
					},
					{
						"from": [  5, 16,  5 ],
						"to":   [ 11,  1, 11 ],
						"faces": {
							"north": { "uv": [  0, 15,  6,  0 ], "texture": "interior", "cullface": "up" },
							"east":  { "uv": [  0, 15,  6,  0 ], "texture": "interior", "cullface": "up" },
							"south": { "uv": [  0, 15,  6,  0 ], "texture": "interior", "cullface": "up" },
							"west":  { "uv": [  0, 15,  6,  0 ], "texture": "interior", "cullface": "up" },
							"up":    { "uv": [ 10, 10, 16, 16 ], "texture": "interior", "cullface": "up" }
						}
					}
				]
			}"""
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.STONE).addElement(this.getId());
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
			.pattern("m m", "s s", "sbs")
			.where('m', BigTechItemTags.MAGNETITE_INGOTS)
			.where('s', BigTechItemTags.SILVER_INGOTS)
			.where('b', Items.DISPENSER)
			.result(this.getId())
			.toString()
		);
	}
}