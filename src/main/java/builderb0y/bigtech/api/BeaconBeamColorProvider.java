package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
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
	returns the color to apply to beacon beams.
	to preserve forward compatibility wherever possible,
	it is recommended to use the packing method below,
	as opposed to concerning yourself with
	what order the channels should be in.
	*/
	public abstract int getBeaconColor(World world, BlockPos pos, BlockState state);

	public static int packRGB(int red, int green, int blue) {
		return ColorHelper.Argb.getArgb(255, red, green, blue);
	}

	public static int getRed(int color) {
		return ColorHelper.Argb.getRed(color);
	}

	public static int getGreen(int color) {
		return ColorHelper.Argb.getGreen(color);
	}

	public static int getBlue(int color) {
		return ColorHelper.Argb.getBlue(color);
	}

	public static final Object INITIALIZER = new Object() {{
		LOOKUP.registerFallback((World world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, @Nullable BeaconBlockEntity beacon) -> {
			if (state.getBlock() instanceof BeaconBeamColorProvider provider) {
				return provider;
			}
			if (state.getBlock() instanceof Stainable stainable) {
				return (World world1, BlockPos pos1, BlockState state1) -> stainable.getColor().getEntityColor();
			}
			return null;
		});
	}};
}