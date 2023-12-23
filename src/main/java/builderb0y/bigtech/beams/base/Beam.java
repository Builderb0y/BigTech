package builderb0y.bigtech.beams.base;

import java.util.*;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.joml.Vector3fc;

import net.minecraft.block.BlockState;
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

after being created, a beam will first spread via {@link #fire()},
then it is typically added to the world {@link #addToWorld()}.
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

	public void fire(BlockPos startPos, BeamDirection direction, double distance) {
		this.prepare(startPos, direction, distance);
		this.fire();
		this.addToWorld();
	}

	public void prepare(BlockPos startPos, BeamDirection direction, double distance) {
		this.origin = startPos.toImmutable();
		this.queue.addLast(new SpreadingBeamSegment(this.origin, new BeamSegment(this, direction, true, null), distance));
	}

	public void fire() {
		for (SpreadingBeamSegment segment; (segment = this.queue.pollFirst()) != null;) {
			if (this.seen.addSegment(segment, true) && segment.segment.direction != BeamDirection.CENTER) {
				BlockPos pos = segment.endPos;
				BlockState state = this.world.getBlockState(pos);
				BeamInteractor interactor = BeamInteractor.LOOKUP.find(this.world, pos, state, null, this);
				if (interactor != null) {
					if (interactor instanceof BeamCallback) {
						this.callbacks.add(pos);
					}
					if (interactor.spreadOut(segment, state)) {
						continue;
					}
				}
				this.defaultSpreadOut(segment, state);
			}
		}
	}

	public void addSegment(BlockPos pos, BeamSegment segment, double distanceRemaining) {
		if (segment != null) this.queue.addLast(new SpreadingBeamSegment(pos, segment, distanceRemaining));
	}

	public void addSegment(SpreadingBeamSegment segment) {
		if (segment != null) this.queue.addLast(segment);
	}

	public VoxelShape getShape(BlockPos pos, BlockState state) {
		return state.getCollisionShape(this.world, pos);
	}

	public static BlockHitResult rayCast(SpreadingBeamSegment segment, VoxelShape shape) {
		BlockPos pos = segment.startPos;
		BeamDirection direction = segment.segment.direction;
		Vec3d start = new Vec3d(
			pos.x + 0.5D - direction.x,
			pos.y + 0.5D - direction.y,
			pos.z + 0.5D - direction.z
		);
		Vec3d end = new Vec3d(
			pos.x + 0.5D + direction.x,
			pos.y + 0.5D + direction.y,
			pos.z + 0.5D + direction.z
		);
		return shape.raycast(start, end, pos);
	}

	public void defaultSpreadOut(SpreadingBeamSegment segment, BlockState state) {
		BlockHitResult hitResult = rayCast(segment, this.getShape(segment.startPos, state));
		if (hitResult != null) {
			this.handleIntersection(segment, state, hitResult);
		}
		else {
			this.handleNonIntersection(segment, state);
		}
	}

	public void handleIntersection(SpreadingBeamSegment segment, BlockState state, BlockHitResult hitResult) {
		this.addSegment(segment.terminate());
	}

	public void handleNonIntersection(SpreadingBeamSegment segment, BlockState state) {
		this.addSegment(segment.extend());
	}

	public abstract void addToWorld();

	@Override
	public String toString() {
		return "${this.getClass().getName()}: { type: ${this.type}, uuid: ${this.uuid}, origin: ${this.origin} }";
	}
}