package builderb0y.bigtech.mixins;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.render.RenderPhase;
import net.minecraft.util.Identifier;

@Mixin(RenderPhase.Texture.class)
public interface RenderLayerTexture_IdAccess {

	@Invoker("getId")
	public abstract Optional<Identifier> bigtech_getId();
}