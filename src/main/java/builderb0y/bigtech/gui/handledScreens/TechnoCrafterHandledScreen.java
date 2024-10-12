package builderb0y.bigtech.gui.handledScreens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.gui.screenHandlers.TechnoCrafterScreenHandler;

public class TechnoCrafterHandledScreen extends BigTechHandledScreen<TechnoCrafterScreenHandler> {

	public static final Identifier TEXTURE = BigTechMod.modID("textures/gui/container/techno_crafter.png");

	public TechnoCrafterHandledScreen(TechnoCrafterScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.backgroundWidth = 202;
	}

	@Override
	public void init() {
		super.init();
		this.titleX = (176 - this.textRenderer.getWidth(this.title)) >> 1;
	}

	@Override
	public Identifier getBackgroundTexture() {
		return TEXTURE;
	}
}