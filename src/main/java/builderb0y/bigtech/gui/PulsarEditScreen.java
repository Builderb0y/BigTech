package builderb0y.bigtech.gui;

import org.lwjgl.glfw.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blockEntities.PulsarBlockEntity;
import builderb0y.bigtech.blockEntities.PulsarBlockEntity.TimeGetter;
import builderb0y.bigtech.datagen.impl.PulsarDataGenerator.PulsarTexts;
import builderb0y.bigtech.networking.UpdatePulsarPacket;

public class PulsarEditScreen extends Screen {

	public static final Identifier BACKGROUND_TEXTURE = BigTechMod.modID("textures/gui/pulsar.png");
	public static final int
		BACKGROUND_WIDTH = 180,
		BACKGROUND_HEIGHT = 180;
	public static final byte
		ON_TIME_VALID  = 1,
		OFF_TIME_VALID = 2,
		OFFSET_VALID   = 4;

	public PulsarBlockEntity pulsar;
	public int titleWidth;
	public TextFieldWidget onTime, offTime, offset;
	public byte validFlags = ON_TIME_VALID | OFF_TIME_VALID | OFFSET_VALID;
	public CyclingButtonWidget<TimeGetter> relativeTo;
	public ButtonWidget done, cancel;

	public PulsarEditScreen(PulsarBlockEntity pulsar) {
		super(Text.translatable(PulsarTexts.TITLE));
		this.pulsar = pulsar;
	}

	public void setValidFlag(byte flag, boolean valid) {
		this.validFlags = (byte)(valid ? this.validFlags | flag : this.validFlags & ~flag);
		this.done.active = this.validFlags == (ON_TIME_VALID | OFF_TIME_VALID | OFFSET_VALID);
	}

	@Override
	public void init() {
		super.init();
		this.titleWidth = this.textRenderer.getWidth(this.title);
		this.onTime = this.addDrawableChild(
			new TextFieldWidget(
				this.textRenderer,
				(this.width >> 1) + 10,
				(this.height >> 1) - 70,
				60,
				20,
				Text.empty()
			)
		);
		this.onTime.setText(String.valueOf(this.pulsar.onTime));
		this.onTime.setChangedListener((String text) -> this.setValidFlag(ON_TIME_VALID, parseInt(text) > 0));
		this.onTime.setRenderTextProvider((String text, Integer what) -> {
			return OrderedText.styledForwardsVisitedString(text, (this.validFlags & ON_TIME_VALID) != 0 ? Style.EMPTY : Style.EMPTY.withColor(Formatting.RED));
		});
		this.offTime = this.addDrawableChild(
			new TextFieldWidget(
				this.textRenderer,
				(this.width >> 1) + 10,
				(this.height >> 1) - 40,
				60,
				20,
				Text.empty()
			)
		);
		this.offTime.setText(String.valueOf(this.pulsar.offTime));
		this.offTime.setChangedListener((String text) -> this.setValidFlag(OFF_TIME_VALID, parseInt(text) > 0));
		this.offTime.setRenderTextProvider((String text, Integer what) -> {
			return OrderedText.styledForwardsVisitedString(text, (this.validFlags & OFF_TIME_VALID) != 0 ? Style.EMPTY : Style.EMPTY.withColor(Formatting.RED));
		});
		this.relativeTo = this.addDrawableChild(
			CyclingButtonWidget
			.builder((TimeGetter getter) -> Text.translatable(getter.translationKey))
			.values(TimeGetter.VALUES)
			.initially(this.pulsar.relativeTo)
			.omitKeyText()
			.build(
				(this.width >> 1) + 10,
				(this.height >> 1) - 10,
				60,
				20,
				Text.empty(),
				(CyclingButtonWidget<TimeGetter> button, TimeGetter timeGetter) -> {}
			)
		);
		this.offset = this.addDrawableChild(
			new TextFieldWidget(
				this.textRenderer,
				(this.width >> 1) + 10,
				(this.height >> 1) + 20,
				60,
				20,
				Text.empty()
			)
		);
		this.offset.setText(String.valueOf(this.pulsar.offset));
		this.offset.setChangedListener((String text) -> this.setValidFlag(OFFSET_VALID, parseInt(text) >= 0));
		this.offset.setRenderTextProvider((String text, Integer what) -> {
			return OrderedText.styledForwardsVisitedString(text, (this.validFlags & OFFSET_VALID) != 0 ? Style.EMPTY : Style.EMPTY.withColor(Formatting.RED));
		});
		this.done = this.addDrawableChild(
			ButtonWidget
			.builder(
				Text.translatable(PulsarTexts.DONE),
				(ButtonWidget button) -> this.done()
			)
			.dimensions(
				(this.width >> 1) - 70,
				(this.height >> 1) + 60,
				60,
				20
			)
			.build()
		);
		this.cancel = this.addDrawableChild(
			ButtonWidget
			.builder(
				Text.translatable(PulsarTexts.CANCEL),
				(ButtonWidget button) -> this.cancel()
			)
			.dimensions(
				(this.width >> 1) + 10,
				(this.height >> 1) + 60,
				60,
				20
			)
			.build()
		);
	}

	public void done() {
		cancel: {
			int onTime = parseInt(this.onTime.getText());
			if (onTime <= 0) break cancel;
			int offTime = parseInt(this.offTime.getText());
			if (offTime <= 0) break cancel;
			int offset = parseInt(this.offset.getText());
			if (offset < 0) break cancel;
			TimeGetter relativeTo = this.relativeTo.getValue();
			UpdatePulsarPacket.INSTANCE.send(this.pulsar.getPos(), onTime, offTime, relativeTo, offset);
		}
		this.close();
	}

	public void cancel() {
		this.close();
	}

	public static int parseInt(String text) {
		int value = 0;
		for (int index = 0, length = text.length(); index < length; index++) {
			char c = text.charAt(index);
			if (c >= '0' && c <= '9') {
				value = value * 10 + (c - '0');
				if (value > 1_000_000) {
					return -1;
				}
			}
			else {
				return -1;
			}
		}
		return value;
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		//why does minecraft require this
		String onTime  = this.onTime.getText();
		String offTime = this.offTime.getText();
		String offset  = this.offset.getText();
		TimeGetter relativeTo = this.relativeTo.getValue();
		byte validFlags = this.validFlags;
		super.resize(client, width, height);
		this.onTime.setText(onTime);
		this.offTime.setText(offTime);
		this.offset.setText(offset);
		this.relativeTo.setValue(relativeTo);
		this.validFlags = validFlags;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ENTER) {
			this.done();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		int baseHeight = (this.height - this.textRenderer.fontHeight) >> 1;
		context.drawText(
			this.textRenderer,
			this.title,
			(this.width - this.titleWidth) >> 1,
			baseHeight - 80,
			0x404040,
			false
		);
		context.drawText(
			this.textRenderer,
			Text.translatable(PulsarTexts.ON_TIME),
			(this.width >> 1) - 70,
			baseHeight - 60,
			0x404040,
			false
		);
		context.drawText(
			this.textRenderer,
			Text.translatable(PulsarTexts.OFF_TIME),
			(this.width >> 1) - 70,
			baseHeight - 30,
			0x404040,
			false
		);
		context.drawText(
			this.textRenderer,
			Text.translatable(PulsarTexts.RELATIVE_TO),
			(this.width >> 1) - 70,
			baseHeight,
			0x404040,
			false
		);
		context.drawText(
			this.textRenderer,
			Text.translatable(PulsarTexts.OFFSET),
			(this.width >> 1) - 70,
			baseHeight + 30,
			0x404040,
			false
		);
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		super.renderBackground(context, mouseX, mouseY, delta);
		context.drawTexture(
			RenderLayer::getGuiTextured,
			BACKGROUND_TEXTURE,
			(this.width - BACKGROUND_WIDTH) >> 1,
			(this.height - BACKGROUND_HEIGHT) >> 1,
			0,
			0,
			BACKGROUND_WIDTH,
			BACKGROUND_HEIGHT,
			256,
			256
		);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}