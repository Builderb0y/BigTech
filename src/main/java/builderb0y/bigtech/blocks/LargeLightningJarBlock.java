package builderb0y.bigtech.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class LargeLightningJarBlock extends AbstractLightningJarBlock {

	public static final VoxelShape SHAPE = VoxelShapes.union(
		VoxelShapes.cuboid(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D),
		VoxelShapes.cuboid(0.0625D, 0.1875D, 0.0625D, 0.9375D, 0.9375D, 0.9375D)
	);

	public LargeLightningJarBlock(Settings settings) {
		super(settings);
	}

	@Override
	public int getCapacity() {
		return 6000;
	}

	@Override
	public int getPulseSteps() {
		return 32;
	}

	@Override
	@Deprecated
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
}