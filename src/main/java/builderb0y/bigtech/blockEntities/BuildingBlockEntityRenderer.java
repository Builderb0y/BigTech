package builderb0y.bigtech.blockEntities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class BuildingBlockEntityRenderer implements BlockEntityRenderer<BuildingBlockEntity> {

	public BuildingBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

	@Override
	public void render(
		BuildingBlockEntity buildingBlockEntity,
		float tickProgress,
		MatrixStack matrices,
		VertexConsumerProvider vertexConsumers,
		int light,
		int overlay,
		Vec3d cameraPos
	) {
		if (buildingBlockEntity.isBeamVisible()) {
			BeaconBlockEntityRenderer.renderBeam(
				matrices,
				vertexConsumers,
				BeaconBlockEntityRenderer.BEAM_TEXTURE,
				tickProgress,
				1.0F,
				buildingBlockEntity.getWorld().getTime(),
				0,
				buildingBlockEntity.getWorld().getTopYInclusive() + 1 - buildingBlockEntity.getPos().getY(),
				-1,
				0.2F,
				0.25F
			);
		}
	}

	@Override
	public boolean rendersOutsideBoundingBox() {
		return true;
	}

	@Override
	public int getRenderDistance() {
		return MinecraftClient.getInstance().options.getClampedViewDistance() << 4;
	}

	@Override
	public boolean isInRenderDistance(BuildingBlockEntity blockEntity, Vec3d pos) {
		double dx = blockEntity.getPos().getX() + 0.5D - pos.x;
		double dz = blockEntity.getPos().getZ() + 0.5D - pos.z;
		double threshold = this.getRenderDistance();
		return dx * dx + dz * dz < threshold * threshold;
	}
}