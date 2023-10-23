package builderb0y.bigtech.datagen.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

/**
a Table is a combination of some data, and some way of formatting that data.
the data can be some arbitrary type, and the format will output that data
in a neat, human-readable, and vertically aligned fashion, intended for json files.
example usage: {@code
	record Person(String firstname, String lastname, Double balance) {}

	TableFormat<Person> format = (
		new TableFormat<Person>()
		.addLiteral("{ ")
		.addJoined(", ", f -> f
			.addJsonString("firstname", Justification.left(), Person::firstname)
			.addJsonString("lastname",  Justification.right(), Person::lastname)
			.addJsonNumber("balance", Person::balance)
		)
		.addLiteral(" }")
		.addLineDeliminator(",")
	);

	Table<Person> table = (
		new Table<Person>()
		.addRow(new Person("Alice", "Bob", 123.0))
		.addRow(new Person("Charlie", null, 4.33))
		.addRow(new Person(null, "Davidson", null))
	);

	System.out.println(table.toString());
	//the following is printed:
	{ "firstname": "Alice",   "lastname":      "Bob", "balance": 123.0  },
	{ "firstname": "Charlie",                         "balance":   4.33 },
	{                         "lastname": "Davidson"                    }

	//as you can see above, firstname is left-justified, lastname is right-justified,
	//and balance aligns the decimal separator.
	//additionally, all field names are aligned vertically,
	//each field has comma separators in the correct places,
	//and missing (null) fields are simply filled with spaces.
}
*/
public class Table<R> {

	public final TableFormat<R> format;
	public final List<R> rows = new ArrayList<>(16);

	public Table(TableFormat<R> format) {
		this.format = format;
	}

	public Table<R> addRow(R row) {
		this.rows.add(row);
		return this;
	}

	public Table<R> addRows(Iterable<R> rows) {
		if (rows instanceof Collection<R> collection) this.rows.addAll(collection);
		else rows.forEach(this.rows::add);
		return this;
	}

	public static <R> Collector<R, Table<R>, Table<R>> collector(TableFormat<R> format) {
		return Collector.of(
			() -> new Table<>(format),
			Table::addRow,
			(table1, table2) -> { table1.addRows(table2.rows); return table1; }
		);
	}

	@Override
	public String toString() {
		int rowCount = this.rows.size();
		int columnCount = this.format.columnFormatters.size();
		if (columnCount == 0) return "\n".repeat(rowCount);
		ColumnFormatter.Context<R> context = new ColumnFormatter.Context<>(this);
		this.format.formatTo(context);
		return context.toString();
	}
}