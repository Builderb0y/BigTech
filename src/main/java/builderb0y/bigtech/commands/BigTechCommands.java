package builderb0y.bigtech.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.server.command.ServerCommandSource;

public class BigTechCommands {

	public static final boolean ENABLE_UNSAFE_COMMANDS = Boolean.getBoolean("bigtech.enableUnsafeCommands");

	public static void init() {
		CommandRegistrationCallback.EVENT.register(
			(
				CommandDispatcher<ServerCommandSource> dispatcher,
				CommandRegistryAccess registryAccess,
				RegistrationEnvironment environment
			) -> {
				if (ENABLE_UNSAFE_COMMANDS) {
					UnsafeRemoveAllBeamsCommand.register(dispatcher);
				}
			}
		);
	}
}