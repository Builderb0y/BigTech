package builderb0y.bigtech.datagen.base;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blocks.BigTechBlockTags;

public interface BlockDataGenerator extends LocalizedDataGenerator {

	public abstract Block getBlock();

	@Override
	public default Identifier getId() {
		return Registries.BLOCK.getId(this.getBlock());
	}

	@Override
	public default void run(DataGenContext context) {
		LocalizedDataGenerator.super.run(context);

		this.writeBlockstateJson(context);
		this.writeBlockModels(context);
		this.writeLootTableJson(context);
		this.setupMiningToolTags(context);
		this.setupMiningLevelTags(context);
		this.setupOtherBlockTags(context);
	}

	public abstract void writeBlockstateJson(DataGenContext context);

	public abstract void writeBlockModels(DataGenContext context);

	public abstract void writeLootTableJson(DataGenContext context);

	public abstract void setupMiningToolTags(DataGenContext context);

	public abstract void setupMiningLevelTags(DataGenContext context);

	public abstract void setupOtherBlockTags(DataGenContext context);

	public static class MiningToolTags {

		public static final TagKey<Block>
			PICKAXE =        BlockTags.PICKAXE_MINEABLE,
			AXE     =        BlockTags.    AXE_MINEABLE,
			SHOVEL  =        BlockTags. SHOVEL_MINEABLE,
			HOE     =        BlockTags.    HOE_MINEABLE,
			SWORD   =        BlockTags. SWORD_EFFICIENT,
			SHEARS  = BigTechBlockTags. SHEARS_MINEABLE;
	}

	public static class MiningLevelTags {

		public static final TagKey<Block>
			STONE   = BlockTags.  NEEDS_STONE_TOOL,
			IRON    = BlockTags.   NEEDS_IRON_TOOL,
			DIAMOND = BlockTags.NEEDS_DIAMOND_TOOL;
	}
}