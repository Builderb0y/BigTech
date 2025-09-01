package builderb0y.bigtech.util;

public record BitField(
	int bitCount,
	int offset,
	int rightMask,
	int offsetMask,
	int inverseRightMask,
	int inverseOffsetMask
) {

	public static BitField create(int bitCount, int offset) {
		int rightMask = (1 << bitCount) - 1;
		int offsetMask = rightMask << offset;
		return new BitField(bitCount, offset, rightMask, offsetMask, ~rightMask, ~offsetMask);
	}

	public int get(int value) {
		return (value >>> this.offset) & this.rightMask;
	}

	public int set(int value, int field) {
		return (value & this.inverseOffsetMask) | (field << this.offset);
	}

	public int assemble(int field) {
		return field << this.offset;
	}

	public int nextOffset() {
		return this.offset + this.bitCount;
	}
}