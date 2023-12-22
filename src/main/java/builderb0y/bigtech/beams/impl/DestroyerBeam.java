package builderb0y.bigtech.beams.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.joml.Vector3f;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.impl.DestructionManager.DestroyQueue;
import builderb0y.bigtech.blocks.FunctionalBlocks;

public class DestroyerBeam extends PersistentBeam {

	public static final Vector3f DEFAULT_COLOR = new Vector3f(0.25F, 0.75F, 0.25F);

	public Map<BlockPos, DestroyQueue> toDestroy = new HashMap<>();

	public DestroyerBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	public boolean serverTick(ItemStack tool) {
		boolean toolChanged = false;
		if (!this.toDestroy.isEmpty) {
			for (Iterator<DestroyQueue> iterator = this.toDestroy.values().iterator(); iterator.hasNext(); ) {
				DestroyQueue queue = iterator.next();
				queue.populate();
				if (queue.inactive.isEmpty) {
					iterator.remove();
					continue;
				}
				//noinspection NonShortCircuitBooleanExpression
				toolChanged |= queue.tick(tool);
			}
		}
		return toolChanged;
	}

	@Override
	public void onRemoved() {
		super.onRemoved();
		if (!this.toDestroy.isEmpty) {
			DestructionManager manager = DestructionManager.forWorld(this.world);
			for (DestroyQueue queue : this.toDestroy.values()) {
				if (queue.populated && !queue.inactive.isEmpty) {
					manager.resetProgress(queue.inactive.lastKey());
				}
			}
		}
	}

	@Override
	public void defaultSpreadOut(BlockPos pos, BlockState state, BeamSegment segment) {
		super.defaultSpreadOut(pos, state, segment);
		if (!state.isAir && state.getFluidState().getBlockState() != state) {
			this.toDestroy.merge(
				pos.toImmutable(),
				new DestroyQueue(this.world, pos, segment.distanceRemaining),
				(oldQueue, newQueue) -> {
					oldQueue.maxDistanceSquared = Math.max(oldQueue.maxDistanceSquared, newQueue.maxDistanceSquared);
					oldQueue.destroySpeed += newQueue.destroySpeed;
					return oldQueue;
				}
			);
		}
	}

	@Override
	public BeamType getType() {
		return BeamTypes.DESTROYER;
	}

	@Override
	public Vector3f getInitialColor() {
		return DEFAULT_COLOR;
	}

	@Override
	public void onBlockChanged(BlockPos pos, BlockState oldState, BlockState newState) {
		BlockState originState = this.world.getBlockState(this.origin);
		if (originState.isOf(FunctionalBlocks.LONG_RANGE_DESTROYER)) {
			this.world.addSyncedBlockEvent(this.origin, FunctionalBlocks.LONG_RANGE_DESTROYER, 0, 0);
		}
		else {
			this.removeFromWorld();
		}
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		super.writeToNbt(nbt);
		nbt.putSubList("destroy_queues", (NbtList destroyQueuesNbt) -> {
			this.toDestroy.values().forEach((DestroyQueue queue) -> {
				destroyQueuesNbt.addCompound((NbtCompound queueNbt) -> queueNbt
					.withBlockPos("origin", queue.origin)
					.withInt("dist", queue.maxDistanceSquared)
					.withFloat("speed", queue.destroySpeed)
				);
			});
		});
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		super.readFromNbt(nbt);
		this.toDestroy =
		nbt
		.getList("destroy_queues", NbtElement.COMPOUND_TYPE)
		.stream()
		.map(NbtCompound.class::cast)
		.map((NbtCompound queueNbt) -> new DestroyQueue(
			this.world,
			queueNbt.getBlockPos("origin"),
			queueNbt.getInt("dist"),
			queueNbt.getFloat("speed")
		))
		.collect(Collectors.toMap(
			queue -> queue.origin,
			Function.identity(),
			(queue1, queue2) -> {
				throw new IllegalStateException("Multiple queues at ${queue1.origin}");
			},
			HashMap::new //ensure mutable.
		));
	}
}