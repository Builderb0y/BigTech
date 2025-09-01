package builderb0y.bigtech.datagen.impl.functional.magnets;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class MagneticArrowDataGenerator extends BasicItemDataGenerator {

	public MagneticArrowDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(ItemTags.ARROWS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("n", "s", "f")
			.where('n', BigTechItemTags.MAGNETITE_NUGGETS)
			.where('s', ConventionalItemTags.WOODEN_RODS)
			.where('f', Items.FEATHER)
			.result(this.getId())
			.toString()
		);
	}
}