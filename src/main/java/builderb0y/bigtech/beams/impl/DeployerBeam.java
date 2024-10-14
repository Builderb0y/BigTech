package builderb0y.bigtech.beams.impl;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PulseBeam;
import builderb0y.bigtech.beams.base.SpreadingBeamSegment;
import builderb0y.bigtech.blockEntities.AbstractDeployerBlockEntity;
import builderb0y.bigtech.util.WorldHelper;

public class DeployerBeam extends PulseBeam {

	public static final Vector3fc COLOR = new Vector3f(0.03125F, 0.5F, 1.0F);

	public Set<BlockPos> placementPositions;

	public DeployerBeam(World world, UUID uuid) {
		super(world, uuid);
		this.placementPositions = new LinkedHashSet<>(4);
	}

	@Override
	public VoxelShape getShape(BlockPos pos, BlockState state) {
		return state.isAir() ? VoxelShapes.empty() : VoxelShapes.fullCube();
	}

	@Override
	public void handleIntersection(SpreadingBeamSegment segment, BlockState state, BlockHitResult hitResult) {
		super.handleIntersection(segment, state, hitResult);
		this.placementPositions.add(segment.startPos());
	}

	@Override
	public void onAdded() {
		super.onAdded();
		AbstractDeployerBlockEntity deployer = WorldHelper.getBlockEntity(this.world, this.origin, AbstractDeployerBlockEntity.class);
		if (deployer != null) {
			Direction facing = deployer.getCachedState().get(Properties.HORIZONTAL_FACING);
			for (BlockPos pos : this.placementPositions) {
				if (!deployer.deploy(pos, facing)) {
					break;
				}
			}
		}
	}

	@Override
	public BeamType getType() {
		return BeamTypes.DEPLOYER;
	}

	@Override
	public Vector3fc getInitialColor() {
		return COLOR;
	}
}