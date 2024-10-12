package builderb0y.bigtech.gui.handledScreens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class BigTechHandledScreen<T_Handler extends ScreenHandler> extends HandledScreen<T_Handler> {

	public BigTechHandledScreen(T_Handler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	public abstract Identifier getBackgroundTexture();

	@Override
	public void init() {
		super.init();
		this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) >> 1;
	}

	@Override
	public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		context.drawTexture(
			this.getBackgroundTexture(),
			(this.width - this.backgroundWidth) >> 1,
			(this.height - this.backgroundHeight) >> 1,
			0,
			0,
			this.backgroundWidth,
			this.backgroundHeight
		);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}
}