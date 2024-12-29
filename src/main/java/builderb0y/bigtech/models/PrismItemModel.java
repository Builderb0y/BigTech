package builderb0y.bigtech.models;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderState.LayerRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.blockEntities.PrismBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class PrismItemModel implements ItemModel {

	public final BakedModel prism, lens;

	public PrismItemModel(BakedModel prism, BakedModel lens) {
		this.prism = prism;
		this.lens = lens;
	}

	@Override
	public void update(ItemRenderState state, ItemStack stack, ItemModelManager resolver, ModelTransformationMode transformationMode, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed) {
		LayerRenderState layer = state.newLayer();
		if (stack.hasGlint()) {
			layer.setGlint(ItemRenderState.Glint.STANDARD);
		}
		NbtComponent data = stack.get(DataComponentTypes.BLOCK_ENTITY_DATA);
		int lenses = data != null ? data.getNbt().getInt("lenses") & PrismBlockEntity.FLAG_MASK : 0;
		layer.setModel(
			new PrismBakedModel(this.prism, this.lens, lenses),
			TexturedRenderLayers.getItemEntityTranslucentCull()
		);
	}

	public static class Unbaked implements ItemModel.Unbaked {

		public static final MapCodec<Unbaked> CODEC = BigTechAutoCodec.callerMapCodec();

		public final Identifier prism, lens;

		public Unbaked(Identifier prism, Identifier lens) {
			this.prism = prism;
			this.lens = lens;
		}

		@Override
		public ItemModel bake(BakeContext context) {
			return new PrismItemModel(context.bake(this.prism), context.bake(this.lens));
		}

		@Override
		public MapCodec<? extends ItemModel.Unbaked> getCodec() {
			return CODEC;
		}

		@Override
		public void resolve(Resolver resolver) {}
	}
}