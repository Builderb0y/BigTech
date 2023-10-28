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
		BlockPos placementPos = context.blockPos;
		BlockPos againstPos = placementPos;
		if (!context.canReplaceExisting()) {
			againstPos = againstPos.offset(context.side.opposite);
		}
		BlockState againstState = context.world.getBlockState(againstPos);
		if (againstState.getBlock() instanceof CatwalkStairsBlock) {
			placementPos = againstPos;
			if (againstState.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
				placementPos = placementPos.down();
			}
			Direction againstFacing = againstState.get(Properties.HORIZONTAL_FACING);
			Direction  playerFacing = context.horizontalPlayerFacing;
			double sidewaysPosition = switch (againstFacing) {
				case NORTH    ->        (context.hitPos.x - againstPos.x);
				case SOUTH    -> 1.0D - (context.hitPos.x - againstPos.x);
				case EAST     ->        (context.hitPos.z - againstPos.z);
				case WEST     -> 1.0D - (context.hitPos.z - againstPos.z);
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
					playerFacing = playerFacing.opposite;
				}
				int dot = playerFacing.offsetX * againstFacing.offsetX + playerFacing.offsetZ * againstFacing.offsetZ;
				placementPos = placementPos.add(playerFacing.offsetX, dot, playerFacing.offsetZ);
			}
			return new AutomaticItemPlacementContext(context.world, placementPos, againstFacing, context.stack, context.side);
		}
		else if (againstState.getBlock() instanceof CatwalkPlatformBlock platform) {
			Direction offsetDirection = platform.getPlacementDirection(againstPos, againstState, context);
			placementPos = againstPos.offset(offsetDirection);
			if (context.shouldCancelInteraction()) {
				placementPos = placementPos.down();
				offsetDirection = offsetDirection.opposite;
			}
			return new AutomaticItemPlacementContext(context.world, placementPos, offsetDirection, context.stack, context.side);
		}
		else {
			return context;
		}
	}
}