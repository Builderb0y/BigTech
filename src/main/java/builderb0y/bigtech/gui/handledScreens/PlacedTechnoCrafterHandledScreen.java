package builderb0y.bigtech.gui.handledScreens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.gui.screenHandlers.PlacedTechnoCrafterScreenHandler;

public class PlacedTechnoCrafterHandledScreen extends TechnoCrafterHandledScreen<PlacedTechnoCrafterScreenHandler> {

	public static final Identifier BACKGROUND = BigTechMod.modID("textures/gui/container/placed_techno_crafter.png");

	public PlacedTechnoCrafterHandledScreen(PlacedTechnoCrafterScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title, BACKGROUND);
		this.backgroundHeight = 233;
		this.playerInventoryTitleY = 139;
	}
}