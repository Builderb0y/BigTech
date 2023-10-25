package builderb0y.bigtech.blocks.belts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class TrapdoorBeltBlock extends RedstoneReceivingBeltBlock {

	public TrapdoorBeltBlock(Settings settings) {
		super(settings);
		this.defaultState = this.defaultState.with(Properties.INVERTED, Boolean.FALSE);
	}

	@Override
	public boolean canMove(World world, BlockPos pos, BlockState state, Entity entity) {
		return state.get(Properties.POWERED) == state.get(Properties.INVERTED) && super.canMove(world, pos, state, entity);
	}

	@Override
	public void setPowered(World world, BlockPos pos, BlockState state, boolean powered) {
		super.setPowered(world, pos, state, powered);
		world.playSound(null, pos, powered == state.get(Properties.INVERTED) ? SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE : SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		boolean inverted;
		world.setBlockState(pos, state = state.with(Properties.INVERTED, inverted = !state.get(Properties.INVERTED)));
		world.playSound(null, pos, state.get(Properties.POWERED) == inverted ? SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE : SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS);
		return ActionResult.SUCCESS;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(Properties.WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return state;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return true;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return state.get(Properties.POWERED) == state.get(Properties.INVERTED) ? super.getCollisionShape(state, world, pos, context) : VoxelShapes.empty();
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.INVERTED);
	}
}