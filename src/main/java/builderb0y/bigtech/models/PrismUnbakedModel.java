package builderb0y.bigtech.models;

import java.util.Collection;
import java.util.List;
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
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;

@Environment(EnvType.CLIENT)
public class PrismUnbakedModel implements UnbakedModel {

	public UnbakedModel base;

	@Override
	public Collection<Identifier> getModelDependencies() {
		return List.of(
			BigTechMod.modID("block/prism_base"),
			BigTechMod.modID("block/prism_lens")
		);
	}

	@Override
	public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
		this.base = modelLoader.apply(BigTechMod.modID("block/prism_base"));
		this.base.setParents(modelLoader); //why is this necessary?
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