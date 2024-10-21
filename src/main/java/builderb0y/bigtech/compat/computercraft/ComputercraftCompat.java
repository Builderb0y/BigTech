package builderb0y.bigtech.compat.computercraft;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.FunctionalBlocks;

public class ComputercraftCompat {

	public static final boolean INSTALLED = FabricLoader.getInstance().isModLoaded("computercraft");

	public static void init() {
		if (INSTALLED) try {
			CCCode.init();
		}
		catch (LinkageError error) {
			BigTechMod.LOGGER.error("Failed to setup Computercraft integration:", error);
		}
	}

	public static class CCCode {

		public static void init() {
			PeripheralLookup.get().registerForBlocks(
				(
					World world,
					BlockPos pos,
					BlockState state,
					@Nullable BlockEntity blockEntity,
					Direction side
				)
				-> new RadioPeripheral(world, pos),

				FunctionalBlocks.RADIO
			);
		}
	}
}