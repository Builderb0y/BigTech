package builderb0y.bigtech.datagen.impl;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.*;

@Dependencies(ArcFurnaceRecipesDataGenerator.class)
public class CrucibleDataGenerator extends UnplaceableBlockDataGenerator {

	public final Block block;

	public CrucibleDataGenerator(Block block) {
		this.block = block;
	}

	@Override
	public Block getBlock() {
		return this.block;
	}

	@Override
	public Identifier getDroppedItem() {
		return Identifier.ofVanilla("cauldron");
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		this.writeDefaultBlockstateJson(context, Identifier.ofVanilla("cauldron"));
	}

	@Override
	public void writeBlockModels(DataGenContext context) {

	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.STONE).addElement(this.getId());
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {

	}
}