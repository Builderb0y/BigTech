package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BlockItem;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.DeployerBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.Inventories2.SlotStack;
import builderb0y.bigtech.util.WorldHelper;

public class ShortRangeDeployerBlock extends AbstractDestroyerBlock {

	public static final MapCodec<ShortRangeDeployerBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	public ShortRangeDeployerBlock(Settings settings) {
		super(settings);
	}

	@Override
	public MapCodec<? extends Block> getCodec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new DeployerBlockEntity(pos, state);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (world instanceof ServerWorld serverWorld && state.get(Properties.POWERED)) {
			serverWorld.addSyncedBlockEvent(pos, this, 0, 0);
		}
		super.onBlockAdded(state, world, pos, oldState, notify);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!newState.isOf(this)) {
			Inventory inventory = WorldHelper.getBlockEntity(world, pos, Inventory.class);
			if (inventory != null) ItemScatterer.spawn(world, pos, inventory);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		DeployerBlockEntity deployer;
		SlotStack slotStack;
		if (
			world instanceof ServerWorld serverWorld &&
			(deployer = WorldHelper.getBlockEntity(world, pos, DeployerBlockEntity.class)) != null &&
			(slotStack = deployer.getRandomSlotStack(world.random)) != null &&
			slotStack.stack().getItem() instanceof BlockItem blockItem
		) {
			Direction facing = state.get(Properties.HORIZONTAL_FACING);
			BlockPos placementPos = pos.offset(facing);
			AutomaticItemPlacementContext context = new AutomaticItemPlacementContext(
				serverWorld,
				placementPos,
				facing,
				slotStack.stack(),
				facing.getOpposite()
			);
			blockItem.place(context);
		}
		return false;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(WorldHelper.getBlockEntity(world, pos, Inventory.class));
	}
}