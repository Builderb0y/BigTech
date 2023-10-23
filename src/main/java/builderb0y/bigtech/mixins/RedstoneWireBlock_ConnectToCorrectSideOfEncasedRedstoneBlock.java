package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.blocks.EncasedRedstoneBlock;

@Mixin(RedstoneWireBlock.class)
public class RedstoneWireBlock_ConnectToCorrectSideOfEncasedRedstoneBlock {

	@Inject(method = "connectsTo(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"), cancellable = true)
	private static void bigtech_connectToCorrectSideOfEncasedRedstoneBlock(BlockState state, Direction direction, CallbackInfoReturnable<Boolean> callback) {
		if (state.getBlock() instanceof EncasedRedstoneBlock) {
			callback.setReturnValue(direction == state.get(Properties.FACING).opposite);
		}
	}
}