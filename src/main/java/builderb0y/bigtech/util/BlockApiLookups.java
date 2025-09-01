package builderb0y.bigtech.util;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockApiLookups {

	public static <T_API, T_Context> BlockApiLookup.BlockApiProvider<T_API, T_Context> constant(T_API api) {
		return (World world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, T_Context context) -> api;
	}
}