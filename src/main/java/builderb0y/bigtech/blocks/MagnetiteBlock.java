package builderb0y.bigtech.blocks;

import java.util.function.Predicate;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.collection.PaletteStorage;
import net.minecraft.util.math.MathHelper;
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
	called from {@link MagnetiteAttractableEntity}, via mixin for vanilla entities.
	since this method is called every tick, I want it to run as fast as possible.
	that's why I do all these hacks to get block states in such an unsafe way.
	*/
	public static void attract(Entity entity, double force, double range, boolean markVelocityChanged) {
		World world = entity.getWorld();
		//figure out what area we want to iterate over.
		double
			entityX      = entity.getX(),
			entityY      = entity.getY(),
			entityZ      = entity.getZ(),
			boxMinX      = entityX - range,
			boxMinY      = entityY - range,
			boxMinZ      = entityZ - range,
			boxMaxX      = entityX + range,
			boxMaxY      = entityY + range,
			boxMaxZ      = entityZ + range;
		int
			blockMinX    = MathHelper.floor(boxMinX),
			blockMinY    = MathHelper.floor(boxMinY),
			blockMinZ    = MathHelper.floor(boxMinZ),
			blockMaxX    = MathHelper.floor(boxMaxX),
			blockMaxY    = MathHelper.floor(boxMaxY),
			blockMaxZ    = MathHelper.floor(boxMaxZ),
			sectionMinX  = blockMinX >> 4,
			sectionMinY  = blockMinY >> 4,
			sectionMinZ  = blockMinZ >> 4,
			sectionMaxX  = blockMaxX >> 4,
			sectionMaxY  = blockMaxY >> 4,
			sectionMaxZ  = blockMaxZ >> 4;
		//sum up the net force in this area.
		BlockState search = FunctionalBlocks.MAGNETITE_BLOCK.getDefaultState();
		Predicate<BlockState> predicate = (BlockState state) -> state == search;
		double targetX = 0.0D, targetY = 0.0D, targetZ = 0.0D;
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
					section.lock();
					try {
						if (section.hasAny(predicate)) {
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
											//integrate(integrate(integrate([x, y, z] / sqrt(x^2 + y^2 + z^2), x, iMinX, iMaxX), y, iMinY, iMaxY), z, iMinZ, iMaxZ)
											//proved to be too complicated of an integral for maxima to handle,
											//so I'm doing numerical integration here with d = 0.25.
											double
												iMinX = Math.floor(Math.max(x + 0.125D, boxMinX) * 4.0D) * 0.25D + 0.125D,
												iMinY = Math.floor(Math.max(y + 0.125D, boxMinY) * 4.0D) * 0.25D + 0.125D,
												iMinZ = Math.floor(Math.max(z + 0.125D, boxMinZ) * 4.0D) * 0.25D + 0.125D,
												iMaxX = Math.floor(Math.min(x + 0.875D, boxMaxX) * 4.0D) * 0.25D + 0.125D,
												iMaxY = Math.floor(Math.min(y + 0.875D, boxMaxY) * 4.0D) * 0.25D + 0.125D,
												iMaxZ = Math.floor(Math.min(z + 0.875D, boxMaxZ) * 4.0D) * 0.25D + 0.125D;
											for (double sampleZ = iMinZ; sampleZ <= iMaxZ; sampleZ += 0.25D) {
												double dz = sampleZ - entityZ;
												for (double sampleY = iMinY; sampleY <= iMaxY; sampleY += 0.25D) {
													double dy = sampleY - entityY;
													for (double sampleX = iMinX; sampleX <= iMaxX; sampleX += 0.25D) {
														double dx = sampleX - entityX;
														double squareDistance = dx * dx + dy * dy + dz * dz;
														if (squareDistance > 0.0D) {
															double scalar = force / Math.sqrt(squareDistance);
															targetX += dx * scalar;
															targetY += dy * scalar;
															targetZ += dz * scalar;
														}
													}
												}
											}
										}
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
		if (targetX != 0.0D || targetY != 0.0D || targetZ != 0.0D) {
			targetX *= 1.0D / (4.0D * 4.0D * 4.0D);
			targetY *= 1.0D / (4.0D * 4.0D * 4.0D);
			targetZ *= 1.0D / (4.0D * 4.0D * 4.0D);
			entity.addVelocity(targetX, targetY, targetZ);
			if (markVelocityChanged) {
				//I have no idea which one of these does what, so set them both.
				entity.velocityDirty = true;
				entity.velocityModified = true;
			}
		}
	}
}