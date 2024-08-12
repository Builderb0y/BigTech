package builderb0y.bigtech.blocks.belts;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.Directions;

public class RedstoneReceivingBeltBlock extends DirectionalBeltBlock {

	public static final MapCodec<RedstoneReceivingBeltBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public RedstoneReceivingBeltBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.POWERED, Boolean.FALSE)
		);
	}

	/**
	doesn't actually emit redstone, but this method controls
	whether or not redstone dust will connect to this block.
	*/
	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	public boolean shouldBePowered(World world, BlockPos pos, BlockState state) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		if (world.getEmittedRedstonePower(mutable.set(pos, Direction.DOWN), Direction.DOWN) > 0) return true;
		for (Direction direction : Directions.HORIZONTAL) {
			if (world.getEmittedRedstonePower(mutable.set(pos, direction), direction) > 0) return true;
		}
		return false;
	}

	public boolean isPowered(World world, BlockPos pos, BlockState state) {
		return state.get(Properties.POWERED);
	}

	public void setPowered(World world, BlockPos pos, BlockState state, boolean powered) {
		world.setBlockState(pos, state.with(Properties.POWERED, powered));
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		BlockState state = super.getPlacementState(context);
		return state.with(Properties.POWERED, this.shouldBePowered(context.getWorld(), context.getBlockPos(), state));
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean moved) {
		super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, moved);
		boolean powered = this.isPowered(world, pos, state);
		boolean shouldBePowered = this.shouldBePowered(world, pos, state);
		if (powered != shouldBePowered) {
			this.setPowered(world, pos, state, shouldBePowered);
		}
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}
}