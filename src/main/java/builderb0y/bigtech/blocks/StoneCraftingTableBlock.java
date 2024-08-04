package builderb0y.bigtech.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.StoneCraftingTableBlockEntity;
import builderb0y.bigtech.util.WorldHelper;

public class StoneCraftingTableBlock extends CraftingTableBlock implements BlockEntityProvider {

	public StoneCraftingTableBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.onPlaced(world, pos, state, placer, stack);
		if (stack.hasCustomName()) {
			StoneCraftingTableBlockEntity craftingTable = WorldHelper.getBlockEntity(world, pos, StoneCraftingTableBlockEntity.class);
			if (craftingTable != null) {
				craftingTable.customName = stack.getName();
			}
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.isOf(newState.getBlock())) {
			return;
		}
		StoneCraftingTableBlockEntity craftingTable = WorldHelper.getBlockEntity(world, pos, StoneCraftingTableBlockEntity.class);
		if (craftingTable != null) ItemScatterer.spawn(world, pos, craftingTable);
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new StoneCraftingTableBlockEntity(pos, state);
	}

	@Override
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return world.getBlockEntity(pos) instanceof NamedScreenHandlerFactory factory ? factory : null;
	}
}