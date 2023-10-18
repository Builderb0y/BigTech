package builderb0y.bigtech.datagen.impl.belts;

import java.util.Map;

import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.items.BigTechItemTags;

public abstract class BeltDataGenerator extends BasicBlockDataGenerator {

	public BeltDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	public void writeBeltBlockModel(DataGenContext context, Identifier id) {
		context.writeToFile(
			context.blockModelPath(id),
			context.replace(
				"""
				{
					"parent": "bigtech:block/template_belt",
					"textures": {
						"belt": "%TEX"
					}
				}
				""",
				Map.of("TEX", context.prefixPath("block/", id).toString())
			)
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeBeltBlockModel(context, this.id);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		this.writeBeltItemModel(context, this.id);
	}

	public void writeBeltItemModel(DataGenContext context, Identifier parent) {
		context.writeToFile(
			context.itemModelPath(this.id),
			context.replace(
				"""
				{
					"parent": "%PARENT"
				}
				""",
				Map.of("PARENT", context.prefixPath("block/", parent).toString())
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