package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.entities.MinerEntity;

@Mixin(Block.class)
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
		//minecraft removed ChestBlock.onPlaced(),
		//so now I have to inject into the super method instead.
		//I have tried to override that method with mixin, without much success.
		//the main issue is compatibility.
		//the best way to ensure compatibility is to have 1 mixin
		//add the method in a way which just delegates to super,
		//and then have a 2nd mixin which injects
		//into the method added by the first mixin.
		//the problem with this is that the annotation processor
		//can't find a refmap for the method in this case.
		if (((Object)(this)) instanceof ChestBlock) {
			MinerEntity.trySpawnAt(world, pos, state);
		}
	}
}