package builderb0y.bigtech.datagen.impl.material.circuits;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;
import net.minecraft.recipe.book.CraftingRecipeCategory;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.MaterialItems;

public class CrossoverCircuitDataGenerator extends BasicItemDataGenerator {

	public CrossoverCircuitDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupLang(DataGenContext context) {
		super.setupLang(context);
		context.lang.put("tooltip.bigtech.circuit.crossover.horizontal_output", "Horizontal Output: %d");
		context.lang.put("tooltip.bigtech.circuit.crossover.vertical_output", "Vertical Output: %d");
	}

	@Override
	public void writeItemDefinitions(DataGenContext context) {
		context.writeToFile(
			context.itemDefinitionPath(this.getId()),
			//language=json
			"""
			{
				"model": {
					"type": "minecraft:model",
					"model": "bigtech:item/crossover_circuit",
					"tints": [
						{
							"type": "minecraft:constant",
							"value": -1
						},
						{
							"type": "bigtech:circuit",
							"index": 1
						},
						{
							"type": "bigtech:circuit",
							"index": 2
						}
					]
				}
			}"""
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(this.getId()),
			//language=json
			"""
			{
				"parent": "minecraft:item/generated",
				"textures": {
					"layer0": "bigtech:item/crossover_circuit_base",
					"layer1": "bigtech:item/crossover_circuit_tint_horizontal",
					"layer2": "bigtech:item/crossover_circuit_tint_vertical"
				}
			}"""
		);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapelessRecipeBuilder()
			.group("bigtech:circuit_components")
			.category(CraftingRecipeCategory.REDSTONE)
			.ingredient(ConventionalItemTags.REDSTONE_DUSTS)
			.ingredient(ConventionalItemTags.REDSTONE_DUSTS)
			.ingredient(MaterialItems.SILICON)
			.result(this.getId())
			.toString()
		);
	}
}