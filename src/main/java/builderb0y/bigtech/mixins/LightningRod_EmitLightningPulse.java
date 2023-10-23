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
		//gaussian approximation between 500 and 1500, averaging 1000.
		int energy = (
			500 +
			world.random.nextInt(200) +
			world.random.nextInt(200) +
			world.random.nextInt(200) +
			world.random.nextInt(200) +
			world.random.nextInt(200)
		);
		new LightningPulse(world, pos, energy, 64).run();
	}
}