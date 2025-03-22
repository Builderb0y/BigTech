package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
import net.minecraft.client.render.RenderPhase;

@Mixin(MultiPhaseParameters.class)
public interface MultiPhaseParameters_TextureAccess {

	@Accessor("texture")
	public abstract RenderPhase.TextureBase bigtech_getTexture();
}