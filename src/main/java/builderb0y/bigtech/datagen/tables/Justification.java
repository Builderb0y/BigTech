package builderb0y.bigtech.datagen.tables;

import java.util.List;

public interface Justification {

	public abstract void justify(ColumnFormatter.Context<?> context);

	/**
	does not justify text at all, and does not append whitspace to either side of it.
	useful when all the text is known in advance to be the same length,
	or if the text being justified is at the end of a line.
	*/
	public static Justification none() {
		return context -> {};
	}

	/**
	keeps text on the left, and whitespace on the right. for example:
	|a  |
	|ab |
	|abc|
	*/
	public static Justification left() {
		return Justifications.LEFT;
	}

	/**
	keeps text on the right, and whitespace on the left. for example:
	|  a|
	| ab|
	|abc|
	*/
	public static Justification right() {
		return Justifications.RIGHT;
	}

	/**
	keeps the first occurrence of the provided string aligned vertically.
	if the string being justified does not contain the alignment string,
	then the string behaves as if the alignment string follows directly after it.
	example of justifying on ".":
	|1234   |
	| 123.45|
	|  12.3 |
	|   1   |
	*/
	public static Justification on(String align) {
		return new AlignJustification(align);
	}

	public static enum Justifications implements Justification {
		LEFT,
		RIGHT;

		@Override
		public void justify(ColumnFormatter.Context<?> context) {
			int rowCount = context.rows();
			int maxLength = 0;
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				List<CharSequence> builder = context.stringBuilders.get(rowIndex);
				maxLength = Math.max(maxLength, builder.get(builder.size() - 1).length());
			}
			for (int row = 0; row < rowCount; row++) {
				List<CharSequence> builder = context.stringBuilders.get(row);
				builder.add(
					builder.size() - this.ordinal(),
					new Whitespace(maxLength - builder.get(builder.size() - 1).length())
				);
			}
		}

		@Override
		public String toString() {
			return this == LEFT ? "Justification.left()" : "Justification.right()";
		}
	}

	public static class AlignJustification implements Justification {

		public final String align;

		public AlignJustification(String align) {
			this.align = align;
		}

		@Override
		public void justify(ColumnFormatter.Context<?> context) {
			int rowCount = context.rows();
			int beforeMaxLength = 0;
			int afterMaxLength = 0;
			boolean seenAlign = false;
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				List<CharSequence> builder = context.stringBuilders.get(rowIndex);
				CharSequence text = builder.get(builder.size() - 1);
				int alignIndex = this.indexOf(text);
				if (alignIndex != text.length()) seenAlign = true;
				beforeMaxLength = Math.max(beforeMaxLength, alignIndex);
				afterMaxLength = Math.max(afterMaxLength, text.length() - (alignIndex + this.align.length()));
			}
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				List<CharSequence> builder = context.stringBuilders.get(rowIndex);
				CharSequence last = builder.get(builder.size() - 1);
				int radixPointIndex = this.indexOf(last);
				builder.add(builder.size() - 1, new Whitespace(beforeMaxLength - radixPointIndex));
				if (seenAlign) builder.add(new Whitespace(afterMaxLength - (last.length() - (radixPointIndex + this.align.length()))));
			}
		}

		public int indexOf(CharSequence text) {
			String align = this.align;
			int alignLength = align.length();
			outer:
			for (int textIndex = 0, limit = text.length() - alignLength; textIndex < limit; textIndex++) {
				for (int alignIndex = 0; alignIndex < alignLength; alignIndex++) {
					if (text.charAt(textIndex + alignIndex) != align.charAt(alignIndex)) {
						continue outer;
					}
				}
				return textIndex;
			}
			return text.length();
		}

		@Override
		public String toString() {
			return "Justification.on(${ColumnFormatter.quoteAndEscape(this.align)})";
		}
	}
}