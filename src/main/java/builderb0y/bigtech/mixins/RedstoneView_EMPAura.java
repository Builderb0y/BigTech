package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RedstoneView;

import builderb0y.bigtech.blocks.ElectrumCoilBlock;

@Mixin(RedstoneView.class)
public interface RedstoneView_EMPAura {

	@ModifyVariable(method = "getEmittedRedstonePower(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)I", at = @At(value = "STORE"))
	private int bigtech_includeAura(int original, @Local(argsOnly = true) BlockPos pos) {
		if (((Object)(this)) instanceof ServerWorld serverWorld) {
			Long2ByteOpenHashMap map = ElectrumCoilBlock.EMP_AURAS.get(serverWorld);
			if (map != null) {
				return Math.max(original, map.get(pos.asLong()));
			}
		}
		return original;
	}
}