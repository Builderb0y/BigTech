package builderb0y.bigtech.circuits;

import org.jetbrains.annotations.Range;

public class NeighborIO {

	public static @Range(from = 0L, to = 15L) int getLevel(
		/* short */ int value,
		CircuitDirection direction
	) {
		return (value >>> (direction.ordinal() << 2)) & 15;
	}

	public static /* short */ int setLevel(/* short */
		int value,
		CircuitDirection direction,
		@Range(from = 0L, to = 15L) int level
	) {
		int shift = direction.ordinal() << 2;
		return (value & ~(15 << shift)) | (level << shift);
	}

	public static /* short */ int assembleLevels(
		@Range(from = 0L, to = 15L) int front,
		@Range(from = 0L, to = 15L) int back,
		@Range(from = 0L, to = 15L) int left,
		@Range(from = 0L, to = 15L) int right
	) {
		return (
			(front << (CircuitDirection.FRONT.ordinal() << 2)) |
			(back  << (CircuitDirection.BACK .ordinal() << 2)) |
			(left  << (CircuitDirection.LEFT .ordinal() << 2)) |
			(right << (CircuitDirection.RIGHT.ordinal() << 2))
		);
	}

	public static /* short */ int rotateShortLeft(/* short */ int value, int by) {
		value &= 0xFFFF;
		return ((value << (by & 15)) | (value >>> ((-by) & 15))) & 0xFFFF;
	}

	public static /* short */ int rotateShortRight(/* short */ int value, int by) {
		value &= 0xFFFF;
		return ((value >>> (by & 15)) | (value << ((-by) & 15))) & 0xFFFF;
	}

	public static /* byte */ int rotateByteLeft(/* byte */ int value, int by) {
		value &= 0xFF;
		return ((value << (by & 7)) | (value >>> ((-by) & 7))) & 0xFF;
	}

	public static /* byte */ int rotateByteRight(/* byte */ int value, int by) {
		value &= 0xFF;
		return ((value >>> (by & 7)) | (value << ((-by) & 7))) & 0xFF;
	}

	public static /* short */ int rotateLevels(/* short */ int levels, CircuitRotation rotation) {
		return rotateShortLeft(levels, rotation.ordinal() << 2);
	}

	public static /* short */ int unrotateLevels(/* short */ int levels, CircuitRotation rotation) {
		return rotateShortRight(levels, rotation.ordinal() << 2);
	}
}