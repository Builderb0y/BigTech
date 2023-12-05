package builderb0y.bigtech.beams.impl;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import builderb0y.bigtech.api.Harvestable;
import builderb0y.bigtech.api.Harvestable.MultiHarvestContext;
import builderb0y.bigtech.models.IntRng;
import builderb0y.bigtech.util.WorldHelper;

public class DestructionManager {

	public static final WeakHashMap<World, DestructionManager> WORLDS = new WeakHashMap<>(3);

	public final World world;
	public final Map<BlockPos, Info> breakingPositions = new HashMap<>(16);

	public DestructionManager(World world) {
		this.world = world;
	}

	public static DestructionManager forWorld(World world) {
		return WORLDS.computeIfAbsent(world, DestructionManager::new);
	}

	public Info getInfo(BlockPos pos) {
		return this.breakingPositions.computeIfAbsent(pos.toImmutable(), Info::new);
	}

	public void resetProgress(BlockPos pos) {
		Info info = this.breakingPositions.remove(pos);
		if (info != null) this.world.setBlockBreakingInfo(info.breakerID, pos, -1);
	}

	public static class Info {

		public int breakerID;
		public float progress;

		public Info(int breakerID) {
			this.breakerID = breakerID;
		}

		public Info(BlockPos pos) {
			this.breakerID = IntRng.permute(0, pos.x, pos.y, pos.z);
		}
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

		public boolean tick(ItemStack tool) {
			DestructionManager manager = DestructionManager.forWorld(this.world);
			BlockPos pos = this.inactive.lastKey();
			StateHarvestable data = this.inactive.get(pos);
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
							speed *= this.destroySpeed * 0.0625F;

							DestructionManager.Info info = manager.getInfo(pos);
							info.progress += speed;
							this.world.setBlockBreakingInfo(info.breakerID, pos, (int)(info.progress * 10.0F));
							if (info.progress >= 1.0F) {
								manager.breakingPositions.remove(pos);
								WorldHelper.destroyBlockWithTool((ServerWorld)(this.world), pos, actualState, tool);
								this.inactive.remove(pos);
								if (tool.damage(1, this.world.random, null)) {
									tool.decrement(1);
									tool.setDamage(0);
								}
								return true;
							}
						}
						else if (hardness == 0.0F) {
							WorldHelper.destroyBlockWithTool((ServerWorld)(this.world), pos, actualState, tool);
							this.inactive.remove(pos);
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
				this.inactive.remove(pos);
			}
			return false;
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