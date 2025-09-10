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
import builderb0y.bigtech.BigTechMod;

@Config(name = BigTechMod.MODID)
public class BigTechConfig {

	@Excluded
	public static Supplier<BigTechConfig> INSTANCE = ClothConfigCompat.init();

	public static void init() {}

	public void validatePostLoad() {
		this.client.validatePostLoad();
		this.server.validatePostLoad();
	}

	@DefaultIgnore
	@Tooltip(count = 3)
	@UseName("Block State Cycling")
	@Tooltips({
		"If true, the 'Cycle State' keybind + scrolling while holding a placeable item will modify the state of the block you're about to place, and shift clicking air with a block will revert to default logic.",
		"If false, default placement logic will always be used.",
		"Usually, the state modification consists of rotating the block in some way."
	})
	public boolean stateCycling = true;

	@DefaultIgnore
	@UseName("Client")
	@Tooltip(count = 1)
	@CollapsibleObject(startExpanded = true)
	@Tooltips({ "Config options that control client-sided behavior." })
	public final Client client = new Client();
	public static class Client {

		public void validatePostLoad() {}

		@DefaultIgnore
		@Tooltip(count = 3)
		@UseName("Placement Preview")
		@Tooltips({
			"If true, Big Tech will render a preview of the block you're about to place.",
			"If false, Big Tech will use the default block outline.",
			"Previewing is useful when cycling the state."
		})
		public boolean placementPreview = true;
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