package builderb0y.bigtech.datagen.impl;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.armorMaterials.ArmorMaterialTags;
import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.MagnetiteArmorMaterial;

public class MagnetiteArmorDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(ArmorMaterialTags.SHOCK_PROTECTION).add(MagnetiteArmorMaterial.INSTANCE);
	}

	@Dependencies(MagnetiteArmorDataGenerator.class)
	public static class Helmet extends BasicItemDataGenerator {

		public Helmet(Item item) {
			super(item);
		}

		@Override
		public void setupOtherItemTags(DataGenContext context) {
			context.getTags(ItemTags.HEAD_ARMOR).addElement(this.getId());
		}

		@Override
		public void writeRecipes(DataGenContext context) {
			context.writeToFile(
				context.recipePath(this.getId()),
				new ShapedRecipeBuilder()
				.pattern("iii", "i i")
				.where('i', BigTechItemTags.MAGNETITE_INGOTS)
				.result(this.getId())
				.toString()
			);
		}
	}

	@Dependencies(MagnetiteArmorDataGenerator.class)
	public static class Chestplate extends BasicItemDataGenerator {

		public Chestplate(Item item) {
			super(item);
		}

		@Override
		public void setupOtherItemTags(DataGenContext context) {
			context.getTags(ItemTags.CHEST_ARMOR).addElement(this.getId());
		}

		@Override
		public void writeRecipes(DataGenContext context) {
			context.writeToFile(
				context.recipePath(this.getId()),
				new ShapedRecipeBuilder()
				.pattern("i i", "iii", "iii")
				.where('i', BigTechItemTags.MAGNETITE_INGOTS)
				.result(this.getId())
				.toString()
			);
		}
	}

	@Dependencies(MagnetiteArmorDataGenerator.class)
	public static class Leggings extends BasicItemDataGenerator {

		public Leggings(Item item) {
			super(item);
		}

		@Override
		public void setupOtherItemTags(DataGenContext context) {
			context.getTags(ItemTags.LEG_ARMOR).addElement(this.getId());
		}

		@Override
		public void writeRecipes(DataGenContext context) {
			context.writeToFile(
				context.recipePath(this.getId()),
				new ShapedRecipeBuilder()
				.pattern("iii", "i i", "i i")
				.where('i', BigTechItemTags.MAGNETITE_INGOTS)
				.result(this.getId())
				.toString()
			);
		}
	}

	@Dependencies(MagnetiteArmorDataGenerator.class)
	public static class Boots extends BasicItemDataGenerator {

		public Boots(Item item) {
			super(item);
		}

		@Override
		public void setupOtherItemTags(DataGenContext context) {
			context.getTags(ItemTags.FOOT_ARMOR).addElement(this.getId());
		}

		@Override
		public void writeRecipes(DataGenContext context) {
			context.writeToFile(
				context.recipePath(this.getId()),
				new ShapedRecipeBuilder()
				.pattern("i i", "i i")
				.where('i', BigTechItemTags.MAGNETITE_INGOTS)
				.result(this.getId())
				.toString()
			);
		}
	}
}