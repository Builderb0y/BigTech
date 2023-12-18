package builderb0y.bigtech.handledScreens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.entities.MinerEntity;
import builderb0y.bigtech.screenHandlers.MinerScreenHandler;

public class MinerHandledScreen extends BigTechHandledScreen<MinerScreenHandler> {

	public static final Identifier TEXTURE = BigTechMod.modID("textures/gui/container/miner.png");

	public MinerHandledScreen(MinerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.backgroundWidth = 202;
		this.backgroundHeight = 222;
		this.playerInventoryTitleY = 222 - 94;
	}

	@Override
	public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		super.drawBackground(context, delta, mouseX, mouseY);
		int burn = this.handler.fuelTicks.get();
		if (burn > 0) {
			burn = MathHelper.ceil(burn * (13.0F / MinerEntity.SLOWDOWN_THRESHOLD));
			context.drawTexture(TEXTURE, this.x + 177, this.y + 173 - burn, 202, 13 - burn, 13, burn);
		}
	}

	@Override
	public Identifier getBackgroundTexture() {
		return TEXTURE;
	}
}