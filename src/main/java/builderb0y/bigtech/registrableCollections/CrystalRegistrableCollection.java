package builderb0y.bigtech.registrableCollections;

import java.util.*;
import java.util.function.Consumer;

import org.joml.Vector3f;

import net.minecraft.util.DyeColor;

import builderb0y.bigtech.api.BeaconBeamColorProvider;

public abstract class CrystalRegistrableCollection<T> implements RegistrableCollection<T> {

	public final T
		red,
		yellow,
		green,
		cyan,
		blue,
		magenta,
		black,
		white;

	public CrystalRegistrableCollection(
		boolean register,
		T red,
		T yellow,
		T green,
		T cyan,
		T blue,
		T magenta,
		T black,
		T white
	) {
		this.red     = red;
		this.yellow  = yellow;
		this.green   = green;
		this.cyan    = cyan;
		this.blue    = blue;
		this.magenta = magenta;
		this.black   = black;
		this.white   = white;

		if (register) this.register();
	}

	public CrystalRegistrableCollection(boolean register, CrystalClusterFactory<T> factory) {
		this(
			register,
			factory.create(CrystalColor.RED),
			factory.create(CrystalColor.YELLOW),
			factory.create(CrystalColor.GREEN),
			factory.create(CrystalColor.CYAN),
			factory.create(CrystalColor.BLUE),
			factory.create(CrystalColor.MAGENTA),
			factory.create(CrystalColor.BLACK),
			factory.create(CrystalColor.WHITE)
		);
	}

	public abstract void register();

	public static interface CrystalClusterFactory<T> {

		public abstract T create(CrystalColor color);
	}

	public T get(CrystalColor color) {
		return switch (color) {
			case RED     -> this.red;
			case YELLOW  -> this.yellow;
			case GREEN   -> this.green;
			case CYAN    -> this.cyan;
			case BLUE    -> this.blue;
			case MAGENTA -> this.magenta;
			case BLACK   -> this.black;
			case WHITE   -> this.white;
		};
	}

	@Override
	public List<T> asList() {
		return Arrays.asList(
			this.red,
			this.yellow,
			this.green,
			this.cyan,
			this.blue,
			this.magenta,
			this.black,
			this.white
		);
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		action.accept(this.red);
		action.accept(this.yellow);
		action.accept(this.green);
		action.accept(this.cyan);
		action.accept(this.blue);
		action.accept(this.magenta);
		action.accept(this.black);
		action.accept(this.white);
	}

	@Override
	public Spliterator<T> spliterator() {
		return Spliterators.spliterator(
			new Object[] {
				this.red,
				this.yellow,
				this.green,
				this.cyan,
				this.blue,
				this.magenta,
				this.black,
				this.white
			},
			Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL
		);
	}

	@Override
	public boolean contains(T object) {
		return (
			this.red     == object ||
			this.yellow  == object ||
			this.green   == object ||
			this.cyan    == object ||
			this.blue    == object ||
			this.magenta == object ||
			this.black   == object ||
			this.white   == object
		);
	}

	@Override
	@SuppressWarnings("unchecked") //generic array creation.
	public RegistrableVariant<T>[] getRegistrableVariants() {
		return new RegistrableVariant[] {
			new RegistrableVariant<>(this.red,     CrystalColor.RED),
			new RegistrableVariant<>(this.yellow,  CrystalColor.YELLOW),
			new RegistrableVariant<>(this.green,   CrystalColor.GREEN),
			new RegistrableVariant<>(this.cyan,    CrystalColor.CYAN),
			new RegistrableVariant<>(this.blue,    CrystalColor.BLUE),
			new RegistrableVariant<>(this.magenta, CrystalColor.MAGENTA),
			new RegistrableVariant<>(this.black,   CrystalColor.BLACK),
			new RegistrableVariant<>(this.white,   CrystalColor.WHITE),
		};
	}

	public static enum CrystalColor {
		RED    (DyeColor.RED,     255,  63,  63),
		YELLOW (DyeColor.YELLOW,  255, 255,  63),
		GREEN  (DyeColor.LIME,     63, 255,  63),
		CYAN   (DyeColor.CYAN,     63, 255, 255),
		BLUE   (DyeColor.BLUE,     63,  63, 255),
		MAGENTA(DyeColor.MAGENTA, 255,  63, 255),
		BLACK  (DyeColor.BLACK,    63,  63,  63),
		WHITE  (DyeColor.WHITE,   255, 255, 255);

		public static final CrystalColor[] VALUES = values();

		public final DyeColor closestDyeColor;
		public final String prefix;
		public final int red, green, blue;
		public final int color;
		public final Vector3f colorVector;

		CrystalColor(DyeColor dyeColor, int red, int green, int blue) {
			this.closestDyeColor = dyeColor;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.color = BeaconBeamColorProvider.packRGB(red, green, blue);
			this.colorVector = new Vector3f(red, green, blue).div(255.0F);
			this.prefix = this.name().toLowerCase(Locale.ROOT) + '_';
		}
	}
}