package builderb0y.bigtech.blocks.belts;

import com.mojang.serialization.MapCodec;
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

import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class EjectorBeltBlock extends RedstoneReceivingBeltBlock {

	public static final MapCodec<EjectorBeltBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public EjectorBeltBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void setPowered(World world, BlockPos pos, BlockState state, boolean powered) {
		super.setPowered(world, pos, state, powered);
		if (powered) {
			Direction forward = state.get(Properties.HORIZONTAL_FACING);
			Direction backward = forward.getOpposite();
			Storage<ItemVariant> storage = ItemStorage.SIDED.find(world, pos.offset(backward), forward);
			if (storage != null) {
				try (Transaction transaction = Transaction.openOuter()) {
					ResourceAmount<ItemVariant> extracted = StorageUtil.extractAny(storage, 1L, transaction);
					if (extracted != null && extracted.amount() == 1L) {
						transaction.commit();
						ItemEntity itemEntity = new ItemEntity(
							world,
							pos.getX() + 0.5D + backward.getOffsetX() * (0.5D - EntityType.ITEM.getWidth() * 0.5D),
							pos.getY() + 0.25D,
							pos.getZ() + 0.5D + backward.getOffsetZ() * (0.5D - EntityType.ITEM.getWidth() * 0.5D),
							extracted.resource().toStack(1),
							forward.getOffsetX() * 0.125D,
							0.0D,
							forward.getOffsetZ() * 0.125D
						);
						world.spawnEntity(itemEntity);
					}
				}
			}
		}
	}
}