package builderb0y.bigtech.blocks;

import java.util.Random;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class CrystallineSandBlock extends FallingBlock {

	public static final Random RANDOM = new Random();

	public static final MapCodec<CrystallineSandBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public CrystallineSandBlock(Settings settings) {
		super(settings);
	}

	@Override
	public int getColor(BlockState state, BlockView world, BlockPos pos) {
		return RANDOM.nextInt() | 0xFF000000;
	}
}