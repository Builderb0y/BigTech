package builderb0y.bigtech.datagen.tables;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
internal class for dealing with rows with deliminators joining cells together.
*/
public class JoiningTableFormat<R> extends TableFormat<R> {

	public final String deliminator;
	public final List<Predicate<R>> predicates = new ArrayList<>(8);

	public JoiningTableFormat(String deliminator) {
		this.deliminator = deliminator;
	}

	@Override
	public TableFormat<R> addField(Justification justification, Function<? super R, ?> getter) {
		this.predicates.add(row -> getter.apply(row) != null);
		return super.addField(justification, getter);
	}

	@Override
	public TableFormat<R> addField(DecimalFormat format, Function<? super R, ? extends Number> getter) {
		this.predicates.add(row -> getter.apply(row) != null);
		return super.addField(format, getter);
	}

	@Override
	public TableFormat<R> addLiteral(String text) {
		this.predicates.add(row -> true);
		return super.addLiteral(text);
	}

	@Override
	public TableFormat<R> addLiteral(String text, Predicate<R> predicate) {
		this.predicates.add(predicate);
		return super.addLiteral(text, predicate);
	}

	@Override
	public TableFormat<R> addJoined(String deliminator, Consumer<TableFormat<R>> configurator) {
		JoiningTableFormat<R> collector = new JoiningTableFormat<>(deliminator);
		configurator.accept(collector);
		this.predicates.add(row -> {
			for (Predicate<R> predicate : collector.predicates) {
				if (predicate.test(row)) {
					return true;
				}
			}
			return false;
		});
		return this.addColumn(collector);
	}

	@Override
	public void formatTo(Context<R> context) {
		List<ColumnFormatter<R>> formatters = this.columnFormatters;
		for (int formatterIndex = 0, size = formatters.size(); formatterIndex < size; formatterIndex++) {
			formatters.get(formatterIndex).formatTo(context);
			for (int rowIndex = 0, rowCount = context.rows(); rowIndex < rowCount; rowIndex++) {
				R row = context.row(rowIndex);
				List<CharSequence> builder = context.stringBuilders.get(rowIndex);
				if (formatterIndex != size - 1) {
					if (this.predicates.get(formatterIndex).test(row) && this.testAfter(context, rowIndex, formatterIndex)) {
						if (builder.get(builder.size() - 1) instanceof Whitespace) {
							builder.add(builder.size() - 1, this.deliminator);
						}
						else {
							builder.add(this.deliminator);
						}
					}
					else {
						builder.add(new Whitespace(this.deliminator.length()));
					}
				}
			}
		}
	}

	public boolean testAfter(Context<R> context, int rowIndex, int predicateIndex) {
		R row = context.row(rowIndex);
		while (++predicateIndex < this.predicates.size()) {
			if (this.predicates.get(predicateIndex).test(row)) {
				return true;
			}
		}
		return false;
	}
}