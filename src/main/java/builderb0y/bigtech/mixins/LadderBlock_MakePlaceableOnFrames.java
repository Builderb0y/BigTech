package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import builderb0y.bigtech.blocks.FrameBlock;

@Mixin(LadderBlock.class)
public class LadderBlock_MakePlaceableOnFrames {

	/**
	normally I would redirect isSideSolidFullSquare(world, pos, side)
	to isSideSolid(world, pos, side, SideShapeType.RIGID),
	but unfortunately RIGID has a bug in it where it only works for the top face.
	so, I am using inject instead and hard-coding frames specifically,
	instead of all blocks with rigid sides.
	*/
	@Inject(
		method = "canPlaceOn",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/block/BlockState;isSideSolidFullSquare(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"
		),
		locals = LocalCapture.CAPTURE_FAILSOFT,
		cancellable = true
	)
	private void bigtech_makePlaceableOnFrames(BlockView world, BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> callback, BlockState againstState) {
		if (againstState.block instanceof FrameBlock) {
			callback.setReturnValue(Boolean.TRUE);
		}
	}
}