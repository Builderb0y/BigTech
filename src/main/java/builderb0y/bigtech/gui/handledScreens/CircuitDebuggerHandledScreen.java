package builderb0y.bigtech.gui.handledScreens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.gui.screenHandlers.CircuitDebuggerScreenHandler;
import builderb0y.bigtech.networking.ExitDebuggerPacket;

public class CircuitDebuggerHandledScreen extends BigTechHandledScreen<CircuitDebuggerScreenHandler> {

	public static final Identifier BACKGROUND = BigTechMod.modID("textures/gui/container/circuit_debugger.png");

	public CircuitDebuggerHandledScreen(CircuitDebuggerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title, BACKGROUND);
		this.backgroundHeight = 226;
		this.playerInventoryTitleY = 132;
	}

	@Override
	public void close() {
		ExitDebuggerPacket.INSTANCE.send();
	}
}