package builderb0y.bigtech.blocks.belts;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import builderb0y.bigtech.api.AscenderInteractor;

public class LauncherBeltBlock extends AbstractBeltBlock {

	public LauncherBeltBlock(Settings settings) {
		super(settings);
	}

	@Override
	public int getAscenderPriority(World world, BlockPos pos, BlockState state, Direction face) {
		if (face == Direction.UP) return AscenderInteractor.BELT_TOP;
		if (face == Direction.DOWN) return AscenderInteractor.BLOCKED;
		return AscenderInteractor.BELT_BACK;
	}

	@Override
	public void move(World world, BlockPos pos, BlockState state, Entity entity) {
		Vec3d oldMotion = entity.velocity;
		double newX = oldMotion.x, newZ = oldMotion.z;
		//move towards the center of the block.
		newX += (pos.x + 0.5D - entity.x) * 0.25D;
		newZ += (pos.z + 0.5D - entity.z) * 0.25D;
		entity.velocity = new Vec3d(newX, 0.8D, newZ);
		entity.isOnGround = false; //hack for XP orbs.
	}
}