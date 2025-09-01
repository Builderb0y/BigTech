package builderb0y.bigtech.circuits;

import org.jetbrains.annotations.Range;

import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.item.ItemStack;

import builderb0y.autocodec.annotations.AddPseudoField;
import builderb0y.autocodec.annotations.RecordLike;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.items.MaterialItems;
import builderb0y.bigtech.util.BitField;

@AddPseudoField(name = "active", getter = "isActive")
@RecordLike(value = { "rotation", "active" }, name = "getInstance")
public class DiodeCircuitComponent extends ByteBackedCircuitComponent implements RotatableCircuitComponent {

	public static final BitField
		ROTATION = BitField.create(2, 0),
		ACTIVE   = BitField.create(1, ROTATION.nextOffset());
	public static final Canonicalizer<DiodeCircuitComponent>
		CANONICALIZER = new Canonicalizer<>(1 << ACTIVE.nextOffset(), DiodeCircuitComponent::new);

	public DiodeCircuitComponent(byte data) {
		super(data);
	}

	public static DiodeCircuitComponent getInstance(byte state) {
		return CANONICALIZER.get(state);
	}

	public static DiodeCircuitComponent getInstance(CircuitRotation rotation, boolean active) {
		return CANONICALIZER.apply(ROTATION.assemble(rotation.ordinal()) | ACTIVE.assemble(active ? 1 : 0));
	}

	public boolean isActive() {
		return ACTIVE.get(this.data) != 0;
	}

	public DiodeCircuitComponent setActive(boolean active) {
		return getInstance((byte)(ACTIVE.set(this.data, active ? 1 : 0)));
	}

	@Override
	public CircuitRotation getRotation() {
		return CircuitRotation.VALUES[ROTATION.get(this.data)];
	}

	@Override
	public CircuitComponent withRotation(CircuitRotation rotation) {
		return getInstance((byte)(ROTATION.set(this.data, rotation.ordinal())));
	}

	@Override
	public CircuitComponent getDefaultState() {
		return CANONICALIZER.apply(0);
	}

	@Override
	public @Range(from = 0L, to = 15L) int getRawOutputLevel(CircuitDirection side) {
		return side == CircuitDirection.FRONT && this.isActive() ? 15 : 0;
	}

	@Override
	public int getColor(ItemStack stack, int layer) {
		return layer == 1 ? RedstoneWireBlock.getWireColor(this.isActive() ? 15 : 0) : -1;
	}

	@Override
	public ItemStack createItemStack() {
		ItemStack stack = new ItemStack(MaterialItems.DIODE_CIRCUIT);
		stack.set(BigTechDataComponents.CIRCUIT, this);
		return stack;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": { rotation: " + this.getRotation() + ", active: " + this.isActive() + " }";
	}

	@Override
	public CircuitComponent rawTick(int externalInput) {
		if (
			NeighborIO.getLevel(externalInput, CircuitDirection.LEFT) != 0 ||
			NeighborIO.getLevel(externalInput, CircuitDirection.RIGHT) != 0
		) {
			return this;
		}
		return this.setActive(NeighborIO.getLevel(externalInput, CircuitDirection.BACK) != 0);
	}
}