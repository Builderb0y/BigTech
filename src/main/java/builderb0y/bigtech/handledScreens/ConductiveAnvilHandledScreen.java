package builderb0y.bigtech.handledScreens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.screenHandlers.ConductiveAnvilScreenHandler;

public class ConductiveAnvilHandledScreen extends BigTechHandledScreen<ConductiveAnvilScreenHandler> {

	public static final Identifier TEXTURE = BigTechMod.modID("textures/gui/container/conductive_anvil.png");

	public ConductiveAnvilHandledScreen(ConductiveAnvilScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.backgroundHeight = 130;
		this.playerInventoryTitleY = 36;
	}

	@Override
	public Identifier getBackgroundTexture() {
		return TEXTURE;
	}
}