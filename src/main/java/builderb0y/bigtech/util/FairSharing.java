package builderb0y.bigtech.util;

/**
https://en.wikipedia.org/wiki/Thue%E2%80%93Morse_sequence
this class also provides additional methods which
generalize this sequence to other bases besides 2.
all indexes are assumed to be unsigned.
all bases must be greater than 0.
*/
public class FairSharing {

	/**
	the original sequence, in base 2.
	index is interpreted as unsigned.
	implementation checks if the sum of all bits is odd or even.
	*/
	public static int compute2(int index) {
		return Integer.bitCount(index) & 1; //use intrinsic over bitwise operations.
	}

	/** generalization for bases which are a power of 2. */
	public static int computePowerOf2(int index, int mask) {
		int shift = 32 - Integer.numberOfLeadingZeros(mask);
		assert shift > 0 && shift < 32 && (mask & (mask + 1)) == 0 : "mask: " + mask;
		int result = 0;
		while (index != 0) {
			result += index & mask;
			index >>>= shift;
		}
		return result & mask;
	}

	/**
	generalization for all bases.
	this implementation is slow and should not be
	used if a faster specialization is available.
	*/
	public static int computeOther(int index, int base) {
		assert base > 1 : "base: " + base;
		int newIndex = index;
		int result = 0;
		if (newIndex < 0) {
			result += Integer.remainderUnsigned(newIndex, base);
			newIndex = Integer.divideUnsigned(newIndex, base);
		}
		assert newIndex >= 0 : index + " (unsigned: " + Integer.toUnsignedString(index) + ") / " + base + " -> " + newIndex + " (unsigned: " + Integer.toUnsignedString(newIndex) + ')';
		while (newIndex != 0) {
			result += newIndex % base;
			newIndex /= base;
		}
		return result % base;
	}

	/**
	generalization for all bases.
	this implementation will check if a fast
	implementation is available for the given base.
	if there is, it will be used.
	otherwise, delegates to computeOther().
	*/
	public static int compute(int index, int base) {
		if (base <= 0) throw new IllegalArgumentException("Base must be positive");
		if (base == 1) return 0;
		if (base == 2) return compute2(index);
		int mask = base - 1;
		if ((base & mask) == 0) return computePowerOf2(index, mask);
		return computeOther(index, base);
	}
}