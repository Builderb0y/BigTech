package builderb0y.bigtech.mixins;

import java.util.LinkedList;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;

@Mixin(Entity.class)
public abstract class Entity_CollideWithBeams {

	@Shadow public abstract World getWorld();

	@Inject(
		method = "method_67632(Lit/unimi/dsi/fastutil/longs/LongSet;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/EntityCollisionHandler$Impl;Lnet/minecraft/util/math/BlockPos;I)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/BlockState;isAir()Z"
		)
	)
	private void bigtech_collideWithBeams(CallbackInfo callback, @Local(argsOnly = true) BlockPos mutablePos) {
		if (this.getWorld() instanceof ServerWorld serverWorld) {
			CommonSectionBeamStorage sectionStorage = ChunkBeamStorageHolder.KEY.get(this.getWorld().getChunk(mutablePos)).require().get(mutablePos.getY() >> 4);
			if (sectionStorage != null) {
				LinkedList<BeamSegment> segments = sectionStorage.checkSegments(mutablePos);
				if (segments != null) {
					for (BeamSegment segment : segments) {
						segment.beam().<PersistentBeam>as().onEntityCollision(serverWorld, mutablePos, this.as());
					}
				}
			}
		}
	}
}