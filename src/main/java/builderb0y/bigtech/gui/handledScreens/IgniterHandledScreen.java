package builderb0y.bigtech.gui.handledScreens;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.gui.screenHandlers.IgniterScreenHandler;

public class IgniterHandledScreen extends BigTechHandledScreen<IgniterScreenHandler> {

	public static final Identifier BACKGROUND = BigTechMod.modID("textures/gui/container/igniter.png");

	public IgniterHandledScreen(IgniterScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title, BACKGROUND);
		this.backgroundHeight = 148;
		this.playerInventoryTitleY = 54;
	}

	@Override
	public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		super.drawBackground(context, delta, mouseX, mouseY);
		int totalTicks = this.handler.totalBurnTime.get();
		int remainingTicks = this.handler.remainingBurnTime.get();
		if (totalTicks > 0 && remainingTicks > 0) {
			int pixels = (remainingTicks * 14 + (totalTicks - 1)) / totalTicks;
			context.drawTexture(
				RenderPipelines.GUI_TEXTURED,
				BACKGROUND,
				this.x + 81,
				this.y + 32 - pixels,
				176,
				14 - pixels,
				14,
				pixels,
				256,
				256
			);
		}
	}
}