package builderb0y.bigtech.datagen.impl.functional;

import java.util.Objects;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;

public abstract class CommonCoilDataGenerator extends BasicBlockDataGenerator {

	public CommonCoilDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		Direction direction = state.get(Properties.FACING);
		return new BlockStateJsonVariant(
			state,
			context.prefixPath("block/", this.getId()).toString(),
			BlockStateJsonVariant.xFromUp(direction),
			Objects.requireNonNullElse(BlockStateJsonVariant.yFromNorth(direction), 0)
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			new RetexturedModelBuilder()
			.blockParent(Identifier.ofVanilla("cube_column"))
			.blockTexture("side", context.suffixPath(this.getId(), "_side"))
			.blockTexture("end", context.suffixPath(this.getId(), "_end"))
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

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}
}