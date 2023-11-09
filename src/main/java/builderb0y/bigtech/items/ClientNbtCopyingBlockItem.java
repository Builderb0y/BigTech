package builderb0y.bigtech.items;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
ctrl + pick block allows you to copy NBT data of block entities that occupy the block you picked.
BlockItem will restore that NBT data when you place the block back down again.
however, it will only restore the NBT data on the server.
this is an issue for beam interceptors, because their color depends on block entity data.
as such, they get left as white on placement.
granted, it is possible to force a sync afterwards, but even this requires a few
hacks to convince the client to redraw the block even though it didn't change.
additionally, the block still flickers white before getting the correct color.
so, this class will copy NBT data on the client too, allowing me to remove
those hacks AND have the block have the correct color from the start.
*/
public class ClientNbtCopyingBlockItem extends BlockItem {

	public ClientNbtCopyingBlockItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	public boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
		if (world.isClient) {
			NbtCompound storedData = BlockItem.getBlockEntityNbt(stack);
			if (storedData != null) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity != null) {
					NbtCompound data = blockEntity.createNbt();
					data.copyFrom(storedData);
					blockEntity.readNbt(data);
					return true;
				}
			}
		}
		return super.postPlacement(pos, world, player, stack, state);
	}
}