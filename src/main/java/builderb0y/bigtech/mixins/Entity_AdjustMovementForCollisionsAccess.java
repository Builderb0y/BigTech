package builderb0y.bigtech.mixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.throwables.MixinError;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

@Mixin(Entity.class)
public interface Entity_AdjustMovementForCollisionsAccess {

	@Invoker("adjustMovementForCollisions")
	public static Vec3d bigtech_adjustMovementForCollisions(Vec3d movement, Box entityBoundingBox, List<VoxelShape> collisions) {
		throw new MixinError("Invoker not applied");
	}
}