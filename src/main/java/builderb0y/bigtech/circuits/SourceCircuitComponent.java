package builderb0y.bigtech.circuits;

import java.util.List;

import org.jetbrains.annotations.Range;

import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import builderb0y.autocodec.annotations.AddPseudoField;
import builderb0y.autocodec.annotations.RecordLike;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.items.MaterialItems;

@AddPseudoField("level")
@RecordLike(value = { "level" }, name = "getInstance")
public class SourceCircuitComponent extends ByteBackedCircuitComponent {

	public static final Canonicalizer<SourceCircuitComponent>
		CANONICALIZER = new Canonicalizer<>(16, SourceCircuitComponent::new);

	public SourceCircuitComponent(byte data) {
		super(data);
	}

	public static SourceCircuitComponent getInstance(int level) {
		return CANONICALIZER.apply(level);
	}

	@Override
	public void appendTooltips(ItemStack stack, List<Text> lines) {
		int level = this.level();
		if (level > 0) {
			lines.add(
				1,
				Text
				.translatable("tooltip.bigtech.circuit.output", level)
				.styled(CircuitComponent.colorize(level))
			);
		}
	}

	public int level() {
		return this.data;
	}

	@Override
	public CircuitComponent getDefaultState() {
		return CANONICALIZER.apply(0);
	}

	@Override
	public int getColor(ItemStack stack, int layer) {
		return layer == 1 ? RedstoneWireBlock.getWireColor(this.level()) : -1;
	}

	@Override
	public @Range(from = 0L, to = 15L) int getOutputLevel(CircuitDirection side) {
		return this.level();
	}

	@Override
	public int getComplexity() {
		return 15;
	}

	@Override
	public CircuitComponent cycle(boolean backward) {
		return getInstance(backward ? Math.max(this.level() - 1, 0) : Math.min(this.level() + 1, 15));
	}

	@Override
	public CircuitComponent tick(int externalInput) {
		return this;
	}

	@Override
	public ItemStack createItemStack() {
		ItemStack stack = new ItemStack(MaterialItems.SOURCE_CIRCUIT);
		stack.set(BigTechDataComponents.CIRCUIT, this);
		return stack;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": { level: " + this.level() + " }";
	}
}