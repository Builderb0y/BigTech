package builderb0y.bigtech.datagen.tables;

import java.util.ArrayList;
import java.util.List;

/**
a ColumnFormatter appends a single column of data to a Context.
every Table has a List<ColumnFormatter> which, combined, will output all the columns.
a column can be some literal text, or a property of the row data.
*/
public interface ColumnFormatter<R> {

	/**
	appends a column of data to the provided Context.
	note that this method is responsible for appending
	columns FOR ALL ROWS, not just one row.
	it is also responsible for calculating
	whitespace to potentially add to the column.
	*/
	public abstract void formatTo(Context<R> context);

	public static CharSequence quoteAndEscape(CharSequence text) {
		if (text == null) return null;
		StringBuilder builder = new StringBuilder(text.length() + 2);
		builder.append('"');
		for (int index = 0, length = text.length(); index < length; index++) {
			char c = text.charAt(index);
			switch (c) {
				case '\n' -> builder.append("\\n");
				case '\r' -> builder.append("\\r");
				case '\t' -> builder.append("\\t");
				case '\'' -> builder.append("\\'");
				case '"' -> builder.append("\\\"");
				case '\\' -> builder.append("\\\\");
				default -> {
					if (c >= ' ' && c <= '~') {
						builder.append(c);
					}
					else {
						builder
						.append("\\u")
						.append(hex((c >>> 12) & 15))
						.append(hex((c >>>  8) & 15))
						.append(hex((c >>>  4) & 15))
						.append(hex((c       ) & 15));
					}
				}
			}
		}
		return builder.append('"');
	}

	public static char hex(int number) {
		return (char)(number + (number >= 10 ? 'A' - 10 : '0'));
	}

	public static class Context<R> {

		public final Table<R> table;
		/**
		the outer List is for rows, and the inner list is for columns.
		in other words, context.stringBuilders.get(0) returns the list
		corresponding to the first *row* of the formatted data.
		after all ColumnFormatter's have finished outputting their text
		to all the rows and columns, they are concatenated to form the final String.
		see {@link #toString()}.
		*/
		public final List<List<CharSequence>> stringBuilders;

		public Context(Table<R> table) {
			this.table = table;
			int numberOfRows = table.rows.size();
			this.stringBuilders = new ArrayList<>(numberOfRows);
			for (int index = 0; index < numberOfRows; index++) {
				this.stringBuilders.add(new ArrayList<>(16));
			}
		}

		public int rows() {
			return this.stringBuilders.size();
		}

		public R row(int row) {
			return this.table.rows.get(row);
		}

		@Override
		public String toString() {
			int textLength = this.stringBuilders.size() << 8;
			StringBuilder combined = new StringBuilder(textLength);
			for (List<CharSequence> row : this.stringBuilders) {
				for (CharSequence column : row) {
					combined.append(column);
				}
				combined.append('\n');
			}
			combined.setLength(combined.length() - 1); //strip trailing newline.
			return combined.toString();
		}
	}
}