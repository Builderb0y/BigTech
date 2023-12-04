package builderb0y.bigtech.blocks.belts;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpeedyBeltBlock extends DirectionalBeltBlock {

	public SpeedyBeltBlock(Settings settings) {
		super(settings);
	}

	@Override
	public double getSpeed(World world, BlockPos pos, BlockState state, Entity entity) {
		return 2.0D;
	}
}