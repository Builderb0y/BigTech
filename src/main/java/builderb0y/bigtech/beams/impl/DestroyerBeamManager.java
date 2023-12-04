package builderb0y.bigtech.beams.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.models.IntRng;

public class DestroyerBeamManager {

	public static final WeakHashMap<World, DestroyerBeamManager> WORLDS = new WeakHashMap<>(3);

	public final World world;
	public final Map<BlockPos, Info> breakingPositions = new HashMap<>(16);

	public DestroyerBeamManager(World world) {
		this.world = world;
	}

	public static DestroyerBeamManager forWorld(World world) {
		return WORLDS.computeIfAbsent(world, DestroyerBeamManager::new);
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
}