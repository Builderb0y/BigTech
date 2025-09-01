package builderb0y.bigtech.circuits;

import org.jetbrains.annotations.Range;

import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.item.ItemStack;

import builderb0y.autocodec.annotations.AddPseudoField;
import builderb0y.autocodec.annotations.RecordLike;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.items.MaterialItems;
import builderb0y.bigtech.util.BitField;

@AddPseudoField(name = "level", getter = "getLevel")
@RecordLike(value = { "level", "rotation" }, name = "getInstance")
public class SubtracterCircuitComponent extends ByteBackedCircuitComponent implements RotatableCircuitComponent {

	public static final BitField
		LEVEL    = BitField.create(4, 0),
		ROTATION = BitField.create(2, LEVEL.nextOffset());
	public static final Canonicalizer<SubtracterCircuitComponent>
		CANONICALIZER = new Canonicalizer<>(1 << ROTATION.nextOffset(), SubtracterCircuitComponent::new);

	public SubtracterCircuitComponent(byte data) {
		super(data);
	}

	public static SubtracterCircuitComponent getInstance(byte data) {
		return CANONICALIZER.get(data);
	}

	public static SubtracterCircuitComponent getInstance(int level, CircuitRotation rotation) {
		return CANONICALIZER.apply(LEVEL.assemble(level) | ROTATION.assemble(rotation.ordinal()));
	}

	public @Range(from = 0L, to = 15L) int getLevel() {
		return LEVEL.get(this.data);
	}

	public SubtracterCircuitComponent withLevel(@Range(from = 0L, to = 15L) int level) {
		return getInstance((byte)(LEVEL.set(this.data, level)));
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
	public @Range(from = 0L, to = 15L) int getRawOutputLevel(CircuitDirection side) {
		return side == CircuitDirection.FRONT ? this.getLevel() : 0;
	}

	@Override
	public CircuitComponent getDefaultState() {
		return CANONICALIZER.apply(0);
	}

	@Override
	public int getColor(ItemStack stack, int layer) {
		return layer == 1 ? RedstoneWireBlock.getWireColor(this.getLevel()) : -1;
	}

	@Override
	public ItemStack createItemStack() {
		ItemStack stack = new ItemStack(MaterialItems.SUBTRACTER_CIRCUIT);
		stack.set(BigTechDataComponents.CIRCUIT, this);
		return stack;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": { rotation: " + this.getRotation() + ", level: " + this.getLevel() + " }";
	}

	@Override
	public CircuitComponent rawTick(int externalInput) {
		int backInput = NeighborIO.getLevel(externalInput, CircuitDirection.BACK);
		if (backInput == 0) return this.withLevel(0);
		int sideInput = Math.max(
			NeighborIO.getLevel(externalInput, CircuitDirection.LEFT),
			NeighborIO.getLevel(externalInput, CircuitDirection.RIGHT)
		);
		return this.withLevel(Math.max(backInput - sideInput, 0));
	}
}