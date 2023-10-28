package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.block.NeighborUpdater;

import builderb0y.bigtech.blocks.CatwalkPlatformBlock;

@Mixin(targets = "net/minecraft/world/block/ChainRestrictedNeighborUpdater\$StateReplacementEntry")
public class ChainRestrictedNeighborUpdater_UpdateDiagonalForCatwalkPlatforms {

	@Shadow private @Final Direction direction;
	@Shadow private @Final BlockState neighborState;
	@Shadow private @Final BlockPos pos;
	@Shadow private @Final BlockPos neighborPos;
	@Shadow private @Final int updateFlags;
	@Shadow private @Final int updateLimit;

	@Inject(method = "update", at = @At("RETURN"))
	private void bigtech_updateDiagonalForCatwalkPlatforms(World world, CallbackInfoReturnable<Boolean> callback) {
		if (this.direction.horizontal >= 0) {
			BlockPos upPos = this.pos.up();
			BlockState upState = world.getBlockState(upPos);
			if (upState.getBlock() instanceof CatwalkPlatformBlock) {
				BlockPos neighbor = this.neighborPos.up();
				NeighborUpdater.replaceWithStateForNeighborUpdate(world, this.direction, world.getBlockState(neighbor), upPos, neighbor, this.updateFlags, this.updateLimit);
			}
		}
	}
}