package builderb0y.bigtech.beams.storage.section;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReferenceArray;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadView;
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
import builderb0y.bigtech.entities.EntityRenderHelper;

public class ClientSectionBeamStorage extends CommonSectionBeamStorage {

	static {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			initClient();
		}
	}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		WorldRenderEvents.AFTER_ENTITIES.register((WorldRenderContext context) -> {
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null) {
				AtomicReferenceArray<WorldChunk> chunks = player.getWorld().getChunkManager().<ClientChunkManager>as().chunks.chunks;
				Vec3d cameraPos = context.camera().getPos();
				VertexConsumer vertices = context.consumers().getBuffer(RenderLayer.getSolid());
				EntityRenderHelper helper = (
					new EntityRenderHelper()
					.vertexConsumer(vertices)
					.lightmap(0xF000F0)
				);
				MatrixStack matrixStack = context.matrixStack();
				boolean renderedAny = false;
				for (int index = 0; index < chunks.length(); index++) {
					WorldChunk chunk = chunks.getPlain(index);
					if (chunk != null) {
						ClientChunkBeamStorage chunkStorage = ChunkBeamStorageHolder.KEY.get(chunk).require().as();
						if (chunkStorage.size() <= 4 || context.frustum().isVisible(chunkStorage.box)) {
							for (ClientSectionBeamStorage sectionStorage : chunkStorage.values().<Iterable<ClientSectionBeamStorage>>as()) {
								if (!sectionStorage.isEmpty() && context.frustum().isVisible(sectionStorage.box)) {
									matrixStack.push();
									try {
										matrixStack.translate(
											sectionStorage.chunk.getPos().getStartX() - cameraPos.x,
											(sectionStorage.sectionCoordY << 4) - cameraPos.y,
											sectionStorage.chunk.getPos().getStartZ() - cameraPos.z
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
	}

	public final Box box;
	@Environment(EnvType.CLIENT)
	public Mesh mesh;

	public ClientSectionBeamStorage(WorldChunk chunk, int sectionCoordY) {
		super(chunk, sectionCoordY);
		this.box = new Box(
			chunk.getPos().getStartX() - 0.5D,
			(sectionCoordY << 4) - 0.5D,
			chunk.getPos().getStartZ() - 0.5D,
			chunk.getPos().getStartX() + 16.5D,
			(sectionCoordY << 4) + 16.5D,
			chunk.getPos().getStartZ() + 16.5D
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