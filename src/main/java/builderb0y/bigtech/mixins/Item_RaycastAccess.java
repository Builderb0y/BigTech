package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.throwables.MixinError;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.World;

@Mixin(Item.class)
public interface Item_RaycastAccess {

	@Invoker("raycast")
	public static BlockHitResult bigtech_raycast(World world, PlayerEntity player, FluidHandling fluidHandling) {
		throw new MixinError();
	}
}