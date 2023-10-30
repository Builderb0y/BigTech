package builderb0y.bigtech.extensions.net.minecraft.util.math.random.Random;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import net.minecraft.util.math.random.Random;

@Extension
public class RandomExtensions {

	public static int nextInt(@This Random thiz, int min, int max) {
		return thiz.nextInt(max - min) + min;
	}

	public static float nextFloat(@This Random thiz, float max) {
		return thiz.nextFloat() * max;
	}

	public static float nextFloat(@This Random thiz, float min, float max) {
		return nextFloat(thiz, max - min) + min;
	}

	public static double nextDouble(@This Random thiz, double max) {
		return thiz.nextDouble() * max;
	}

	public static double nextDouble(@This Random thiz, double min, double max) {
		return nextDouble(thiz, max - min) + min;
	}
}