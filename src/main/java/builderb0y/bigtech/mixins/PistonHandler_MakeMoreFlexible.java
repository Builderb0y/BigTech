package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.throwables.MixinError;

import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.api.PistonInteractor;
import builderb0y.bigtech.api.PistonInteractor.PistonHandlerInfo;
import builderb0y.bigtech.mixinterfaces.VariablePiston;

@Mixin(PistonHandler.class)
public abstract class PistonHandler_MakeMoreFlexible implements VariablePiston {

	@Shadow @Final private World world;
	@Shadow @Final private Direction motionDirection;
	@Unique private int bigtech_blocksToMove = 12;
	@Shadow @Final private boolean retracted;

	@Override
	public int bigtech_getBlocksToMove() {
		return this.bigtech_blocksToMove;
	}

	@Override
	public void bigtech_setBlocksToMove(int blocks) {
		this.bigtech_blocksToMove = blocks;
	}

	/** this... may or may not be correct. idk. this method is only called on the block anyway. */
	@Override
	public boolean bigtech_isSticky() {
		return this.retracted;
	}

	@ModifyConstant(method = "tryMove", constant = @Constant(intValue = 12))
	private int bigtech_modifyBlocksToMove(int original) {
		return this.bigtech_blocksToMove;
	}

	@Shadow
	private static boolean isBlockSticky(BlockState state) {
		throw new MixinError("Method didn't get shadowed");
	}

	@Shadow
	private static boolean isAdjacentBlockStuck(BlockState state, BlockState adjacentState) {
		throw new MixinError("Method didn't get shadowed");
	}

	@Redirect(method = "calculatePush", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/piston/PistonHandler;isBlockSticky(Lnet/minecraft/block/BlockState;)Z"), require = 1, allow = 1)
	private boolean bigtech_redirectIsSticky1(BlockState state, @Local(index = 3) BlockPos pos) {
		PistonInteractor interactor = PistonInteractor.get(this.world, pos, state);
		return interactor != null ? interactor.isSticky(new PistonHandlerInfo(this.as()), pos, state) : isBlockSticky(state);
	}

	@Redirect(method = "tryMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/piston/PistonHandler;isBlockSticky(Lnet/minecraft/block/BlockState;)Z", ordinal = 0), require = 1, allow = 1)
	private boolean bigtech_redirectIsSticky2(BlockState state, BlockPos pos) {
		PistonInteractor interactor = PistonInteractor.get(this.world, pos, state);
		return interactor != null ? interactor.isSticky(new PistonHandlerInfo(this.as()), pos, state) : isBlockSticky(state);
	}

	@Redirect(method = "tryMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/piston/PistonHandler;isAdjacentBlockStuck(Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;)Z"), require = 1, allow = 1)
	private boolean bigtech_redirectIsAdjacentBlockStuck1(BlockState state, BlockState adjacentState, BlockPos pos, @Local(index = 4) int i, @Local(index = 5) BlockPos otherPos) {
		Direction opposite = this.motionDirection.getOpposite();
		BlockPos offsetPos = pos.offset(opposite, i);
		PistonInteractor interactor = PistonInteractor.get(this.world, offsetPos, state);
		return interactor != null ? interactor.canStickTo(new PistonHandlerInfo(this.as()), offsetPos, state, pos, adjacentState, opposite) : isAdjacentBlockStuck(state, adjacentState);
	}

	@Redirect(method = "tryMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/piston/PistonHandler;isBlockSticky(Lnet/minecraft/block/BlockState;)Z", ordinal = 1), require = 1, allow = 1)
	private boolean bigtech_redirectIsSticky3(BlockState state, @Local(index = 10) BlockPos pos) {
		PistonInteractor interactor = PistonInteractor.get(this.world, pos, state);
		return interactor != null ? interactor.isSticky(new PistonHandlerInfo(this.as()), pos, state) : isBlockSticky(state);
	}

	@Redirect(method = "tryMoveAdjacentBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/piston/PistonHandler;isAdjacentBlockStuck(Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;)Z"), require = 1, allow = 1)
	private boolean bigtech_redirectIsAdjacentBlockStuck2(BlockState adjacentState, BlockState state, BlockPos pos, @Local(index = 7) BlockPos adjacentPos, @Local(index = 6) Direction direction) {
		PistonInteractor interactor = PistonInteractor.get(this.world, pos, state);
		return interactor != null ? interactor.canStickTo(new PistonHandlerInfo(this.as()), pos, state, adjacentPos, adjacentState, direction) : isAdjacentBlockStuck(state, adjacentState);
	}
}