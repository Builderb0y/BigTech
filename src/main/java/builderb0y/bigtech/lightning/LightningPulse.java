package builderb0y.bigtech.lightning;

import java.util.List;
import java.util.Map;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.beams.impl.LightningBeam;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.mixins.CreeperEntity_ChargedAccessor;
import builderb0y.bigtech.util.EquipmentSlots;

public class LightningPulse {

	public final ServerWorld world;
	public final LinkedBlockPos originPos;
	public final int totalEnergy;

	public final Set<LinkedBlockPos> explored;
	public final List<LinkedBlockPos> spreadQueue;
	public final Set<LinkedBlockPos> sinks;

	public final Map<LinkedBlockPos, LightningBeam> lightningBeams;

	public LightningPulse(
		ServerWorld world,
		BlockPos originPos,
		int totalEnergy,
		int remainingSpreadEvents
	) {
		this.world          = world;
		this.originPos      = new LinkedBlockPos(originPos, remainingSpreadEvents, null);
		this.totalEnergy    = totalEnergy;
		this.explored       = new ObjectOpenHashSet<>(remainingSpreadEvents);
		this.spreadQueue    = new ObjectArrayList<>(remainingSpreadEvents << 1);
		this.sinks          = new ObjectOpenHashSet<>(4);
		this.lightningBeams = new Object2ObjectOpenHashMap<>(2);
		this.addNode(this.originPos);
	}

	public LightningPulse(
		ServerWorld world,
		BlockPos blockPos,
		BlockState state,
		LightningPulseInteractor interactor,
		int totalEnergy,
		int remainingSpreadEvents
	) {
		this.world          = world;
		this.originPos      = new LinkedBlockPos(blockPos, remainingSpreadEvents, null);
		this.totalEnergy    = totalEnergy;
		this.explored       = new ObjectOpenHashSet<>(remainingSpreadEvents);
		this.spreadQueue    = new ObjectArrayList<>(remainingSpreadEvents << 1);
		this.sinks          = new ObjectOpenHashSet<>(4);
		this.lightningBeams = new Object2ObjectOpenHashMap<>(2);
		interactor.spreadIn(world, this.originPos, state, this);
	}

	public static void shockEntity(ServerWorld serverWorld, Entity entity, float amount, DamageSource source) {
		float multiplier = 1.0F;
		if (entity instanceof LivingEntity living) {
			for (EquipmentSlot slot : EquipmentSlots.ALL_ARMOR) {
				ItemStack stack = living.getEquippedStack(slot);
				if (stack.isIn(BigTechItemTags.SHOCK_PROTECTIVE_ARMOR)) {
					multiplier -= 0.25F;
				}
			}
		}
		amount *= multiplier;
		if (
			amount > 0.0F &&
			entity.damage(serverWorld, source, amount) &&
			entity instanceof CreeperEntity creeper
		) {
			creeper.getDataTracker().set(CreeperEntity_ChargedAccessor.getCharged(), Boolean.TRUE);
		}
	}

	public void run() {
		this.spread();
		this.interact();
	}

	public void spread() {
		while (!this.spreadQueue.isEmpty()) {
			int index = this.world.random.nextInt(this.spreadQueue.size());
			LinkedBlockPos from = (
				index == this.spreadQueue.size() - 1
				? this.spreadQueue.remove(index)
				: this.spreadQueue.set(index, this.spreadQueue.remove(this.spreadQueue.size() - 1))
			);
			BlockState fromState = this.world.getBlockState(from);
			LightningPulseInteractor.get(this.world, from, fromState).spreadOut(this.world, from, fromState, this);
		}
	}

	public void interact() {
		if (this.sinks.isEmpty()) {
			this.interactWithoutSinks();
		}
		else {
			this.interactWithSinks();
		}
	}

	public void interactWithoutSinks() {
		for (LinkedBlockPos pos : this.explored) {
			BlockState state = this.world.getBlockState(pos);
			LightningPulseInteractor.get(this.world, pos, state).onPulse(this.world, pos, state, this);
		}
	}

	public void interactWithSinks() {
		Set<BlockPos> seen = new ObjectOpenHashSet<>(this.explored.size());
		for (LinkedBlockPos pos : this.sinks) {
			do {
				if (seen.add(pos)) {
					BlockState state = this.world.getBlockState(pos);
					LightningPulseInteractor.get(this.world, pos, state).onPulse(this.world, pos, state, this);
				}
				pos = pos.previous;
			}
			while (pos != null);
		}
	}

	public boolean hasNode(LinkedBlockPos pos) {
		return this.explored.contains(pos);
	}

	public void addNode(LinkedBlockPos pos) {
		if (this.explored.add(pos)) {
			this.spreadQueue.add(pos);
		}
	}

	public void addSink(LinkedBlockPos pos) {
		this.sinks.add(pos);
	}

	@SuppressWarnings("UseOfDivisionOperator")
	public int getDistributedEnergy() {
		return this.totalEnergy / Math.max(1, (this.sinks.isEmpty() ? this.explored : this.sinks).size());
	}

	public static class LinkedBlockPos extends BlockPos {

		public final LinkedBlockPos previous;
		public final float distanceRemaining;

		public LinkedBlockPos(int x, int y, int z, float distanceRemaining, LinkedBlockPos previous) {
			super(x, y, z);
			this.previous = previous;
			this.distanceRemaining = distanceRemaining;
		}

		public LinkedBlockPos(Vec3i pos, float distanceRemaining, LinkedBlockPos previous) {
			super(pos);
			this.previous = previous;
			this.distanceRemaining = distanceRemaining;
		}

		@Override
		@Deprecated //you probably want a LinkedBlockPos, which you won't get from this overload.
		public BlockPos offset(Direction direction) {
			return super.offset(direction);
		}

		@Override
		@Deprecated //you probably want a LinkedBlockPos, which you won't get from this overload.
		public BlockPos offset(Direction direction, int i) {
			return super.offset(direction, i);
		}

		public LinkedBlockPos offset(Direction direction, float resistance) {
			return new LinkedBlockPos(
				this.getX() + direction.getOffsetX(),
				this.getY() + direction.getOffsetY(),
				this.getZ() + direction.getOffsetZ(),
				this.distanceRemaining - resistance,
				this
			);
		}

		public LinkedBlockPos offset(Direction direction, int i, float resistance) {
			return new LinkedBlockPos(
				this.getX() + direction.getOffsetX() * i,
				this.getY() + direction.getOffsetY() * i,
				this.getZ() + direction.getOffsetZ() * i,
				this.distanceRemaining - resistance,
				this
			);
		}

		public LinkedBlockPos add(int x, int y, int z, float resistance) {
			return new LinkedBlockPos(
				this.getX() + x,
				this.getY() + y,
				this.getZ() + z,
				this.distanceRemaining - resistance,
				this
			);
		}

		public LinkedBlockPos add(Vec3i that, float resistance) {
			return new LinkedBlockPos(
				this.getX() + that.getX(),
				this.getY() + that.getY(),
				this.getZ() + that.getZ(),
				this.distanceRemaining - resistance,
				this
			);
		}
	}
}