package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;

@Mixin(WorldRenderer.class)
public interface WorldRenderer_FrustumGetter {

	@Accessor("frustum")
	public abstract Frustum bigtech_getFrustum();
}