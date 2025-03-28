package builderb0y.bigtech.items;

import java.util.function.Consumer;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import builderb0y.bigtech.blocks.AbstractLightningJarBlock;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;

public class LightningJarBlockItem extends BlockItem {

	public LightningJarBlockItem(AbstractLightningJarBlock block, Settings settings) {
		super(block, settings);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
		super.appendTooltip(stack, context, displayComponent, textConsumer, type);
		Integer stored = stack.get(BigTechDataComponents.LIGHTNING_ENERGY);
		if (stored != null) {
			AbstractLightningJarBlock block = (AbstractLightningJarBlock)(this.getBlock());
			textConsumer.accept(Text.translatable("bigtech.lightning_jar.stored", stored, block.getCapacity(), stored * 100 / block.getCapacity()));
		}
	}
}