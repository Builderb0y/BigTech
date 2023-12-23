package builderb0y.bigtech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import builderb0y.bigtech.api.BeamInteractor;
import builderb0y.bigtech.beams.base.SpreadingBeamSegment;

public class PhaseManipulatorBlock extends Block implements BeamInteractor {

	public final boolean visible;

	public PhaseManipulatorBlock(Settings settings, boolean visible) {
		super(settings);
		this.visible = visible;
	}

	@Override
	public boolean spreadOut(SpreadingBeamSegment inputSegment, BlockState state) {
		inputSegment.beam.addSegment(inputSegment.withVisibility(this.visible).extend());
		return true;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}
}