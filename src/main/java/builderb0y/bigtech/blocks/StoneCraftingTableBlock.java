package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.StoneCraftingTableBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.WorldHelper;

public class StoneCraftingTableBlock extends CraftingTableBlock implements BlockEntityProvider {

	public static final MapCodec<StoneCraftingTableBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public StoneCraftingTableBlock(Settings settings) {
		super(settings);
	}

	@Override
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