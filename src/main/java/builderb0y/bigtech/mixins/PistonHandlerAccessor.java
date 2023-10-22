package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.piston.PistonHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Mixin(PistonHandler.class)
public interface PistonHandlerAccessor {

	@Accessor("world")
	public abstract World bigtech_getWorld();

	@Accessor("posFrom")
	public abstract BlockPos bigtech_getPosFrom();

	@Accessor("retracted")
	public abstract boolean bigtech_isRetracted();

	@Accessor("motionDirection")
	public abstract Direction bigtech_getMotionDirection();

	@Accessor("pistonDirection")
	public abstract Direction bigtech_getPistonDirection();
}