package builderb0y.bigtech.blocks.belts;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.AscenderInteractor;

public class BrakeBeltBlock extends RedstoneReceivingBeltBlock {

	public BrakeBeltBlock(Settings settings) {
		super(settings);
	}

	@Override
	public int getAscenderPriority(World world, BlockPos pos, BlockState state, Direction face) {
		if (state.get(Properties.POWERED)) return AscenderInteractor.BLOCKED;
		return super.getAscenderPriority(world, pos, state, face);
	}

	@Override
	public double getSpeed(World world, BlockPos pos, BlockState state, Entity entity) {
		return 0.25D;
	}

	@Override
	public boolean canMove(World world, BlockPos pos, BlockState state, Entity entity) {
		return !state.get(Properties.POWERED) && super.canMove(world, pos, state, entity);
	}
}