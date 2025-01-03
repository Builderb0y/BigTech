package builderb0y.bigtech.blockEntities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import builderb0y.bigtech.BigTechMod;

@Environment(EnvType.CLIENT)
public class CrucibleBlockEntityRenderer implements BlockEntityRenderer<CrucibleBlockEntity> {

	public static final Identifier
		SLAG = BigTechMod.modID("block/slag"),
		LAVA = Identifier.ofVanilla("block/lava_still");

	public CrucibleBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

	@Override
	public void render(CrucibleBlockEntity crucible, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		int itemCount = crucible.countItems();
		if (itemCount != 0) {
			float heightFraction = ((float)(itemCount)) / ((float)(crucible.size()));
			float centerY = MathHelper.lerp(heightFraction, 3.0F / 16.0F, 14.0F / 16.0F);
			SpriteAtlasTexture atlas = MinecraftClient.getInstance().getBakedModelManager().getAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
			vertices(
				matrices,
				vertexConsumers.getBuffer(RenderLayer.getSolid()),
				atlas.getSprite(SLAG),
				centerY - 0.0001F,
				1.0F,
				light,
				0.0F
			);
			if (crucible.progress != null) {
				float alpha = ((float)(crucible.progress.heat)) / ((float)(crucible.progress.maxHeat));
				vertices(
					matrices,
					vertexConsumers.getBuffer(RenderLayer.getTranslucent()),
					atlas.getSprite(LAVA),
					centerY + 0.0001F,
					alpha,
					light,
					alpha
				);
			}
		}
	}

	public static void vertices(MatrixStack matrices, VertexConsumer consumer, Sprite sprite, float y, float alpha, int light, float addedLight) {
		int alphaAsInt = Math.min((int)(alpha * 256.0F), 255);
		if (addedLight > 0.0F) {
			light = (light & 0xFF00) | MathHelper.lerp(addedLight, light & 0xFF, 239);
		}
		MatrixStack.Entry entry = matrices.peek();
		vertex(entry, consumer, sprite,  2.0F / 16.0F, y,  2.0F / 16.0F, alphaAsInt, light);
		vertex(entry, consumer, sprite,  2.0F / 16.0F, y, 14.0F / 16.0F, alphaAsInt, light);
		vertex(entry, consumer, sprite, 14.0F / 16.0F, y, 14.0F / 16.0F, alphaAsInt, light);
		vertex(entry, consumer, sprite, 14.0F / 16.0F, y,  2.0F / 16.0F, alphaAsInt, light);
	}

	public static void vertex(MatrixStack.Entry matrixEntry, VertexConsumer consumer, Sprite sprite, float x, float y, float z, int alpha, int light) {
		consumer
		.vertex(matrixEntry, x, y, z)
		.color(255, 255, 255, alpha)
		.texture(
			MathHelper.lerp(x, sprite.getMinU(), sprite.getMaxU()),
			MathHelper.lerp(z, sprite.getMinV(), sprite.getMaxV())
		)
		.overlay(OverlayTexture.DEFAULT_UV)
		.light(light)
		.normal(0.0F, 1.0F, 0.0F)
		;
	}
}