package builderb0y.bigtech.datagen.formats;

import java.util.*;
import java.util.stream.Stream;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.datagen.tables.ColumnFormatter;
import builderb0y.bigtech.datagen.tables.Justification;
import builderb0y.bigtech.datagen.tables.TableFormat;
import builderb0y.bigtech.util.NumericStringComparator;

public class TableFormats {

	public static record BlockStateJsonVariant(String properties, String model, Integer x, Integer y) {

		public static final Direction[] FACING_ORDER = {
			Direction.UP,
			Direction.DOWN,
			Direction.NORTH,
			Direction.EAST,
			Direction.SOUTH,
			Direction.WEST
		};

		public static final Direction[] HORIZONTAL_FACING_ORDER = {
			Direction.NORTH,
			Direction.EAST,
			Direction.SOUTH,
			Direction.WEST
		};

		public static final TableFormat<BlockStateJsonVariant> FORMAT = (
			new TableFormat<BlockStateJsonVariant>()
			.prefix(
				"""
				{
					"variants": {
				"""
			)
			.addLiteral("\t\t")
			.addJoined(": ", format -> format
				.addField(Justification.left(), variant -> ColumnFormatter.quoteAndEscape(variant.properties))
				.addLiteral("{ ")
			)
			.addJoined(", ", format -> format
				.addJsonString("model", Justification.left(), BlockStateJsonVariant::model)
				.addJsonNumber("x", BlockStateJsonVariant::x)
				.addJsonNumber("y", BlockStateJsonVariant::y)
			)
			.addLiteral(" }")
			.addLineDeliminator(",")
			.suffix("\n\t}\n}")
		);

		public BlockStateJsonVariant(BlockState state, String model, Integer x, Integer y) {
			this(decodeState(state), model, x, y);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public static String decodeState(BlockState state) {
			StringBuilder builder = new StringBuilder(32);
			List<Property<?>> properties = new ArrayList<>(state.getProperties());
			properties.sort(Comparator.comparing(Property::getName));
			for (Property property : properties) {
				if (property != Properties.WATERLOGGED) {
					builder.append(property.name).append('=').append(property.name(state.get(property))).append(',');
				}
			}
			builder.length = Math.max(builder.length() - 1, 0);
			return builder.toString();
		}

		public static @Nullable Integer yFromNorth(Direction direction) {
			return switch (direction) {
				case NORTH -> 0;
				case EAST  -> 90;
				case SOUTH -> 180;
				case WEST  -> 270;
				case UP, DOWN -> null;
			};
		}

		public static Integer xFromUp(Direction direction) {
			return switch (direction) {
				case UP -> 0;
				case DOWN -> 180;
				case NORTH, EAST, SOUTH, WEST -> 90;
			};
		}

		public static Stream<BlockState> streamStatesSorted(Block block) {
			ArrayList<Property<?>> properties = new ArrayList<>(block.getStateManager().getProperties());
			properties.remove(Properties.WATERLOGGED);
			if (properties.isEmpty) return Stream.of(block.getDefaultState().withIfExists(Properties.WATERLOGGED, Boolean.FALSE));
			properties.sort(Comparator.comparing(Property::getName));
			Comparator<BlockState> comparator = comparator(properties.get(0));
			for (int index = 1, size = properties.size(); index < size; index++) {
				comparator = comparator.thenComparing(comparator(properties.get(index)));
			}
			return block.getStateManager().getStates().stream().filter(state -> !state.contains(Properties.WATERLOGGED) || !state.get(Properties.WATERLOGGED)).sorted(comparator);
		}

		public static <C extends Comparable<C>> Comparator<BlockState> comparator(Property<C> property) {
			if (property.type == Direction.class) {
				return Comparator.comparingInt(
					state -> switch (state.get(property).<Direction>as()) {
						case UP    -> 0;
						case DOWN  -> 1;
						case NORTH -> 2;
						case EAST  -> 3;
						case SOUTH -> 4;
						case WEST  -> 5;
					}
				);
			}
			else if (property.type == int.class || property.type == Integer.class) {
				return Comparator.comparingInt(
					state -> state.get(property).as()
				);
			}
			else {
				return Comparator.comparing(
					state -> property.name(state.get(property)),
					NumericStringComparator.INSTANCE
				);
			}
		}
	}

	public static record BlockStateJsonMultipart(String predicateName, String predicateValue, String model, Integer x, Integer y) {

		public static final TableFormat<BlockStateJsonMultipart> FORMAT = (
			new TableFormat<BlockStateJsonMultipart>()
			.prefix(
				"""
				{
					"multipart": [
				"""
			)
			.addLiteral("\t\t{ ")
			.addLiteral("\"when\": { ", multipart -> multipart.predicateName != null)
			.addJsonString(BlockStateJsonMultipart::predicateName, Justification.left(), BlockStateJsonMultipart::predicateValue, Justification.left())
			.addLiteral(" }, ", multipart -> multipart.predicateName != null)
			.addLiteral("\"apply\": { ")
			.addJoined(", ", format -> format
				.addJsonString("model", Justification.left(), BlockStateJsonMultipart::model)
				.addJsonNumber("x", BlockStateJsonMultipart::x)
				.addJsonNumber("y", BlockStateJsonMultipart::y)
			)
			.addLiteral(" } }")
			.addLineDeliminator(",")
			.suffix("\n\t]\n}")
		);
	}

	public static record BlockStateJsonMultipart2(
		String predicateName1,
		String predicateValue1,
		String predicateName2,
		String predicateValue2,
		String model,
		Integer x,
		Integer y
	) {

		public static final TableFormat<BlockStateJsonMultipart2> FORMAT = (
			new TableFormat<BlockStateJsonMultipart2>()
			.prefix(
				"""
				{
					"multipart": [
				"""
			)
			.addLiteral("\t\t{ ")
			.addLiteral("\"when\": { ", multipart -> multipart.predicateName1 != null || multipart.predicateName2 != null)
			.addJoined(", ", format -> format
				.addJsonString(BlockStateJsonMultipart2::predicateName1, Justification.left(), BlockStateJsonMultipart2::predicateValue1, Justification.left())
				.addJsonString(BlockStateJsonMultipart2::predicateName2, Justification.left(), BlockStateJsonMultipart2::predicateValue2, Justification.left())
			)
			.addLiteral(" }, ", multipart -> multipart.predicateName1 != null || multipart.predicateName2 != null)
			.addLiteral("\"apply\": { ")
			.addJoined(", ", format -> format
				.addJsonString("model", Justification.left(), BlockStateJsonMultipart2::model)
				.addJsonNumber("x", BlockStateJsonMultipart2::x)
				.addJsonNumber("y", BlockStateJsonMultipart2::y)
			)
			.addLiteral(" } }")
			.addLineDeliminator(",")
			.suffix("\n\t]\n}")
		);
	}

	public static record BlockStateJsonMultipart3(
		String predicateName1,
		String predicateValue1,
		String predicateName2,
		String predicateValue2,
		String predicateName3,
		String predicateValue3,
		String model,
		Integer x,
		Integer y
	) {

		public static final TableFormat<BlockStateJsonMultipart3> FORMAT = (
			new TableFormat<BlockStateJsonMultipart3>()
			.prefix(
				"""
				{
					"multipart": [
				"""
			)
			.addLiteral("\t\t{ ")
			.addLiteral("\"when\": { ", multipart -> multipart.predicateName1 != null || multipart.predicateName2 != null || multipart.predicateName3 != null)
			.addJoined(", ", format -> format
				.addJsonString(BlockStateJsonMultipart3::predicateName1, Justification.left(), BlockStateJsonMultipart3::predicateValue1, Justification.left())
				.addJsonString(BlockStateJsonMultipart3::predicateName2, Justification.left(), BlockStateJsonMultipart3::predicateValue2, Justification.left())
				.addJsonString(BlockStateJsonMultipart3::predicateName3, Justification.left(), BlockStateJsonMultipart3::predicateValue3, Justification.left())
			)
			.addLiteral(" }, ", multipart -> multipart.predicateName1 != null || multipart.predicateName2 != null || multipart.predicateName3 != null)
			.addLiteral("\"apply\": { ")
			.addJoined(", ", format -> format
				.addJsonString("model", Justification.left(), BlockStateJsonMultipart3::model)
				.addJsonNumber("x", BlockStateJsonMultipart3::x)
				.addJsonNumber("y", BlockStateJsonMultipart3::y)
			)
			.addLiteral(" } }")
			.addLineDeliminator(",")
			.suffix("\n\t]\n}")
		);
	}

	public static record LangEntry(String key, String value) {

		public static final TableFormat<LangEntry> FORMAT = (
			new TableFormat<LangEntry>()
			.prefix("{\n")
			.addLiteral("\t")
			.addJoined(": ", format -> format
				.addField(Justification.left(), lang -> ColumnFormatter.quoteAndEscape(lang.key))
				.addField(Justification.none(), lang -> ColumnFormatter.quoteAndEscape(lang.value))
			)
			.addLineDeliminator(",")
			.suffix("\n}")
		);

		public LangEntry(Map.Entry<String, String> entry) {
			this(entry.key, entry.value);
		}
	}

	public static record RetextureEntry(String key, String value) {

		public static final TableFormat<RetextureEntry> FORMAT = (
			new TableFormat<RetextureEntry>()
			.addLiteral("\t\t")
			.addJoined(": ", format -> format
				.addField(Justification.left(), lang -> ColumnFormatter.quoteAndEscape(lang.key))
				.addField(Justification.none(), lang -> ColumnFormatter.quoteAndEscape(lang.value))
			)
			.addLineDeliminator(",")
		);

		public RetextureEntry(Map.Entry<String, String> entry) {
			this(entry.key, entry.value);
		}
	}

	public static record TagElement(String name) {

		public static final TableFormat<TagElement> FORMAT = (
			new TableFormat<TagElement>()
			.prefix(
				"""
				{
					"replace": false,
					"values": [
				"""
			)
			.addLiteral("\t\t")
			.addField(Justification.none(), element -> ColumnFormatter.quoteAndEscape(element.name))
			.addLineDeliminator(",")
			.suffix("\n\t]\n}")
		);

		public TagElement(TagOrItem tagOrItem) {
			this(tagOrItem.toString());
		}
	}

	public static record KeyedRecipeIngredient(String key, boolean isTag, String id) {

		public static final TableFormat<KeyedRecipeIngredient> FORMAT = (
			new TableFormat<KeyedRecipeIngredient>()
			.addLiteral("\t\t")
			.addField(Justification.none(), ingredient -> ColumnFormatter.quoteAndEscape(ingredient.key))
			.addLiteral(": { ")
			.addJoined(": ", format -> format
				.addField(Justification.left(), ingredient -> ingredient.isTag ? "\"tag\"" : "\"item\"")
				.addField(Justification.left(), ingredient -> ColumnFormatter.quoteAndEscape(ingredient.id))
			)
			.addLiteral(" }")
			.addLineDeliminator(",")
		);

		public static KeyedRecipeIngredient create(char key, String string) {
			return (
				!string.isEmpty && string.charAt(0) == '#'
				? new KeyedRecipeIngredient(String.valueOf(key), true, string.substring(1))
				: new KeyedRecipeIngredient(String.valueOf(key), false, string)
			);
		}

		public static KeyedRecipeIngredient create(Char2ObjectMap.Entry<String> entry) {
			return create(entry.key, entry.value);
		}
	}

	public static record UnkeyedRecipeIngredient(boolean isTag, String id) {

		public static final TableFormat<UnkeyedRecipeIngredient> FORMAT = (
			new TableFormat<UnkeyedRecipeIngredient>()
			.addLiteral("\t\t{ ")
			.addJoined(": ", format -> format
				.addField(Justification.left(), ingredient -> ingredient.isTag ? "\"tag\"" : "\"item\"")
				.addField(Justification.left(), ingredient -> ColumnFormatter.quoteAndEscape(ingredient.id))
			)
			.addLiteral(" }")
			.addLineDeliminator(",")
		);

		public static UnkeyedRecipeIngredient create(String string) {
			return (
				!string.isEmpty && string.charAt(0) == '#'
				? new UnkeyedRecipeIngredient(true, string.substring(1))
				: new UnkeyedRecipeIngredient(false, string)
			);
		}
	}

	public static record ModelFace(String name, int minX, int minY, int maxX, int maxY, String texture, String cullface, Integer rotation) {

		public static final TableFormat<ModelFace> FORMAT = (
			new TableFormat<ModelFace>()
			.addLiteral("\t\t\t\t")
			.addJoined(": ", format -> format
				.addField(Justification.left(), face -> ColumnFormatter.quoteAndEscape(face.name))
				.addLiteral("{ ")
			)
			.addLiteral("\"uv\": [ ")
			.addJoined(", ", format -> format
				.addField(Justification.on("."), ModelFace::minX)
				.addField(Justification.on("."), ModelFace::minY)
				.addField(Justification.on("."), ModelFace::maxX)
				.addField(Justification.on("."), ModelFace::maxY)
			)
			.addLiteral(" ], ")
			.addJoined(", ", format -> format
				.addJsonString("texture", Justification.left(), ModelFace::texture)
				.addJsonString("cullface", Justification.left(), ModelFace::cullface)
				.addJsonNumber("rotation", ModelFace::rotation)
			)
			.addLiteral(" }")
			.addLineDeliminator(",")
		);
	}
}