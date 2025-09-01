package builderb0y.bigtech.datagen.impl.material.lapis;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ArcFurnaceRecipeBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class LapisAlloyIngotDataGenerator extends BasicItemDataGenerator {

	public LapisAlloyIngotDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.LAPIS_ALLOY_INGOTS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_arc_furnace")),
			new ArcFurnaceRecipeBuilder()
			.input(ConventionalItemTags.LAPIS_GEMS)
			.input(ConventionalItemTags.GOLD_INGOTS)
			.slowCoolResult(this.getId())
			.energy(500)
			.coolingRate(10)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_block")),
			new ShapelessRecipeBuilder()
			.ingredient(BigTechItemTags.LAPIS_ALLOY_BLOCKS)
			.result(this.getId())
			.count(9)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_nuggets")),
			new ShapedRecipeBuilder()
			.pattern("nnn", "nnn", "nnn")
			.where('n', BigTechItemTags.LAPIS_ALLOY_NUGGETS)
			.result(this.getId())
			.toString()
		);
	}
}