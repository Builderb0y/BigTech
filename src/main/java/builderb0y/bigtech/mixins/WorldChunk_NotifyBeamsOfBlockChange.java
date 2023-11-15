package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.base.PersistentBeam;

@Mixin(WorldChunk.class)
public class WorldChunk_NotifyBeamsOfBlockChange {

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
		PersistentBeam.notifyBlockChanged(this.<WorldChunk>as(), pos, oldState, state);
	}
}