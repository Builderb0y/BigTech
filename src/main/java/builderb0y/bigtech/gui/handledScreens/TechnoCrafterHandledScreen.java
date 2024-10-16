package builderb0y.bigtech.gui.handledScreens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.gui.screenHandlers.TechnoCrafterScreenHandler;

public abstract class TechnoCrafterHandledScreen<T_Handler extends TechnoCrafterScreenHandler> extends BigTechHandledScreen<T_Handler> {

	public TechnoCrafterHandledScreen(T_Handler handler, PlayerInventory inventory, Text title, Identifier background) {
		super(handler, inventory, title, background);
		this.backgroundWidth = 202;
	}

	@Override
	public void init() {
		super.init();
		this.titleX = (176 - this.textRenderer.getWidth(this.title)) >> 1;
	}
}