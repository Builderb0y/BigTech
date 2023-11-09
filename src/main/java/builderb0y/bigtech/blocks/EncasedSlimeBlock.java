package builderb0y.bigtech.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.api.PistonInteractor;

public class EncasedSlimeBlock extends Block implements PistonInteractor {

	public final boolean isHoney;

	public EncasedSlimeBlock(Settings settings, boolean honey) {
		super(settings);
		this.isHoney = honey;
		this.defaultState = this.defaultState.with(Properties.FACING, Direction.UP);
	}

	@Override
	public boolean isSticky(PistonHandlerInfo handler, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean canStickTo(
		PistonHandlerInfo handler,
		BlockPos selfPos,
		BlockState selfState,
		BlockPos otherPos,
		BlockState otherState,
		Direction face
	) {
		if (this.isHoney) {
			if (otherState.isOf(Blocks.SLIME_BLOCK)) return false;
		}
		else {
			if (otherState.isOf(Blocks.HONEY_BLOCK)) return false;
		}
		return face == selfState.get(Properties.FACING);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.defaultState.with(Properties.FACING, context.playerLookDirection.opposite);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(Properties.HORIZONTAL_FACING, mirror.apply(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING);
	}
}