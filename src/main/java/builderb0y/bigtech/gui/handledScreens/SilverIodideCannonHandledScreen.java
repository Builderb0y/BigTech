package builderb0y.bigtech.gui.handledScreens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.networking.SilverIodideCannonFirePacket;
import builderb0y.bigtech.gui.screenHandlers.SilverIodideCannonScreenHandler;

@Environment(EnvType.CLIENT)
public class SilverIodideCannonHandledScreen extends BigTechHandledScreen<SilverIodideCannonScreenHandler> {

	public static final Identifier
		BACKGROUND            = BigTechMod.modID("textures/gui/container/silver_iodide_cannon.png"),
		MAKE_RAINY            = BigTechMod.modID("make_rainy"),
		MAKE_RAINY_HOVERED    = BigTechMod.modID("make_rainy_hovered"),
		MAKE_SUNNY            = BigTechMod.modID("make_sunny"),
		MAKE_SUNNY_HOVERED    = BigTechMod.modID("make_sunny_hovered"),
		MAKE_THUNDERY         = BigTechMod.modID("make_thundery"),
		MAKE_THUNDERY_HOVERED = BigTechMod.modID("make_thundery_hovered");

	public SilverIodideCannonHandledScreen(SilverIodideCannonScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title, BACKGROUND);
		this.backgroundHeight = 148;
		this.playerInventoryTitleY = 54;
	}

	@Override
	public void init() {
		super.init();
		this.addDrawableChild(new WeatherButton(this.x +  43, this.y + 16, 18, 18, Text.literal(""), false));
		this.addDrawableChild(new WeatherButton(this.x + 115, this.y + 16, 18, 18, Text.literal(""), true));
	}

	public Text getStatusMessage() {
		World world = this.client.world;
		if (world.getTopY(Heightmap.Type.MOTION_BLOCKING, this.handler.pos.getX(), this.handler.pos.getZ()) > this.handler.pos.getY() + 1) {
			return Text.translatable("bigtech.silver_iodide_cannon.cant_see_the_sky");
		}
		ItemStack stack = this.handler.inventory.getStack(0);
		if (stack.isEmpty()) {
			return Text.translatable("bigtech.silver_iodide_cannon.no_ammunition");
		}
		if (stack.getItem() != Items.FIREWORK_ROCKET) {
			return Text.translatable("bigtech.silver_iodide_cannon.not_a_firework");
		}
		FireworksComponent fireworks = stack.get(DataComponentTypes.FIREWORKS);
		if (fireworks == null || fireworks.explosions().isEmpty()) {
			return Text.translatable("bigtech.silver_iodide_cannon.no_stars");
		}
		return Text.translatable("bigtech.silver_iodide_cannon.ready_to_launch");
	}

	@Override
	public void drawForeground(DrawContext context, int mouseX, int mouseY) {
		super.drawForeground(context, mouseX, mouseY);
		Text text = this.getStatusMessage();
		int x = (this.backgroundWidth - this.textRenderer.getWidth(text)) >> 1;
		int y = 40;
		context.drawText(this.textRenderer, text, x, y, -1, false);
	}

	@Environment(EnvType.CLIENT)
	public static class WeatherButton extends PressableWidget {

		public boolean moreRainy;

		public WeatherButton(int x, int y, int width, int height, Text text, boolean moreRainy) {
			super(x, y, width, height, text);
			this.setTooltip(Tooltip.of(Text.translatable("container.bigtech.silver_iodide_cannon.make_${moreRainy ? \"more\" : \"less\"}_rainy")));
			this.moreRainy = moreRainy;
		}

		@Override
		public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
			context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			Identifier texture;
			if (this.moreRainy) {
				if (MinecraftClient.getInstance().world.isRaining()) {
					texture = this.hovered ? MAKE_THUNDERY_HOVERED : MAKE_THUNDERY;
				}
				else {
					texture = this.hovered ? MAKE_RAINY_HOVERED : MAKE_RAINY;
				}
			}
			else {
				if (MinecraftClient.getInstance().world.isThundering()) {
					texture = this.hovered ? MAKE_RAINY_HOVERED : MAKE_RAINY;
				}
				else {
					texture = this.hovered ? MAKE_SUNNY_HOVERED : MAKE_SUNNY;
				}
			}
			context.drawGuiTexture(texture, this.getX(), this.getY(), this.width, this.height);
		}

		@Override
		public void onPress() {
			SilverIodideCannonFirePacket.INSTANCE.send(this.moreRainy);
			MinecraftClient.getInstance().player.closeHandledScreen();
		}

		@Override
		public void appendClickableNarrations(NarrationMessageBuilder builder) {
			this.appendDefaultNarrations(builder);
		}
	}
}