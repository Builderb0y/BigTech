package builderb0y.bigtech.datagen.impl.material.circuits;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.MaterialItems;

public class DiodeCircuitDataGenerator extends RotatableCircuitDataGenerator {

	public DiodeCircuitDataGenerator(Item item) {
		super(item);
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
			.ingredient(Items.REPEATER)
			.ingredient(MaterialItems.SILICON)
			.result(this.getId())
			.toString()
		);
	}
}