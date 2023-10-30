package builderb0y.bigtech.registrableCollections;

import java.util.*;
import java.util.function.Consumer;

import net.minecraft.util.DyeColor;

public abstract class CrystalClusterRegistrableCollection<T> implements RegistrableCollection<T> {

	public final T
		red,
		yellow,
		green,
		cyan,
		blue,
		magenta,
		black,
		white;

	public CrystalClusterRegistrableCollection(
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

	public CrystalClusterRegistrableCollection(boolean register, CrystalClusterFactory<T> factory) {
		this(
			register,
			factory.create(CrystalClusterColor.RED),
			factory.create(CrystalClusterColor.YELLOW),
			factory.create(CrystalClusterColor.GREEN),
			factory.create(CrystalClusterColor.CYAN),
			factory.create(CrystalClusterColor.BLUE),
			factory.create(CrystalClusterColor.MAGENTA),
			factory.create(CrystalClusterColor.BLACK),
			factory.create(CrystalClusterColor.WHITE)
		);
	}

	public abstract void register();

	public static interface CrystalClusterFactory<T> {

		public abstract T create(CrystalClusterColor color);
	}

	public T get(CrystalClusterColor color) {
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
			new RegistrableVariant<>(this.red,     CrystalClusterColor.RED),
			new RegistrableVariant<>(this.yellow,  CrystalClusterColor.YELLOW),
			new RegistrableVariant<>(this.green,   CrystalClusterColor.GREEN),
			new RegistrableVariant<>(this.cyan,    CrystalClusterColor.CYAN),
			new RegistrableVariant<>(this.blue,    CrystalClusterColor.BLUE),
			new RegistrableVariant<>(this.magenta, CrystalClusterColor.MAGENTA),
			new RegistrableVariant<>(this.black,   CrystalClusterColor.BLACK),
			new RegistrableVariant<>(this.white,   CrystalClusterColor.WHITE),
		};
	}

	public static enum CrystalClusterColor {
		RED    (DyeColor.RED,     1.0F,  0.25F, 0.25F),
		YELLOW (DyeColor.YELLOW,  1.0F,  1.0F,  0.25F),
		GREEN  (DyeColor.LIME,    0.25F, 1.0F,  0.25F),
		CYAN   (DyeColor.CYAN,    0.25F, 1.0F,  1.0F ),
		BLUE   (DyeColor.BLUE,    0.25F, 0.25F, 1.0F ),
		MAGENTA(DyeColor.MAGENTA, 1.0F,  0.25F, 1.0F ),
		BLACK  (DyeColor.BLACK,   0.25F, 0.25F, 0.25F),
		WHITE  (DyeColor.WHITE,   1.0F,  1.0F,  1.0F );

		public static final CrystalClusterColor[] VALUES = values();

		public final DyeColor closestDyeColor;
		public final String prefix;
		public final float[] color;

		CrystalClusterColor(DyeColor dyeColor, float red, float green, float blue) {
			this.closestDyeColor = dyeColor;
			this.color = new float[] { red, green, blue };
			this.prefix = this.name().toLowerCase(Locale.ROOT) + '_';
		}
	}
}