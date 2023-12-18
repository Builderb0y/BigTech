package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.entities.MinerEntity;

@Mixin(ChestBlock.class)
public class ChestBlock_CreateMiner {

	@Inject(method = "onPlaced", at = @At("TAIL"))
	private void bigtech_createMiner(
		World world,
		BlockPos pos,
		BlockState state,
		LivingEntity placer,
		ItemStack itemStack,
		CallbackInfo callback
	) {
		MinerEntity.trySpawnAt(world, pos, state);
	}
}