package builderb0y.bigtech.datagen.impl.material.glowstone;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ArcFurnaceRecipeBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class GlowstoneAlloyIngotDataGenerator extends BasicItemDataGenerator {

	public GlowstoneAlloyIngotDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.GLOWSTONE_ALLOY_INGOTS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_arc_furnace")),
			new ArcFurnaceRecipeBuilder()
			.input(ConventionalItemTags.GLOWSTONE_DUSTS)
			.input(ConventionalItemTags.GOLD_INGOTS)
			.slowCoolResult(this.getId())
			.energy(500)
			.coolingRate(10)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_block")),
			new ShapelessRecipeBuilder()
			.ingredient(BigTechItemTags.GLOWSTONE_ALLOY_BLOCKS)
			.result(this.getId())
			.count(9)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_nuggets")),
			new ShapedRecipeBuilder()
			.pattern("nnn", "nnn", "nnn")
			.where('n', BigTechItemTags.GLOWSTONE_ALLOY_NUGGETS)
			.result(this.getId())
			.toString()
		);
	}
}