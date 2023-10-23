package builderb0y.bigtech.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class EncasedRedstoneBlock extends Block {

	public EncasedRedstoneBlock(Settings settings) {
		super(settings);
		this.defaultState = this.defaultState.with(Properties.FACING, Direction.UP);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.defaultState.with(Properties.FACING, context.playerLookDirection.opposite);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return direction == state.get(Properties.FACING).opposite ? 15 : 0;
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING);
	}
}