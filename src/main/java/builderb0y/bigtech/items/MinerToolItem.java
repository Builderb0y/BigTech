package builderb0y.bigtech.items;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import builderb0y.bigtech.blocks.BigTechBlockTags;

public class MinerToolItem extends Item implements InventoryVariants {

	public MinerToolItem(Settings settings) {
		super(settings.tool(ToolMaterial.IRON, BigTechBlockTags.MINER_BREAKABLE, 1.0F, -1.0F, 0.0F));
	}

	@Override
	public Optional<TooltipData> getTooltipData(ItemStack stack) {
		return super.getTooltipData(stack);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void appendTooltip(
		ItemStack stack,
		Item.TooltipContext context,
		TooltipDisplayComponent displayComponent,
		Consumer<Text> textConsumer,
		TooltipType type
	) {
		super.appendTooltip(stack, context, displayComponent, textConsumer, type);
		textConsumer.accept(Text.literal("This is the item that miners use to break blocks with."));
		textConsumer.accept(Text.literal("It should not be obtainable or used for other purposes."));
	}

	@Override
	public Stream<ItemStack> getInventoryStacks() {
		return Stream.empty();
	}
}