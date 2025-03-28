package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.base.PersistentBeam;

@Mixin(WorldChunk.class)
public class WorldChunk_NotifyBeamsOfBlockChange {

	@Inject(method = "setBlockState", at = @At("TAIL"))
	private void bigtech_notifyBeams(
		BlockPos pos,
		BlockState state,
		int flags,
		CallbackInfoReturnable<BlockState> callback,
		@Local(index = 10) BlockState oldState
	) {
		PersistentBeam.notifyBlockChanged(this.<WorldChunk>as(), pos, oldState, state);
	}
}