package builderb0y.bigtech.gui.handledScreens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BigTechHandledScreen<T_Handler extends ScreenHandler> extends HandledScreen<T_Handler> {

	public final Identifier backgroundTexture;

	public BigTechHandledScreen(T_Handler handler, PlayerInventory inventory, Text title, Identifier texture) {
		super(handler, inventory, title);
		this.backgroundTexture = texture;
	}

	@Override
	public void init() {
		super.init();
		this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) >> 1;
	}

	@Override
	public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		context.drawTexture(
			RenderLayer::getGuiTextured,
			this.backgroundTexture,
			(this.width - this.backgroundWidth) >> 1,
			(this.height - this.backgroundHeight) >> 1,
			0,
			0,
			this.backgroundWidth,
			this.backgroundHeight,
			256,
			256
		);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}
}