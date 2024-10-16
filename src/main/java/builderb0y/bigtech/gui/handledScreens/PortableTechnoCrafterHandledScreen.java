package builderb0y.bigtech.gui.handledScreens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.gui.screenHandlers.PortableTechnoCrafterScreenHandler;

public class PortableTechnoCrafterHandledScreen extends TechnoCrafterHandledScreen<PortableTechnoCrafterScreenHandler> {

	public static final Identifier BACKGROUND = BigTechMod.modID("textures/gui/container/portable_techno_crafter.png");

	public PortableTechnoCrafterHandledScreen(PortableTechnoCrafterScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title, BACKGROUND);
	}
}