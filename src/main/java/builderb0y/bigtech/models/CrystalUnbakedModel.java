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
	public Collection<Identifier> getModelDependencies() {
		return Collections.singletonList(new Identifier("minecraft", "block/block"));
	}

	@Override
	public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
		this.block = modelLoader.apply(new Identifier("minecraft", "block/block"));
	}

	@Nullable
	@Override
	@SuppressWarnings("deprecation")
	public BakedModel bake(
		Baker baker,
		Function<SpriteIdentifier, Sprite> textureGetter,
		ModelBakeSettings rotationContainer,
		Identifier modelId
	) {
		return new CrystalBakedModel(
			textureGetter.apply(
				new SpriteIdentifier(
					SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE,
					this.texture
				)
			),
			this.block.<JsonUnbakedModel>as().transformations
		);
	}
}