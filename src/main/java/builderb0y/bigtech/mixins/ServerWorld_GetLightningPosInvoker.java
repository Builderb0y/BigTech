package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(ServerWorld.class)
public interface ServerWorld_GetLightningPosInvoker {

	@Invoker("getLightningPos")
	public BlockPos bigtech_getLightningPos(BlockPos pos);
}