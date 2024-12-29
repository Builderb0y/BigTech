package builderb0y.bigtech.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CrystalUnbakedModel implements UnbakedModel {

	public final Identifier texture;
	public UnbakedModel block;

	public CrystalUnbakedModel(Identifier texture) {
		this.texture = texture;
	}

	@Override
	public void resolve(Resolver resolver) {
		this.block = resolver.resolve(Identifier.ofVanilla("block/block"));
	}

	@Override
	public BakedModel bake(
		ModelTextures textures,
		Baker baker,
		ModelBakeSettings settings,
		boolean ambientOcclusion,
		boolean isSideLit,
		ModelTransformation transformation
	) {
		return new CrystalBakedModel(
			baker.getSpriteGetter().get(
				new SpriteIdentifier(
					SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
					this.texture
				)
			),
			UnbakedModel.getTransformations(this.block)
		);
	}
}