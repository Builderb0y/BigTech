package builderb0y.bigtech.mixins;

import java.util.TreeSet;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.longs.LongSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;
import builderb0y.bigtech.util.Lockable;
import builderb0y.bigtech.util.Locked;

@Mixin(Entity.class)
public abstract class Entity_CollideWithBeams {

	@Shadow public abstract World getWorld();

	@Inject(
		method = "method_67632(Lit/unimi/dsi/fastutil/longs/LongSet;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/EntityCollisionHandler$Impl;Lnet/minecraft/util/math/BlockPos;I)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/BlockState;isAir()Z"
		)
	)
	private void bigtech_collideWithBeams(LongSet longSet, Vec3d vec3d, Vec3d vec3d2, EntityCollisionHandler.Impl impl, BlockPos pos, int version, CallbackInfoReturnable<Boolean> callback, @Local(argsOnly = true) BlockPos mutablePos) {
		if (this.getWorld() instanceof ServerWorld serverWorld) {
			CommonSectionBeamStorage sectionStorage = ChunkBeamStorageHolder.KEY.get(this.getWorld().getChunk(mutablePos)).require().get(mutablePos.getY() >> 4);
			if (sectionStorage != null) {
				Lockable<TreeSet<BeamSegment>> segments = sectionStorage.checkSegments(mutablePos);
				if (segments != null) {
					try (Locked<TreeSet<BeamSegment>> locked = segments.read()) {
						for (BeamSegment segment : locked.value) {
							segment.beam().<PersistentBeam>as().onEntityCollision(serverWorld, mutablePos, this.as());
						}
					}
				}
			}
		}
	}
}