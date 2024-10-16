package builderb0y.bigtech.gui.handledScreens;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.gui.screenHandlers.DislocatorScreenHandler;
import builderb0y.bigtech.networking.DislocatorSetDepthPacket;

public class DislocatorHandledScreen extends BigTechHandledScreen<DislocatorScreenHandler> {

	public static final Identifier BACKGROUND = BigTechMod.modID("textures/gui/container/dislocator.png");

	public DepthSlider depthSlider;
	public ButtonWidget increment, decrement;

	public DislocatorHandledScreen(DislocatorScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title, BACKGROUND);
		this.backgroundHeight = 206;
		this.playerInventoryTitleY = 112;
	}

	@Override
	public void init() {
		super.init();
		this.depthSlider = this.addDrawableChild(
			new DepthSlider(
				(this.width >> 1) - 27,
				(this.height >> 1) - 23,
				54,
				20,
				ScreenTexts.EMPTY,
				0.0D
			)
		);
		this.decrement = this.addDrawableChild(
			ButtonWidget
			.builder(Text.literal("-"), (ButtonWidget button) -> {
				this.depthSlider.previousValue--;
				this.depthSlider.setValue(this.depthSlider.previousValue / 8.0D);
				this.updateButtons();
				DislocatorSetDepthPacket.INSTANCE.decrement();
			})
			.dimensions(
				(this.width >> 1) - 57,
				(this.height >> 1) - 23,
				20,
				20
			)
			.build()
		);
		this.increment = this.addDrawableChild(
			ButtonWidget
			.builder(Text.literal("+"), (ButtonWidget button) -> {
				this.depthSlider.previousValue++;
				this.depthSlider.setValue(this.depthSlider.previousValue / 8.0D);
				this.updateButtons();
				DislocatorSetDepthPacket.INSTANCE.increment();
			})
			.dimensions(
				(this.width >> 1) + 37,
				(this.height >> 1) - 23,
				20,
				20
			)
			.build()
		);
	}

	public void updateButtons() {
		this.increment.active = this.depthSlider.previousValue < 8;
		this.decrement.active = this.depthSlider.previousValue > 0;
		this.depthSlider.updateMessage();
	}

	public class DepthSlider extends SliderWidget {

		public int previousValue;

		public DepthSlider(int x, int y, int width, int height, Text text, double value) {
			super(x, y, width, height, text, value);
			this.updateMessage();
		}

		public double getValue() {
			return this.value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		@Override
		public void updateMessage() {
			this.setMessage(Text.translatable("container.bigtech.dislocator.depth", this.previousValue));
		}

		@Override
		public void applyValue() {
			double valueD = Math.min(Math.floor(this.value * 9.0D), 8.0D);
			this.value = valueD / 8.0D;
			if (this.previousValue != (int)(valueD)) {
				this.previousValue = (int)(valueD);
				DislocatorHandledScreen.this.updateButtons();
				DislocatorSetDepthPacket.INSTANCE.set(this.previousValue);
			}
		}
	}
}