package builderb0y.bigtech.commands;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public class UnsafeRemoveAllBeamsCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(
			CommandManager.literal("${builderb0y.bigtech.BigTechMod.MODID}:unsafeRemoveAllBeams")
			.requires(source -> source.hasPermissionLevel(4))
			.executes(context -> {
				CommonWorldBeamStorage.KEY.get(context.source.world).clear();
				return 1;
			})
		);
	}
}