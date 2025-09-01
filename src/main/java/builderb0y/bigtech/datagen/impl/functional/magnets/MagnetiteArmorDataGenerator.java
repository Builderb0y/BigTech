package builderb0y.bigtech.datagen.impl.functional.magnets;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.FunctionalItems;

public class MagnetiteArmorDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(BigTechItemTags.SHOCK_PROTECTIVE_ARMOR).addAll(
			FunctionalItems.MAGNETITE_HELMET,
			FunctionalItems.MAGNETITE_CHESTPLATE,
			FunctionalItems.MAGNETITE_LEGGINGS,
			FunctionalItems.MAGNETITE_BOOTS
		);
		context.writeToFile(
			context.genericAssetsPath(BigTechMod.modID("magnetite"), "equipment"),
			//language=json
			"""
			{
				"layers": {
					"humanoid": [
						{ "texture": "bigtech:magnetite" }
					],
					"humanoid_leggings": [
						{ "texture": "bigtech:magnetite" }
					]
				}
			}"""
		);
	}

	@Dependencies(MagnetiteArmorDataGenerator.class)
	public static class Helmet extends BasicItemDataGenerator {

		public Helmet(Item item) {
			super(item);
		}

		@Override
		public void setupOtherItemTags(DataGenContext context) {
			context.getTags(ItemTags.HEAD_ARMOR).addElement(this.getId());
			context.getTags(BigTechItemTags.MAGNETIC_ARMOR).addElement(this.getId());
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
			context.getTags(BigTechItemTags.MAGNETIC_ARMOR).addElement(this.getId());
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
			context.getTags(BigTechItemTags.MAGNETIC_ARMOR).addElement(this.getId());
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
			context.getTags(BigTechItemTags.MAGNETIC_ARMOR).addElement(this.getId());
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