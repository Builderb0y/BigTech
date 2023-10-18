package builderb0y.bigtech.datagen.impl.belts;

import java.util.Map;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.items.BigTechItemTags;

public abstract class BeltDataGenerator extends BasicBlockDataGenerator {

	public BeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	public void writeBeltBlockModel(DataGenContext context, String name) {
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("belts/${name}")),
			context.replace(
				"""
				{
					"parent": "bigtech:block/belts/template",
					"textures": {
						"belt": "bigtech:block/belts/%TEX"
					}
				}
				""",
				Map.of("TEX", name)
			)
		);
	}

	@Override
	public abstract void writeItemModels(DataGenContext context);

	public void writeBeltItemModel(DataGenContext context, String parent) {
		context.writeToFile(
			context.itemModelPath(this.id),
			context.replace(
				"""
				{
					"parent": "bigtech:block/belts/%PARENT"
				}
				""",
				Map.of("PARENT", parent)
			)
		);
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
		context.getTags(BigTechBlockTags.BELTS).add(this.id.toString());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.BELTS).add(this.id.toString());
	}
}