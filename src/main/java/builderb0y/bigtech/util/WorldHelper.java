package builderb0y.bigtech.util;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import builderb0y.bigtech.BigTechMod;

public class WorldHelper {

	@SuppressWarnings("unchecked")
	public static <B extends BlockEntity> @Nullable B getBlockEntity(BlockView world, BlockPos pos, BlockEntityType<B> type) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null && blockEntity.type == type) {
			return (B)(blockEntity);
		}
		else {
			BigTechMod.LOGGER.warn("Expected to find ${type} at ${pos}, but got ${blockEntity} instead.");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <B> @Nullable B getBlockEntity(BlockView world, BlockPos pos, Class<B> type) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (type.isInstance(blockEntity)) {
			return (B)(blockEntity);
		}
		else {
			BigTechMod.LOGGER.warn("Expected to find ${type} at ${pos}, but got ${blockEntity} instead.");
			return null;
		}
	}
}