package builderb0y.bigtech.items;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.blocks.CatwalkPlatformBlock;
import builderb0y.bigtech.blocks.CatwalkStairsBlock;

public class CatwalkStairsBlockItem extends BlockItem {

	public CatwalkStairsBlockItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		return this.place(this.modifyPlacementContext(new ItemPlacementContext(context)));
	}

	public @Nullable ItemPlacementContext modifyPlacementContext(ItemPlacementContext context) {
		BlockPos placementPos = context.getBlockPos();
		BlockPos againstPos = placementPos;
		if (!context.canReplaceExisting()) {
			againstPos = againstPos.offset(context.getSide().getOpposite());
		}
		BlockState againstState = context.getWorld().getBlockState(againstPos);
		if (againstState.getBlock() instanceof CatwalkStairsBlock) {
			placementPos = againstPos;
			if (againstState.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
				placementPos = placementPos.down();
			}
			Direction againstFacing = againstState.get(Properties.HORIZONTAL_FACING);
			Direction  playerFacing = context.getHorizontalPlayerFacing();
			double sidewaysPosition = switch (againstFacing) {
				case NORTH    ->        (context.getHitPos().x - againstPos.getX());
				case SOUTH    -> 1.0D - (context.getHitPos().x - againstPos.getX());
				case EAST     ->        (context.getHitPos().z - againstPos.getZ());
				case WEST     -> 1.0D - (context.getHitPos().z - againstPos.getZ());
				case UP, DOWN -> throw new AssertionError();
			};
			if (sidewaysPosition < 0.0626D) {
				placementPos = placementPos.offset(againstFacing.rotateYCounterclockwise());
			}
			else if (sidewaysPosition > 0.9374D) {
				placementPos = placementPos.offset(againstFacing.rotateYClockwise());
			}
			else {
				if (context.shouldCancelInteraction()) {
					playerFacing = playerFacing.getOpposite();
				}
				int dot = playerFacing.getOffsetX() * againstFacing.getOffsetX() + playerFacing.getOffsetZ() * againstFacing.getOffsetZ();
				placementPos = placementPos.add(playerFacing.getOffsetX(), dot, playerFacing.getOffsetZ());
			}
			return new AutomaticItemPlacementContext(context.getWorld(), placementPos, againstFacing, context.getStack(), context.getSide());
		}
		else if (againstState.getBlock() instanceof CatwalkPlatformBlock platform) {
			Direction offsetDirection = platform.getPlacementDirection(againstPos, againstState, context);
			placementPos = againstPos.offset(offsetDirection);
			if (context.shouldCancelInteraction()) {
				placementPos = placementPos.down();
				offsetDirection = offsetDirection.getOpposite();
			}
			return new AutomaticItemPlacementContext(context.getWorld(), placementPos, offsetDirection, context.getStack(), context.getSide());
		}
		else {
			return context;
		}
	}
}