package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import builderb0y.bigtech.api.PistonInteractor;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.items.CatwalkStairsBlockItem;
import builderb0y.bigtech.mixinterfaces.HeldItemGetter;
import builderb0y.bigtech.util.VoxelShapeBuilder;

public class CatwalkStairsBlock extends Block implements Waterloggable, PistonInteractor {

	public static final VoxelShape[]
		SHAPES         = generateShapes(false),
		HOVERED_SHAPES = generateShapes(true );

	public static final MapCodec<CatwalkStairsBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public CatwalkStairsBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
			.with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER)
			.with(BigTechProperties.LEFT, Boolean.TRUE)
			.with(BigTechProperties.RIGHT, Boolean.TRUE)
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	public static VoxelShape[] generateShapes(boolean hovered) {
		VoxelShapeBuilder root = VoxelShapeBuilder.create();
		VoxelShape[] shapes = new VoxelShape[32];
		DoubleBlockHalf[] halves = DoubleBlockHalf.values();
		for (int index = 0; index < 32; index++) {
			Direction facing = Direction.fromHorizontalQuarterTurns(index >>> 3);
			DoubleBlockHalf half = halves[(index & 4) >>> 2];
			boolean left  = (index & 2) != 0;
			boolean right = (index & 1) != 0;

			VoxelShapeBuilder builder = root.rotateAround(0.5D, 0.0D, 0.5D, switch (facing) {
				case NORTH -> BlockRotation.NONE;
				case EAST  -> BlockRotation.CLOCKWISE_90;
				case SOUTH -> BlockRotation.CLOCKWISE_180;
				case WEST  -> BlockRotation.COUNTERCLOCKWISE_90;
				default -> throw new AssertionError("non-horizontal direction: " + facing);
			});

			if (half == DoubleBlockHalf.UPPER) {
				builder = builder.translate(0.0D, -1.0D, 0.0D);
			}

			builder.addCuboid(0.0D, hovered ? 0.0D :  7.0D / 16.0D, 0.5D, 1.0D, 0.5D, 1.0D);
			builder.addCuboid(0.0D, hovered ? 0.5D : 15.0D / 16.0D, 0.0D, 1.0D, 1.0D, 0.5D);
			if (left) {
				builder.addCuboid(0.0D, 0.5D, 0.5D, 1.0D / 16.0D, 1.25D, 1.0D);
				builder.addCuboid(0.0D, 1.0D, 0.0D, 1.0D / 16.0D, 1.75D, 0.5D);
			}
			if (right) {
				builder.addCuboid(15.0D / 16.0D, 0.5D, 0.5D, 1.0D, 1.25D, 1.0D);
				builder.addCuboid(15.0D / 16.0D, 1.0D, 0.0D, 1.0D, 1.75D, 0.5D);
			}
			shapes[index] = builder.buildAndReset();
		}
		return shapes;
	}

	public static int getShapeIndex(BlockState state) {
		int index = state.get(Properties.HORIZONTAL_FACING).getHorizontalQuarterTurns() << 3;
		index |= state.get(Properties.DOUBLE_BLOCK_HALF).ordinal() << 2;
		if (state.get(BigTechProperties.LEFT)) index |= 2;
		if (state.get(BigTechProperties.RIGHT)) index |= 1;
		return index;
	}

	@Override
	public boolean isSticky(PistonHandlerInfo handler, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean canStickTo(
		PistonHandlerInfo handler,
		BlockPos selfPos,
		BlockState selfState,
		BlockPos otherPos,
		BlockState otherState,
		Direction face
	) {
		return face == getOtherHalfDirection(selfState) && otherState.isOf(this);
	}

	public static Direction getOtherHalfDirection(DoubleBlockHalf half) {
		return switch (half) {
			case LOWER -> Direction.UP;
			case UPPER -> Direction.DOWN;
		};
	}

	public static Direction getOtherHalfDirection(BlockState state) {
		return getOtherHalfDirection(state.get(Properties.DOUBLE_BLOCK_HALF));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return (
			context instanceof HeldItemGetter getter && (
				getter.bigtech_getHeldItem().getItem() instanceof BlockItem ||
				getter.bigtech_getHeldItem().isSuitableFor(state)
			)
			? HOVERED_SHAPES
			: SHAPES
		)
		[getShapeIndex(state)];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return HOVERED_SHAPES[getShapeIndex(state)];
	}

	public BlockState transferState(BlockState state, BlockState from) {
		return (
			state
			.with(       Properties.HORIZONTAL_FACING, from.get(       Properties.HORIZONTAL_FACING))
			.with(BigTechProperties.LEFT,              from.get(BigTechProperties.LEFT             ))
			.with(BigTechProperties.RIGHT,             from.get(BigTechProperties.RIGHT            ))
		);
	}

	public BlockState getRemovalState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Blocks.WATER : Blocks.AIR).getDefaultState();
	}

	public boolean needsRail(BlockState thisState, BlockState thatState) {
		return !(
			thatState.getBlock() instanceof CatwalkStairsBlock &&
			thatState.get(Properties.HORIZONTAL_FACING) == thisState.get(Properties.HORIZONTAL_FACING) &&
			thatState.get(Properties.DOUBLE_BLOCK_HALF) == thisState.get(Properties.DOUBLE_BLOCK_HALF)
		);
	}

	public BlockState handleMismatchedNeighbor(
		WorldView world,
		BlockPos pos,
		BlockState state,
		BlockPos neighborPos,
		BlockState neighborState,
		Direction direction
	) {
		//workaround for the fact that pistons call this logic before all blocks are placed.
		if (world instanceof World liveWorld) {
			liveWorld.addSyncedBlockEvent(pos.toImmutable(), this, 0, 0);
		}
		return state;
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
		if (direction.getAxis() == Axis.Y) {
			DoubleBlockHalf half = state.get(Properties.DOUBLE_BLOCK_HALF);
			if (direction == getOtherHalfDirection(half)) {
				if (neighborState.isOf(this) && neighborState.get(Properties.DOUBLE_BLOCK_HALF) != half) {
					return this.transferState(state, neighborState);
				}
				else {
					return this.handleMismatchedNeighbor(
						world,
						pos,
						state,
						neighborPos,
						neighborState,
						direction
					);
				}
			}
		}
		else {
			Direction facing = state.get(Properties.HORIZONTAL_FACING);
			if (direction == facing.rotateYClockwise()) {
				return state.with(BigTechProperties.RIGHT, this.needsRail(state, neighborState));
			}
			else if (direction == facing.rotateYCounterclockwise()) {
				return state.with(BigTechProperties.LEFT, this.needsRail(state, neighborState));
			}
		}
		return state;
	}

	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		BlockPos neighborPos = pos.offset(getOtherHalfDirection(state));
		BlockState neighborState = world.getBlockState(neighborPos);
		BlockState replacementState = (
			neighborState.isOf(this)
			? this.transferState(state, neighborState)
			: this.getRemovalState(state)
		);
		world.setBlockState(pos, replacementState, NOTIFY_ALL);
		return super.onSyncedBlockEvent(state, world, pos, type, data);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		World world = context.getWorld();
		BlockPos bottomPos = context.getBlockPos();
		if (world.isOutOfHeightLimit(bottomPos.getY() + 1)) return null;
		BlockPos topPos = bottomPos.up();
		if (!world.getBlockState(topPos).canReplace(context)) return null;

		Direction front = context.getHorizontalPlayerFacing();
		BlockState state = this.getDefaultState().with(Properties.HORIZONTAL_FACING, front);
		return (
			state
			.with(BigTechProperties.RIGHT, this.needsRail(state, world.getBlockState(bottomPos.offset(front.rotateYClockwise()))))
			.with(BigTechProperties.LEFT,  this.needsRail(state, world.getBlockState(bottomPos.offset(front.rotateYCounterclockwise()))))
			.with(Properties.WATERLOGGED,  world.getFluidState(bottomPos).getFluid() == Fluids.WATER)
		);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		BlockPos upPos = pos.up();
		world.setBlockState(upPos, state.with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).with(Properties.WATERLOGGED, world.getFluidState(upPos).isEqualAndStill(Fluids.WATER)));
	}

	@Override
	public ActionResult onUseWithItem(ItemStack stack, BlockState againstState, World world, BlockPos againstPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (stack.getItem() instanceof BlockItem blockItem && !(blockItem instanceof CatwalkStairsBlockItem)) {
			if (againstState.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
				if (hit.getBlockPos().getY() - hit.getBlockPos().getY() < 0.0D) {
					againstPos = againstPos.down();
				}
			}
			else {
				if (hit.getBlockPos().getY() - hit.getBlockPos().getY() > 1.0D) {
					againstPos = againstPos.up();
				}
			}
			Direction offsetDirection;
			Direction againstFacing = againstState.get(Properties.HORIZONTAL_FACING);
			Direction playerFacing = player.getHorizontalFacing();
			double sidewaysPosition = switch (againstFacing) {
				case NORTH    ->        (hit.getPos().x - againstPos.getX());
				case SOUTH    -> 1.0D - (hit.getPos().x - againstPos.getX());
				case EAST     ->        (hit.getPos().z - againstPos.getZ());
				case WEST     -> 1.0D - (hit.getPos().z - againstPos.getZ());
				case UP, DOWN -> throw new AssertionError();
			};
			if (sidewaysPosition < 0.0626D) {
				offsetDirection = againstFacing.rotateYCounterclockwise();
			}
			else if (sidewaysPosition > 0.9374D) {
				offsetDirection = againstFacing.rotateYClockwise();
			}
			else {
				if (playerFacing == againstFacing) {
					if (againstState.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
						againstPos = againstPos.up();
					}
				}
				else {
					if (againstState.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
						againstPos = againstPos.down();
					}
				}
				offsetDirection = playerFacing;
			}
			ItemPlacementContext context = new ItemPlacementContext(player, hand, stack, new BlockHitResult(hit.getPos(), offsetDirection, againstPos, hit.isInsideBlock()));
			ActionResult result = blockItem.place(context);
			return (
				result.isAccepted()
				? result
				: ActionResult.CONSUME
			);
		}
		return super.onUseWithItem(stack, againstState, world, againstPos, player, hand, hit);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(Properties.HORIZONTAL_FACING, mirror.apply(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING, Properties.DOUBLE_BLOCK_HALF, BigTechProperties.LEFT, BigTechProperties.RIGHT, Properties.WATERLOGGED);
	}
}