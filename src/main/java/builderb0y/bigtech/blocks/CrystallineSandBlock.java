package builderb0y.bigtech.blocks;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class CrystallineSandBlock extends FallingBlock {

	public static final Random RANDOM = new Random();

	public CrystallineSandBlock(Settings settings) {
		super(settings);
	}

	@Override
	public int getColor(BlockState state, BlockView world, BlockPos pos) {
		return RANDOM.nextInt() | 0xFF000000;
	}
}