package builderb0y.bigtech.gui.handledScreens;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.gui.screenHandlers.SpawnerInterceptorScreenHandler;

public class SpawnerInterceptorHandledScreen extends BigTechHandledScreen<SpawnerInterceptorScreenHandler> {

	public static final Identifier TEXTURE = BigTechMod.modID("textures/gui/container/spawner_interceptor.png");

	public SpawnerInterceptorHandledScreen(SpawnerInterceptorScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.backgroundHeight = 136;
		this.playerInventoryTitleY = 42;
	}

	@Override
	public Identifier getBackgroundTexture() {
		return TEXTURE;
	}
}