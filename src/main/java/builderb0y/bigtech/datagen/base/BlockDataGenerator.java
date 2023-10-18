package builderb0y.bigtech.datagen.base;

import net.fabricmc.fabric.api.mininglevel.v1.FabricMineableTags;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public interface BlockDataGenerator extends DataGenerator {

	public abstract Block getBlock();

	@Override
	public default Identifier getId() {
		return Registries.BLOCK.getId(this.block);
	}

	@Override
	public default void run(DataGenContext context) {
		DataGenerator.super.run(context);

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
			PICKAXE =          BlockTags.PICKAXE_MINEABLE,
			AXE     =          BlockTags.    AXE_MINEABLE,
			SHOVEL  =          BlockTags. SHOVEL_MINEABLE,
			HOE     =          BlockTags.    HOE_MINEABLE,
			SWORD   = FabricMineableTags.  SWORD_MINEABLE,
			SHEARS  = FabricMineableTags. SHEARS_MINEABLE;
	}

	public static class MiningLevelTags {

		public static final TagKey<Block>
			STONE   = BlockTags.  NEEDS_STONE_TOOL,
			IRON    = BlockTags.   NEEDS_IRON_TOOL,
			DIAMOND = BlockTags.NEEDS_DIAMOND_TOOL;
	}
}