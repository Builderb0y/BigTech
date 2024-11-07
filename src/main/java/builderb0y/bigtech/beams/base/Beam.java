package builderb0y.bigtech.beams.base;

import java.util.*;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.joml.Vector3fc;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import builderb0y.bigtech.api.BeamInteractor;
import builderb0y.bigtech.api.BeamInteractor.BeamCallback;
import builderb0y.bigtech.beams.storage.SortedSectionBeamStorage;

/**
a laser beam!

after being created, a beam will first spread via {@link #fire(ServerWorld)},
then it is typically added to the world {@link #addToWorld(ServerWorld)}.
what happens next depends on what type of beam this is.
there are 2 variants of beams: {@link PulseBeam} and {@link PersistentBeam}.
see their documentation for what they do when added to a world.
*/
public abstract class Beam {

	public final World world;
	public final UUID uuid;
	public BlockPos origin;
	public ArrayDeque<SpreadingBeamSegment> queue;
	public SortedSectionBeamStorage seen;
	public Set<BlockPos> callbacks;

	public Beam(World world, UUID uuid) {
		this.world     = world;
		this.uuid      = uuid;
		this.queue     = new ArrayDeque<>(8);
		this.seen      = new SortedSectionBeamStorage();
		this.callbacks = new ObjectOpenHashSet<>(8);
	}

	public abstract BeamType getType();

	public abstract Vector3fc getInitialColor();

	public void fire(ServerWorld world, BlockPos startPos, BeamDirection direction, double distance) {
		this.prepare(world, startPos, direction, distance);
		this.fire(world);
		this.addToWorld(world);
	}

	public void prepare(ServerWorld world, BlockPos startPos, BeamDirection direction, double distance) {
		this.origin = startPos.toImmutable();
		this.addSegment(world, this.origin, new BeamSegment(this, direction, true, null), distance);
	}

	public void fire(ServerWorld world) {
		for (SpreadingBeamSegment segment; (segment = this.queue.pollFirst()) != null;) {
			if (this.seen.addSegment(segment, true) && segment.segment().direction() != BeamDirection.CENTER) {
				BlockPos pos = segment.endPos();
				BlockState state = world.getBlockState(pos);
				BeamInteractor interactor = BeamInteractor.LOOKUP.find(world, pos, state, null, this);
				if (interactor != null) {
					if (interactor instanceof BeamCallback) {
						this.callbacks.add(pos);
					}
					if (interactor.spreadOut(world, pos, state, segment)) {
						continue;
					}
				}
				this.defaultSpreadOut(world, pos, state, segment);
			}
		}
	}

	public void addSegment(ServerWorld world, BlockPos pos, BeamSegment segment, double distanceRemaining) {
		this.queue.addLast(new SpreadingBeamSegment(pos, segment, distanceRemaining));
	}

	public void addSegment(ServerWorld world, SpreadingBeamSegment segment) {
		this.queue.addLast(Objects.requireNonNull(segment, "segment"));
	}

	public VoxelShape getShape(BlockPos pos, BlockState state) {
		return state.getCollisionShape(this.world, pos);
	}

	public static BlockHitResult rayCast(SpreadingBeamSegment segment, VoxelShape shape) {
		BlockPos pos = segment.startPos();
		BeamDirection direction = segment.segment().direction();
		Vec3d start = new Vec3d(
			pos.getX() + 0.5D - direction.x,
			pos.getY() + 0.5D - direction.y,
			pos.getZ() + 0.5D - direction.z
		);
		Vec3d end = new Vec3d(
			pos.getX() + 0.5D + direction.x,
			pos.getY() + 0.5D + direction.y,
			pos.getZ() + 0.5D + direction.z
		);
		return shape.raycast(start, end, pos);
	}

	public void defaultSpreadOut(ServerWorld world, BlockPos pos, BlockState state, SpreadingBeamSegment segment) {
		BlockHitResult hitResult = rayCast(segment, this.getShape(pos, state));
		if (hitResult != null) {
			this.handleIntersection(world, pos, state, segment, hitResult);
		}
		else {
			this.handleNonIntersection(world, pos, state, segment);
		}
	}

	public void handleIntersection(ServerWorld world, BlockPos pos, BlockState state, SpreadingBeamSegment segment, BlockHitResult hitResult) {
		this.addSegment(world, segment.terminate());
	}

	public void handleNonIntersection(ServerWorld world, BlockPos pos, BlockState state, SpreadingBeamSegment segment) {
		this.addSegment(world, segment.extend());
	}

	public abstract void addToWorld(ServerWorld world);

	@Override
	public String toString() {
		return "${this.getClass().getName()}: { type: ${this.getType()}, uuid: ${this.uuid}, origin: ${this.origin} }";
	}
}