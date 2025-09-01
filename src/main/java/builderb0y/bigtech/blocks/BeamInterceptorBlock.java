package builderb0y.bigtech.blocks;

import java.text.NumberFormat;
import java.util.EnumMap;
import java.util.LinkedList;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import builderb0y.bigtech.api.BeamInteractor.BeamCallback;
import builderb0y.bigtech.beams.BeamUtil;
import builderb0y.bigtech.beams.base.*;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;
import builderb0y.bigtech.blockEntities.BeamInterceptorBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.WorldHelper;

public class BeamInterceptorBlock extends Block implements BeamCallback, Waterloggable, BlockEntityProvider, LegacyOnStateReplaced {

	public static EnumMap<Direction, VoxelShape> SHAPES = new EnumMap<>(Direction.class);
	static {
		SHAPES.put(Direction.DOWN, VoxelShapes.union(
			VoxelShapes.cuboid(0.25D, 0.0D, 0.25D, 0.75D, 0.125D, 0.75D),
			VoxelShapes.cuboid(7.0D / 16.0D, 2.0D / 16.0D, 7.0D / 16.0D, 9.0D / 16.0D, 9.0D / 16.0D, 9.0D / 16.0D)
		));
		SHAPES.put(Direction.UP, VoxelShapes.union(
			VoxelShapes.cuboid(0.25D, 0.875D, 0.25D, 0.75D, 1.0D, 0.75D),
			VoxelShapes.cuboid(7.0D / 16.0D, 7.0D / 16.0D, 7.0D / 16.0D, 9.0D / 16.0D, 14.0D / 16.0D, 9.0D / 16.0D)
		));
		SHAPES.put(Direction.NORTH, VoxelShapes.union(
			VoxelShapes.cuboid(0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.125D),
			VoxelShapes.cuboid(7.0D / 16.0D, 7.0D / 16.0D, 2.0D / 16.0D, 9.0D / 16.0D, 9.0D / 16.0D, 9.0D / 16.0D)
		));
		SHAPES.put(Direction.SOUTH, VoxelShapes.union(
			VoxelShapes.cuboid(0.25D, 0.25D, 0.875D, 0.75D, 0.75D, 1.0D),
			VoxelShapes.cuboid(7.0D / 16.0D, 7.0D / 16.0D, 7.0D / 16.0D, 9.0D / 16.0D, 9.0D / 16.0D, 14.0D / 16.0D)
		));
		SHAPES.put(Direction.WEST, VoxelShapes.union(
			VoxelShapes.cuboid(0.0D, 0.25D, 0.25D, 0.125D, 0.75D, 0.75D),
			VoxelShapes.cuboid(2.0D / 16.0D, 7.0D / 16.0D, 7.0D / 16.0D, 9.0D / 16.0D, 9.0D / 16.0D, 9.0D / 16.0D)
		));
		SHAPES.put(Direction.EAST, VoxelShapes.union(
			VoxelShapes.cuboid(0.875D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D),
			VoxelShapes.cuboid(7.0D / 16.0D, 7.0D / 16.0D, 7.0D / 16.0D, 14.0D / 16.0D, 9.0D / 16.0D, 9.0D / 16.0D)
		));
	}
	public static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();
	static {
		NUMBER_FORMAT.setMinimumFractionDigits(1);
		NUMBER_FORMAT.setMaximumFractionDigits(3);
	}

	public static final MapCodec<BeamInterceptorBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public BeamInterceptorBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.FACING, Direction.DOWN)
			.with(Properties.POWERED, Boolean.FALSE)
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES.get(state.get(Properties.FACING));
	}

	@Override
	public boolean spreadOut(ServerWorld world, BlockPos pos, BlockState state, SpreadingBeamSegment inputSegment) {
		inputSegment.beam().addSegment(world, inputSegment.extend());
		return true;
	}

	@Override
	public void onBeamAdded(ServerWorld world, BlockPos pos, BlockState state, PersistentBeam beam) {
		world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	public void onBeamRemoved(ServerWorld world, BlockPos pos, BlockState state, PersistentBeam beam) {
		world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	public void onBeamPulse(ServerWorld world, BlockPos pos, BlockState state, PulseBeam beam) {
		if (this.setPowered(world, pos, state, beam)) {
			world.scheduleBlockTick(pos, this, 2);
		}
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(Properties.POWERED) ? 15 : 0;
	}

	@Override
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(Properties.POWERED) && state.get(Properties.FACING) == direction.getOpposite() ? 15 : 0;
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		if (state.get(Properties.POWERED)) {
			return Math.clamp(BeamUtil.getSegmentsLeadingOutOf(world, pos, BeamUtil.VISIBLE).count(), 0, 15);
		}
		return 0;
	}

	public boolean checkPowered(World world, BlockPos pos, BlockState state, @Nullable Beam pulse) {
		BeamInterceptorBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, BeamInterceptorBlockEntity.class);
		if (blockEntity == null) {
			return false;
		}
		ColorAccumulator accumulator = new ColorAccumulator();
		if (pulse != null) {
			accumulator.acceptAll(pos, pulse.seen.get(ChunkSectionPos.toLong(pos)));
		}
		accumulator.acceptAll(pos, ChunkBeamStorageHolder.KEY.get(world.getChunk(pos)).require().get(pos.getY() >> 4));
		Vector3f sum = accumulator.getColor();
		if (sum == null) {
			if (!blockEntity.locked) {
				blockEntity.setColorAndSync(null);
			}
			return false;
		}
		if (blockEntity.locked) {
			return sum.equals(blockEntity.color, 1.0F / 1024.0F);
		}
		else {
			blockEntity.setColorAndSync(sum);
			return true;
		}
	}

	public boolean setPowered(ServerWorld world, BlockPos pos, BlockState state, @Nullable Beam beam) {
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = this.checkPowered(world, pos, state, beam);
		if (powered != shouldBePowered) {
			world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered));
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		this.setPowered(world, pos, state, null);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return (
			super
			.getPlacementState(context)
			.with(Properties.FACING, context.getSide().getOpposite())
			.with(Properties.WATERLOGGED, context.getWorld().getFluidState(context.getBlockPos()).isEqualAndStill(Fluids.WATER))
		);
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = state.get(Properties.FACING);
		BlockPos offset = pos.offset(direction);
		return world.getBlockState(offset).isSideSolid(world, offset, direction.getOpposite(), SideShapeType.CENTER);
	}

	@Override
	public ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.getStackInHand(hand).isEmpty()) {
			return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
		}
		if (!world.isClient) {
			BeamInterceptorBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, BeamInterceptorBlockEntity.class);
			if (blockEntity != null) {
				if (player.isSneaking()) {
					if (blockEntity.locked) {
						blockEntity.locked = false;
						player.sendMessage(Text.translatable("bigtech.beam_interceptor.unlocked"), true);
						world.scheduleBlockTick(pos, this, 2);
					}
					else {
						if (blockEntity.color != null) {
							blockEntity.locked = true;
							player.sendMessage(getColorCode("bigtech.beam_interceptor.locked", blockEntity.color), true);
						}
						else {
							player.sendMessage(Text.translatable("bigtech.beam_interceptor.cant_lock"), true);
						}
					}
					blockEntity.markDirty();
				}
				else {
					player.sendMessage(getColorCode("bigtech.beam_interceptor.current", blockEntity.color), true);
				}
			}
		}
		return ActionResult.SUCCESS;
	}

	public static Text getColorCode(String prefix, Vector3f color) {
		return Text.translatable(
			prefix,
			color == null
			? Text.translatable("bigtech.beam_interceptor.no_color")
			: (
				MutableText
				.of(PlainTextContent.EMPTY)
				.append(Text.literal(NUMBER_FORMAT.format(color.x)).styled((Style style) -> style.withColor(0xFFFF3F3F)))
				.append(Text.literal(", "))
				.append(Text.literal(NUMBER_FORMAT.format(color.y)).styled((Style style) -> style.withColor(0xFF3FFF3F)))
				.append(Text.literal(", "))
				.append(Text.literal(NUMBER_FORMAT.format(color.z)).styled((Style style) -> style.withColor(0xFF3F3FFF)))
			)
		);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BeamInterceptorBlockEntity(pos, state);
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
		if (direction == state.get(Properties.FACING) && !neighborState.isSideSolid(world, pos, direction.getOpposite(), SideShapeType.CENTER)) {
			return (state.get(Properties.WATERLOGGED) ? Blocks.WATER : Blocks.AIR).getDefaultState();
		}
		return state;
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (state.get(Properties.POWERED)) {
			world.updateNeighbors(pos.offset(state.get(Properties.FACING)), this);
		}
		world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	public void legacyOnStateReplaced(ServerWorld world, BlockPos pos, BlockState state, BlockState newState, boolean moved) {
		if (state.get(Properties.POWERED)) {
			world.updateNeighbors(pos.offset(state.get(Properties.FACING)), this);
		}
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(Properties.FACING, mirror.apply(state.get(Properties.FACING)));
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING, Properties.POWERED, Properties.WATERLOGGED);
	}
}