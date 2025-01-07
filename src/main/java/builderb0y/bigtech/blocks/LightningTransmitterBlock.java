package builderb0y.bigtech.blocks;

import java.util.UUID;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.impl.LightningBeam;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;

public class LightningTransmitterBlock extends BeamBlock implements LightningPulseInteractor {

	public static final MapCodec<LightningTransmitterBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public LightningTransmitterBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.POWERED, Boolean.FALSE)
		);
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return false;
	}

	@Override
	public void spreadOut(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		Direction facing = state.get(Properties.HORIZONTAL_FACING);
		float resistance = this.getResistance(world, pos, state, facing);
		if (!(resistance <= pos.distanceRemaining)) return;
		LightningBeam beam = new LightningBeam(world, UUID.randomUUID());
		beam.pulse = pulse;
		beam.prepare(world, pos, BeamDirection.from(facing), pos.distanceRemaining - resistance);
		beam.fire(world);
		pulse.lightningBeams.put(pos, beam);
	}

	@Override
	public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		LightningBeam beam = pulse.lightningBeams.get(pos);
		if (beam != null) beam.addToWorld(world);
		if (!state.get(Properties.POWERED)) {
			world.setBlockState(pos, state.with(Properties.POWERED, Boolean.TRUE));
		}
		world.scheduleBlockTick(new BlockPos(pos), this, 4);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		if (state.get(Properties.POWERED)) {
			world.setBlockState(pos, state.with(Properties.POWERED, Boolean.FALSE));
		}
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}
}