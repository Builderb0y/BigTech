package builderb0y.bigtech.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;

public class BuildingBlockItem extends BlockItem {

	public BuildingBlockItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (context.getPlayer() != null && !context.getPlayer().isSneaking() && context.getWorld().getBlockState(context.getBlockPos()).isOf(FunctionalBlocks.BUILDING_BLOCK)) {
			context.getStack().set(BigTechDataComponents.BUILDING_BLOCK_LINK, context.getBlockPos());
			return ActionResult.SUCCESS;
		}
		else {
			return super.useOnBlock(context);
		}
	}

	@Override
	public ActionResult use(World world, PlayerEntity player, Hand hand) {
		if (player.isSneaking()) {
			player.getStackInHand(hand).remove(BigTechDataComponents.BUILDING_BLOCK_LINK);
		}
		return super.use(world, player, hand);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return stack.contains(BigTechDataComponents.BUILDING_BLOCK_LINK) || super.hasGlint(stack);
	}
}