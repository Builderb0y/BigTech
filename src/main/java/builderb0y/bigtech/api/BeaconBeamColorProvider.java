package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;

public interface BeaconBeamColorProvider {

	public static final BlockApiLookup<BeaconBeamColorProvider, @Nullable BeaconBlockEntity> LOOKUP = BlockApiLookup.get(BigTechMod.modID("beacon_beam_color_provider"), BeaconBeamColorProvider.class, BeaconBlockEntity.class);

	public abstract float[] getBeaconColor(World world, BlockPos pos, BlockState state);

	public static final Object INITIALIZER = new Object() {{
		LOOKUP.registerFallback((world, pos, state, blockEntity, beacon) -> {
			return state.block instanceof Stainable stainable ? (world1, pos1, state1) -> stainable.color.colorComponents : null;
		});
	}};
}