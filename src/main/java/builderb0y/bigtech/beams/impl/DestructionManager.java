package builderb0y.bigtech.beams.impl;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.fabricmc.fabric.api.entity.FakePlayer;

import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import builderb0y.bigtech.api.Harvestable;
import builderb0y.bigtech.api.Harvestable.MultiHarvestContext;
import builderb0y.bigtech.models.IntRng;
import builderb0y.bigtech.util.WorldHelper;

public class DestructionManager {

	public static final WeakHashMap<ServerWorld, DestructionManager> WORLDS = new WeakHashMap<>(3);
	public static final WeakReference<FakePlayer> EMPTY_FAKE_PLAYER = new WeakReference<>(null);

	public final ServerWorld world;
	public final Map<BlockPos, Info> breakingPositions = new HashMap<>(16);
	public WeakReference<FakePlayer> fakePlayer = EMPTY_FAKE_PLAYER;

	public DestructionManager(ServerWorld world) {
		this.world = world;
	}

	public FakePlayer getFakePlayer() {
		WeakReference<FakePlayer> holder = this.fakePlayer;
		FakePlayer player = holder.get();
		if (player == null) {
			this.fakePlayer = new WeakReference<>(player = FakePlayer.get(this.world.as()));
		}
		return player;
	}

	public static DestructionManager forWorld(ServerWorld world) {
		return WORLDS.computeIfAbsent(world, DestructionManager::new);
	}

	public Info getInfo(BlockPos pos) {
		return this.breakingPositions.computeIfAbsent(pos.toImmutable(), Info::new);
	}

	public static enum IncreaseDamageResult {
		DIDNT_DESTROY,
		DESTROYED,
		DESTROYED_AND_DAMAGED_TOOL;
	}

	public IncreaseDamageResult increaseDamage(BlockPos pos, BlockState actualState, float baseSpeed, ItemStack tool) {
		if (!actualState.isToolRequired() || tool.isSuitableFor(actualState)) {
			FakePlayer player = this.getFakePlayer();
			player.setOnGround(true);
			ItemStack oldHeldItem = player.getStackInHand(Hand.MAIN_HAND);
			player.setStackInHand(Hand.MAIN_HAND, tool);
			float delta;
			try {
				delta = actualState.calcBlockBreakingDelta(player, this.world, pos) * baseSpeed;
			}
			finally {
				player.setStackInHand(Hand.MAIN_HAND, oldHeldItem);
			}
			if (delta > 0.0F) {
				DestructionManager.Info info = this.getInfo(pos);
				info.progress += delta;
				this.world.setBlockBreakingInfo(info.breakerID, pos, (int)(info.progress * 10.0F));
				if (info.progress >= 1.0F) {
					this.breakingPositions.remove(pos);
					WorldHelper.breakBlockWithTool(this.world.as(), pos, actualState, null, tool);
					if (actualState.getHardness(this.world, pos) != 0.0F) {
						ToolComponent toolComponent = tool.get(DataComponentTypes.TOOL);
						if (toolComponent != null && toolComponent.damagePerBlock() > 0) {
							tool.damage(toolComponent.damagePerBlock(), this.world.as(), null, (Item item) -> {});
							return IncreaseDamageResult.DESTROYED_AND_DAMAGED_TOOL;
						}
					}
					return IncreaseDamageResult.DESTROYED;
				}
			}
			else { //made no progress; treat the block as unbreakable.
				this.resetProgress(pos);
			}
		}
		else { //can't break with current tool.
			this.resetProgress(pos);
		}
		return IncreaseDamageResult.DIDNT_DESTROY;
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
			this.breakerID = IntRng.permute(0, pos.getX(), pos.getY(), pos.getZ());
		}
	}

	public static class DestroyQueue implements MultiHarvestContext {

		public final ServerWorld world;
		public final BlockPos origin;
		public int maxDistanceSquared;
		public float destroySpeed;

		//lazy-initialized by populate().
		public boolean populated;
		public ArrayDeque<PosStateHarvestable> active;
		public Object2ObjectLinkedOpenHashMap<BlockPos, StateHarvestable> inactive;

		public DestroyQueue(ServerWorld world, BlockPos origin, double distanceRemaining) {
			this.world = world;
			this.origin = origin.toImmutable();
			this.destroySpeed = ((float)(distanceRemaining)) * 0.0625F;
			this.maxDistanceSquared = (int)(distanceRemaining * distanceRemaining);
		}

		public DestroyQueue(ServerWorld world, BlockPos origin, int maxDistanceSquared, float destroySpeed) {
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
			if (actualState.getBlock() == expectedState.getBlock()) {
				if (data.harvestable.canHarvest(this.world, pos, actualState)) {
					IncreaseDamageResult result = manager.increaseDamage(pos, actualState, this.destroySpeed, tool);
					if (result != IncreaseDamageResult.DIDNT_DESTROY) {
						this.inactive.remove(pos);
					}
					return result == IncreaseDamageResult.DESTROYED_AND_DAMAGED_TOOL;
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
				MathHelper.square(pos.getX() - this.origin.getX()) +
				MathHelper.square(pos.getY() - this.origin.getY()) +
				MathHelper.square(pos.getZ() - this.origin.getZ())
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