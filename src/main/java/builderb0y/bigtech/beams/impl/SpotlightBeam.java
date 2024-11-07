package builderb0y.bigtech.beams.impl;

import java.util.UUID;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.ShortIterator;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.light.LightingProvider;

import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.blocks.FunctionalBlocks;

public class SpotlightBeam extends PersistentBeam {

	public static final Vector3fc COLOR = new Vector3f(1.0F, 1.0F, 0.5F);

	public SpotlightBeam(World world, UUID uuid) {
		super(world, uuid);
	}

	@Override
	public int getLightLevel(BeamSegment segment) {
		return segment.visible() && segment.direction() != BeamDirection.CENTER ? 15 : 0;
	}

	@Override
	public BeamType getType() {
		return BeamTypes.SPOTLIGHT;
	}

	@Override
	public Vector3fc getInitialColor() {
		return COLOR;
	}

	@Override
	public void onBlockChanged(ServerWorld world, BlockPos pos, BlockState oldState, BlockState newState) {
		BlockState originState = world.getBlockState(this.origin);
		if (originState.isOf(FunctionalBlocks.SPOTLIGHT)) {
			world.addSyncedBlockEvent(this.origin, FunctionalBlocks.SPOTLIGHT, 0, 0);
		}
		else {
			this.removeFromWorld(world);
		}
	}

	@Override
	public void onAdded(ServerWorld world) {
		super.onAdded(world);
		this.updateLightLevels(world);
	}

	@Override
	public void onRemoved(ServerWorld world) {
		super.onRemoved(world);
		this.updateLightLevels(world);
	}

	public void updateLightLevels(ServerWorld world) {
		LightingProvider lightingProvider = world.getChunkManager().getLightingProvider();
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> sectionIterator = this.seen.long2ObjectEntrySet().fastIterator();
		while (sectionIterator.hasNext()) {
			Long2ObjectMap.Entry<BasicSectionBeamStorage> sectionEntry = sectionIterator.next();
			int chunkY = ChunkSectionPos.unpackY(sectionEntry.getLongKey()) << 4;
			if (world.isOutOfHeightLimit(chunkY)) continue;
			int chunkX = ChunkSectionPos.unpackX(sectionEntry.getLongKey()) << 4;
			int chunkZ = ChunkSectionPos.unpackZ(sectionEntry.getLongKey()) << 4;
			ShortIterator blockIterator = sectionEntry.getValue().keySet().iterator();
			while (blockIterator.hasNext()) {
				short localPos = blockIterator.nextShort();
				int x = chunkX | BasicSectionBeamStorage.unpackX(localPos);
				int y = chunkY | BasicSectionBeamStorage.unpackY(localPos);
				int z = chunkZ | BasicSectionBeamStorage.unpackZ(localPos);
				lightingProvider.checkBlock(mutablePos.set(x, y, z));
			}
		}
	}
}