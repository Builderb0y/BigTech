package builderb0y.bigtech.util;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;

public class BlockApiLookups {

	public static <T_API, T_Context> BlockApiLookup.BlockApiProvider<T_API, T_Context> constant(T_API api) {
		return (world, pos, state, blockEntity, context) -> api;
	}
}