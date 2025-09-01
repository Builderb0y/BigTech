package builderb0y.bigtech.datagen.impl.material.steel;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ArcFurnaceRecipeBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class SteelIngotDataGenerator extends BasicItemDataGenerator {

	public SteelIngotDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.STEEL_INGOTS).addElement(this.getId());
		context.getTags(ItemTags.BEACON_PAYMENT_ITEMS).add(BigTechItemTags.STEEL_INGOTS);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("steel_ingot_crafting")),
			new ShapedRecipeBuilder()
			.pattern("nnn", "nnn", "nnn")
			.where('n', BigTechItemTags.STEEL_NUGGETS)
			.result(this.getId())
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("steel_ingot_uncrafting")),
			new ShapedRecipeBuilder()
			.pattern("b")
			.where('b', BigTechItemTags.STEEL_BLOCKS)
			.result(this.getItem())
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("steel_ingot_arc_furnace")),
			new ArcFurnaceRecipeBuilder()
			.input(ConventionalItemTags.IRON_INGOTS)
			.input(ItemTags.COALS)
			.slowCoolResult(this.getId())
			.energy(2000)
			.coolingRate(20)
			.toString()
		);
	}
}