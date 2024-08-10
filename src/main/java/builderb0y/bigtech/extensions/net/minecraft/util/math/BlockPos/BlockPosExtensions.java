package builderb0y.bigtech.extensions.net.minecraft.util.math.BlockPos;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.beams.base.BeamDirection;

@Extension
public class BlockPosExtensions {

	public static BlockPos offset(@This BlockPos thiz, BeamDirection direction) {
		return new BlockPos(thiz.getX() + direction.x, thiz.getY() + direction.y, thiz.getZ() + direction.z);
	}

	public static BlockPos offset(@This BlockPos thiz, BeamDirection direction, int distance) {
		return new BlockPos(thiz.getX() + direction.x * distance, thiz.getY() + direction.y * distance, thiz.getZ() + direction.z * distance);
	}
}