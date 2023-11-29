package builderb0y.bigtech.datagen.impl.catwalkStairs;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.registrableCollections.WoodRegistrableCollection;

public class WoodCatwalkStairsDataGenerator extends CatwalkStairsDataGenerator {

	public final WoodRegistrableCollection.Type type;
	public final Item planks;

	public WoodCatwalkStairsDataGenerator(BlockItem blockItem, WoodRegistrableCollection.Type type) {
		super(blockItem);
		this.type = type;
		this.planks = BigTechBlocks.VANILLA_PLANKS.get(type).asItem();
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.WOODEN_CATWALK_STAIRS).addElement(this.id);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.WOODEN_CATWALK_STAIRS).addElement(this.id);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeCatwalkStairsRecipe(
			context,
			new TagOrItem(this.planks),
			new TagOrItem(Items.STICK)
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeCatwalkStairsBlockModels(
			context,
			new Identifier("minecraft", "${this.type.prefix}planks"),
			BigTechMod.modID("${this.type.prefix}catwalk_stairs")
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		this.writeCatwalkStairsItemModels(
			context,
			new Identifier("minecraft", "${this.type.prefix}planks"),
			BigTechMod.modID("${this.type.prefix}catwalk_stairs")
		);
	}
}