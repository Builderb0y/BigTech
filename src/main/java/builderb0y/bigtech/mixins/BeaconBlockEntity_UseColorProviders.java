package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.api.BeaconBeamColorProvider;
import builderb0y.bigtech.asm.BeaconBlockEntityASM;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntity_UseColorProviders {

	/** called by ASM, see {@link BeaconBlockEntityASM}. */
	@Unique
	private static float[] bigtech_getColor(World world, BlockPos pos, BlockState state, BeaconBlockEntity beacon) {
		BeaconBeamColorProvider provider = BeaconBeamColorProvider.LOOKUP.find(world, pos, state, null, beacon);
		return provider != null ? provider.getBeaconColor(world, pos, state) : null;
	}
}