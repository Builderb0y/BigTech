package builderb0y.bigtech.items;

import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.BigTechBlockTags;

public class MinerToolItem extends MiningToolItem implements InventoryVariants {

	public MinerToolItem(Settings settings) {
		super(1.0F, 1.0F, ToolMaterials.IRON, BigTechBlockTags.MINER_BREAKABLE, settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.literal("This is the item that miners use to break blocks with."));
		tooltip.add(Text.literal("It should not be obtainable or used for other purposes."));
	}

	@Override
	public Stream<ItemStack> getInventoryStacks() {
		return Stream.empty();
	}
}