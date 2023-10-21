package builderb0y.bigtech.items;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BeltBlockItem extends BlockItem {

	public BeltBlockItem(Block block, Settings settings) {
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
				Direction direction = actual.get(Properties.HORIZONTAL_FACING);
				while (true) {
					pos.move(direction);
					actual = world.getBlockState(pos);
					if (actual.isOf(expected)) {
						if (actual.get(Properties.HORIZONTAL_FACING) == direction) {
							continue;
						}
						else {
							return null;
						}
					}
					else {
						if (actual.canReplace(context)) {
							return new AutomaticItemPlacementContext(world, pos.toImmutable(), direction, context.getStack(), context.getSide());
						}
						else {
							return null;
						}
					}
				}
			}
		}
		return context;
	}
}