package builderb0y.bigtech.beams.storage.section;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReferenceArray;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.BeamMeshBuilder;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.chunk.ClientChunkBeamStorage;
import builderb0y.bigtech.compatibility.SodiumCompatibility;

public class ClientSectionBeamStorage extends CommonSectionBeamStorage {

	static {
		if (FabricLoader.instance.environmentType == EnvType.CLIENT) {
			initClient();
		}
	}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		WorldRenderEvents.AFTER_ENTITIES.register(context -> {
			ClientPlayerEntity player = MinecraftClient.instance.player;
			if (player != null) {
				AtomicReferenceArray<WorldChunk> chunks = ((ClientChunkManager)(player.world.chunkManager)).chunks.chunks;
				Vec3d cameraPos = context.camera().pos;
				boolean renderedAny = false;
				for (int index = 0; index < chunks.length(); index++) {
					WorldChunk chunk = chunks.getPlain(index);
					if (chunk != null) {
						ClientChunkBeamStorage chunkStorage = ChunkBeamStorageHolder.KEY.get(chunk).require().as();
						if (chunkStorage.size() <= 4 || context.frustum().isVisible(chunkStorage.box)) {
							for (ClientSectionBeamStorage sectionStorage : chunkStorage.values().<Iterable<ClientSectionBeamStorage>>as()) {
								if (!sectionStorage.isEmpty && context.frustum().isVisible(sectionStorage.box)) {
									context.matrixStack().push();
									try {
										context.matrixStack().translate(
											sectionStorage.chunk.pos.startX - cameraPos.x,
											(sectionStorage.sectionCoordY << 4) - cameraPos.y,
											sectionStorage.chunk.pos.startZ - cameraPos.z
										);
										MatrixStack.Entry matrices = context.matrixStack().peek();
										VertexConsumer vertices = context.consumers().getBuffer(RenderLayer.solid);
										sectionStorage.getMesh().forEach(quad -> {
											vertices.vertex(matrices.positionMatrix, quad.x(0), quad.y(0), quad.z(0)).color(quad.color(0)).texture(quad.u(0), quad.v(0)).light(quad.lightmap(0)).normal(matrices.normalMatrix, quad.normalX(0), quad.normalY(0), quad.normalZ(0)).next();
											vertices.vertex(matrices.positionMatrix, quad.x(1), quad.y(1), quad.z(1)).color(quad.color(1)).texture(quad.u(1), quad.v(1)).light(quad.lightmap(1)).normal(matrices.normalMatrix, quad.normalX(1), quad.normalY(1), quad.normalZ(1)).next();
											vertices.vertex(matrices.positionMatrix, quad.x(2), quad.y(2), quad.z(2)).color(quad.color(2)).texture(quad.u(2), quad.v(2)).light(quad.lightmap(2)).normal(matrices.normalMatrix, quad.normalX(2), quad.normalY(2), quad.normalZ(2)).next();
											vertices.vertex(matrices.positionMatrix, quad.x(3), quad.y(3), quad.z(3)).color(quad.color(3)).texture(quad.u(3), quad.v(3)).light(quad.lightmap(3)).normal(matrices.normalMatrix, quad.normalX(3), quad.normalY(3), quad.normalZ(3)).next();
										});
										renderedAny = true;
									}
									finally {
										context.matrixStack().pop();
									}
								}
							}
						}
					}
				}
				if (renderedAny) {
					SodiumCompatibility.markSpriteActive(
						MinecraftClient
						.instance
						.bakedModelManager
						.getAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)
						.getSprite(BigTechMod.modID("block/beam"))
					);
				}
			}
		});
	}

	public final Box box;
	@Environment(EnvType.CLIENT)
	public Mesh mesh;

	public ClientSectionBeamStorage(WorldChunk chunk, int sectionCoordY) {
		super(chunk, sectionCoordY);
		this.box = new Box(
			chunk.pos.startX - 0.5D,
			(sectionCoordY << 4) - 0.5D,
			chunk.pos.startZ - 0.5D,
			chunk.pos.startX + 16.5D,
			(sectionCoordY << 4) + 16.5D,
			chunk.pos.startZ + 16.5D
		);
	}

	@Environment(EnvType.CLIENT)
	public Mesh getMesh() {
		Mesh mesh = this.mesh;
		if (mesh == null) mesh = this.mesh = BeamMeshBuilder.build(this);
		return mesh;
	}

	@Override
	public LinkedList<BeamSegment> getSegments(int index) {
		this.invalidate();
		return super.getSegments(index);
	}

	public void invalidate() {
		this.invalidateClient();
	}

	@Environment(EnvType.CLIENT)
	public void invalidateClient() {
		this.mesh = null;
	}
}