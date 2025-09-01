package builderb0y.bigtech.datagen.impl.material.electrum;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ArcFurnaceRecipeBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class ElectrumNuggetDataGenerator extends BasicItemDataGenerator {

	public ElectrumNuggetDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.ELECTRUM_NUGGETS).addElement(this.getId());
		context.getTags(ConventionalItemTags.NUGGETS).add(BigTechItemTags.ELECTRUM_NUGGETS);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.pattern("s")
			.where('s', BigTechItemTags.ELECTRUM_INGOTS)
			.result(this.getId())
			.count(9)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("electrum_nugget_arc_furnace")),
			new ArcFurnaceRecipeBuilder()
			.input(ConventionalItemTags.GOLD_NUGGETS)
			.input(BigTechItemTags.SILVER_NUGGETS)
			.slowCoolResult(this.getId())
			.slowCoolQuantity(2)
			.energy(200)
			.coolingRate(15)
			.toString()
		);
	}
}