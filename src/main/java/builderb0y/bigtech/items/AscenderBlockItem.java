package builderb0y.bigtech.items;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.AscenderBlock;

public class AscenderBlockItem extends BlockItem {

	public AscenderBlockItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Nullable
	@Override
	public ItemPlacementContext getPlacementContext(ItemPlacementContext context) {
		if (!context.shouldCancelInteraction()) {
			World world = context.world;
			BlockPos.Mutable pos = context.blockPos.mutableCopy();
			Block expected = this.getBlock();
			BlockState actual = world.getBlockState(pos);
			if (actual.isOf(expected)) {
				Direction direction = ((AscenderBlock)(expected)).direction;
				while (true) {
					pos.move(direction);
					actual = world.getBlockState(pos);
					if (actual.isOf(expected)) {
						continue;
					}
					else {
						if (actual.canReplace(context)) {
							return ItemPlacementContext.offset(context, pos, direction);
						}
					}
				}
			}
		}
		return context;
	}
}