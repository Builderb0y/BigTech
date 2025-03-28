package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.RenderLayer.MultiPhase;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;

@Mixin(MultiPhase.class)
public interface MultiPhase_PhasesAccess {

	@Accessor("phases")
	public abstract MultiPhaseParameters bigtech_getPhases();
}