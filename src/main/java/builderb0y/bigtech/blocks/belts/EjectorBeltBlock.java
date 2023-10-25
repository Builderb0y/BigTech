package builderb0y.bigtech.blocks.belts;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EjectorBeltBlock extends RedstoneReceivingBeltBlock {

	public EjectorBeltBlock(Settings settings) {
		super(settings);
	}

	@Override
	@SuppressWarnings("UnstableApiUsage")
	public void setPowered(World world, BlockPos pos, BlockState state, boolean powered) {
		super.setPowered(world, pos, state, powered);
		if (powered) {
			Direction forward = state.get(Properties.HORIZONTAL_FACING);
			Direction backward = forward.opposite;
			Storage<ItemVariant> storage = ItemStorage.SIDED.find(world, pos.offset(backward), forward);
			if (storage != null) {
				try (Transaction transaction = Transaction.openOuter()) {
					ResourceAmount<ItemVariant> extracted = StorageUtil.extractAny(storage, 1L, transaction);
					if (extracted != null && extracted.amount > 0) {
						transaction.commit();
						ItemEntity itemEntity = new ItemEntity(
							world,
							pos.x + 0.5D + backward.offsetX * (0.5D - EntityType.ITEM.width * 0.5D),
							pos.y + 0.25D,
							pos.z + 0.5D + backward.offsetZ * (0.5D - EntityType.ITEM.width * 0.5D),
							extracted.resource.toStack((int)(extracted.amount)),
							forward.offsetX * 0.125D,
							0.0D,
							forward.offsetZ * 0.125D
						);
						world.spawnEntity(itemEntity);
					}
				}
			}
		}
	}
}