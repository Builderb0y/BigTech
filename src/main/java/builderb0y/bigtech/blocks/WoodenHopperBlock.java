package builderb0y.bigtech.blocks;

import com.google.common.collect.ObjectArrays;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class WoodenHopperBlock extends Block {

	//shapes from HopperBlock. those are private and I can't be bothered
	//to add an accessor or AW for all of them, so I just copy-pasted them.
	public static final VoxelShape
		TOP_SHAPE           = Block.createCuboidShape(0.0, 10.0, 0.0, 16.0, 16.0, 16.0),
		MIDDLE_SHAPE        = Block.createCuboidShape(4.0,  4.0, 4.0, 12.0, 10.0, 12.0),
		OUTSIDE_SHAPE       = VoxelShapes.union(MIDDLE_SHAPE, TOP_SHAPE),
		INSIDE_SHAPE        = Block.createCuboidShape(2.0, 11.0, 2.0, 14.0, 16.0, 14.0),
		DEFAULT_SHAPE       = VoxelShapes.combineAndSimplify(OUTSIDE_SHAPE, INSIDE_SHAPE, BooleanBiFunction.ONLY_FIRST),
		DOWN_SHAPE          = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape( 6.0, 0.0,  6.0, 10.0, 4.0, 10.0)),
		EAST_SHAPE          = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape(12.0, 4.0,  6.0, 16.0, 8.0, 10.0)),
		NORTH_SHAPE         = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape( 6.0, 4.0,  0.0, 10.0, 8.0,  4.0)),
		SOUTH_SHAPE         = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape( 6.0, 4.0, 12.0, 10.0, 8.0, 16.0)),
		WEST_SHAPE          = VoxelShapes.union(DEFAULT_SHAPE, Block.createCuboidShape( 0.0, 4.0,  6.0,  4.0, 8.0, 10.0)),
		DOWN_RAYCAST_SHAPE  = INSIDE_SHAPE,
		EAST_RAYCAST_SHAPE  = VoxelShapes.union(INSIDE_SHAPE, Block.createCuboidShape(12.0, 8.0,  6.0, 16.0, 10.0, 10.0)),
		NORTH_RAYCAST_SHAPE = VoxelShapes.union(INSIDE_SHAPE, Block.createCuboidShape( 6.0, 8.0,  0.0, 10.0, 10.0,  4.0)),
		SOUTH_RAYCAST_SHAPE = VoxelShapes.union(INSIDE_SHAPE, Block.createCuboidShape( 6.0, 8.0, 12.0, 10.0, 10.0, 16.0)),
		WEST_RAYCAST_SHAPE  = VoxelShapes.union(INSIDE_SHAPE, Block.createCuboidShape( 0.0, 8.0,  6.0,  4.0, 10.0, 10.0));

	public static final MapCodec<WoodenHopperBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public WoodenHopperBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(Properties.HOPPER_FACING, Direction.DOWN));
		ItemStorage.SIDED.registerForBlocks(
			(
				World world,
				BlockPos pos,
				BlockState state,
				BlockEntity blockEntity,
				Direction side
			)
			-> {
				Direction facing = state.get(Properties.HOPPER_FACING);
				BlockPos adjacentPos = pos.offset(facing);
				BlockState adjacentState = world.getBlockState(adjacentPos);
				VoxelShape shape = adjacentState.getCollisionShape(world, adjacentPos);
				if (!shape.isEmpty()) {
					double x = 0.5D, y = 0.0D, z = 0.5D;
					if (facing == Direction.DOWN) {
						y += 1.0D - EntityType.ITEM.getHeight() - 0.125D;
					}
					else {
						x += (facing.getOffsetX() + facing.getOffsetX() * EntityType.ITEM.getWidth()) * 0.5D - facing.getOffsetX();
						y += 0.125D;
						z += (facing.getOffsetZ() + facing.getOffsetZ() * EntityType.ITEM.getWidth()) * 0.5D - facing.getOffsetZ();
					}
					VoxelShape spawnArea = VoxelShapes.cuboid(EntityType.ITEM.getSpawnBox(x, y, z));
					if (VoxelShapes.matchesAnywhere(shape, spawnArea, BooleanBiFunction.AND)) {
						return null;
					}
				}
				return new WoodenHopperStorage(
					new WoodenHopperSnapshot(
						world, pos, facing
					)
				);
			},
			this
		);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch (state.get(Properties.HOPPER_FACING)) {
			case UP    -> DEFAULT_SHAPE;
			case DOWN  ->    DOWN_SHAPE;
			case NORTH ->   NORTH_SHAPE;
			case SOUTH ->   SOUTH_SHAPE;
			case WEST  ->    WEST_SHAPE;
			case EAST  ->    EAST_SHAPE;
		};
	}

	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return switch (state.get(Properties.HOPPER_FACING)) {
			case UP    ->        INSIDE_SHAPE;
			case DOWN  ->  DOWN_RAYCAST_SHAPE;
			case NORTH -> NORTH_RAYCAST_SHAPE;
			case SOUTH -> SOUTH_RAYCAST_SHAPE;
			case WEST  ->  WEST_RAYCAST_SHAPE;
			case EAST  ->  EAST_RAYCAST_SHAPE;
		};
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		Direction facing = context.getSide();
		return this.getDefaultState().with(Properties.HOPPER_FACING, facing.getAxis() == Axis.Y ? Direction.DOWN : facing.getOpposite());
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.HOPPER_FACING, rotation.rotate(state.get(Properties.HOPPER_FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(Properties.HOPPER_FACING)));
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HOPPER_FACING);
	}

	@Override
	public boolean canPathfindThrough(BlockState state, NavigationType type) {
		return false;
	}

	public static class WoodenHopperSnapshot extends SnapshotParticipant<ItemStack[]> {

		public static final ItemStack[] EMPTY_STACK_ARRAY = {};

		public final World world;
		public final BlockPos pos;
		public final Direction facing;
		public ItemStack[] stacks = EMPTY_STACK_ARRAY;

		public WoodenHopperSnapshot(World world, BlockPos pos, Direction facing) {
			this.world = world;
			this.pos = pos;
			this.facing = facing;
		}

		@Override
		public ItemStack[] createSnapshot() {
			return this.stacks.clone();
		}

		@Override
		public void readSnapshot(ItemStack[] stacks) {
			this.stacks = stacks;
		}

		@Override
		public void onFinalCommit() {
			double
				x = this.pos.getX() + 0.5D,
				y = this.pos.getY(),
				z = this.pos.getZ() + 0.5D;
			if (this.facing == Direction.DOWN) {
				y -= EntityType.ITEM.getHeight() + 0.125D;
			}
			else {
				x += (this.facing.getOffsetX() + this.facing.getOffsetX() * EntityType.ITEM.getWidth()) * 0.5D;
				y += 0.125D;
				z += (this.facing.getOffsetZ() + this.facing.getOffsetZ() * EntityType.ITEM.getWidth()) * 0.5D;
			}
			double
				velocityX = this.facing.getOffsetX() * 0.125D,
				velocityZ = this.facing.getOffsetZ() * 0.125D;
			for (ItemStack stack : this.stacks) {
				this.world.spawnEntity(new ItemEntity(this.world, x, y, z, stack, velocityX, 0.0D, velocityZ));
			}
			this.stacks = EMPTY_STACK_ARRAY;
		}

		public void add(TransactionContext context, ItemStack stack) {
			this.updateSnapshots(context);
			this.stacks = ObjectArrays.concat(this.stacks, stack);
		}
	}

	public static class WoodenHopperStorage implements InsertionOnlyStorage<ItemVariant> {

		public final WoodenHopperSnapshot snapshot;

		public WoodenHopperStorage(WoodenHopperSnapshot snapshot) {
			this.snapshot = snapshot;
		}

		@Override
		public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
			if (maxAmount <= 0) return 0L;
			long remaining = maxAmount;
			int stackLimit = resource.getItem().getMaxCount();
			while (remaining > stackLimit) {
				this.snapshot.add(transaction, resource.toStack(stackLimit));
				remaining -= stackLimit;
			}
			if (remaining > 0) {
				this.snapshot.add(transaction, resource.toStack((int)(remaining)));
			}
			return maxAmount;
		}
	}
}