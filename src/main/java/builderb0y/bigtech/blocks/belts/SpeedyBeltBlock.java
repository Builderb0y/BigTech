package builderb0y.bigtech.blocks.belts;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class SpeedyBeltBlock extends DirectionalBeltBlock {

	public static final MapCodec<SpeedyBeltBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public SpeedyBeltBlock(Settings settings) {
		super(settings);
	}

	@Override
	public double getSpeed(World world, BlockPos pos, BlockState state, Entity entity) {
		return 2.0D;
	}
}