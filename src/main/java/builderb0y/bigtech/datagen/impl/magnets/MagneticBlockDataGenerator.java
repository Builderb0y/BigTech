package builderb0y.bigtech.datagen.impl.magnets;

import net.minecraft.item.BlockItem;
import net.minecraft.registry.tag.BlockTags;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.items.BigTechItemTags;

@Dependencies(MagneticBlockDataGenerator.CommonMagneticBlockDataGenerator.class)
public abstract class MagneticBlockDataGenerator extends BasicBlockDataGenerator {

	public MagneticBlockDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.STONE).addElement(this.getId());
	}

	public static abstract class FerromagneticBlockDataGenerator extends MagneticBlockDataGenerator {

		public FerromagneticBlockDataGenerator(BlockItem blockItem) {
			super(blockItem);
		}

		@Override
		public void setupOtherBlockTags(DataGenContext context) {
			context.getTags(BigTechBlockTags.FERROMAGNETIC_BLOCKS).addElement(this.getId());
		}

		@Override
		public void setupOtherItemTags(DataGenContext context) {
			context.getTags(BigTechItemTags.FERROMAGNETIC_BLOCKS).addElement(this.getId());
		}
	}

	public static abstract class ElectromagneticBlockDataGenerator extends MagneticBlockDataGenerator {

		public ElectromagneticBlockDataGenerator(BlockItem blockItem) {
			super(blockItem);
		}

		@Override
		public void setupOtherBlockTags(DataGenContext context) {
			context.getTags(BigTechBlockTags.ELECTROMAGNETIC_BLOCKS).addElement(this.getId());
		}

		@Override
		public void setupOtherItemTags(DataGenContext context) {
			context.getTags(BigTechItemTags.ELECTROMAGNETIC_BLOCKS).addElement(this.getId());
		}
	}

	public static class CommonMagneticBlockDataGenerator implements DataGenerator {

		@Override
		public void run(DataGenContext context) {
			context.getTags(BigTechBlockTags.MAGNETIC_BLOCKS).addAll(BigTechBlockTags.FERROMAGNETIC_BLOCKS, BigTechBlockTags.ELECTROMAGNETIC_BLOCKS);
			context.getTags(BigTechItemTags.MAGNETIC_BLOCKS).addAll(BigTechItemTags.FERROMAGNETIC_BLOCKS, BigTechItemTags.ELECTROMAGNETIC_BLOCKS);
			context.getTags(BlockTags.BEACON_BASE_BLOCKS).add(BigTechBlockTags.FERROMAGNETIC_BLOCKS);
		}
	}
}