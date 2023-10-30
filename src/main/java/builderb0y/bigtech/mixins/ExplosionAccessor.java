package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.explosion.Explosion;

@Mixin(Explosion.class)
public interface ExplosionAccessor {

	@Accessor("power")
	public abstract float bigtech_getPower();

	@Accessor("x")
	public abstract double bigtech_getX();

	@Accessor("y")
	public abstract double bigtech_getY();

	@Accessor("z")
	public abstract double bigtech_getZ();
}