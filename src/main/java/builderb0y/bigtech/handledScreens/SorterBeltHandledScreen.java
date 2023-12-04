package builderb0y.bigtech.handledScreens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.screenHandlers.SorterBeltScreenHandler;

@Environment(EnvType.CLIENT)
public class SorterBeltHandledScreen extends BigTechHandledScreen<SorterBeltScreenHandler> {

	public static final Identifier TEXTURE = BigTechMod.modID("textures/gui/container/sorter_belt.png");

	public SorterBeltHandledScreen(SorterBeltScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	public Identifier getBackgroundTexture() {
		return TEXTURE;
	}
}