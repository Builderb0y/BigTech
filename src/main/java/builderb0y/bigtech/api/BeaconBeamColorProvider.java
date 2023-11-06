package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;

/**
provides colors for beacon beams. and laser beams.

register blocks with {@link #LOOKUP} OR implement
this interface on your Block class. either works.
*/
public interface BeaconBeamColorProvider {

	public static final BlockApiLookup<BeaconBeamColorProvider, @Nullable BeaconBlockEntity> LOOKUP = BlockApiLookup.get(BigTechMod.modID("beacon_beam_color_provider"), BeaconBeamColorProvider.class, BeaconBlockEntity.class);

	/**
	returns the RGB values to use for tinting the beacon beam.
	index 0 of the returned float[] should represent the red component of the tint,
	index 1 of the returned float[] should represent the green component of the tint, and
	index 2 of the returned float[] should represent the blue component of the tint.
	all 3 indexes should be in the range 0 to 1.
	*/
	public abstract float[] getBeaconColor(World world, BlockPos pos, BlockState state);

	public static final Object INITIALIZER = new Object() {{
		LOOKUP.registerFallback((world, pos, state, blockEntity, beacon) -> {
			if (state.block instanceof BeaconBeamColorProvider provider) {
				return provider;
			}
			if (state.block instanceof Stainable stainable) {
				return (world1, pos1, state1) -> stainable.color.colorComponents;
			}
			return null;
		});
	}};
}