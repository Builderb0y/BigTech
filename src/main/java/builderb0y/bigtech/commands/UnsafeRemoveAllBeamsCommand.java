package builderb0y.bigtech.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public class UnsafeRemoveAllBeamsCommand {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(
			CommandManager.literal("${builderb0y.bigtech.BigTechMod.MODID}:unsafeRemoveAllBeams")
			.requires((ServerCommandSource source) -> source.hasPermissionLevel(4))
			.executes((CommandContext<ServerCommandSource> context) -> {
				CommonWorldBeamStorage.KEY.get(context.getSource().getWorld()).clear();
				//todo: make feedback message translatable.
				context.getSource().sendFeedback(() -> Text.literal("Removed all beams from the world. This action will take effect when the world is reloaded, and you will get a lot of warnings in your log file because of it."), true);
				return 1;
			})
		);
	}
}