package builderb0y.bigtech.api;

import java.util.function.Predicate;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.util.BlockApiLookups;
import builderb0y.bigtech.util.WorldHelper;

/**
attached to a block via {@link #LOOKUP} to provide more
information on how it should be harvested by a destroyer beam.
*/
public interface Harvestable {

	public static final BlockApiLookup<Harvestable, Void> LOOKUP = BlockApiLookup.get(BigTechMod.modID("harvestable_crops"), Harvestable.class, Void.class);
	public static final Harvestable
		SIMPLE_HARVEST = new Harvestable() {

			@Override
			public String toString() {
				return "Harvestable.SIMPLE_HARVEST";
			}
		},
		NEVER_HARVEST = new Harvestable() {

			@Override
			public boolean canHarvest(World world, BlockPos pos, BlockState state) {
				return false;
			}

			@Override
			public String toString() {
				return "Harvestable.NEVER_HARVEST";
			}
		},
		GENERIC_CROP = new Harvestable() {

			@Override
			public boolean canHarvest(World world, BlockPos pos, BlockState state) {
				return state.getBlock().<CropBlock>as().isMature(state);
			}

			@Override
			public String toString() {
				return "Harvestable.GENERIC_CROP";
			}
		},
		SWEET_BERRY_BUSH = new Harvestable() {

			@Override
			public boolean canHarvest(World world, BlockPos pos, BlockState state) {
				return state.get(SweetBerryBushBlock.AGE) > 1;
			}

			@Override
			public void harvest(World world, BlockPos pos, BlockState state, ItemStack tool) {
				//mostly a copy-paste of SweetBerryBushBlock.onUse().
				int age = state.get(SweetBerryBushBlock.AGE);
				boolean fullyGrown = age == 3;
				int drops = 1 + world.random.nextInt(2);
				SweetBerryBushBlock.dropStack(world, pos, new ItemStack(Items.SWEET_BERRIES, drops + (fullyGrown ? 1 : 0)));
				world.playSound(null, pos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS, 1.0f, 0.8f + world.random.nextFloat() * 0.4f);
				BlockState blockState = state.with(SweetBerryBushBlock.AGE, 1);
				world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
			}

			@Override
			public String toString() {
				return "Harvestable.SWEET_BERRY_BUSH";
			}
		},
		NETHER_WART = Harvestables.crop(NetherWartBlock.AGE, 3),
		COCOA = Harvestables.crop(CocoaBlock.AGE, CocoaBlock.MAX_AGE),
		OAK_AZALEA_TREE = Harvestables.tree(
			Harvestables.isIn(Blocks.OAK_LOG, Blocks.OAK_WOOD),
			Harvestables.isIn(Blocks.OAK_LEAVES, Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES)
		),
		SPRUCE_TREE = Harvestables.tree(Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.SPRUCE_LEAVES),
		BIRCH_TREE = Harvestables.tree(Blocks.BIRCH_LOG, Blocks.BIRCH_WOOD, Blocks.BIRCH_LEAVES),
		JUNGLE_TREE = Harvestables.tree(Blocks.JUNGLE_LOG, Blocks.JUNGLE_WOOD, Blocks.JUNGLE_LEAVES),
		ACACIA_TREE = Harvestables.tree(Blocks.ACACIA_LOG, Blocks.ACACIA_WOOD, Blocks.ACACIA_LEAVES),
		DARK_OAK_TREE = Harvestables.tree(Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_WOOD, Blocks.DARK_OAK_LEAVES),
		CHERRY_TREE = Harvestables.tree(Blocks.CHERRY_LOG, Blocks.CHERRY_WOOD, Blocks.CHERRY_LEAVES),
		MANGROVE_TREE = Harvestables.tree(Blocks.MANGROVE_LOG, Blocks.MANGROVE_WOOD, Blocks.MANGROVE_LEAVES),
		GENERIC_TREE = Harvestables.tree(
			Harvestables.isIn(BlockTags.LOGS),
			Harvestables.isIn(BlockTags.LEAVES)
		),
		VINES = Harvestables.downOnly(Blocks.VINE),
		WEEPING_VINES = Harvestables.downOnly(state -> state.isOf(Blocks.WEEPING_VINES) || state.isOf(Blocks.WEEPING_VINES_PLANT)),
		TWISTING_VINES = Harvestables.downOnly(state -> state.isOf(Blocks.TWISTING_VINES) || state.isOf(Blocks.TWISTING_VINES_PLANT)),
		BAMBOO = Harvestables.upOnly(Blocks.BAMBOO),
		SUGAR_CANE = Harvestables.upOnly(Blocks.SUGAR_CANE),
		CACTUS = Harvestables.upOnly(Blocks.CACTUS),
		KELP = Harvestables.upOnly(state -> state.isOf(Blocks.KELP) || state.isOf(Blocks.KELP_PLANT)),
		HUGE_MUSHROOM = Harvestables.hugeMushroom(
			Harvestables.isOf(Blocks.MUSHROOM_STEM),
			Harvestables.isIn(Blocks.RED_MUSHROOM_BLOCK, Blocks.BROWN_MUSHROOM_BLOCK)
		),
		CHORUS_TREE = Harvestables.chorusTree(Harvestables.isIn(Blocks.CHORUS_PLANT, Blocks.CHORUS_FLOWER));

	public static Harvestable get(World world, BlockPos pos) {
		Harvestable harvestable = LOOKUP.find(world, pos, null);
		return harvestable != null ? harvestable : SIMPLE_HARVEST;
	}

	public static Harvestable get(World world, BlockPos pos, BlockState state) {
		Harvestable harvestable = LOOKUP.find(world, pos, state, null, null);
		return harvestable != null ? harvestable : SIMPLE_HARVEST;
	}

	public static Harvestable get(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		Harvestable harvestable = LOOKUP.find(world, pos, state, blockEntity, null);
		return harvestable != null ? harvestable : SIMPLE_HARVEST;
	}

	/**
	returns true if this block can be harvested, false otherwise.
	in general, this method should only return true if the associated
	block is fully grown (if applicable). for example, vanilla wheat
	has multiple stages, and only one of them is considered "fully grown".
	by contrast, vanilla saplings get replaced by a different block (logs)
	when growing, so the sapling is *not* considered fully grown,
	while the log *is* considered fully grown.

	the default implementation of this method returns true,
	which indicates that this block can always be harvested.
	*/
	public default boolean canHarvest(World world, BlockPos pos, BlockState state) {
		return true;
	}

	/**
	destroys this block (if applicable) with the provided tool (if applicable),
	and drops the relevant item(s). if this block consists of a part that can
	be destroyed and a part that shouldn't be, then only the part that can be
	destroyed is destroyed. for example, sweet berry bushes destroy the berries,
	but not the bush. this mimics how a player would normally harvest the block.
	the provided tool can change what items the block drops, so overriders should
	take that into account. for example, vines will only drop items when the tool is shears.
	the tool may also be enchanted, which could also change the block's drops.
	for example, silk touch or fortune could change the drops, or the number of drops.
	note: the provided tool may be an empty stack too, in which case this
	method should drop items as if the block were broken with an empty hand.
	*/
	public default void harvest(World world, BlockPos pos, BlockState state, ItemStack tool) {
		WorldHelper.destroyBlockWithTool(world.as(), pos, state, tool);
	}

	/**
	if this block is part of a larger multi-block crop structure (for example, trees),
	then this method should invoke {@link MultiHarvestContext#queueNeighbor(BlockPos, BlockState, Harvestable)}
	on the direct neighbors of the provided block which are part of the same structure.
	this includes diagonal neighbors, if applicable. the queued Harvestable will
	later have this method invoked again to queue the neighbor's neighbors.
	this allows fine-tuning of how exactly to detect parts of a structure.
	for example, trees again. the leaves are part of the tree structure,
	but a leaf should never add another log to the queue,
	otherwise we risk queueing more than one tree in total.
	see {@link Harvestables#tree(Predicate, Predicate, Predicate, Direction)}
	for the code used for identifying vanilla trees.

	the default implementation of this method does nothing,
	which indicates that this block exists "on its own",
	and is not part of a bigger multi-block structure.
	*/
	public default void queueNeighbors(World world, BlockPos pos, BlockState state, MultiHarvestContext context) {

	}

	@NonExtendable
	public static interface MultiHarvestContext {

		public abstract void queueNeighbor(BlockPos pos, BlockState state, Harvestable harvestable);
	}

	public static final Object INITIALIZER = new Object() {{
		LOOKUP.registerForBlocks(
			BlockApiLookups.constant(NEVER_HARVEST),
			Blocks.PITCHER_CROP,
			Blocks.TORCHFLOWER_CROP,
			Blocks.PUMPKIN_STEM,
			Blocks.ATTACHED_PUMPKIN_STEM,
			Blocks.MELON_STEM,
			Blocks.ATTACHED_MELON_STEM,
			Blocks.RED_MUSHROOM,
			Blocks.BROWN_MUSHROOM
		);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(OAK_AZALEA_TREE), Blocks.OAK_LOG, Blocks.OAK_WOOD);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(SPRUCE_TREE), Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(BIRCH_TREE), Blocks.BIRCH_LOG, Blocks.BIRCH_WOOD);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(JUNGLE_TREE), Blocks.JUNGLE_LOG, Blocks.JUNGLE_WOOD);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(ACACIA_TREE), Blocks.ACACIA_LOG, Blocks.ACACIA_WOOD);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(DARK_OAK_TREE), Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_WOOD);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(CHERRY_TREE), Blocks.CHERRY_LOG, Blocks.CHERRY_WOOD);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(MANGROVE_TREE), Blocks.MANGROVE_LOG, Blocks.MANGROVE_WOOD);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(CHORUS_TREE), Blocks.CHORUS_PLANT);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(HUGE_MUSHROOM), Blocks.MUSHROOM_STEM);

		LOOKUP.registerForBlocks(BlockApiLookups.constant(VINES), Blocks.VINE);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(WEEPING_VINES), Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(TWISTING_VINES), Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(BAMBOO), Blocks.BAMBOO);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(SUGAR_CANE), Blocks.SUGAR_CANE);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(CACTUS), Blocks.CACTUS);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(KELP), Blocks.KELP, Blocks.KELP_PLANT);

		LOOKUP.registerForBlocks(BlockApiLookups.constant(NETHER_WART), Blocks.NETHER_WART);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(COCOA), Blocks.COCOA);
		LOOKUP.registerForBlocks(BlockApiLookups.constant(SWEET_BERRY_BUSH), Blocks.SWEET_BERRY_BUSH);

		LOOKUP.registerFallback((world, pos, state, blockEntity, context) -> {
			if (state.getBlock() instanceof CropBlock) {
				return GENERIC_CROP;
			}
			if (state.isIn(BlockTags.LOGS)) {
				return GENERIC_TREE;
			}
			if (state.isIn(BlockTags.SAPLINGS)) {
				return NEVER_HARVEST;
			}
			return null;
		});
	}};
}