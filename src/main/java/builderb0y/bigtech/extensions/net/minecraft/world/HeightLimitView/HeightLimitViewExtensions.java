package builderb0y.bigtech.extensions.net.minecraft.world.HeightLimitView;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import net.minecraft.world.HeightLimitView;

@Extension
public class HeightLimitViewExtensions {

	public static int minY(@This HeightLimitView self) {
		return self.getBottomY();
	}

	public static int maxY(@This HeightLimitView self) {
		return self.getBottomY() + self.getHeight();
	}

	public static int height(@This HeightLimitView self) {
		return self.getHeight();
	}

	public static int sectionMinY(@This HeightLimitView self) {
		return self.minY() >> 4;
	}

	public static int sectionMaxY(@This HeightLimitView self) {
		return self.maxY() >> 4;
	}

	public static int sectionHeight(@This HeightLimitView self) {
		return self.height() >> 4;
	}
}