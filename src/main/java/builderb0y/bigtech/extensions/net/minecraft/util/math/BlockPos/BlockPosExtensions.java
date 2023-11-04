package builderb0y.bigtech.extensions.net.minecraft.util.math.BlockPos;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.beams.BeamDirection;

@Extension
public class BlockPosExtensions {

	public static BlockPos offset(@This BlockPos thiz, BeamDirection direction) {
		return new BlockPos(thiz.x + direction.x, thiz.y + direction.y, thiz.z + direction.z);
	}

	public static BlockPos offset(@This BlockPos thiz, BeamDirection direction, int distance) {
		return new BlockPos(thiz.x + direction.x * distance, thiz.y + direction.y * distance, thiz.z + direction.z * distance);
	}
}