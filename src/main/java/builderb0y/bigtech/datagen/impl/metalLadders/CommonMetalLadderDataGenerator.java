package builderb0y.bigtech.datagen.impl.metalLadders;

import java.util.List;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.blocks.DecoBlocks;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningLevelTags;
import builderb0y.bigtech.datagen.base.BlockDataGenerator.MiningToolTags;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.DecoItems;

public class CommonMetalLadderDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.getTags(BigTechBlockTags.METAL_LADDERS).addAll(List.of(
			new TagOrItem(DecoBlocks.IRON_LADDER),
			new TagOrItem(BigTechBlockTags.COPPER_LADDERS)
		));
		context.getTags(BigTechBlockTags.LADDERS).addAll(List.of(
			new TagOrItem(BigTechBlockTags.METAL_LADDERS),
			new TagOrItem(Blocks.LADDER)
		));
		context.getTags(BigTechItemTags.METAL_LADDERS).addAll(List.of(
			new TagOrItem(DecoItems.IRON_LADDER),
			new TagOrItem(BigTechItemTags.COPPER_LADDERS)
		));
		context.getTags(BigTechItemTags.LADDERS).addAll(List.of(
			new TagOrItem(BigTechItemTags.METAL_LADDERS),
			new TagOrItem(Items.LADDER)
		));
		context.getTags(MiningToolTags.PICKAXE).add(BigTechBlockTags.METAL_LADDERS);
		context.getTags(MiningLevelTags.STONE).add(BigTechBlockTags.METAL_LADDERS);
		context.getTags(BlockTags.CLIMBABLE).add(BigTechBlockTags.METAL_LADDERS);
	}
}