package builderb0y.bigtech.mixinterfaces;

import net.minecraft.block.PistonBlock;
import net.minecraft.block.piston.PistonHandler;

/**
implemented on both {@link PistonBlock}
and {@link PistonHandler} via mixin.
*/
public interface VariablePiston {

	public abstract void bigtech_setBlocksToMove(int blocks);

	public abstract int bigtech_getBlocksToMove();

	public abstract boolean bigtech_isSticky();
}