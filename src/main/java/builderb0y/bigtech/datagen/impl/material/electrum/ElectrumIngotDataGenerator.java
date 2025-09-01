package builderb0y.bigtech.datagen.impl.material.electrum;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ArcFurnaceRecipeBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class ElectrumIngotDataGenerator extends BasicItemDataGenerator {

	public ElectrumIngotDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.ELECTRUM_INGOTS).addElement(this.getId());
		context.getTags(ConventionalItemTags.INGOTS).add(BigTechItemTags.ELECTRUM_INGOTS);
		context.getTags(ItemTags.BEACON_PAYMENT_ITEMS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "packing")),
			new ShapedRecipeBuilder()
			.pattern("sss", "sss", "sss")
			.where('s', BigTechItemTags.ELECTRUM_NUGGETS)
			.result(this.getId())
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "unpacking")),
			new ShapedRecipeBuilder()
			.pattern("s")
			.where('s', BigTechItemTags.ELECTRUM_BLOCKS)
			.result(this.getId())
			.count(9)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("electrum_ingot_arc_furnace")),
			new ArcFurnaceRecipeBuilder()
			.input(ConventionalItemTags.GOLD_INGOTS)
			.input(BigTechItemTags.SILVER_INGOTS)
			.slowCoolResult(this.getId())
			.slowCoolQuantity(2)
			.energy(1500)
			.coolingRate(15)
			.toString()
		);
	}
}