package builderb0y.bigtech.entities;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;

public class MagneticArrowEntityRenderer extends ProjectileEntityRenderer<MagneticArrowEntity> {

	public static final Identifier TEXTURE = BigTechMod.modID("textures/entity/magnetic_arrow.png");

	public MagneticArrowEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(MagneticArrowEntity entity) {
		return TEXTURE;
	}
}