package builderb0y.bigtech.items;

import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import builderb0y.bigtech.compat.computercraft.ComputercraftCompat;

public class RadioBlockItem extends BlockItem {

	public RadioBlockItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
		super.appendTooltip(stack, context, displayComponent, textConsumer, type);
		if (ComputercraftCompat.INSTALLED) {
			textConsumer.accept(Text.translatable("tooltip.bigtech.radio.usage"));
		}
		else {
			textConsumer.accept(Text.translatable("tooltip.bigtech.radio.computercraft_not_installed"));
		}
	}
}