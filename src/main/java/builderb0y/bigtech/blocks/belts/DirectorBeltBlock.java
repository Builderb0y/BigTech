package builderb0y.bigtech.blocks.belts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DirectorBeltBlock extends RedstoneReceivingBeltBlock {

	public DirectorBeltBlock(Settings settings) {
		super(settings);
		this.defaultState = this.defaultState.with(Properties.INVERTED, Boolean.FALSE);
	}

	@Override
	public AscenderIOType getAscenderIOType(World world, BlockPos pos, BlockState state, Direction face) {
		if (face == Direction.UP) return AscenderIOType.SECONDARY_INPUT;
		if (face == Direction.DOWN) return AscenderIOType.NO_INPUT;
		Direction facing = state.get(Properties.FACING);
		if (face == facing.opposite) return AscenderIOType.PRIMARY_INPUT;
		return AscenderIOType.NO_INPUT;
	}

	@Override
	public Direction getDirection(World world, BlockPos pos, BlockState state, Entity entity) {
		Direction forward = state.get(Properties.HORIZONTAL_FACING);
		return state.get(Properties.POWERED) == state.get(Properties.INVERTED) ? forward.rotateYCounterclockwise() : forward.rotateYClockwise();
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			world.setBlockState(pos, state.with(Properties.INVERTED, !state.get(Properties.INVERTED)));
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.5F);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.INVERTED);
	}
}