package builderb0y.bigtech.mixins;

import java.util.LinkedList;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;

@Mixin(WorldChunk.class)
public class WorldChunk_NotifyBeamsOfBlockChange {

	@Shadow @Final World world;

	@Inject(method = "setBlockState", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void bigtech_notifyBeams(
		BlockPos pos,
		BlockState state,
		boolean moved,
		CallbackInfoReturnable<BlockState> callback,
		int y,
		ChunkSection section,
		boolean chunkSectionEmtpy,
		int relativeX,
		int relativeY,
		int relativeZ,
		BlockState oldState
	) {
		if (!this.world.isClient) {
			CommonSectionBeamStorage sectionStorage = ChunkBeamStorageHolder.KEY.get(this).require().get(pos.y >> 4);
			if (sectionStorage != null) {
				LinkedList<BeamSegment> segments = sectionStorage.checkSegments(pos);
				if (segments != null) {
					for (BeamSegment segment : segments) {
						((PersistentBeam)(segment.beam)).onBlockChanged(pos, oldState, state);
					}
				}
			}
		}
	}
}