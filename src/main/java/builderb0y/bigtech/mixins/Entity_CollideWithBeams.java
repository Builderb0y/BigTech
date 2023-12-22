package builderb0y.bigtech.mixins;

import java.util.LinkedList;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;

@Mixin(Entity.class)
public abstract class Entity_CollideWithBeams {

	@Shadow public abstract World getWorld();

	@Inject(method = "checkBlockCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;onBlockCollision(Lnet/minecraft/block/BlockState;)V", shift = Shift.AFTER))
	private void bigtech_collideWithBeams(CallbackInfo callback, @Local(ordinal = 0) BlockPos.Mutable mutablePos) {
		if (!this.getWorld().isClient) {
			CommonSectionBeamStorage sectionStorage = ChunkBeamStorageHolder.KEY.get(this.getWorld().getChunk(mutablePos)).require().get(mutablePos.getY() >> 4);
			if (sectionStorage != null) {
				LinkedList<BeamSegment> segments = sectionStorage.checkSegments(mutablePos);
				if (segments != null) {
					for (BeamSegment segment : segments) {
						((PersistentBeam)(segment.beam)).onEntityCollision(mutablePos, (Entity)(Object)(this));
					}
				}
			}
		}
	}
}