package builderb0y.bigtech.beams;

import java.util.*;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.apache.commons.lang3.NotImplementedException;
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

public abstract class Beam {

	public final World world;
	public final UUID uuid;
	public BlockPos origin;
	public ArrayDeque<PositionedBeamSegment> queue;
	public SortedSectionBeamStorage seen;
	public Map<BlockPos, BeamCallback> callbacks;

	public Beam(World world, UUID uuid) {
		this.world     = world;
		this.uuid      = uuid;
		this.queue     = new ArrayDeque<>(8);
		this.seen      = new SortedSectionBeamStorage();
		this.callbacks = new Object2ObjectLinkedOpenHashMap<>(8);
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
					if (interactor instanceof BeamCallback callback) {
						BeamCallback old = this.callbacks.putIfAbsent(pos, callback);
						if (old != null && old != callback) {
							throw new NotImplementedException("Multiple callbacks at ${pos}: ${old} -> ${callback}");
						}
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

	public void defaultSpreadOut(BlockPos pos, BlockState state, BeamSegment segment) {
		VoxelShape shape = this.getShape(pos, state);
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
		BlockHitResult hitResult = shape.raycast(start, end, pos);
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