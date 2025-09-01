package builderb0y.bigtech.datagen.impl.material;

import net.minecraft.item.BlockItem;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;
import builderb0y.bigtech.items.MaterialItems;

public class CrystallineSandDataGenerator extends BasicBlockDataGenerator {

	public CrystallineSandDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_all"))
			.blockTexture("all", this.getId())
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.SHOVEL);
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapelessRecipeBuilder()
			.ingredient(ItemTags.SAND)
			.ingredient(MaterialItems.CRYSTAL_DEBRIS)
			.result(this.getId())
			.toString()
		);
	}
}