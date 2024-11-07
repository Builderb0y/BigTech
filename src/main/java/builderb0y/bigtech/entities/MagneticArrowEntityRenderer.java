package builderb0y.bigtech.entities;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;

public class MagneticArrowEntityRenderer extends ProjectileEntityRenderer<MagneticArrowEntity, ProjectileEntityRenderState> {

	public static final Identifier TEXTURE = BigTechMod.modID("textures/entity/magnetic_arrow.png");

	public MagneticArrowEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public ProjectileEntityRenderState createRenderState() {
		return new ProjectileEntityRenderState();
	}

	@Override
	public Identifier getTexture(ProjectileEntityRenderState state) {
		return TEXTURE;
	}
}