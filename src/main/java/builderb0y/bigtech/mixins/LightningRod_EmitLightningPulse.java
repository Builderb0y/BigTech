package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.lightning.LightningPulse;

@Mixin(LightningRodBlock.class)
public class LightningRod_EmitLightningPulse {

	@Inject(method = "setPowered", at = @At("RETURN"))
	private void bigtech_emitLightningPulse(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
		//gaussian approximation between 5000 and 15000, averaging 10000.
		int energy = (
			5000 +
			world.random.nextInt(2000) +
			world.random.nextInt(2000) +
			world.random.nextInt(2000) +
			world.random.nextInt(2000) +
			world.random.nextInt(2000)
		);
		new LightningPulse(world, pos, energy, 64).run();
	}
}