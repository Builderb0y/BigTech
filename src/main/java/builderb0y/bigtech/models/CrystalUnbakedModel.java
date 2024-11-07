package builderb0y.bigtech.models;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.Sprite;
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

	@Nullable
	@Override
	public BakedModel bake(
		Baker baker,
		Function<SpriteIdentifier, Sprite> textureGetter,
		ModelBakeSettings rotationContainer
	) {
		return new CrystalBakedModel(
			textureGetter.apply(
				new SpriteIdentifier(
					SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
					this.texture
				)
			),
			this.block.<JsonUnbakedModel>as().getTransformations()
		);
	}
}