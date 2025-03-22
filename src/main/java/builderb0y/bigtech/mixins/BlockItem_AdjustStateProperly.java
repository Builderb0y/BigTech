package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.util.Directions;

@Mixin(BlockItem.class)
public class BlockItem_AdjustStateProperly {

	@ModifyVariable(method = "placeFromNbt", at = @At(value = "STORE"), index = 6)
	private BlockState bigtech_modifyState(
		BlockState state,
		@Local(argsOnly = true) World world,
		@Local(argsOnly = true) BlockPos pos
	) {
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		for (Direction direction : Directions.ALL) {
			state = state.getStateForNeighborUpdate(
				world,
				world,
				pos,
				direction,
				mutablePos.set(pos, direction),
				world.getBlockState(mutablePos),
				world.random
			);
		}
		return state;
	}
}