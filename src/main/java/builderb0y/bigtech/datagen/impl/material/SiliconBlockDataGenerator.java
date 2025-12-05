package builderb0y.bigtech.datagen.impl.material;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Models;
import builderb0y.bigtech.datagen.formats.ArcFurnaceRecipeBuilder;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.MaterialItems;

public class SiliconBlockDataGenerator extends BasicBlockDataGenerator {

	public SiliconBlockDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.parent(Models.block.cube_all.id)
			.itemTexture(Models.block.cube_all.all, BigTechMod.modID("silicon"))
			.toString()
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//wood is fine.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.SILICON_BLOCKS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.SILICON_BLOCKS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_arc_furnace")),
			new ArcFurnaceRecipeBuilder()
			.input(ItemTags.SAND)
			.slowCoolResult(this.getId())
			.fastCoolResult(Items.STONE)
			.energy(1000)
			.coolingRate(5)
			.toString()
		);
	}
}