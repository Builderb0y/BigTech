package builderb0y.bigtech.circuits;

public enum CircuitRotation {
	DEFAULT,
	ROTATE_RIGHT,
	ROTATE_180,
	ROTATE_LEFT;

	public static final CircuitRotation[] VALUES = values();

	public CircuitRotation rotate(int by) {
		return VALUES[(this.ordinal() + by) & 3];
	}

	public CircuitRotation rotate(CircuitRotation by) {
		return this.rotate(by.ordinal());
	}

	public CircuitRotation next() {
		return this.rotate(1);
	}

	public CircuitRotation previous() {
		return this.rotate(-1);
	}
}