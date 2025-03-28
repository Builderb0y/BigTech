package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.blocks.LegacyOnStateReplaced;

@Mixin(WorldChunk.class)
public class WorldChunk_CallLegacyOnStateReplaced {

	@Shadow @Final World world;

	@Inject(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z", ordinal = 0))
	private void bigtech_callLegacyOnStateReplaced(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<BlockState> callback, @Local(index = 10) BlockState oldState) {
		if (oldState.getBlock() instanceof LegacyOnStateReplaced legacy && this.world instanceof ServerWorld serverWorld) {
			legacy.legacyOnStateReplaced(serverWorld, pos, oldState, state, (flags & Block.MOVED) != 0);
		}
	}
}