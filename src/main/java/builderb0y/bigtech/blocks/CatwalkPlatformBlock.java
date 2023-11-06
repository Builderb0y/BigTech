package builderb0y.bigtech.blocks;

import java.util.EnumMap;

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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

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
			if ((index & (1 << Direction.NORTH.horizontal)) != 0) shape = VoxelShapes.combine(shape, north, BooleanBiFunction.OR);
			if ((index & (1 << Direction.EAST .horizontal)) != 0) shape = VoxelShapes.combine(shape, east,  BooleanBiFunction.OR);
			if ((index & (1 << Direction.SOUTH.horizontal)) != 0) shape = VoxelShapes.combine(shape, south, BooleanBiFunction.OR);
			if ((index & (1 << Direction.WEST .horizontal)) != 0) shape = VoxelShapes.combine(shape, west,  BooleanBiFunction.OR);
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

	public CatwalkPlatformBlock(Settings settings) {
		super(settings);
		this.defaultState = (
			this.defaultState
			.with(Properties.NORTH, Boolean.TRUE)
			.with(Properties.EAST,  Boolean.TRUE)
			.with(Properties.SOUTH, Boolean.TRUE)
			.with(Properties.WEST,  Boolean.TRUE)
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	public static int getShapeIndex(BlockState state) {
		int index = 0;
		if (state.get(Properties.NORTH)) index |= 1 << Direction.NORTH.horizontal;
		if (state.get(Properties.EAST )) index |= 1 << Direction.EAST .horizontal;
		if (state.get(Properties.SOUTH)) index |= 1 << Direction.SOUTH.horizontal;
		if (state.get(Properties.WEST )) index |= 1 << Direction.WEST .horizontal;
		return index;
	}

	public static boolean touchesEdge(VoxelShape shape, Direction direction) {
		return switch (direction.direction) {
			case POSITIVE -> shape.getMax(direction.axis) >= 1.0D;
			case NEGATIVE -> shape.getMin(direction.axis) <= 0.0D;
		};
	}

	public boolean shouldHaveRail(
		BlockView world,
		BlockPos pos,
		Direction direction,
		boolean hasFrame
	) {
		if (hasFrame) return false;
		BlockPos adjacentPos = pos.offset(direction);
		BlockState adjacentState = world.getBlockState(adjacentPos);
		if (adjacentState.isIn(BlockTags.CLIMBABLE)) return false;
		if (touchesEdge(adjacentState.getCollisionShape(world, adjacentPos), direction.opposite)) return false;
		BlockPos diagonalPos = adjacentPos.down();
		BlockState diagonalState = world.getBlockState(diagonalPos);
		if (diagonalState.isIn(BlockTags.CLIMBABLE)) return false;
		VoxelShape diagonalShape = diagonalState.getCollisionShape(world, diagonalPos);
		if (touchesEdge(diagonalShape, direction.opposite) && touchesEdge(diagonalShape, Direction.UP)) return false;
		return true;
	}

	public boolean shouldHaveRail_checkFrame(
		BlockView world,
		BlockPos pos,
		Direction direction
	) {
		BlockState downState = world.getBlockState(pos.down());
		return this.shouldHaveRail(world, pos, direction, downState.getBlock() instanceof FrameBlock && downState.isIn(BlockTags.CLIMBABLE));
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(
		BlockState state,
		Direction direction,
		BlockState neighborState,
		WorldAccess world,
		BlockPos pos,
		BlockPos neighborPos
	) {
		if (world.isClient) return state;
		if (state.get(Properties.WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (direction == Direction.DOWN) {
			if (neighborState.getBlock() instanceof FrameBlock && neighborState.isIn(BlockTags.CLIMBABLE)) {
				return (
					state
					.with(Properties.NORTH, Boolean.FALSE)
					.with(Properties.EAST,  Boolean.FALSE)
					.with(Properties.SOUTH, Boolean.FALSE)
					.with(Properties.WEST,  Boolean.FALSE)
				);
			}
			else {
				return (
					state
					.with(Properties.NORTH, this.shouldHaveRail(world, pos, Direction.NORTH, false))
					.with(Properties.EAST,  this.shouldHaveRail(world, pos, Direction.EAST,  false))
					.with(Properties.SOUTH, this.shouldHaveRail(world, pos, Direction.SOUTH, false))
					.with(Properties.WEST,  this.shouldHaveRail(world, pos, Direction.WEST,  false))
				);
			}
		}
		else if (direction == Direction.UP) {
			return state;
		}
		else {
			return state.with(PROPERTY_LOOKUP.get(direction), this.shouldHaveRail_checkFrame(world, pos, direction));
		}
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		BlockState state = super.getPlacementState(context);
		state = state.with(Properties.WATERLOGGED, context.world.getFluidState(context.blockPos).isEqualAndStill(Fluids.WATER));
		BlockState downState = context.world.getBlockState(context.blockPos.down());
		if (downState.getBlock() instanceof FrameBlock && downState.isIn(BlockTags.CLIMBABLE)) {
			state = (
				state
				.with(Properties.NORTH, Boolean.FALSE)
				.with(Properties.EAST,  Boolean.FALSE)
				.with(Properties.SOUTH, Boolean.FALSE)
				.with(Properties.WEST,  Boolean.FALSE)
			);
		}
		else {
			state = (
				state
				.with(Properties.NORTH, this.shouldHaveRail(context.world, context.blockPos, Direction.NORTH, false))
				.with(Properties.EAST,  this.shouldHaveRail(context.world, context.blockPos, Direction.EAST,  false))
				.with(Properties.SOUTH, this.shouldHaveRail(context.world, context.blockPos, Direction.SOUTH, false))
				.with(Properties.WEST,  this.shouldHaveRail(context.world, context.blockPos, Direction.WEST,  false))
			);
		}
		return state;
	}

	public Direction getPlacementDirection(BlockPos origin, BlockState state, ItemPlacementContext context) {
		Vec3d hitVec = context.hitPos;
		double y = hitVec.y - origin.y;
		boolean useRotation = context.side.horizontal < 0 && y < 0.5D;
		if (useRotation) {
			return context.horizontalPlayerFacing;
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
	@Deprecated
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.item instanceof BlockItem blockItem && !(blockItem instanceof CatwalkStairsBlockItem)) {
			ItemPlacementContext context = new ItemPlacementContext(player, hand, stack, hit);
			Direction direction = this.getPlacementDirection(pos, state, context);
			context = new ItemPlacementContext(player, hand, stack, hit.withSide(direction));
			ActionResult result = blockItem.place(context);
			return result.isAccepted ? result : ActionResult.CONSUME_PARTIAL;
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES[getShapeIndex(state)];
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.NORTH, Properties.EAST, Properties.SOUTH, Properties.WEST, Properties.WATERLOGGED);
	}
}