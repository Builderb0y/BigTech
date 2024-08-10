package builderb0y.bigtech.blocks.belts;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class InjectorBeltBlock extends DirectionalBeltBlock {

	public static final MapCodec<InjectorBeltBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public InjectorBeltBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void move(World world, BlockPos pos, BlockState state, Entity entity) {
		if (world.isClient || !this.tryStoreInBlocks(world, pos, state, entity)) {
			super.move(world, pos, state, entity);
		}
	}

	public boolean tryStoreInBlocks(World world, BlockPos pos, BlockState state, Entity entity) {
		if (entity instanceof ItemEntity item) {
			Direction forward = this.getDirection(world, pos, state, entity);
			Direction left = forward.rotateYCounterclockwise();
			Direction right = forward.rotateYClockwise();
			//if anyone else knows a good way to try these 3 directions in a random
			//order WITHOUT allocating an array and shuffling it, please let me know.
			return switch (world.random.nextInt(6)) {
				case 0 -> this.tryStoreInBlocks(world, pos, item, forward, left, right);
				case 1 -> this.tryStoreInBlocks(world, pos, item, forward, right, left);
				case 2 -> this.tryStoreInBlocks(world, pos, item, left, forward, right);
				case 3 -> this.tryStoreInBlocks(world, pos, item, left, right, forward);
				case 4 -> this.tryStoreInBlocks(world, pos, item, right, forward, left);
				case 5 -> this.tryStoreInBlocks(world, pos, item, right, left, forward);
				default -> false;
			};
		}
		return false;
	}

	public boolean tryStoreInBlocks(World world, BlockPos pos, ItemEntity item, Direction first, Direction second, Direction third) {
		if (!item.getStack().isEmpty()) this.tryStoreInBlocks(world, pos, item, first );
		if (!item.getStack().isEmpty()) this.tryStoreInBlocks(world, pos, item, second);
		if (!item.getStack().isEmpty()) this.tryStoreInBlocks(world, pos, item, third );
		return item.getStack().isEmpty();
	}

	@SuppressWarnings("UnstableApiUsage")
	public void tryStoreInBlocks(World world, BlockPos pos, ItemEntity item, Direction direction) {
		BlockPos adjacentPos = pos.offset(direction);
		Storage<ItemVariant> storage = ItemStorage.SIDED.find(world, adjacentPos, direction.getOpposite());
		if (storage != null) {
			try (Transaction transaction = Transaction.openOuter()) {
				ItemStack stack = item.getStack();
				int inserted = (int)(storage.insert(ItemVariant.of(stack), stack.getCount(), transaction));
				if (inserted > 0) {
					item.setStack(stack = stack.copyWithCount(stack.getCount() - inserted));
					if (stack.isEmpty()) item.discard();
					transaction.commit();
					world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 0.25F);
				}
			}
		}
	}
}