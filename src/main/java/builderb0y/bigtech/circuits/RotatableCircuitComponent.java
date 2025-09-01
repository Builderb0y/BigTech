package builderb0y.bigtech.circuits;

import org.jetbrains.annotations.Range;

import builderb0y.autocodec.annotations.AddPseudoField;

@AddPseudoField(name = "rotation", getter = "getRotation")
public interface RotatableCircuitComponent extends CircuitComponent {

	public default CircuitDirection rotateToLocal(CircuitDirection side) {
		return side.unrotate(this.getRotation());
	}

	public default int rotateExternalInputsToLocal(int externalInputs) {
		return NeighborIO.unrotateLevels(externalInputs, this.getRotation());
	}

	@Override
	public default @Range(from = 0L, to = 15L) int getOutputLevel(CircuitDirection side) {
		return this.getRawOutputLevel(this.rotateToLocal(side));
	}

	public abstract @Range(from = 0L, to = 15L) int getRawOutputLevel(CircuitDirection side);

	@Override
	public default CircuitComponent tick(int externalInput) {
		return this.rawTick(this.rotateExternalInputsToLocal(externalInput));
	}

	public abstract CircuitComponent rawTick(int externalInput);

	public abstract CircuitRotation getRotation();

	public abstract CircuitComponent withRotation(CircuitRotation rotation);

	public default CircuitComponent rotate(int by) {
		return this.withRotation(this.getRotation().rotate(by));
	}

	public default CircuitComponent rotate(CircuitRotation by) {
		return this.rotate(by.ordinal());
	}

	@Override
	public default CircuitComponent getDefaultState() {
		return this.withRotation(CircuitRotation.DEFAULT);
	}

	@Override
	public default CircuitComponent cycle(boolean backward) {
		return this.rotate(backward ? 1 : -1);
	}
}