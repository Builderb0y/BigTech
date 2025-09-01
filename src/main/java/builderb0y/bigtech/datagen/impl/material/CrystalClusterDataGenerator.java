package builderb0y.bigtech.datagen.impl.material;

import java.util.Map;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection.CrystalColor;

public class CrystalClusterDataGenerator extends BasicBlockDataGenerator {

	public final CrystalColor color;

	public CrystalClusterDataGenerator(BlockItem blockItem, CrystalColor color) {
		super(blockItem);
		this.color = color;
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return "δβπψζλωΩ".charAt(this.color.ordinal()) + " Crystal Cluster";
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		//no-op.
	}

	@Override
	public void writeItemDefinitions(DataGenContext context) {
		context.writeToFile(
			context.itemDefinitionPath(this.getId()),
			context.replace(
				//language=json
				"""
				{
					"model": {
						"type": "bigtech:crystal_cluster",
						"texture": "%ID"
					}
				}""",
				Map.of("ID", context.prefixPath("block/", this.getId()).toString())
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
		context.getTags(BigTechBlockTags.CRYSTAL_CLUSTERS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.CRYSTAL_CLUSTERS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {

	}
}