package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.throwables.MixinError;

import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.api.PistonInteractor;
import builderb0y.bigtech.api.PistonInteractor.PistonHandlerInfo;
import builderb0y.bigtech.asm.PistonHandlerASM;

@Mixin(PistonHandler.class)
public abstract class PistonHandler_AddNecessaryMethods {

	@Shadow
	private static boolean isBlockSticky(BlockState state) {
		throw new MixinError("Method didn't get shadowed");
	}

	@Shadow
	private static boolean isAdjacentBlockStuck(BlockState state, BlockState adjacentState) {
		throw new MixinError("Method didn't get shadowed");
	}

	/** called from ASM, see {@link PistonHandlerASM}. */
	@Unique
	private static boolean bigtech_isBlockSticky(BlockState state, BlockPos pos, PistonHandler handler) {
		PistonHandlerAccessor accessor = handler.as();
		PistonInteractor interactor = PistonInteractor.get(accessor.bigtech_getWorld(), pos, state);
		return interactor != null ? interactor.isSticky(new PistonHandlerInfo(accessor), pos, state) : isBlockSticky(state);
	}

	/** called from ASM, see {@link PistonHandlerASM}. */
	@Unique
	private static boolean bigtech_isAdjacentBlockStuck(BlockState state, BlockState otherState, BlockPos pos, BlockPos otherPos, Direction face, PistonHandler handler) {
		PistonHandlerAccessor accessor = handler.as();
		PistonInteractor interactor = PistonInteractor.get(accessor.bigtech_getWorld(), pos, state);
		return interactor != null ? interactor.canStickTo(new PistonHandlerInfo(accessor), pos, state, otherPos, otherState, face) : isAdjacentBlockStuck(state, otherState);
	}
}