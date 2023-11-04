package builderb0y.bigtech.beams.storage.section;

import java.util.LinkedList;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.BeamSegment;
import builderb0y.bigtech.mixins.WorldRenderer_FrustumGetter;

public class ClientSectionBeamStorage extends CommonSectionBeamStorage {

	public final Box box;

	public ClientSectionBeamStorage(WorldChunk chunk, int sectionCoordY) {
		super(chunk, sectionCoordY);
		this.box = new Box(
			chunk.pos.startX,
			sectionCoordY << 4,
			chunk.pos.startZ,
			chunk.pos.startX + 16,
			(sectionCoordY << 4) + 16,
			chunk.pos.startZ + 16
		);
	}

	@Override
	public void tick() {
		this.tickClient();
	}

	@Environment(EnvType.CLIENT)
	public void tickClient() {
		//todo: migrate to actual geometry instead of spawning particles every tick.
		if (
			!this.isEmpty
			&&
			this.box.center.squaredDistanceTo(
				MinecraftClient.getInstance().gameRenderer.camera.pos
			)
			< 48.0D * 48.0D
			&&
			MinecraftClient
			.getInstance()
			.worldRenderer
			.<WorldRenderer_FrustumGetter>as()
			.bigtech_getFrustum()
			.isVisible(this.box)
		) {
			double startX = this.chunk.pos.startX + 0.5D;
			double startY = (this.sectionCoordY << 4) + 0.5D;
			double startZ = this.chunk.pos.startZ + 0.5D;
			Random random = this.chunk.world.random;
			ObjectIterator<Short2ObjectMap.Entry<LinkedList<BeamSegment>>> iterator = this.short2ObjectEntrySet().fastIterator();
			while (iterator.hasNext()) {
				Short2ObjectMap.Entry<LinkedList<BeamSegment>> entry = iterator.next();
				double centerX = startX + ChunkSectionPos.unpackLocalX(entry.shortKey);
				double centerY = startY + ChunkSectionPos.unpackLocalY(entry.shortKey);
				double centerZ = startZ + ChunkSectionPos.unpackLocalZ(entry.shortKey);
				for (BeamSegment segment : entry.value) {
					double fraction = random.nextDouble();
					double dx       = segment.direction.x;
					double dy       = segment.direction.y;
					double dz       = segment.direction.z;
					double x        = centerX + dx * fraction;
					double y        = centerY + dy * fraction;
					double z        = centerZ + dz * fraction;
					this.chunk.world.addParticle(new DustParticleEffect(segment.color, 1.0F), x, y, z, dx, dy, dz);
				}
			}
		}
	}
}