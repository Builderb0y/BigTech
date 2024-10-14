package builderb0y.bigtech.beams.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadView;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.BeamMeshBuilder.AdjacentSegmentLoader;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.chunk.ClientChunkBeamStorage;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.beams.storage.section.ClientSectionBeamStorage;
import builderb0y.bigtech.compatibility.SodiumCompatibility;
import builderb0y.bigtech.entities.EntityRenderHelper;

@Environment(EnvType.CLIENT)
public class BeamRendering {

	public static final List<PulseRenderState> PULSES = new ArrayList<>(16);

	public static void initClient() {
		WorldRenderEvents.AFTER_ENTITIES.register((WorldRenderContext context) -> {
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null) {
				AtomicReferenceArray<WorldChunk> chunks = player.getWorld().getChunkManager().<ClientChunkManager>as().chunks.chunks;
				Vec3d cameraPos = context.camera().getPos();
				EntityRenderHelper helper = (
					new EntityRenderHelper()
					.vertexConsumer(context.consumers().getBuffer(RenderLayer.getSolid()))
					.lightmap(0xF000F0)
				);
				MatrixStack matrixStack = context.matrixStack();
				boolean renderedAny = false;
				for (int index = 0, size = chunks.length(); index < size; index++) {
					WorldChunk chunk = chunks.getPlain(index);
					if (chunk != null) {
						ClientChunkBeamStorage chunkStorage = ChunkBeamStorageHolder.KEY.get(chunk).require().as();
						if (chunkStorage.size() <= 4 || context.frustum().isVisible(chunkStorage.box)) {
							for (ClientSectionBeamStorage sectionStorage : chunkStorage.values().<Iterable<ClientSectionBeamStorage>>as()) {
								if (!sectionStorage.isEmpty() && context.frustum().isVisible(sectionStorage.box)) {
									matrixStack.push();
									try {
										matrixStack.translate(
											(sectionStorage.sectionX << 4) - cameraPos.x,
											(sectionStorage.sectionY << 4) - cameraPos.y,
											(sectionStorage.sectionZ << 4) - cameraPos.z
										);
										helper.transform(matrixStack.peek());
										sectionStorage.getMesh().forEach((QuadView quad) -> {
											helper.quad(
												quad.x(0), quad.x(1), quad.x(2), quad.x(3),
												quad.y(0), quad.y(1), quad.y(2), quad.y(3),
												quad.z(0), quad.z(1), quad.z(2), quad.z(3),
												quad.u(0), quad.u(3), quad.v(0), quad.v(1),
												quad.color(0),
												quad.normalX(0), quad.normalY(0), quad.normalZ(0)
											);
										});
										renderedAny = true;
									}
									finally {
										matrixStack.pop();
									}
								}
							}
						}
					}
				}
				helper.vertexConsumer(context.consumers().getBuffer(RenderLayer.getTranslucent()));
				for (int index = 0, size = PULSES.size(); index < size; index++) {
					PulseRenderState pulse = PULSES.get(index);
					if (context.frustum().isVisible(pulse.box)) {
						matrixStack.push();
						try {
							matrixStack.translate(
								(pulse.sectionX << 4) - cameraPos.x,
								(pulse.sectionY << 4) - cameraPos.y,
								(pulse.sectionZ << 4) - cameraPos.z
							);
							helper.transform(matrixStack.peek());
							int alpha = (int)((pulse.timeRemaining - context.tickCounter().getTickDelta(false)) * (255.0F / PulseRenderState.MAX_TIME)) << 24;
							pulse.getMesh().forEach((QuadView quad) -> {
								helper.quad(
									quad.x(0), quad.x(1), quad.x(2), quad.x(3),
									quad.y(0), quad.y(1), quad.y(2), quad.y(3),
									quad.z(0), quad.z(1), quad.z(2), quad.z(3),
									quad.u(0), quad.u(3), quad.v(0), quad.v(1),
									(quad.color(0) & 0x00FFFFFF) | alpha,
									quad.normalX(0), quad.normalY(0), quad.normalZ(0)
								);
							});
						}
						finally {
							matrixStack.pop();
						}
					}
				}
				if (renderedAny) {
					SodiumCompatibility.markSpriteActive(
						MinecraftClient
						.getInstance()
						.getBakedModelManager()
						.getAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
						.getSprite(BigTechMod.modID("block/beam"))
					);
				}
			}
		});
		ClientTickEvents.END_CLIENT_TICK.register((MinecraftClient client) -> {
			if (!PULSES.isEmpty()) {
				PULSES.removeIf((PulseRenderState pulse) -> --pulse.timeRemaining <= 0);
			}
		});
	}

	public static void addPulse(PulseBeam beam) {
		for (
			ObjectIterator<Long2ObjectMap.Entry<BasicSectionBeamStorage>> iterator = beam.seen.long2ObjectEntrySet().fastIterator();
			iterator.hasNext();
		) {
			Long2ObjectMap.Entry<BasicSectionBeamStorage> entry = iterator.next();
			PULSES.add(
				new PulseRenderState(
					ChunkSectionPos.unpackX(entry.getLongKey()),
					ChunkSectionPos.unpackY(entry.getLongKey()),
					ChunkSectionPos.unpackZ(entry.getLongKey()),
					beam
				)
			);
		}
	}

	public static class PulseRenderState {

		public static final byte MAX_TIME = 10;

		public int sectionX, sectionY, sectionZ;
		public Box box;
		public PulseBeam beam;
		public Mesh mesh;
		public byte timeRemaining = MAX_TIME;

		public PulseRenderState(int sectionX, int sectionY, int sectionZ, PulseBeam beam) {
			this.sectionX = sectionX;
			this.sectionY = sectionY;
			this.sectionZ = sectionZ;
			this.beam = beam;
			this.box = new Box(
				sectionX << 4,
				sectionY << 4,
				sectionZ << 4,
				(sectionX << 4) + 16,
				(sectionY << 4) + 16,
				(sectionZ << 4) + 16
			);
		}

		public Mesh getMesh() {
			if (this.mesh == null) {
				this.mesh = BeamMeshBuilder.build(
					this.beam.seen.getSegments(this.sectionX, this.sectionY, this.sectionZ),
					AdjacentSegmentLoader.pulse(this.beam)
				);
			}
			return this.mesh;
		}
	}
}