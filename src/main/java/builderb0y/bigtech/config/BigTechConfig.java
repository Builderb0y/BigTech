package builderb0y.bigtech.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip;

import builderb0y.autocodec.annotations.Mirror;
import builderb0y.autocodec.annotations.UseName;
import builderb0y.autocodec.annotations.VerifyNullable;

@Config(name = "bigtech")
public class BigTechConfig {

	@Excluded
	public static Supplier<BigTechConfig> INSTANCE = ClothConfigCompat.init();

	public static void init() {}

	public void validatePostLoad() {
		this.client.validatePostLoad();
		this.server.validatePostLoad();
	}

	@DefaultIgnore
	@UseName("Client")
	@Tooltip(count = 1)
	@CollapsibleObject(startExpanded = true)
	@Tooltips({ "Config options that control client-sided behavior." })
	public final Client client = new Client();
	public static class Client {

		@DefaultIgnore
		@Tooltip(count = 3)
		@UseName("Replace Vanilla Lightning Renderer")
		@Tooltips({
			"If true, Big Tech will use its own custom renderer for vanilla lightning bolts.",
			"If false, Big Tech will use the vanilla renderer for vanilla lightning bolts.",
			"Changing this option requires a game restart."
		})
		public boolean replaceVanillaLightningRenderer = true;

		@DefaultIgnore
		@Tooltip(count = 3)
		@UseName("Lightning Renderer Quality")
		@Tooltips({
			"Higher quality will result in more subdivisions of lightning geometry.",
			"Lower quality will result in more straight edges in lightning geometry.",
			"WARNING: Every time you increase this number by 1, it TRIPLES the amount of lightning geometry."
		})
		public int lightningRendererQuality = 8;

		@DefaultIgnore
		@Tooltip(count = 3)
		@UseName("Tesla Coil Renderer Quality")
		@Tooltips({
			"Higher quality will result in more subdivisions of tesla coil geometry.",
			"Lower quality will result in more straight edges in tesla coil geometry.",
			"WARNING: Every time you increase this number by 1, it TRIPLES the amount of tesla coil geometry."
		})
		public int teslaCoilRendererQuality = 6;

		public void validatePostLoad() {
			if (this.lightningRendererQuality < 0 || this.lightningRendererQuality > 12) {
				throw new IllegalArgumentException("Lightning Renderer Quality must be between 0 and 12.");
			}
			if (this.teslaCoilRendererQuality < 0 || this.teslaCoilRendererQuality > 12) {
				throw new IllegalArgumentException("Tesla Coil Renderer Quality must be between 0 and 12.");
			}
		}
	}

	@DefaultIgnore
	@UseName("Server")
	@Tooltip(count = 1)
	@CollapsibleObject(startExpanded = true)
	@Tooltips({ "Config options that control server-sided behavior." })
	public final Server server = new Server();
	public static class Server {

		@DefaultIgnore
		@Tooltip(count = 1)
		@UseName("Minimum Lightning Energy")
		@Tooltips({ "Minimum amount of energy you can capture from a bolt of lightning." })
		public int minLightningEnergy = 25000;

		@DefaultIgnore
		@Tooltip(count = 1)
		@UseName("Maximum Lightning Energy")
		@Tooltips({ "Maximum amount of energy you can capture from a bolt of lightning." })
		public int maxLightningEnergy = 75000;

		public void validatePostLoad() {
			if (this.minLightningEnergy < 0) {
				throw new IllegalArgumentException("Minimum Lightning Energy must be greater than or equal to 0.");
			}
			if (this.maxLightningEnergy < this.minLightningEnergy) {
				throw new IllegalArgumentException("Maximum Lightning Energy must be greater than or equal to Minimum Lightning Energy.");
			}
		}
	}

	/**
	tricks AutoCodec into ignoring missing data and leaving
	the java object as it was after initialization,
	while simultaneously tricking intellij into
	*not* complaining that the object can be null.
	*/
	@VerifyNullable
	@Mirror(VerifyNullable.class)
	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface DefaultIgnore {}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Tooltips {

		public abstract String[] value();
	}
}