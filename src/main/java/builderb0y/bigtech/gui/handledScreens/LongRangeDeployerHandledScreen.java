package builderb0y.bigtech.gui.handledScreens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.gui.screenHandlers.LongRangeDeployerScreenHandler;
import builderb0y.bigtech.networking.LongRangeDeployerEverywherePacket;

public class LongRangeDeployerHandledScreen extends BigTechHandledScreen<LongRangeDeployerScreenHandler> {

	public static final Identifier BACKGROUND = BigTechMod.modID("textures/gui/container/long_range_deployer.png");

	public CheckboxWidget everywhere;

	public LongRangeDeployerHandledScreen(LongRangeDeployerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title, BACKGROUND);
	}

	@Override
	public void init() {
		super.init();
		this.addDrawableChild(
			this.everywhere = (
				CheckboxWidget
				.builder(
					Text.translatable("container.bigtech.long_range_deployer.everywhere"),
					this.textRenderer
				)
				.pos(this.x + 72, this.y + 34)
				.checked(this.handler.everywhere.get() != 0)
				.callback((CheckboxWidget checkbox, boolean checked) -> {
					LongRangeDeployerEverywherePacket.INSTANCE.send(checked);
				})
				.build()
			)
		);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.everywhere.checked = this.handler.everywhere.get() != 0;
		super.render(context, mouseX, mouseY, delta);
	}
}