package builderb0y.bigtech.api;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.util.Directions;

public class Harvestables {

	public static final BeamDirection[] BEAM_DIRECTIONS_BY_DISTANCE = (
		Arrays
		.stream(BeamDirection.VALUES)
		.filter(direction -> direction != BeamDirection.CENTER)
		.sorted(Comparator.comparing(direction -> direction.type))
		.toArray(BeamDirection[]::new)
	);

	public static <C extends Comparable<C>> Harvestable crop(Property<C> property, C value) {
		return new Harvestable() {

			@Override
			public boolean canHarvest(World world, BlockPos pos, BlockState state) {
				return state.get(property) == value;
			}
		};
	}

	public static Harvestable chorusTree(Predicate<BlockState> isPartOfTree) {
		return new Harvestable() {

			@Override
			public void queueNeighbors(World world, BlockPos pos, BlockState state, MultiHarvestContext context) {
				for (Direction direction : Directions.ALL) {
					if (direction == Direction.DOWN) continue;
					BlockPos adjacentPos = pos.offset(direction);
					BlockState adjacentState = world.getBlockState(adjacentPos);
					if (isPartOfTree.test(adjacentState)) {
						context.queueNeighbor(adjacentPos, adjacentState, this);
					}
				}
			}
		};
	}

	public static Harvestable tree(Block log, Block wood, Block leaf) {
		return tree(isIn(log, wood), isOf(leaf));
	}

	public static Harvestable tree(Predicate<BlockState> isLog, Predicate<BlockState> isLeaf) {
		return tree(isLog, isLeaf, isOf(Blocks.VINE), Direction.DOWN);
	}

	public static Harvestable tree(Predicate<BlockState> isLog, Predicate<BlockState> isLeaf, Predicate<BlockState> isVines, Direction vineGrowDirection) {
		Harvestable vines = new Harvestable() {

			@Override
			public void queueNeighbors(World world, BlockPos pos, BlockState state, MultiHarvestContext context) {
				BlockPos adjacentPos = pos.offset(vineGrowDirection);
				BlockState adjacentState = world.getBlockState(adjacentPos);
				if (isVines.test(adjacentState)) {
					context.queueNeighbor(adjacentPos, adjacentState, this);
				}
			}

			@Override
			public String toString() {
				return "Harvestables.tree(vines part: ${isVines})";
			}
		};
		Harvestable leaves = new Harvestable() {

			@Override
			public void queueNeighbors(World world, BlockPos pos, BlockState state, MultiHarvestContext context) {
				int currentDistance = state.contains(LeavesBlock.DISTANCE) ? state.get(LeavesBlock.DISTANCE) : -1;
				for (Direction direction : Directions.ALL) {
					BlockPos adjacentPos = pos.offset(direction);
					BlockState adjacentState = world.getBlockState(adjacentPos);
					if (isLeaf.test(adjacentState) && (!adjacentState.contains(LeavesBlock.DISTANCE) || adjacentState.get(LeavesBlock.DISTANCE) >= currentDistance)) {
						context.queueNeighbor(adjacentPos, adjacentState, this);
					}
					else if (isVines.test(adjacentState)) {
						context.queueNeighbor(adjacentPos, adjacentState, vines);
					}
				}
			}

			@Override
			public String toString() {
				return "Harvestables.tree(leaves part: ${isLeaf})";
			}
		};
		Harvestable log = new Harvestable() {

			@Override
			public void queueNeighbors(World world, BlockPos pos, BlockState state, MultiHarvestContext context) {
				for (BeamDirection direction : BEAM_DIRECTIONS_BY_DISTANCE) {
					BlockPos adjacentPos = pos.offset(direction);
					BlockState adjacentState = world.getBlockState(adjacentPos);
					if (isLeaf.test(adjacentState)) {
						context.queueNeighbor(adjacentPos, adjacentState, leaves);
					}
					else if (isLog.test(adjacentState)) {
						context.queueNeighbor(adjacentPos, adjacentState, this);
					}
					else if (isVines.test(adjacentState)) {
						context.queueNeighbor(adjacentPos, adjacentState, vines);
					}
				}
			}

			@Override
			public String toString() {
				return "Harvestables.tree(log part: ${isLog})";
			}
		};
		return new Harvestable() {

			@Override
			public void queueNeighbors(World world, BlockPos pos, BlockState state, MultiHarvestContext context) {
				if (isLog.test(state)) {
					log.queueNeighbors(world, pos, state, context);
				}
				else if (isLeaf.test(state)) {
					leaves.queueNeighbors(world, pos, state, context);
				}
				else if (isVines.test(state)) {
					vines.queueNeighbors(world, pos, state, context);
				}
			}

			@Override
			public String toString() {
				return "Harvestables.tree(${isLog}, ${isLeaf}, ${isVines})";
			}
		};
	}

	public static Harvestable hugeMushroom(Predicate<BlockState> isStem, Predicate<BlockState> isBlock) {
		Harvestable block = new Harvestable() {

			@Override
			public void queueNeighbors(World world, BlockPos pos, BlockState state, MultiHarvestContext context) {
				for (BeamDirection direction : BEAM_DIRECTIONS_BY_DISTANCE) {
					BlockPos adjacentPos = pos.offset(direction);
					BlockState adjacentState = world.getBlockState(adjacentPos);
					if (isBlock.test(adjacentState)) {
						context.queueNeighbor(adjacentPos, adjacentState, this);
					}
				}
			}

			@Override
			public String toString() {
				return "Harvestables.hugeMushroom(block part: ${isBlock})";
			}
		};
		return new Harvestable() {

			@Override
			public void queueNeighbors(World world, BlockPos pos, BlockState state, MultiHarvestContext context) {
				BlockPos upPos = pos.up();
				BlockState upState = world.getBlockState(upPos);
				if (isStem.test(upState)) {
					context.queueNeighbor(upPos, upState, this);
				}
				else if (isBlock.test(upState)) {
					context.queueNeighbor(upPos, upState, block);
				}
				BlockPos downPos = pos.down();
				BlockState downState = world.getBlockState(downPos);
				if (isStem.test(downState)) {
					context.queueNeighbor(downPos, downState, this);
				}
			}

			@Override
			public String toString() {
				return "Harvestables.hugeMushroom(${isStem}, ${isBlock})";
			}
		};
	}

	public static Harvestable upOnly(Block block) {
		return upOnly(isOf(block));
	}

	public static Harvestable upOnly(Predicate<BlockState> isPartOfTree) {
		return new Harvestable() {

			@Override
			public void queueNeighbors(World world, BlockPos pos, BlockState state, MultiHarvestContext context) {
				BlockPos adjacentPos = pos.up();
				BlockState adjacentState = world.getBlockState(adjacentPos);
				if (isPartOfTree.test(adjacentState)) {
					context.queueNeighbor(adjacentPos, adjacentState, this);
				}
			}

			@Override
			public String toString() {
				return "Harvestables.upOnly(${isPartOfTree})";
			}
		};
	}

	public static Harvestable downOnly(Block block) {
		return downOnly(isOf(block));
	}

	public static Harvestable downOnly(Predicate<BlockState> isPartOfTree) {
		return new Harvestable() {

			@Override
			public void queueNeighbors(World world, BlockPos pos, BlockState state, MultiHarvestContext context) {
				BlockPos adjacentPos = pos.down();
				BlockState adjacentState = world.getBlockState(adjacentPos);
				if (isPartOfTree.test(adjacentState)) {
					context.queueNeighbor(adjacentPos, adjacentState, this);
				}
			}

			@Override
			public String toString() {
				return "Harvestables.downOnly(${isPartOfTree})";
			}
		};
	}

	public static Predicate<BlockState> isOf(Block block) {
		return new Predicate<>() {

			@Override
			public boolean test(BlockState state) {
				return state.isOf(block);
			}

			@Override
			public String toString() {
				return "Harvestables.isOf(${block})";
			}
		};
	}

	@Deprecated //you probably don't want this overload.
	public static Predicate<BlockState> isIn() {
		return Predicates.alwaysFalse();
	}

	@Deprecated //you probably don't want isOf() instead.
	public static Predicate<BlockState> isIn(Block block) {
		return isOf(block);
	}

	public static Predicate<BlockState> isIn(Block... blocks) {
		return new Predicate<>() {

			@Override
			public boolean test(BlockState state) {
				Block block = state.block;
				for (Block compare : blocks) {
					if (block == compare) return true;
				}
				return false;
			}

			@Override
			public String toString() {
				return "Harvestables.isIn${java.util.Arrays.toString(blocks)}";
			}
		};
	}

	public static Predicate<BlockState> isIn(TagKey<Block> tag) {
		return new Predicate<>() {

			@Override
			public boolean test(BlockState state) {
				return state.isIn(tag);
			}

			@Override
			public String toString() {
				return "Harvestables.isIn(${tag})";
			}
		};
	}
}