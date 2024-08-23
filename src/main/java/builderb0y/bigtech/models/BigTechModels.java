package builderb0y.bigtech.models;

import java.util.HashMap;
import java.util.Map;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection.CrystalColor;

@Environment(EnvType.CLIENT)
public class BigTechModels {

	public static final Map<Identifier, UnbakedModel> LOOKUP = new HashMap<>(16);
	static {
		for (CrystalColor color : CrystalColor.VALUES) {
			LOOKUP.put(
				BigTechMod.modID("block/" + color.prefix + "crystal_cluster"),
				new CrystalUnbakedModel(BigTechMod.modID("block/" + color.prefix + "crystal_cluster"))
			);
			LOOKUP.put(
				BigTechMod.modID("item/" + color.prefix + "crystal_cluster"),
				new CrystalUnbakedModel(BigTechMod.modID("block/" + color.prefix + "crystal_cluster"))
			);
		}
		LOOKUP.put(BigTechMod.modID("block/prism"), new PrismUnbakedModel());
		LOOKUP.put(BigTechMod.modID( "item/prism"), new PrismUnbakedModel());
	}

	public static void init() {
		ModelLoadingPlugin.register(pluginContext -> {
			pluginContext.resolveModel().register(resolutionContext -> {
				return LOOKUP.get(resolutionContext.id());
			});
		});
	}
}