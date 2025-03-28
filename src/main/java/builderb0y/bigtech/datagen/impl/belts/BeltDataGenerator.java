package builderb0y.bigtech.datagen.impl.belts;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

@Dependencies(BeltTemplateModelDataGenerator.class)
public abstract class BeltDataGenerator extends BasicBlockDataGenerator {

	public BeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	public void writeBeltBlockModel(DataGenContext context, Identifier id) {
		context.writeToFile(
			context.blockModelPath(id),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_belt"))
			.blockTexture("belt", id)
			.toString()
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeBeltBlockModel(context, this.getId());
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.BELTS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.BELTS).addElement(this.getId());
	}

	public void writeBeltRecipes(DataGenContext context, ItemConvertible item) {
		this.writeBeltRecipes(context, Registries.ITEM.getId(item.asItem()).toString());
	}

	public void writeBeltRecipes(DataGenContext context, TagKey<Item> tag) {
		this.writeBeltRecipes(context, "#" + tag.id());
	}

	public void writeBeltRecipes(DataGenContext context, String otherIngredient) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_paper")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:belts")
			.pattern("ppp", "ioi")
			.where('p', Items.PAPER)
			.where('i', ConventionalItemTags.IRON_INGOTS)
			.where('o', otherIngredient)
			.result(this.blockItem)
			.count(3)
			.toString()
		);
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_from_leather")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:belts")
			.pattern("lll", "ioi")
			.where('l', Items.LEATHER)
			.where('i', ConventionalItemTags.IRON_INGOTS)
			.where('o', otherIngredient)
			.result(this.blockItem)
			.count(6)
			.toString()
		);
	}
}