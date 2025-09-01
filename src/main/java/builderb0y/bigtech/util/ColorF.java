package builderb0y.bigtech.util;

import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.util.math.ColorHelper;

public class ColorF {

	public static int toInt(float red, float green, float blue) {
		return ColorHelper.getArgb(
			(int)(red * 255.0F + 0.5F),
			(int)(green * 255.0F + 0.5F),
			(int)(blue * 255.0F + 0.5F)
		);
	}

	public static int toInt(Vector3fc color) {
		return toInt(color.x(), color.y(), color.z());
	}

	public static float red(int color) {
		return ColorHelper.getRed(color) / 255.0F;
	}

	public static float green(int color) {
		return ColorHelper.getGreen(color) / 255.0F;
	}

	public static float blue(int color) {
		return ColorHelper.getBlue(color) / 255.0F;
	}

	public static Vector3f toVector(int color, Vector3f destination) {
		return destination.set(red(color), green(color), blue(color));
	}

	public static Vector3f toVector(int color) {
		return toVector(color, new Vector3f());
	}
}