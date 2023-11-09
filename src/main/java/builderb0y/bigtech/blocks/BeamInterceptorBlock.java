package builderb0y.bigtech.blocks;

import java.text.NumberFormat;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import builderb0y.bigtech.api.BeamInteractor.BeamCallback;
import builderb0y.bigtech.beams.base.*;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.blockEntities.BeamInterceptorBlockEntity;
import builderb0y.bigtech.util.WorldHelper;

public class BeamInterceptorBlock extends Block implements BeamCallback, Waterloggable, BlockEntityProvider {

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

	public BeamInterceptorBlock(Settings settings) {
		super(settings);
		this.defaultState = (
			this.defaultState
			.with(Properties.FACING, Direction.DOWN)
			.with(Properties.POWERED, Boolean.FALSE)
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPES.get(state.get(Properties.FACING));
	}

	@Override
	public boolean spreadOut(BlockPos pos, BlockState state, BeamSegment inputSegment) {
		inputSegment.beam.addSegment(pos, inputSegment.extend());
		return true;
	}

	@Override
	public void onBeamAdded(BlockPos pos, BlockState state, PersistentBeam beam) {
		beam.world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	public void onBeamRemoved(BlockPos pos, BlockState state, PersistentBeam beam) {
		beam.world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	public void onBeamPulse(BlockPos pos, BlockState state, PulseBeam beam) {
		if (this.setPowered(beam.world, pos, state, beam)) {
			beam.world.scheduleBlockTick(pos, this, 2);
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(Properties.POWERED) ? 15 : 0;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(Properties.POWERED) && state.get(Properties.FACING) == direction.opposite ? 15 : 0;
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
		accumulator.acceptAll(pos, ChunkBeamStorageHolder.KEY.get(world.getChunk(pos)).require().get(pos.y >> 4));
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

	public boolean setPowered(World world, BlockPos pos, BlockState state, @Nullable Beam beam) {
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
	@Deprecated
	@SuppressWarnings("deprecation")
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
			.with(Properties.FACING, context.getSide().opposite)
			.with(Properties.WATERLOGGED, context.world.getFluidState(context.blockPos).isEqualAndStill(Fluids.WATER))
		);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = state.get(Properties.FACING);
		BlockPos offset = pos.offset(direction);
		return world.getBlockState(offset).isSideSolid(world, offset, direction.opposite, SideShapeType.CENTER);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.getStackInHand(hand).isEmpty) {
			return ActionResult.PASS;
		}
		if (!world.isClient) {
			BeamInterceptorBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, BeamInterceptorBlockEntity.class);
			if (blockEntity != null) {
				if (player.isSneaking) {
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
				.of(TextContent.EMPTY)
				.append(Text.literal(NUMBER_FORMAT.format(color.x)).styled(style -> style.withColor(0xFFFF3F3F)))
				.append(Text.literal(", "))
				.append(Text.literal(NUMBER_FORMAT.format(color.y)).styled(style -> style.withColor(0xFF3FFF3F)))
				.append(Text.literal(", "))
				.append(Text.literal(NUMBER_FORMAT.format(color.z)).styled(style -> style.withColor(0xFF3F3FFF)))
			)
		);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BeamInterceptorBlockEntity(pos, state);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(Properties.WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (direction == state.get(Properties.FACING) && !neighborState.isSideSolid(world, pos, direction.opposite, SideShapeType.CENTER)) {
			return (state.get(Properties.WATERLOGGED) ? Blocks.WATER : Blocks.AIR).defaultState;
		}
		return state;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (state.get(Properties.POWERED)) {
			world.updateNeighbors(pos.offset(state.get(Properties.FACING)), this);
		}
		world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		if (state.get(Properties.POWERED)) {
			world.updateNeighbors(pos.offset(state.get(Properties.FACING)), this);
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.FACING, rotation.rotate(state.get(Properties.FACING)));
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(Properties.FACING, mirror.apply(state.get(Properties.FACING)));
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING, Properties.POWERED, Properties.WATERLOGGED);
	}

	public static class ColorAccumulator implements Consumer<BeamSegment> {

		public final Vector3f color = new Vector3f();
		public int count = 0;

		public void acceptAll(BlockPos pos, BasicSectionBeamStorage sectionStorage) {
			if (sectionStorage != null) {
				LinkedList<BeamSegment> segments = sectionStorage.checkSegments(pos);
				if (segments != null) {
					segments.forEach(this);
				}
			}
		}

		@Override
		public void accept(BeamSegment segment) {
			if (segment.visible) this.accept(segment.effectiveColor);
		}

		public void accept(Vector3f color) {
			if (this.count == 0) {
				this.color.set(color);
			}
			else {
				if (this.count == 1) {
					this.color.mul(this.color);
				}
				this.color.x += color.x * color.x;
				this.color.y += color.y * color.y;
				this.color.z += color.z * color.z;
			}
			this.count++;
		}

		public @Nullable Vector3f getColor() {
			return switch (this.count) {
				case 0 -> null;
				case 1 -> this.color;
				default -> {
					this.color.div(this.count);
					this.color.x = MathHelper.sqrt(this.color.x);
					this.color.y = MathHelper.sqrt(this.color.y);
					this.color.z = MathHelper.sqrt(this.color.z);
					yield this.color;
				}
			};
		}
	}
}