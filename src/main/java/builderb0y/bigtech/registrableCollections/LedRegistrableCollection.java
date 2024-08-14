package builderb0y.bigtech.registrableCollections;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;

public abstract class LedRegistrableCollection<T> implements RegistrableCollection<T> {

	public final T
		white,
		orange,
		magenta,
		lightBlue,
		yellow,
		lime,
		pink,
		gray,
		lightGray,
		cyan,
		purple,
		blue,
		brown,
		green,
		red,
		black;

	public LedRegistrableCollection(
		boolean register,
		T white,
		T orange,
		T magenta,
		T lightBlue,
		T yellow,
		T lime,
		T pink,
		T gray,
		T lightGray,
		T cyan,
		T purple,
		T blue,
		T brown,
		T green,
		T red,
		T black
	) {
		this.white     = white;
		this.orange    = orange;
		this.magenta   = magenta;
		this.lightBlue = lightBlue;
		this.yellow    = yellow;
		this.lime      = lime;
		this.pink      = pink;
		this.gray      = gray;
		this.lightGray = lightGray;
		this.cyan      = cyan;
		this.purple    = purple;
		this.blue      = blue;
		this.brown     = brown;
		this.green     = green;
		this.red       = red;
		this.black     = black;

		if (register) this.register();
	}

	public LedRegistrableCollection(boolean register, LedFactory<T> factory) {
		this(
			register,
			factory.create(LedColor.WHITE),
			factory.create(LedColor.ORANGE),
			factory.create(LedColor.MAGENTA),
			factory.create(LedColor.LIGHT_BLUE),
			factory.create(LedColor.YELLOW),
			factory.create(LedColor.LIME),
			factory.create(LedColor.PINK),
			factory.create(LedColor.GRAY),
			factory.create(LedColor.LIGHT_GRAY),
			factory.create(LedColor.CYAN),
			factory.create(LedColor.PURPLE),
			factory.create(LedColor.BLUE),
			factory.create(LedColor.BROWN),
			factory.create(LedColor.GREEN),
			factory.create(LedColor.RED),
			factory.create(LedColor.BLACK)
		);
	}

	public abstract void register();

	public static interface LedFactory<T> {

		public abstract T create(LedColor color);
	}

	public T get(LedColor color) {
		return switch (color) {
			case WHITE      -> this.white;
			case ORANGE     -> this.orange;
			case MAGENTA    -> this.magenta;
			case LIGHT_BLUE -> this.lightBlue;
			case YELLOW     -> this.yellow;
			case LIME       -> this.lime;
			case PINK       -> this.pink;
			case GRAY       -> this.gray;
			case LIGHT_GRAY -> this.lightGray;
			case CYAN       -> this.cyan;
			case PURPLE     -> this.purple;
			case BLUE       -> this.blue;
			case BROWN      -> this.brown;
			case GREEN      -> this.green;
			case RED        -> this.red;
			case BLACK      -> this.black;
		};
	}

	@Override
	public List<T> asList() {
		return Arrays.asList(
			this.white,
			this.orange,
			this.magenta,
			this.lightBlue,
			this.yellow,
			this.lime,
			this.pink,
			this.gray,
			this.lightGray,
			this.cyan,
			this.purple,
			this.blue,
			this.brown,
			this.green,
			this.red,
			this.black
		);
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		action.accept(this.white);
		action.accept(this.orange);
		action.accept(this.magenta);
		action.accept(this.lightBlue);
		action.accept(this.yellow);
		action.accept(this.lime);
		action.accept(this.pink);
		action.accept(this.gray);
		action.accept(this.lightGray);
		action.accept(this.cyan);
		action.accept(this.purple);
		action.accept(this.blue);
		action.accept(this.brown);
		action.accept(this.green);
		action.accept(this.red);
		action.accept(this.black);
	}

	@Override
	public Spliterator<T> spliterator() {
		return Spliterators.spliterator(
			new Object[] {
				this.white,
				this.orange,
				this.magenta,
				this.lightBlue,
				this.yellow,
				this.lime,
				this.pink,
				this.gray,
				this.lightGray,
				this.cyan,
				this.purple,
				this.blue,
				this.brown,
				this.green,
				this.red,
				this.black
			},
			Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL
		);
	}

	@Override
	public boolean contains(T object) {
		return (
			this.white     == object ||
			this.orange    == object ||
			this.magenta   == object ||
			this.lightBlue == object ||
			this.yellow    == object ||
			this.lime      == object ||
			this.pink      == object ||
			this.gray      == object ||
			this.lightGray == object ||
			this.cyan      == object ||
			this.purple    == object ||
			this.blue      == object ||
			this.brown     == object ||
			this.green     == object ||
			this.red       == object ||
			this.black     == object
		);
	}

	@Override
	@SuppressWarnings("unchecked") //generic array creation.
	public RegistrableVariant<T>[] getRegistrableVariants() {
		return new RegistrableVariant[] {
			new RegistrableVariant<>(this.white,     LedColor.WHITE     ),
			new RegistrableVariant<>(this.orange,    LedColor.ORANGE    ),
			new RegistrableVariant<>(this.magenta,   LedColor.MAGENTA   ),
			new RegistrableVariant<>(this.lightBlue, LedColor.LIGHT_BLUE),
			new RegistrableVariant<>(this.yellow,    LedColor.YELLOW    ),
			new RegistrableVariant<>(this.lime,      LedColor.LIME      ),
			new RegistrableVariant<>(this.pink,      LedColor.PINK      ),
			new RegistrableVariant<>(this.gray,      LedColor.GRAY      ),
			new RegistrableVariant<>(this.lightGray, LedColor.LIGHT_GRAY),
			new RegistrableVariant<>(this.cyan,      LedColor.CYAN      ),
			new RegistrableVariant<>(this.purple,    LedColor.PURPLE    ),
			new RegistrableVariant<>(this.blue,      LedColor.BLUE      ),
			new RegistrableVariant<>(this.brown,     LedColor.BROWN     ),
			new RegistrableVariant<>(this.green,     LedColor.GREEN     ),
			new RegistrableVariant<>(this.red,       LedColor.RED       ),
			new RegistrableVariant<>(this.black,     LedColor.BLACK     )
		};
	}

	public static enum LedColor {
		WHITE     (1.0F,    1.0F,   1.0F,   0.75F,  0.75F,  0.75F ),
		ORANGE    (1.0F,    0.75F,  0.5F,   1.0F,   0.5F,   0.0F  ),
		MAGENTA   (1.0F,    0.5F,   1.0F,   0.75F,  0.25F,  0.75F ),
		LIGHT_BLUE(0.75F,   0.875F, 1.0F,   0.375F, 0.5F,   0.875F),
		YELLOW    (1.0F,    1.0F,   0.5F,   0.875F, 0.75F,  0.0F  ),
		LIME      (0.5F,    0.875F, 0.125F, 0.375F, 0.625F, 0.0F  ),
		PINK      (1.0F,    0.75F,  0.875F, 0.875F, 0.5F,   0.625F),
		GRAY      (0.5F,    0.5F,   0.5F,   0.25F,  0.25F,  0.25F ),
		LIGHT_GRAY(0.75F,   0.75F,  0.75F,  0.5F,   0.5F,   0.5F  ),
		CYAN      (0.25F,   0.625F, 0.75F,  0.0F,   0.375F, 0.5F  ),
		PURPLE    (0.75F,   0.5F,   1.0F,   0.5F,   0.0F,   1.0F  ),
		BLUE      (0.375F,  0.5F,   1.0F,   0.0F,   0.125F, 0.5F  ),
		BROWN     (0.75F,   0.5F,   0.375F, 0.5F,   0.25F,  0.125F),
		GREEN     (0.375F,  0.5F,   0.125F, 0.25F,  0.375F, 0.0F  ),
		RED       (1.0F,    0.375F, 0.375F, 0.75F,  0.0F,   0.0F  ),
		BLACK     (0.1875F, 0.125F, 0.25F,  0.125F, 0.0F,   0.25F );

		public static final LedColor[] VALUES = values();

		public final Vector3fc centerColor, edgeColor;

		LedColor(float centerRed, float centerGreen, float centerBlue, float edgeRed, float edgeGreen, float edgeBlue) {
			this.centerColor = new Vector3f(centerRed, centerGreen, centerBlue);
			this.edgeColor   = new Vector3f(  edgeRed,   edgeGreen,   edgeBlue);
		}

		public int startX(int on) {
			return ((this.ordinal() & 3) << 3) | (on << 2);
		}

		public int startY(int on) {
			int row = this.ordinal() >>> 2;
			return (row << 2) + (row << 1);
		}

		public DyeColor dyeColor() {
			return switch (this) {
				case WHITE      -> DyeColor.WHITE;
				case ORANGE     -> DyeColor.ORANGE;
				case MAGENTA    -> DyeColor.MAGENTA;
				case LIGHT_BLUE -> DyeColor.LIGHT_BLUE;
				case YELLOW     -> DyeColor.YELLOW;
				case LIME       -> DyeColor.LIME;
				case PINK       -> DyeColor.PINK;
				case GRAY       -> DyeColor.GRAY;
				case LIGHT_GRAY -> DyeColor.LIGHT_GRAY;
				case CYAN       -> DyeColor.CYAN;
				case PURPLE     -> DyeColor.PURPLE;
				case BLUE       -> DyeColor.BLUE;
				case BROWN      -> DyeColor.BROWN;
				case GREEN      -> DyeColor.GREEN;
				case RED        -> DyeColor.RED;
				case BLACK      -> DyeColor.BLACK;
			};
		}

		public TagKey<Item> dyeTag() {
			return switch (this) {
				case WHITE      -> ConventionalItemTags.WHITE_DYES;
				case ORANGE     -> ConventionalItemTags.ORANGE_DYES;
				case MAGENTA    -> ConventionalItemTags.MAGENTA_DYES;
				case LIGHT_BLUE -> ConventionalItemTags.LIGHT_BLUE_DYES;
				case YELLOW     -> ConventionalItemTags.YELLOW_DYES;
				case LIME       -> ConventionalItemTags.LIME_DYES;
				case PINK       -> ConventionalItemTags.PINK_DYES;
				case GRAY       -> ConventionalItemTags.GRAY_DYES;
				case LIGHT_GRAY -> ConventionalItemTags.LIGHT_GRAY_DYES;
				case CYAN       -> ConventionalItemTags.CYAN_DYES;
				case PURPLE     -> ConventionalItemTags.PURPLE_DYES;
				case BLUE       -> ConventionalItemTags.BLUE_DYES;
				case BROWN      -> ConventionalItemTags.BROWN_DYES;
				case GREEN      -> ConventionalItemTags.GREEN_DYES;
				case RED        -> ConventionalItemTags.RED_DYES;
				case BLACK      -> ConventionalItemTags.BLACK_DYES;
			};
		}
	}
}