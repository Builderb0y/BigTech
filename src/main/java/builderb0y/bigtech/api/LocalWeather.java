package builderb0y.bigtech.api;

import java.util.Set;

import net.minecraft.world.World;

import builderb0y.bigtech.entities.StormCloudEntity;

public class LocalWeather {

	public static float getPrecipitationAt(World world, float partialTicks, double x, double y, double z) {
		return adjustPrecipitation(world, world.getRainGradient(partialTicks), x, y, z);
	}

	public static float adjustPrecipitation(World world, float precipitation, double x, double y, double z) {
		if (precipitation < 1.0F) {
			Set<StormCloudEntity> clouds = StormCloudEntity.ACTIVE.get(world);
			if (clouds != null && !clouds.isEmpty()) {
				for (StormCloudEntity cloud : clouds) {
					precipitation = cloud.accumulateRaininess(x, y, z, precipitation);
				}
			}
		}
		return precipitation;
	}
}