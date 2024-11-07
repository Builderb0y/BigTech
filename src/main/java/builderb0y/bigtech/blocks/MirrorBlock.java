package builderb0y.bigtech.blocks;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import builderb0y.bigtech.api.BeamInteractor;
import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.SpreadingBeamSegment;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class MirrorBlock extends Block implements Waterloggable, BeamInteractor {

	public static final double[] aabbNumbers = { 0.125D, 0.125D, 0.1875D, 0.3125D, 0.46875D, 0.3125D, 0.1875D, 0.125D };
	public static final double[] sinCache = { 0.0D, 0.38268343236509D, 0.707106781186548D, 0.923879532511287D, 1.0D, 0.923879532511287D, 0.707106781186548D, 0.38268343236509D, 0.0D, -0.38268343236509D, -0.707106781186548D, -0.923879532511287D };

	public static final MapCodec<MirrorBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public Map<BlockState, VoxelShape> shapes;

	public MirrorBlock(Settings settings) {
		super(settings);
		this.shapes = this.createShapeMap();
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.FACING, Direction.DOWN)
			.with(Properties.ATTACHED, Boolean.FALSE)
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	public Map<BlockState, VoxelShape> createShapeMap() {
		ImmutableList<BlockState> states = this.stateManager.getStates();
		Map<BlockState, VoxelShape> shapes = new HashMap<>(states.size());
		Map<Box, VoxelShape> cache = new HashMap<>(states.size());
		for (BlockState state : states) {
			Box box = this.computeShape(state);
			VoxelShape shape = cache.computeIfAbsent(box, VoxelShapes::cuboid);
			shapes.put(state, shape);
		}
		return shapes;
	}

	public Box computeShape(BlockState state) {
		int rotation = state.get(BigTechProperties.ROTATION_0_7);
		double startX = aabbNumbers[rotation];
		double startZ = aabbNumbers[(rotation + 4) & 7];
		double startY = state.get(Properties.ATTACHED) ? 0.0D : 0.25D;
		return switch (state.get(Properties.FACING)) {
			case DOWN  -> new Box(startX, startY, startZ, 1.0D - startX, 0.75D,         1.0D - startZ);
			case UP    -> new Box(startX, 0.25D,  startZ, 1.0D - startX, 1.0D - startY, 1.0D - startZ);
			case NORTH -> new Box(startX, startZ, startY, 1.0D - startX, 1.0D - startZ, 0.75D        );
			case SOUTH -> new Box(startX, startZ, 0.25D,  1.0D - startX, 1.0D - startZ, 1.0D - startY);
			case EAST  -> new Box(0.25D,  startZ, startX, 1.0D - startY, 1.0D - startZ, 1.0D - startX);
			case WEST  -> new Box(startY, startZ, startX, 0.75D,         1.0D - startZ, 1.0D - startX);
		};
	}

	@Override
	public boolean spreadOut(ServerWorld world, BlockPos pos, BlockState state, SpreadingBeamSegment inputSegment) {
		BeamDirection entryDirection = inputSegment.direction();
		double normalX, normalY, normalZ;
		int rotation = state.get(BigTechProperties.ROTATION_0_7);
		double sin = sinCache[rotation];
		double cos = sinCache[rotation + 4];
		switch (state.get(Properties.FACING)) {
			case UP    -> { normalX = -sin ; normalY = 0.0D; normalZ =  cos ; }
			case DOWN  -> { normalX =  sin ; normalY = 0.0D; normalZ =  cos ; }
			case NORTH -> { normalX = -sin ; normalY = cos ; normalZ =  0.0D; }
			case SOUTH -> { normalX =  sin ; normalY = cos ; normalZ =  0.0D; }
			case EAST  -> { normalX =  0.0D; normalY = cos ; normalZ = -sin ; }
			case WEST  -> { normalX =  0.0D; normalY = cos ; normalZ =  sin ; }
			default    -> throw new AssertionError(state.toString());
		}
		BeamDirection exitDirection = entryDirection.reflectUnchecked(normalX, normalY, normalZ);
		if (entryDirection == exitDirection) return false;
		inputSegment.beam().addSegment(world, inputSegment.extend(inputSegment.distanceRemaining() - exitDirection.type.magnitude, exitDirection));
		return true;
	}

	@Override
	public ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!stack.isEmpty()) return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
		if (!world.isClient) {
			world.setBlockState(pos, state.with(BigTechProperties.ROTATION_0_7, (state.get(BigTechProperties.ROTATION_0_7) + (player.isSneaking() ? 1 : -1)) & 7));
		}
		return ActionResult.SUCCESS;
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
		if (state.get(Properties.WATERLOGGED)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		Direction currentFacing = state.get(Properties.FACING);
		if (state.get(Properties.ATTACHED)) {
			if (direction == currentFacing) {
				if (!neighborState.isSideSolidFullSquare(world, neighborPos, direction.getOpposite())) {
					return this.tryAttachOpposite(world, pos, state, direction);
				}
			}
		}
		else {
			if (direction == currentFacing) {
				if (neighborState.isSideSolidFullSquare(world, neighborPos, direction.getOpposite())) {
					return state.with(Properties.ATTACHED, Boolean.TRUE);
				}
			}
			else if (direction == currentFacing.getOpposite()) {
				if (neighborState.isSideSolidFullSquare(world, neighborPos, currentFacing)) {
					return (
						state
						.with(Properties.ATTACHED, Boolean.TRUE)
						.with(Properties.FACING, direction)
						.with(BigTechProperties.ROTATION_0_7, (-state.get(BigTechProperties.ROTATION_0_7)) & 7)
					);
				}
			}
		}
		return state;
	}

	public BlockState tryAttachOpposite(WorldView world, BlockPos pos, BlockState state, Direction direction) {
		Direction opposite = direction.getOpposite();
		BlockPos oppositePos = pos.offset(opposite);
		BlockState oppositeState = world.getBlockState(oppositePos);
		if (oppositeState.isSideSolidFullSquare(world, oppositePos, direction)) {
			return (
				state
				.with(Properties.FACING, opposite)
				.with(BigTechProperties.ROTATION_0_7, (-state.get(BigTechProperties.ROTATION_0_7)) & 7)
			);
		}
		else {
			return state.with(Properties.ATTACHED, Boolean.FALSE);
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		BlockPos offsetPos = context.getBlockPos().offset(context.getSide().getOpposite());
		BlockState state = (
			super
			.getPlacementState(context)
			.with(Properties.WATERLOGGED, context.getWorld().getFluidState(context.getBlockPos()).isEqualAndStill(Fluids.WATER))
			.with(Properties.FACING, context.getSide().getOpposite())
			.with(Properties.ATTACHED, context.getWorld().getBlockState(offsetPos).isSideSolidFullSquare(context.getWorld(), offsetPos, context.getSide()))
		);
		PlayerEntity player = context.getPlayer();
		if (player != null) {
			Vec3d look;
			double rotation = switch (context.getSide()) {
				case UP -> player.getYaw() / -22.5F;
				case DOWN -> player.getYaw() / 22.5F;
				case NORTH -> {
					look = player.getRotationVector();
					yield getAngle(look.x, look.y);
				}
				case SOUTH -> {
					look = player.getRotationVector();
					yield getAngle(-look.x, look.y);
				}
				case WEST -> {
					look = player.getRotationVector();
					yield getAngle(-look.z, look.y);
				}
				case EAST -> {
					look = player.getRotationVector();
					yield getAngle(look.z, look.y);
				}
			};
			state = state.with(BigTechProperties.ROTATION_0_7, MathHelper.floor(rotation + 0.5D) & 7);
		}
		return state;
	}

	public static double getAngle(double x, double y) {
		double scale = MathHelper.inverseSqrt(x * x + y * y);
		x *= scale;
		y *= scale;
		return Math.acos(x > 0.0D ? y : -y) * (8.0D / Math.PI);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape = this.shapes.get(state);
		if (shape != null) return shape;
		else throw new IllegalStateException("State not present in map: ${state}");
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).getDefaultState();
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING, BigTechProperties.ROTATION_0_7, Properties.ATTACHED, Properties.WATERLOGGED);
	}
}