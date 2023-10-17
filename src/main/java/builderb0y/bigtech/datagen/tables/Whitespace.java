package builderb0y.bigtech.datagen.tables;

import java.util.Objects;

/**
a {@link CharSequence} which contains a specific number of space characters (U+0020).
*/
public record Whitespace(int length) implements CharSequence {

	@Override
	public char charAt(int index) {
		Objects.checkIndex(index, this.length);
		return ' ';
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		Objects.checkFromToIndex(start, end, this.length);
		return new Whitespace(end - start);
	}

	@Override
	public String toString() {
		return " ".repeat(this.length);
	}
}