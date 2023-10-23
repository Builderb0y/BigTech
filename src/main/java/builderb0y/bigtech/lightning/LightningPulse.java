package builderb0y.bigtech.lightning;

import java.util.List;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class LightningPulse {

	public final World world;
	public final LinkedBlockPos originPos;
	public final int totalEnergy;
	public int remainingSpreadEvents;

	public final Set<LinkedBlockPos> explored;
	public final List<LinkedBlockPos> spreadQueue;
	public final Set<LinkedBlockPos> sinks;

	public LightningPulse(
		World world,
		BlockPos originPos,
		int totalEnergy,
		int remainingSpreadEvents
	) {
		this.world                 = world;
		this.originPos             = new LinkedBlockPos(originPos, null);
		this.totalEnergy           = totalEnergy;
		this.remainingSpreadEvents = remainingSpreadEvents;
		this.explored              = new ObjectOpenHashSet<>(remainingSpreadEvents);
		this.spreadQueue           = new ObjectArrayList<>(remainingSpreadEvents << 1);
		this.sinks                 = new ObjectOpenHashSet<>(4);
		this.addNode(this.originPos);
	}

	public void run() {
		this.spread();
		this.interact();
	}

	public void spread() {
		while (this.remainingSpreadEvents > 0 && !this.spreadQueue.isEmpty) {
			int index = this.world.random.nextInt(this.spreadQueue.size());
			LinkedBlockPos from = (
				index == this.spreadQueue.size() - 1
				? this.spreadQueue.remove(index)
				: this.spreadQueue.set(index, this.spreadQueue.remove(this.spreadQueue.size() - 1))
			);
			BlockState fromState = this.world.getBlockState(from);
			LightningPulseInteractors.forBlock(this.world, from, fromState).spreadOut(this.world, from, fromState, this);
			this.remainingSpreadEvents--;
		}
	}

	public void interact() {
		if (this.sinks.isEmpty) {
			this.interactWithoutSinks();
		}
		else {
			this.interactWithSinks();
		}
	}

	public void interactWithoutSinks() {
		for (LinkedBlockPos pos : this.explored) {
			BlockState state = this.world.getBlockState(pos);
			LightningPulseInteractors.forBlock(this.world, pos, state).onPulse(this.world, pos, state, this);
		}
	}

	public void interactWithSinks() {
		Set<BlockPos> seen = new ObjectOpenHashSet<>(this.explored.size());
		for (LinkedBlockPos pos : this.sinks) {
			do {
				if (seen.add(pos)) {
					BlockState state = this.world.getBlockState(pos);
					LightningPulseInteractors.forBlock(this.world, pos, state).onPulse(this.world, pos, state, this);
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
		return this.totalEnergy / Math.max(1, (this.sinks.isEmpty ? this.explored : this.sinks).size());
	}

	public static class LinkedBlockPos extends BlockPos {

		public final LinkedBlockPos previous;

		public LinkedBlockPos(int x, int y, int z, LinkedBlockPos previous) {
			super(x, y, z);
			this.previous = previous;
		}

		public LinkedBlockPos(Vec3i pos, LinkedBlockPos previous) {
			super(pos);
			this.previous = previous;
		}

		@Override
		public LinkedBlockPos offset(Direction direction, int i) {
			return new LinkedBlockPos(
				this.x + direction.offsetX * i,
				this.y + direction.offsetY * i,
				this.z + direction.offsetZ * i,
				this
			);
		}

		@Override
		public LinkedBlockPos offset(Direction direction) {
			return new LinkedBlockPos(
				this.x + direction.offsetX,
				this.y + direction.offsetY,
				this.z + direction.offsetZ,
				this
			);
		}

		@Override
		public LinkedBlockPos add(int x, int y, int z) {
			return new LinkedBlockPos(
				this.x + x,
				this.y + y,
				this.z + z,
				this
			);
		}

		@Override
		public LinkedBlockPos add(Vec3i that) {
			return new LinkedBlockPos(
				this.x + that.x,
				this.y + that.y,
				this.z + that.z,
				this
			);
		}
	}
}