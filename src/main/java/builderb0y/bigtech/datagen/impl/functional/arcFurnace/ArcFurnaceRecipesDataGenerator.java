package builderb0y.bigtech.datagen.impl.functional.arcFurnace;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Items;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.formats.ArcFurnaceRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.MaterialItems;

public class ArcFurnaceRecipesDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(BigTechItemTags.IRON_DOUBLEABLES).addAll(ConventionalItemTags.IRON_RAW_MATERIALS, ConventionalItemTags.IRON_ORES);
		context.getTags(BigTechItemTags.COPPER_DOUBLEABLES).addAll(ConventionalItemTags.COPPER_RAW_MATERIALS, ConventionalItemTags.IRON_ORES);
		context.getTags(BigTechItemTags.GOLD_DOUBLEABLES).addAll(ConventionalItemTags.GOLD_RAW_MATERIALS, ConventionalItemTags.IRON_ORES);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("iron_doubling")),
			new ArcFurnaceRecipeBuilder()
			.input(BigTechItemTags.IRON_DOUBLEABLES)
			.slowCoolResult(Items.IRON_INGOT)
			.slowCoolQuantity(2)
			.fastCoolResult(MaterialItems.CAST_IRON_INGOT)
			.fastCoolQuantity(2)
			.energy(1000)
			.coolingRate(10)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("iron_ingot_conversion")),
			new ArcFurnaceRecipeBuilder()
			.input(ConventionalItemTags.IRON_INGOTS)
			.slowCoolResult(Items.IRON_INGOT)
			.fastCoolResult(MaterialItems.CAST_IRON_INGOT)
			.energy(500)
			.coolingRate(10)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("iron_block_conversion")),
			new ArcFurnaceRecipeBuilder()
			.input(ConventionalItemTags.STORAGE_BLOCKS_IRON)
			.slowCoolResult(Items.IRON_BLOCK)
			.fastCoolResult(MaterialItems.CAST_IRON_BLOCK)
			.energy(4000)
			.coolingRate(10)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("copper_doubling")),
			new ArcFurnaceRecipeBuilder()
			.input(BigTechItemTags.COPPER_DOUBLEABLES)
			.slowCoolResult(Items.COPPER_INGOT)
			.slowCoolQuantity(2)
			.energy(1000)
			.coolingRate(10)
			.toString()
		);
		context.writeToFile(
			context.recipePath(BigTechMod.modID("gold_doubling")),
			new ArcFurnaceRecipeBuilder()
			.input(BigTechItemTags.GOLD_DOUBLEABLES)
			.slowCoolResult(Items.GOLD_INGOT)
			.slowCoolQuantity(2)
			.energy(1000)
			.coolingRate(10)
			.toString()
		);
	}
}