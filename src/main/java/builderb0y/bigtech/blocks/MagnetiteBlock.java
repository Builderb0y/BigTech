package builderb0y.bigtech.blocks;

import java.util.function.Predicate;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.collection.PaletteStorage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.Palette;
import net.minecraft.world.chunk.PalettedContainer;

import builderb0y.bigtech.api.MagnetiteAttractableEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class MagnetiteBlock extends Block {

	public static final MapCodec<MagnetiteBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public MagnetiteBlock(Settings settings) {
		super(settings);
	}

	public static void attract(
		Entity entity,
		double x,
		double y,
		double z,
		double force,
		double range,
		boolean markVelocityChanged
	) {
		double
			dx = x - entity.getX(),
			dy = y - entity.getY(),
			dz = z - entity.getZ();
		double squareDistance = dx * dx + dy * dy + dz * dz;
		if (squareDistance < range * range) {
			double scalar = force / Math.sqrt(squareDistance);
			entity.addVelocity(dx * scalar, dy * scalar, dz * scalar);
			if (markVelocityChanged) {
				//I have no idea which one of these does what, so set them both.
				entity.velocityDirty = true;
				entity.velocityModified = true;
			}
		}
	}

	/**
	called from mixin {@link MagnetiteAttractableEntity}, via mixin for vanilla entities.
	since this method is called every tick, I want it to run as fast as possible.
	that's why I do all these hacks to get block states in such an unsafe way.
	*/
	public static void attract(Entity entity, double force, double range, boolean markVelocityChanged) {
		World    world  = entity.getWorld();
		BlockPos center = entity.getBlockPos();
		int blockRange  = (int)(range);
		int
			blockMinX   = center.getX() - blockRange,
			blockMinY   = center.getY() - blockRange,
			blockMinZ   = center.getZ() - blockRange,
			blockMaxX   = center.getX() + blockRange,
			blockMaxY   = center.getY() + blockRange,
			blockMaxZ   = center.getZ() + blockRange,
			sectionMinX = blockMinX >> 4,
			sectionMinY = blockMinY >> 4,
			sectionMinZ = blockMinZ >> 4,
			sectionMaxX = blockMaxX >> 4,
			sectionMaxY = blockMaxY >> 4,
			sectionMaxZ = blockMaxZ >> 4;
		BlockState search = FunctionalBlocks.MAGNETITE_BLOCK.getDefaultState();
		Predicate<BlockState> predicate = (BlockState state) -> state == search;
		for (int sectionZ = sectionMinZ; sectionZ <= sectionMaxZ; sectionZ++) {
			int intersectionMinZ = Math.max(blockMinZ, sectionZ << 4);
			int intersectionMaxZ = Math.min(blockMaxZ, (sectionZ << 4) | 15);
			for (int sectionX = sectionMinX; sectionX <= sectionMaxX; sectionX++) {
				int intersectionMinX = Math.max(blockMinX, sectionX << 4);
				int intersectionMaxX = Math.min(blockMaxX, (sectionX << 4) | 15);
				Chunk chunk = world.getChunk(sectionX, sectionZ);
				for (int sectionY = sectionMinY; sectionY <= sectionMaxY; sectionY++) {
					int intersectionMinY = Math.max(blockMinY, sectionY << 4);
					int intersectionMaxY = Math.min(blockMaxY, (sectionY << 4) | 15);
					PalettedContainer<BlockState> section = chunk.getSection(chunk.sectionCoordToIndex(sectionY)).getBlockStateContainer();
					if (section.hasAny(predicate)) {
						section.lock();
						try {
							Palette<BlockState> palette = section.data.palette();
							int searchIndex = palette.index(search);
							PaletteStorage storage = section.data.storage();
							for (int y = intersectionMinY; y <= intersectionMaxY; y++) {
								int yIndex = (y & 15) << 8;
								for (int z = intersectionMinZ; z <= intersectionMaxZ; z++) {
									int zIndex = yIndex | ((z & 15) << 4);
									for (int x = intersectionMinX; x <= intersectionMaxX; x++) {
										int xIndex = zIndex | (x & 15);
										if (storage.get(xIndex) == searchIndex) {
											attract(entity, x + 0.5D, y + 0.5D, z + 0.5D, force, range, markVelocityChanged);
										}
									}
								}
							}
						}
						finally {
							section.unlock();
						}
					}
				}
			}
		}
	}
}