package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.config.BigTechConfig;
import builderb0y.bigtech.lightning.LightningPulse;

@Mixin(LightningRodBlock.class)
public class LightningRod_EmitLightningPulse {

	@Inject(method = "setPowered", at = @At("RETURN"))
	private void bigtech_emitLightningPulse(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
		int minEnergy = BigTechConfig.INSTANCE.get().server.minLightningEnergy;
		int maxEnergy = BigTechConfig.INSTANCE.get().server.maxLightningEnergy;
		int quarterDifference = (maxEnergy - minEnergy) >> 2;
		int energy = (
			minEnergy == maxEnergy
			? minEnergy
			: (
				world.random.nextInt(quarterDifference) +
				world.random.nextInt(quarterDifference) +
				world.random.nextInt(quarterDifference) +
				world.random.nextInt(quarterDifference) +
				minEnergy
			)
		);
		new LightningPulse(world, pos, energy, 64).run();
	}
}