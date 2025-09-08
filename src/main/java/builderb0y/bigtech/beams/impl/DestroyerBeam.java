package builderb0y.bigtech.beams.impl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.ReadView.ListReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.storage.WriteView.ListView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.autocodec.util.AutoCodecUtil;
import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.base.SpreadingBeamSegment;
import builderb0y.bigtech.beams.impl.DestructionManager.DestroyQueue;
import builderb0y.bigtech.blocks.FunctionalBlocks;

public class DestroyerBeam extends PersistentBeam {

	public static final Vector3fc DEFAULT_COLOR = new Vector3f(0.25F, 0.75F, 0.25F);

	public Map<BlockPos, DestroyQueue> toDestroy = new HashMap<>();

	public DestroyerBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	public boolean serverTick(ItemStack tool) {
		boolean toolChanged = false;
		if (!this.toDestroy.isEmpty()) {
			for (Iterator<DestroyQueue> iterator = this.toDestroy.values().iterator(); iterator.hasNext(); ) {
				DestroyQueue queue = iterator.next();
				queue.populate();
				if (queue.inactive.isEmpty()) {
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
	public void onRemoved(ServerWorld world) {
		super.onRemoved(world);
		if (!this.toDestroy.isEmpty()) {
			DestructionManager manager = DestructionManager.forWorld(world);
			for (DestroyQueue queue : this.toDestroy.values()) {
				if (queue.populated && !queue.inactive.isEmpty()) {
					manager.resetProgress(queue.inactive.lastKey());
				}
			}
		}
	}

	@Override
	public void defaultSpreadOut(ServerWorld world, BlockPos pos, BlockState state, SpreadingBeamSegment inputSegment) {
		super.defaultSpreadOut(world, pos, state, inputSegment);
		if (!state.isAir() && state.getFluidState().getBlockState() != state) {
			this.toDestroy.merge(
				pos.toImmutable(),
				new DestroyQueue(world, pos, inputSegment.distanceRemaining()),
				(DestroyQueue oldQueue, DestroyQueue newQueue) -> {
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
	public Vector3fc getInitialColor() {
		return DEFAULT_COLOR;
	}

	@Override
	public void onBlockChanged(ServerWorld world, BlockPos pos, BlockState oldState, BlockState newState) {
		BlockState originState = world.getBlockState(this.origin);
		if (originState.isOf(FunctionalBlocks.LONG_RANGE_DESTROYER)) {
			world.addSyncedBlockEvent(this.origin, FunctionalBlocks.LONG_RANGE_DESTROYER, 0, 0);
		}
		else {
			this.removeFromWorld(world);
		}
	}

	@Override
	public void write(WriteView view) {
		super.write(view);
		view.putSubList("destroy_queues", (ListView destroyQueuesView) -> {
			this.toDestroy.values().forEach((DestroyQueue queue) -> {
				destroyQueuesView.add((WriteView queueView) -> {
					queueView
					.withBlockPos("origin", queue.origin)
					.withInt("dist", queue.maxDistanceSquared)
					.withFloat("speed", queue.destroySpeed);
				});
			});
		});
	}

	@Override
	public void read(ReadView view) {
		super.read(view);
		ListReadView queues = view.getOptionalListReadView("destroy_queues").orElse(null);
		this.toDestroy = (
			queues == null
			? new HashMap<>()
			: queues
			.stream()
			.map(this::parseQueue)
			.filter(Objects::nonNull)
			.collect(Collectors.toMap(
				(DestroyQueue queue) -> queue.origin,
				Function.identity(),
				(DestroyQueue queue1, DestroyQueue queue2) -> queue1,
				HashMap::new //ensure mutable.
			))
		);
	}

	public @Nullable DestroyQueue parseQueue(ReadView view) {
		BlockPos pos = view.getBlockPos("origin").orElse(null);
		if (pos == null) return null;
		int dist = view.getInt("dist", -1);
		if (dist < 0) return null;
		float speed = view.getFloat("speed", Float.NaN);
		if (Float.isNaN(speed)) return null;
		return new DestroyQueue(this.world.as(), pos, dist, speed);
	}
}