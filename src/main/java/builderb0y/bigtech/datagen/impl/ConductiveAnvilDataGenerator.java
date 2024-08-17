package builderb0y.bigtech.datagen.impl;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

@Dependencies(CommonConductiveAnvilDataGenerator.class)
public class ConductiveAnvilDataGenerator extends BasicBlockDataGenerator {

	public final CopperRegistrableCollection.Type type;

	public ConductiveAnvilDataGenerator(BlockItem blockItem, CopperRegistrableCollection.Type type) {
		super(blockItem);
		this.type = type;
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.getBlock())
				.map((BlockState state) -> new BlockStateJsonVariant(
					state,
					context.prefixPath("block/", this.getId()).toString(),
					null,
					BlockStateJsonVariant.yFromNorth(state.get(Properties.HORIZONTAL_FACING))
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		String prefix = this.type.notWaxed().noCopperPrefix;
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(BigTechMod.modID("template_conductive_anvil"))
			.blockTexture("front",  BigTechMod.modID(prefix + "conductive_anvil_front"))
			.blockTexture("side",   BigTechMod.modID(prefix + "conductive_anvil_side"))
			.blockTexture("top",    BigTechMod.modID(prefix + "conductive_anvil_top"))
			.blockTexture("bottom", BigTechMod.modID(prefix + "conductive_anvil_bottom"))
			.toString()
		);
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
		context.getTags(BigTechBlockTags.CONDUCTIVE_ANVILS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.CONDUCTIVE_ANVILS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		//only one variant has a recipe, and that variant is
		//handled by CommonConductiveAnvilDataGenerator instead.
	}
}