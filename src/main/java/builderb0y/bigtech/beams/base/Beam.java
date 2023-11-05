package builderb0y.bigtech.beams.base;

import java.util.*;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.joml.Vector3f;

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
	public ArrayDeque<PositionedBeamSegment> queue;
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

	public abstract Vector3f getInitialColor();

	public void fire(BlockPos startPos, BeamDirection direction, double distance) {
		this.prepare(startPos, direction, distance);
		this.fire();
		this.addToWorld();
	}

	public void prepare(BlockPos startPos, BeamDirection direction, double distance) {
		this.origin = startPos.toImmutable();
		this.queue.addLast(new PositionedBeamSegment(this.origin, new BeamSegment(this, direction, distance, true, null)));
	}

	public void fire() {
		for (PositionedBeamSegment segment; (segment = this.queue.pollFirst()) != null;) {
			if (this.seen.addSegment(segment, true) && segment.segment.direction != BeamDirection.CENTER) {
				BlockPos pos = segment.endPos;
				BlockState state = this.world.getBlockState(pos);
				BeamInteractor interactor = BeamInteractor.LOOKUP.find(this.world, pos, state, null, this);
				if (interactor != null) {
					if (interactor instanceof BeamCallback) {
						this.callbacks.add(pos);
					}
					if (interactor.spreadOut(pos, state, segment.segment)) {
						continue;
					}
				}
				this.defaultSpreadOut(pos, state, segment.segment);
			}
		}
	}

	public void addSegment(BlockPos pos, BeamSegment segment) {
		if (segment != null) this.queue.addLast(new PositionedBeamSegment(pos, segment));
	}

	public void addSegment(PositionedBeamSegment segment) {
		if (segment != null) this.queue.addLast(segment);
	}

	public VoxelShape getShape(BlockPos pos, BlockState state) {
		return state.getCollisionShape(this.world, pos);
	}

	public static BlockHitResult rayCast(BlockPos pos, VoxelShape shape, BeamSegment segment) {
		Vec3d start = new Vec3d(
			pos.x + 0.5D - segment.direction.x,
			pos.y + 0.5D - segment.direction.y,
			pos.z + 0.5D - segment.direction.z
		);
		Vec3d end = new Vec3d(
			pos.x + 0.5D + segment.direction.x,
			pos.y + 0.5D + segment.direction.y,
			pos.z + 0.5D + segment.direction.z
		);
		return shape.raycast(start, end, pos);
	}

	public void defaultSpreadOut(BlockPos pos, BlockState state, BeamSegment segment) {
		BlockHitResult hitResult = rayCast(pos, this.getShape(pos, state), segment);
		if (hitResult != null) {
			this.handleIntersection(pos, state, segment, hitResult);
		}
		else {
			this.handleNonIntersection(pos, state, segment);
		}
	}

	public void handleIntersection(BlockPos pos, BlockState state, BeamSegment segment, BlockHitResult hitResult) {
		this.addSegment(pos, segment.terminate());
	}

	public void handleNonIntersection(BlockPos pos, BlockState state, BeamSegment segment) {
		this.addSegment(pos, segment.extend());
	}

	public abstract void addToWorld();

	@Override
	public String toString() {
		return "${this.getClass().getName()}: { type: ${this.type}, uuid: ${this.uuid}, origin: ${this.origin} }";
	}
}