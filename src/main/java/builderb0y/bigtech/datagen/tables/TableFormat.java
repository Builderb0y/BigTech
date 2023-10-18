package builderb0y.bigtech.datagen.tables;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
a TableFormat<R> is a spec for how to format instances of R.
internally, a TableFormat is a list of {@link ColumnFormatter}'s,
with some helper methods to add new ColumnFormatter's to itself.

instances of TableFormat are typically stored in a static final field,
and then passed into the constructor of a {@link Table} as-needed.
*/
public class TableFormat<R> implements ColumnFormatter<R> {

	public final List<ColumnFormatter<R>> columnFormatters = new ArrayList<>(8);
	public String prefix, suffix;

	public TableFormat<R> prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	public TableFormat<R> suffix(String suffix) {
		this.suffix = suffix;
		return this;
	}

	public TableFormat<R> addField(Justification justification, Function<? super R, ?> getter) {
		return this.addColumn((Context<R> context) -> {
			int rows = context.rows();
			for (int row = 0; row < rows; row++) {
				context.stringBuilders.get(row).add(Objects.toString(getter.apply(context.row(row)), ""));
			}
			justification.justify(context);
		});
	}

	public TableFormat<R> addField(DecimalFormat format, Function<? super R, ? extends Number> getter) {
		return this.addField(
			Justification.on(String.valueOf(format.getDecimalFormatSymbols().getDecimalSeparator())),
			row -> {
				Number number = getter.apply(row);
				return number != null ? format.format(number) : null;
			}
		);
	}

	public TableFormat<R> addLiteral(String text) {
		return this.addColumn((Context<R> context) -> {
			for (List<CharSequence> builder : context.stringBuilders) {
				builder.add(text);
			}
		});
	}

	public TableFormat<R> addLiteral(String text, Predicate<R> predicate) {
		return this.addColumn((Context<R> context) -> {
			int rowCount = context.rows();
			boolean any = false;
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				if (predicate.test(context.row(rowIndex))) {
					any = true;
					break;
				}
			}
			if (any) for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				context.stringBuilders.get(rowIndex).add(
					predicate.test(context.row(rowIndex))
					? text
					: new Whitespace(text.length())
				);
			}
		});
	}

	public TableFormat<R> addJoined(String deliminator, Consumer<TableFormat<R>> configurator) {
		JoiningTableFormat<R> collector = new JoiningTableFormat<>(deliminator);
		configurator.accept(collector);
		return this.addColumn(collector);
	}

	public TableFormat<R> addLineDeliminator(String deliminator) {
		return this.addColumn((Context<R> context) -> {
			int rows = context.rows() - 1;
			for (int row = 0; row < rows; row++) {
				context.stringBuilders.get(row).add(deliminator);
			}
		});
	}

	public TableFormat<R> addJsonString(String name, Justification justification, Function<? super R, ? extends CharSequence> getter) {
		return this.addJoined(": ", format -> format
			.addLiteral(ColumnFormatter.quoteAndEscape(name).toString(), row -> getter.apply(row) != null)
			.addField(justification, row -> ColumnFormatter.quoteAndEscape(getter.apply(row)))
		);
	}

	public TableFormat<R> addJsonNumber(String name, Function<? super R, ? extends Number> getter) {
		return this.addJoined(": ", tableFormat -> tableFormat
			.addLiteral(ColumnFormatter.quoteAndEscape(name).toString(), row -> getter.apply(row) != null)
			.addField(Justification.on("."), getter)
		);
	}

	public TableFormat<R> addColumn(ColumnFormatter<R> formatter) {
		this.columnFormatters.add(formatter);
		return this;
	}

	@Override
	public void formatTo(Context<R> context) {
		for (ColumnFormatter<R> formatter : this.columnFormatters) {
			formatter.formatTo(context);
		}
	}
}