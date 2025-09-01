package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

@Mixin(PistonHeadBlock.class)
public class PistonHeadBlock_DontBreak {

	@Redirect(method = "isAttached", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
	private boolean bigtech_isSteelPiston(BlockState instance, Block block) {
		return instance.getBlock() instanceof PistonBlock;
	}

	@Inject(method = "getPickStack", at = @At("HEAD"), cancellable = true)
	private void bigtech_modifyPickBlock(WorldView world, BlockPos pos, BlockState state, boolean includeData, CallbackInfoReturnable<ItemStack> callback) {
		BlockPos offsetPos = pos.offset(state.get(Properties.FACING).getOpposite());
		BlockState offsetState = world.getBlockState(offsetPos);
		if (offsetState.getBlock() instanceof PistonBlock) {
			callback.setReturnValue(offsetState.getPickStack(world, pos, includeData));
		}
	}
}