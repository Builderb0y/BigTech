package builderb0y.bigtech.util;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.api.EntityAddedToWorldEvent;

public class WorldHelper {

	public static <B extends BlockEntity> @Nullable B getBlockEntity(BlockView world, BlockPos pos, BlockEntityType<B> type) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null && blockEntity.getType() == type) {
			return blockEntity.as();
		}
		else {
			BigTechMod.LOGGER.warn("Expected to find ${type} at ${pos}, but got ${blockEntity} instead.");
			return null;
		}
	}

	public static <B> @Nullable B getBlockEntity(BlockView world, BlockPos pos, Class<B> type) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (type.isInstance(blockEntity)) {
			return blockEntity.as();
		}
		else {
			BigTechMod.LOGGER.warn("Expected to find ${type} at ${pos}, but got ${blockEntity} instead.");
			return null;
		}
	}

	public static final ThreadLocal<SpawnHandler> SPAWN_HANDLER = new ThreadLocal<>();

	static {
		EntityAddedToWorldEvent.EVENT.register((Entity entity) -> {
			SpawnHandler handler = SPAWN_HANDLER.get();
			return handler == null || handler.handleSpawn(entity);
		});
	}

	public static void breakBlockWithTool(ServerWorld world, BlockPos pos, BlockState state, Entity breaker, ItemStack tool) {
		//I have no idea why World.breakBlock() checks for this.
		if (!(state.getBlock() instanceof AbstractFireBlock)) {
			world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(state));
		}
		BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world, pos, blockEntity, breaker, tool);
		BlockState replacement = state.getFluidState().getBlockState();
		if (world.setBlockState(pos, replacement)) {
			world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(null, state));
		}
	}

	public static void runEntitySpawnAction(Runnable action, SpawnHandler handler) {
		SpawnHandler old = SPAWN_HANDLER.get();
		SPAWN_HANDLER.set(handler);
		try {
			action.run();
		}
		finally {
			SPAWN_HANDLER.set(old);
		}
	}

	@FunctionalInterface
	public static interface SpawnHandler {

		public abstract boolean handleSpawn(Entity entity);
	}
}