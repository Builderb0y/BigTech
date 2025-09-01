package builderb0y.bigtech.gui.handledScreens;

import org.lwjgl.glfw.*;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blockEntities.AssemblerBlockEntity;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.gui.screenHandlers.AssemblerScreenHandler;
import builderb0y.bigtech.networking.AssemblerOutputNamePacket;
import builderb0y.bigtech.networking.AssemblerSizePacket;
import builderb0y.bigtech.networking.CycleCircuitPacket;

public class AssemblerHandledScreen extends BigTechHandledScreen<AssemblerScreenHandler> {

	public static final Identifier BACKGROUND = BigTechMod.modID("textures/gui/container/assembler.png");

	public double accumulatedScroll;
	public TextFieldWidget outputName;
	public SizeSlider widthSlider, heightSlider;

	public AssemblerHandledScreen(AssemblerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title, BACKGROUND);
		this.backgroundWidth = 198;
		this.backgroundHeight = 226;
		this.playerInventoryTitleY = 132;
	}

	@Override
	public void init() {
		super.init();
		this.outputName = this.addDrawableChild(new TextFieldWidget(this.textRenderer, this.x + 127, this.y + 20, 66, 18, Text.empty()));
		this.outputName.setText(this.handler.outputName.getString());
		this.outputName.setChangedListener((String text) -> {
			this.handler.outputName = Text.literal(text);
			AssemblerOutputNamePacket.INSTANCE.send(this.handler.outputName);
		});
		this.widthSlider = this.addDrawableChild(this.new SizeSlider(this.x + 128, this.y + 43, 64, 16, this.handler.assemblerProperties.get(AssemblerBlockEntity.PROPERTY_WIDTH)));
		this.heightSlider = this.addDrawableChild(this.new SizeSlider(this.x + 128, this.y + 65, 64, 16, this.handler.assemblerProperties.get(AssemblerBlockEntity.PROPERTY_HEIGHT)));
		this.widthSlider.updateMessage();
		this.heightSlider.updateMessage();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			this.close();
			return true;
		}
		return this.getFocused() != null ? this.getFocused().keyPressed(keyCode, scanCode, modifiers) : super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (this.focusedSlot != null && this.focusedSlot.id >= 36 && this.focusedSlot.id < 36 + 25 && this.focusedSlot.getStack().contains(BigTechDataComponents.CIRCUIT)) {
			this.accumulatedScroll += verticalAmount;
			if (this.accumulatedScroll >= 1.0D) {
				CycleCircuitPacket.INSTANCE.send(this.focusedSlot.getIndex(), true);
				this.accumulatedScroll -= 1.0D;
				if (this.accumulatedScroll >= 1.0D) this.accumulatedScroll = 0.0D;
				return true;
			}
			else if (this.accumulatedScroll <= -1.0D) {
				CycleCircuitPacket.INSTANCE.send(this.focusedSlot.getIndex(), false);
				this.accumulatedScroll += 1.0D;
				if (this.accumulatedScroll <= -1.0D) this.accumulatedScroll = 0.0D;
				return true;
			}
			return false;
		}
		else {
			return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
		}
	}

	@Override
	public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		super.drawBackground(context, delta, mouseX, mouseY);
		int progress = this.handler.assemblerProperties.get(AssemblerBlockEntity.PROPERTY_PROGRESS);
		if (progress > 0) {
			int complexity = this.handler.assemblerProperties.get(AssemblerBlockEntity.PROPERTY_COMPLEXITY);
			progress = (progress * 18 + (complexity - 1)) / complexity;
			context.drawTexture(
				RenderLayer::getGuiTextured,
				BACKGROUND,
				this.x + 123,
				this.y + 112,
				238,
				0,
				progress,
				10,
				256,
				256
			);
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.widthSlider.setIntValue(this.handler.assemblerProperties.get(AssemblerBlockEntity.PROPERTY_WIDTH));
		this.heightSlider.setIntValue(this.handler.assemblerProperties.get(AssemblerBlockEntity.PROPERTY_HEIGHT));
		super.render(context, mouseX, mouseY, delta);
	}

	public class SizeSlider extends SliderWidget {

		public int intValue;

		public SizeSlider(int x, int y, int width, int height, int value) {
			super(x, y, width, height, Text.empty(), (value - 1) / 4.0D);
			this.intValue = value;
		}

		public void setIntValue(int value) {
			if (this.intValue != value) {
				this.intValue = value;
				this.updateMessage();
			}
			this.value = (value - 1) / 4.0D;
		}

		@Override
		public void updateMessage() {
			this.setMessage(
				Text.translatable(
					this == AssemblerHandledScreen.this.widthSlider
					? "gui.bigtech.assembler.width"
					: "gui.bigtech.assembler.height",
					this.intValue
				)
			);
		}

		@Override
		public void applyValue() {
			int newValue = Math.min((int)(this.value * 5.0D), 4) + 1;
			if (this.intValue != newValue) {
				this.intValue = newValue;
				AssemblerSizePacket.INSTANCE.send(
					AssemblerHandledScreen.this.widthSlider.intValue,
					AssemblerHandledScreen.this.heightSlider.intValue
				);
			}
		}
	}
}