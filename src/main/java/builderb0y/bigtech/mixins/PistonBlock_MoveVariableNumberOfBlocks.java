package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.block.PistonBlock;
import net.minecraft.block.piston.PistonHandler;

import builderb0y.bigtech.mixinterfaces.VariablePiston;

@Mixin(PistonBlock.class)
public class PistonBlock_MoveVariableNumberOfBlocks implements VariablePiston {

	@Shadow @Final private boolean sticky;
	@Unique
	private int bigtech_blocksToMove = 12;

	@Override
	public int bigtech_getBlocksToMove() {
		return this.bigtech_blocksToMove;
	}

	@Override
	public void bigtech_setBlocksToMove(int blocks) {
		this.bigtech_blocksToMove = blocks;
	}

	@Override
	public boolean bigtech_isSticky() {
		return this.sticky;
	}

	@ModifyExpressionValue(method = { "tryMove", "move" }, at = @At(value = "NEW", target = "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Z)Lnet/minecraft/block/piston/PistonHandler;"))
	private PistonHandler bigtech_modifyBlocksToMove(PistonHandler original) {
		((VariablePiston)(original)).bigtech_setBlocksToMove(this.bigtech_blocksToMove);
		return original;
	}
}