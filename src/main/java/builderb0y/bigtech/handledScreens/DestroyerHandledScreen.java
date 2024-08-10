package builderb0y.bigtech.handledScreens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.screenHandlers.DestroyerScreenHandler;

public class DestroyerHandledScreen extends BigTechHandledScreen<DestroyerScreenHandler> {

	public static final Identifier TEXTURE = BigTechMod.modID("textures/gui/container/destroyer.png");

	public DestroyerHandledScreen(DestroyerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.backgroundHeight = 130;
		this.playerInventoryTitleY = 36;
	}

	@Override
	public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		super.drawBackground(context, delta, mouseX, mouseY);
		if (this.handler.inventory.getStack(0).isEmpty()) {
			context.drawTexture(
				this.getBackgroundTexture(),
				((this.width - this.backgroundWidth) >> 1) + 80,
				((this.height - this.backgroundHeight) >> 1) + 17,
				176,
				((int)((System.currentTimeMillis() / 1000L) % 6L)) << 4,
				16,
				16
			);
		}
	}

	@Override
	public Identifier getBackgroundTexture() {
		return TEXTURE;
	}
}