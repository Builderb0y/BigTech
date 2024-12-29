package builderb0y.bigtech.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelTransformation;

import builderb0y.bigtech.BigTechMod;

@Environment(EnvType.CLIENT)
public class PrismUnbakedModel implements UnbakedModel {

	public UnbakedModel base;

	@Override
	public void resolve(Resolver resolver) {
		this.base = resolver.resolve(BigTechMod.modID("block/prism_base"));
		resolver.resolve(BigTechMod.modID("block/prism_lens"));
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
		return new PrismBakedModel(
			baker.bake(BigTechMod.modID("block/prism_base"), settings),
			baker.bake(BigTechMod.modID("block/prism_lens"), settings),
			0
		);
	}
}