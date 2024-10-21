package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import builderb0y.bigtech.entities.MinerEntity;

@Mixin(Entity.class)
public class Entity_MakeMinerEntityCollideWithBreakingBlocks {

	@ModifyReturnValue(method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;", at = @At("RETURN"))
	private static Vec3d bigtech_collideWithBreakingBlocks(
		Vec3d old,
		@Local(argsOnly = true) Entity entity,
		@Local(argsOnly = true) Box box
	) {
		if (entity instanceof MinerEntity miner) {
			return miner.modifyMovement(old, box);
		}
		return old;
	}
}