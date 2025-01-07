package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.joml.Vector3d;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class FerromagneticAttractorBlock extends Block implements MagneticBlock {

	public static final MapCodec<FerromagneticAttractorBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public FerromagneticAttractorBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void attractEntity(
		World world,
		BlockPos pos,
		BlockState state,
		Entity entity,
		double dx,
		double dy,
		double dz,
		double force,
		Vector3d accumulatedVelocity
	) {
		double lengthSquared = dx * dx + dy * dy + dz * dz;
		if (lengthSquared > 0.0D) {
			double scalar = force / Math.sqrt(lengthSquared);
			accumulatedVelocity.add(dx * scalar, dy * scalar, dz * scalar);
		}
	}
}