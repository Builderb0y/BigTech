package builderb0y.bigtech.datagen.impl.material.circuits;

import net.minecraft.item.Item;
import net.minecraft.recipe.book.CraftingRecipeCategory;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.MaterialItems;

public class SubtracterCircuitDataGenerator extends RotatableCircuitDataGenerator {

	public SubtracterCircuitDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_conversion")),
			new ShapelessRecipeBuilder()
			.group("bigtech:circuit_components")
			.category(CraftingRecipeCategory.REDSTONE)
			.ingredient(MaterialItems.COMPARATOR_CIRCUIT)
			.result(this.getId())
			.toString()
		);
	}
}