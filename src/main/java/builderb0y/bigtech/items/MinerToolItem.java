package builderb0y.bigtech.items;

import java.util.List;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import builderb0y.bigtech.blocks.BigTechBlockTags;

public class MinerToolItem extends MiningToolItem implements InventoryVariants {

	public MinerToolItem(Settings settings) {
		super(ToolMaterial.IRON, BigTechBlockTags.MINER_BREAKABLE, 1.0F, -1.0F, settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		super.appendTooltip(stack, context, tooltip, type);
		tooltip.add(Text.literal("This is the item that miners use to break blocks with."));
		tooltip.add(Text.literal("It should not be obtainable or used for other purposes."));
	}

	@Override
	public Stream<ItemStack> getInventoryStacks() {
		return Stream.empty();
	}
}