package builderb0y.bigtech.datagen.formats;

import java.util.*;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.datagen.tables.ColumnFormatter;
import builderb0y.bigtech.datagen.tables.Justification;
import builderb0y.bigtech.datagen.tables.TableFormat;

public class TableFormats {

	public static record BlockStateJsonVariant(String properties, String model, Integer x, Integer y) {

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

		public static Integer yFromNorth(Direction direction) {
			return switch (direction) {
				case NORTH -> 0;
				case EAST  -> 90;
				case SOUTH -> 180;
				case WEST  -> 270;
				case UP, DOWN -> null;
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
			return block.getStateManager().getStates().stream().filter(state -> !state.get(Properties.WATERLOGGED)).sorted(comparator);
		}

		public static <C extends Comparable<C>> Comparator<BlockState> comparator(Property<C> property) {
			if (property.type == Direction.class) {
				return Comparator.comparing(
					state -> (Direction)(state.get(property)),
					Comparator.nullsFirst(
						Comparator.comparing(
							BlockStateJsonVariant::yFromNorth
						)
					)
				);
			}
			else {
				return Comparator.comparing(
					state -> property.name(state.get(property))
				);
			}
		}
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