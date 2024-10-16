package builderb0y.bigtech.gui.handledScreens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.gui.screenHandlers.ConductiveAnvilScreenHandler;

public class ConductiveAnvilHandledScreen extends BigTechHandledScreen<ConductiveAnvilScreenHandler> {

	public static final Identifier BACKGROUND = BigTechMod.modID("textures/gui/container/conductive_anvil.png");

	public ConductiveAnvilHandledScreen(ConductiveAnvilScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title, BACKGROUND);
		this.backgroundHeight = 130;
		this.playerInventoryTitleY = 36;
	}
}