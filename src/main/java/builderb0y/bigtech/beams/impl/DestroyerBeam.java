package builderb0y.bigtech.beams.impl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.joml.Vector3f;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import builderb0y.bigtech.api.Harvestable;
import builderb0y.bigtech.api.Harvestable.MultiHarvestContext;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.impl.DestroyerBeam.DestroyQueue.StateHarvestable;
import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.util.WorldHelper;

public class DestroyerBeam extends PersistentBeam {

	public static final Vector3f DEFAULT_COLOR = new Vector3f(0.25F, 0.75F, 0.25F);

	public Map<BlockPos, DestroyQueue> toDestroy = new HashMap<>();

	public DestroyerBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	public boolean serverTick(ItemStack tool) {
		boolean toolChanged = false;
		DestroyerBeamManager manager = DestroyerBeamManager.forWorld(this.world);
		for (Iterator<DestroyQueue> iterator = this.toDestroy.values().iterator(); iterator.hasNext(); ) {
			DestroyQueue queue = iterator.next();
			queue.populate();
			if (queue.inactive.isEmpty) {
				iterator.remove();
				continue;
			}
			BlockPos pos = queue.inactive.lastKey();
			StateHarvestable data = queue.inactive.get(pos);
			BlockState expectedState = data.state;
			BlockState actualState = this.world.getBlockState(pos);
			if (actualState.block == expectedState.block) {
				if (data.harvestable.canHarvest(this.world, pos, actualState)) {
					if (!actualState.isToolRequired || tool.isSuitableFor(actualState)) {
						float hardness = actualState.getHardness(this.world, pos);
						if (hardness > 0.0F) {
							float speed = tool.getMiningSpeedMultiplier(actualState);
							if (speed > 1.0F) {
								int efficiency = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, tool);
								if (efficiency > 0 && !tool.isEmpty) {
									speed += efficiency * efficiency + 1;
								}
							}
							speed /= hardness * 30.0F;
							speed *= queue.destroySpeed * 0.0625F;

							DestroyerBeamManager.Info info = manager.getInfo(pos);
							info.progress += speed;
							this.world.setBlockBreakingInfo(info.breakerID, pos, (int)(info.progress * 10.0F));
							if (info.progress >= 1.0F) {
								manager.breakingPositions.remove(pos);
								WorldHelper.destroyBlockWithTool((ServerWorld)(this.world), pos, actualState, tool);
								queue.inactive.remove(pos);
								if (tool.damage(1, this.world.random, null)) {
									tool.decrement(1);
									tool.setDamage(0);
								}
								toolChanged = true;
							}
						}
						else if (hardness == 0.0F) {
							WorldHelper.destroyBlockWithTool((ServerWorld)(this.world), pos, actualState, tool);
							queue.inactive.remove(pos);
						}
						else { //hardness is negative. or NaN for whatever reason. either way, treat as unbreakable.
							manager.resetProgress(pos);
						}
					}
					else { //can't break with current tool.
						manager.resetProgress(pos);
					}
				}
				else { //can't harvest (harvestable said no).
					manager.resetProgress(pos);
				}
			}
			else { //block changed.
				manager.resetProgress(pos);
				queue.inactive.remove(pos);
			}
		}
		return toolChanged;
	}

	@Override
	public void onRemoved() {
		super.onRemoved();
		for (DestroyQueue queue : this.toDestroy.values()) {
			if (queue.populated && !queue.inactive.isEmpty) {
				DestroyerBeamManager.forWorld(this.world).resetProgress(queue.inactive.lastKey());
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
		if (originState.isOf(FunctionalBlocks.DESTROYER)) {
			this.world.addSyncedBlockEvent(this.origin, FunctionalBlocks.DESTROYER, 0, 0);
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

	public static class DestroyQueue implements MultiHarvestContext {

		public final World world;
		public final BlockPos origin;
		public int maxDistanceSquared;
		public float destroySpeed;

		//lazy-initialized by populate().
		public boolean populated;
		public ArrayDeque<PosStateHarvestable> active;
		public Object2ObjectLinkedOpenHashMap<BlockPos, StateHarvestable> inactive;

		public DestroyQueue(World world, BlockPos origin, double distanceRemaining) {
			this.world = world;
			this.origin = origin.toImmutable();
			this.destroySpeed = (float)(distanceRemaining);
			this.maxDistanceSquared = (int)(distanceRemaining * distanceRemaining);
		}

		public DestroyQueue(World world, BlockPos origin, int maxDistanceSquared, float destroySpeed) {
			this.world = world;
			this.origin = origin;
			this.maxDistanceSquared = maxDistanceSquared;
			this.destroySpeed = destroySpeed;
		}

		public void populate() {
			if (this.populated) return;
			this.populated = true;
			this.active = new ArrayDeque<>();
			this.inactive = new Object2ObjectLinkedOpenHashMap<>();
			BlockState state = this.world.getBlockState(this.origin);
			Harvestable harvestable = Harvestable.get(this.world, this.origin, state);
			this.active.addLast(new PosStateHarvestable(this.origin, state, harvestable));
			this.inactive.put(this.origin, new StateHarvestable(state, harvestable));
			for (PosStateHarvestable data; (data = this.active.pollFirst()) != null;) {
				data.harvestable.queueNeighbors(this.world, data.pos, data.state, this);
			}
		}

		@Override
		public void queueNeighbor(BlockPos pos, BlockState state, Harvestable logic) {
			if (
				MathHelper.square(pos.x - this.origin.x) +
				MathHelper.square(pos.y - this.origin.y) +
				MathHelper.square(pos.z - this.origin.z)
				<= this.maxDistanceSquared
			) {
				pos = pos.toImmutable();
				if (this.inactive.putIfAbsent(pos, new StateHarvestable(state, logic)) == null) {
					this.active.addLast(new PosStateHarvestable(pos, state, logic));
				}
			}
		}

		public static record StateHarvestable(BlockState state, Harvestable harvestable) {}
		public static record PosStateHarvestable(BlockPos pos, BlockState state, Harvestable harvestable) {}
	}
}