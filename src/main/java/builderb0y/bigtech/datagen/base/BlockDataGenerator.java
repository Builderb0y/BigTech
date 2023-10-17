package builderb0y.bigtech.datagen.base;

import java.util.Collection;

import net.fabricmc.fabric.api.mininglevel.v1.FabricMineableTags;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public interface BlockDataGenerator extends DataGenerator {

	public abstract Block getBlock();

	@Override
	public default Identifier getID() {
		return Registries.BLOCK.getId(this.getBlock());
	}

	public abstract void writeBlockstateJson(DataGenContext context);

	public abstract void writeBlockModels(DataGenContext context);

	public abstract void writeLootTableJson(DataGenContext context);

	public abstract MiningTool getMiningTool(DataGenContext context);

	public abstract MiningLevel getMiningLevel(DataGenContext context);

	public abstract Collection<TagKey<Block>> getBlockTags(DataGenContext context);

	public abstract TagKey<Block> getMiningTag(DataGenContext context);

	public static enum MiningTool {
		HAND   (null),
		PICKAXE(         BlockTags.PICKAXE_MINEABLE),
		AXE    (         BlockTags.    AXE_MINEABLE),
		SHOVEL (         BlockTags. SHOVEL_MINEABLE),
		HOE    (         BlockTags.    HOE_MINEABLE),
		SWORD  (FabricMineableTags.  SWORD_MINEABLE),
		SHEARS (FabricMineableTags. SHEARS_MINEABLE);

		public static final MiningTool[] VALUES = values();

		public final TagKey<Block> tag;

		MiningTool(TagKey<Block> tag) {
			this.tag = tag;
		}
	}

	public static enum MiningLevel {
		WOOD   (null),
		STONE  (BlockTags.NEEDS_STONE_TOOL),
		IRON   (BlockTags.NEEDS_IRON_TOOL),
		DIAMOND(BlockTags.NEEDS_DIAMOND_TOOL);

		public static final MiningLevel[] VALUES = values();

		public final TagKey<Block> tag;

		MiningLevel(TagKey<Block> tag) {
			this.tag = tag;
		}
	}
}