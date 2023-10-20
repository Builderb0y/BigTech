package builderb0y.bigtech.blocks;

import java.util.*;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.mixinterfaces.RoutableEntity;
import builderb0y.bigtech.util.Enums;
import builderb0y.bigtech.util.FairSharing;

public class AscenderBlock extends Block implements AscenderInteractor {

	public final Direction direction;

	public AscenderBlock(Settings settings, Direction direction) {
		super(settings);
		this.direction = direction;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}

	public boolean canMove(World world, BlockPos pos, BlockState state, Entity entity) {
		return !(
			entity.isRemoved   ||
			entity.isSpectator ||
			entity.isSneaking  ||
			(entity instanceof PlayerEntity player && player.abilities.flying)
		);
	}

	@Override
	public AscenderIOType getAscenderIOType(World world, BlockPos pos, BlockState state, Direction face) {
		return face == this.direction.opposite ? AscenderIOType.PRIMARY_INPUT : AscenderIOType.NO_INPUT;
	}

	public Direction[] getApplicableDirections(World world, BlockPos pos, BlockState state) {
		record Tuple(Direction direction, AscenderIOType type) {}
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		Map<AscenderIOType, List<Tuple>> map = (
			Arrays
			.stream(Enums.DIRECTIONS)
			.filter(direction -> direction != this.direction.opposite)
			.map(direction -> {
				BlockState adjacentState = world.getBlockState(mutablePos.set(pos, direction));
				return new Tuple(
					direction,
					adjacentState.getBlock() instanceof AscenderInteractor interactor
					? interactor.getAscenderIOType(world, mutablePos, adjacentState, direction.opposite)
					: AscenderIOType.NO_INPUT
				);
			})
			.filter(tuple -> tuple.type != AscenderIOType.NO_INPUT)
			.collect(Collectors.groupingBy(Tuple::type))
		);

		List<Tuple> list = map.get(AscenderIOType.PRIMARY_INPUT);
		if (list != null && !list.isEmpty) {
			return list.stream().map(Tuple::direction).toArray(Direction[]::new);
		}
		list = map.get(AscenderIOType.SECONDARY_INPUT);
		if (list != null && !list.isEmpty) {
			return list.stream().map(Tuple::direction).toArray(Direction[]::new);
		}
		return new Direction[] { this.direction };
	}

	public Direction computeMovementDirection(World world, BlockPos pos, BlockState state, Entity entity) {
		return world.isClient ? this.direction : SortingCache.get(world, pos, state, this::getApplicableDirections).nextDirection();
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		SortingCache.invalidate(world, pos, state);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		SortingCache.invalidate(world, pos, state);
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	public Direction getMovementDirection(World world, BlockPos pos, BlockState state, Entity entity) {
		return ((RoutableEntity)(entity)).bigtech_computeRoutingInfo(pos, state, this::computeMovementDirection).direction;
	}

	public void move(World world, BlockPos pos, BlockState state, Entity entity) {
		Direction direction = this.getMovementDirection(world, pos, state, entity);
		Vec3d velocity = entity.getVelocity();
		double
			motionX = velocity.x,
			motionY = velocity.y,
			motionZ = velocity.z;
		double friction;
		double acceleration;
		if (direction == Direction.DOWN && entity.isOnGround()) {
			friction = 0.625D;
			acceleration = 0.25D;
		}
		else {
			friction = entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity ? 0.625D : 0.875D;
			acceleration = direction.getAxis() == Axis.Y ? 0.03125D : 0.0625D;
		}
		if (direction.getAxis() != Axis.X) motionX = motionX * friction + (pos.getX() + 0.5D - entity.getX()) * acceleration;
		if (direction.getAxis() != Axis.Y) motionY = (motionY + (pos.getY() + 0.5D - entity.getY())) * 0.25D;
		if (direction.getAxis() != Axis.Z) motionZ = motionZ * friction + (pos.getZ() + 0.5D - entity.getZ()) * acceleration;
		switch (direction) {
			case DOWN  -> motionY = motionY * 0.875D - 0.1875D * 0.25D;
			case UP    -> motionY = Math.min(motionY + 0.4375D * 0.25D /* normal acceleration */ - Math.min(motionY * 0.125D, 0.0D) /* bonus acceleration to slow down/stop entities that are already moving downward */, 0.4375D);
			case NORTH -> motionZ = motionZ * friction - acceleration;
			case SOUTH -> motionZ = motionZ * friction + acceleration;
			case WEST  -> motionX = motionX * friction - acceleration;
			case EAST  -> motionX = motionX * friction + acceleration;
		}
		if (direction != Direction.DOWN) {
			entity.setOnGround(false);
		}
		entity.setVelocity(motionX, motionY, motionZ);
		entity.fallDistance = 0.0F;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		if (
			this.canMove(world, pos, state, entity) &&
			entity.blockPos.equals(pos)
		) {
			this.move(world, pos, state, entity);
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		return context.getStack().isOf(this.asItem()) && !context.shouldCancelInteraction();
	}

	public static class SortingCache {

		public static final Map<World, Map<BlockPos, SortingCache>> WORLDS = new WeakHashMap<>(4);

		public final Direction[] directions;
		public int index;

		public SortingCache(Direction[] directions, int index) {
			this.directions = directions;
			this.index = index;
		}

		public static SortingCache get(World world, BlockPos pos, BlockState state, SortingCachePopulator populator) {
			Map<BlockPos, SortingCache> map;
			synchronized (WORLDS) {
				map = WORLDS.computeIfAbsent(world, k -> new HashMap<>(64));
			}
			SortingCache cache;
			synchronized (map) {
				cache = map.get(pos);
			}
			if (cache == null) {
				cache = new SortingCache(populator.compute(world, pos, state), world.random.nextInt());
				synchronized (map) {
					map.put(pos.toImmutable(), cache);
				}
			}
			return cache;
		}

		public static void invalidate(WorldAccess world, BlockPos pos, BlockState state) {
			if (!(world instanceof World)) return;
			Map<BlockPos, SortingCache> map;
			synchronized (WORLDS) {
				map = WORLDS.get(world);
			}
			if (map != null) {
				synchronized (map) {
					map.remove(pos);
				}
			}
		}

		public Direction nextDirection() {
			return this.directions[FairSharing.compute(this.index++, this.directions.length)];
		}
	}

	@FunctionalInterface
	public static interface SortingCachePopulator {

		public abstract Direction[] compute(World world, BlockPos pos, BlockState state);
	}
}