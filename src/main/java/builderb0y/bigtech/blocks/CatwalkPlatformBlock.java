package builderb0y.bigtech.blocks;

import java.util.EnumMap;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.items.CatwalkStairsBlockItem;

public class CatwalkPlatformBlock extends Block implements Waterloggable {

	public static final VoxelShape[] SHAPES = new VoxelShape[16];
	static {
		VoxelShape base  = VoxelShapes.cuboid(0.0D,    0.0D,    0.0D,    1.0D,    0.0625D, 1.0D   );
		VoxelShape north = VoxelShapes.cuboid(0.0D,    0.0625D, 0.0D,    1.0D,    0.75D,   0.0625D);
		VoxelShape east  = VoxelShapes.cuboid(0.9375D, 0.0625D, 0.0D,    1.0D,    0.75D,   1.0D   );
		VoxelShape south = VoxelShapes.cuboid(0.0D,    0.0625D, 0.9375D, 1.0D,    0.75D,   1.0D   );
		VoxelShape west  = VoxelShapes.cuboid(0.0D,    0.0625D, 0.0D,    0.0625D, 0.75D,   1.0D   );
		for (int index = 0; index < 16; index++) {
			VoxelShape shape = base;
			if ((index & (1 << Direction.NORTH.getHorizontal())) != 0) shape = VoxelShapes.combine(shape, north, BooleanBiFunction.OR);
			if ((index & (1 << Direction.EAST .getHorizontal())) != 0) shape = VoxelShapes.combine(shape, east,  BooleanBiFunction.OR);
			if ((index & (1 << Direction.SOUTH.getHorizontal())) != 0) shape = VoxelShapes.combine(shape, south, BooleanBiFunction.OR);
			if ((index & (1 << Direction.WEST .getHorizontal())) != 0) shape = VoxelShapes.combine(shape, west,  BooleanBiFunction.OR);
			SHAPES[index] = shape.simplify();
		}
	}

	public static final EnumMap<Direction, BooleanProperty> PROPERTY_LOOKUP = new EnumMap<>(Direction.class);
	static {
		PROPERTY_LOOKUP.put(Direction.NORTH, Properties.NORTH);
		PROPERTY_LOOKUP.put(Direction.EAST,  Properties.EAST );
		PROPERTY_LOOKUP.put(Direction.SOUTH, Properties.SOUTH);
		PROPERTY_LOOKUP.put(Direction.WEST,  Properties.WEST );
	}

	public static final MapCodec<CatwalkPlatformBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public CatwalkPlatformBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.NORTH, Boolean.TRUE)
			.with(Properties.EAST,  Boolean.TRUE)
			.with(Properties.SOUTH, Boolean.TRUE)
			.with(Properties.WEST,  Boolean.TRUE)
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	public static int getShapeIndex(BlockState state) {
		int index = 0;
		if (state.get(Properties.NORTH)) index |= 1 << Direction.NORTH.getHorizontal();
		if (state.get(Properties.EAST )) index |= 1 << Direction.EAST .getHorizontal();
		if (state.get(Properties.SOUTH)) index |= 1 << Direction.SOUTH.getHorizontal();
		if (state.get(Properties.WEST )) index |= 1 << Direction.WEST .getHorizontal();
		return index;
	}

	public static boolean touchesEdge(VoxelShape shape, Direction direction) {
		return switch (direction.getDirection()) {
			case POSITIVE -> shape.getMax(direction.getAxis()) >= 1.0D;
			case NEGATIVE -> shape.getMin(direction.getAxis()) <= 0.0D;
		};
	}

	public boolean shouldHaveRail(
		BlockView world,
		BlockPos pos,
		Direction direction
	) {
		BlockPos adjacentPos = pos.offset(direction);
		BlockState adjacentState = world.getBlockState(adjacentPos);
		if (adjacentState.isIn(BlockTags.CLIMBABLE)) return false;
		if (touchesEdge(adjacentState.getCollisionShape(world, adjacentPos), direction.getOpposite())) return false;
		BlockPos diagonalPos = adjacentPos.down();
		BlockState diagonalState = world.getBlockState(diagonalPos);
		if (diagonalState.isIn(BlockTags.CLIMBABLE)) return false;
		VoxelShape diagonalShape = diagonalState.getCollisionShape(world, diagonalPos);
		return !touchesEdge(diagonalShape, direction.getOpposite()) || !touchesEdge(diagonalShape, Direction.UP);
	}

	@Override
	public BlockState getStateForNeighborUpdate(
		BlockState state,
		WorldView world,
		ScheduledTickView tickView,
		BlockPos pos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		Random random
	) {
		if (world.isClient()) return state;
		if (state.get(Properties.WATERLOGGED)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (direction == Direction.DOWN) {
			return (
				state
				.with(Properties.NORTH, this.shouldHaveRail(world, pos, Direction.NORTH))
				.with(Properties.EAST,  this.shouldHaveRail(world, pos, Direction.EAST))
				.with(Properties.SOUTH, this.shouldHaveRail(world, pos, Direction.SOUTH))
				.with(Properties.WEST,  this.shouldHaveRail(world, pos, Direction.WEST))
			);
		}
		else if (direction == Direction.UP) {
			return state;
		}
		else {
			return state.with(PROPERTY_LOOKUP.get(direction), this.shouldHaveRail(world, pos, direction));
		}
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		return (
			super.getPlacementState(context)
			.with(Properties.NORTH, this.shouldHaveRail(world, pos, Direction.NORTH))
			.with(Properties.EAST,  this.shouldHaveRail(world, pos, Direction.EAST))
			.with(Properties.SOUTH, this.shouldHaveRail(world, pos, Direction.SOUTH))
			.with(Properties.WEST,  this.shouldHaveRail(world, pos, Direction.WEST))
			.with(Properties.WATERLOGGED, world.getFluidState(pos).isEqualAndStill(Fluids.WATER))
		);
	}

	public Direction getPlacementDirection(BlockPos origin, BlockState state, ItemPlacementContext context) {
		Vec3d hitVec = context.getHitPos();
		double y = hitVec.y - origin.getY();
		boolean useRotation = context.getSide().getHorizontal() < 0 && y < 0.5D;
		if (useRotation) {
			return context.getHorizontalPlayerFacing();
		}
		else {
			double x = hitVec.x - origin.getX();
			double z = hitVec.z - origin.getZ();
			Direction  firstChoice = x > 0.5D ? Direction.EAST  : Direction.WEST;
			Direction secondChoice = z > 0.5D ? Direction.SOUTH : Direction.NORTH;
			if (Math.abs(z - 0.5D) > Math.abs(x - 0.5D)) {
				Direction d = firstChoice;
				firstChoice = secondChoice;
				secondChoice = d;
			}
			if (y <= 0.0625D || state.get(PROPERTY_LOOKUP.get(firstChoice))) {
				return firstChoice;
			}
			else {
				return secondChoice;
			}
		}
	}

	@Override
	public ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (stack.getItem() instanceof BlockItem blockItem && !(blockItem instanceof CatwalkStairsBlockItem)) {
			ItemPlacementContext context = new ItemPlacementContext(player, hand, stack, hit);
			Direction direction = this.getPlacementDirection(pos, state, context);
			context = new ItemPlacementContext(player, hand, stack, hit.withSide(direction));
			ActionResult result = blockItem.place(context);
			return (
				result.isAccepted()
				? result
				: ActionResult.CONSUME
			);
		}
		return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES[getShapeIndex(state)];
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return switch (rotation) {
			case NONE -> (
				state
			);
			case CLOCKWISE_90 -> (
				state
				.with(Properties.NORTH, state.get(Properties.WEST ))
				.with(Properties.EAST,  state.get(Properties.NORTH))
				.with(Properties.SOUTH, state.get(Properties.EAST ))
				.with(Properties.WEST,  state.get(Properties.SOUTH))
			);
			case CLOCKWISE_180 -> (
				state
				.with(Properties.NORTH, state.get(Properties.SOUTH))
				.with(Properties.EAST,  state.get(Properties.WEST ))
				.with(Properties.SOUTH, state.get(Properties.NORTH))
				.with(Properties.WEST,  state.get(Properties.EAST ))
			);
			case COUNTERCLOCKWISE_90 -> (
				state
				.with(Properties.NORTH, state.get(Properties.EAST ))
				.with(Properties.EAST,  state.get(Properties.SOUTH))
				.with(Properties.SOUTH, state.get(Properties.WEST ))
				.with(Properties.WEST,  state.get(Properties.NORTH))
			);
		};
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return switch (mirror) {
			case NONE -> (
				state
			);
			case LEFT_RIGHT -> (
				state
				.with(Properties.NORTH, state.get(Properties.SOUTH))
				.with(Properties.SOUTH, state.get(Properties.NORTH))
			);
			case FRONT_BACK -> (
				state
				.with(Properties.EAST, state.get(Properties.WEST))
				.with(Properties.WEST, state.get(Properties.EAST))
			);
		};
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.NORTH, Properties.EAST, Properties.SOUTH, Properties.WEST, Properties.WATERLOGGED);
	}
}