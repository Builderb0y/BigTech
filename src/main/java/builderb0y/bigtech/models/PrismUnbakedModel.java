package builderb0y.bigtech.models;

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
import net.minecraft.client.util.SpriteIdentifier;

import builderb0y.bigtech.BigTechMod;

@Environment(EnvType.CLIENT)
public class PrismUnbakedModel implements UnbakedModel {

	public UnbakedModel base;

	@Override
	public void resolve(Resolver resolver) {
		this.base = resolver.resolve(BigTechMod.modID("block/prism_base"));
		resolver.resolve(BigTechMod.modID("block/prism_lens"));
	}

	@Nullable
	@Override
	public BakedModel bake(
		Baker baker,
		Function<SpriteIdentifier, Sprite> textureGetter,
		ModelBakeSettings rotationContainer
	) {
		return new PrismBakedModel(
			baker.bake(BigTechMod.modID("block/prism_base"), rotationContainer),
			baker.bake(BigTechMod.modID("block/prism_lens"), rotationContainer),
			this.base.<JsonUnbakedModel>as().getTransformations()
		);
	}
}