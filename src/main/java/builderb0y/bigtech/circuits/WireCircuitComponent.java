package builderb0y.bigtech.circuits;

import java.util.List;

import org.jetbrains.annotations.Range;

import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import builderb0y.autocodec.annotations.AddPseudoField;
import builderb0y.autocodec.annotations.RecordLike;
import builderb0y.bigtech.circuits.WireRouter.ComponentLocation;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.items.MaterialItems;

@AddPseudoField("level")
@RecordLike(value = { "level" }, name = "getInstance")
public class WireCircuitComponent extends ByteBackedCircuitComponent implements RoutableCircuitComponent {

	public static final Canonicalizer<WireCircuitComponent> CANONICALIZER = new Canonicalizer<>(16, WireCircuitComponent::new);

	public WireCircuitComponent(byte data) {
		super(data);
	}

	public static WireCircuitComponent getInstance(int level) {
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

	@Override
	public int getColor(ItemStack stack, int layer) {
		return layer == 1 ? RedstoneWireBlock.getWireColor(this.level()) : -1;
	}

	public int level() {
		return this.data;
	}

	@Override
	public @Range(from = 0L, to = 15L) int getOutputLevel(CircuitDirection side) {
		return this.data;
	}

	@Override
	public void beginRoute(WireRouter router, ComponentLocation[] locationStack) {
		router.spread(new WireRouter.Entry(locationStack, CircuitDirection.FRONT));
	}

	@Override
	public void route(WireRouter router, WireRouter.Entry context) {
		for (CircuitDirection outputSide : CircuitDirection.VALUES) {
			router.add(context.move(outputSide));
		}
	}

	@Override
	public RoutableCircuitComponent postRoute(WireRouter router, WireRouter.Entry context) {
		return getInstance(router.currentLevel);
	}

	@Override
	public CircuitComponent getDefaultState() {
		return getInstance(0);
	}

	@Override
	public int getComplexity() {
		return WIRE_COMPLEXITY;
	}

	@Override
	public ItemStack createItemStack() {
		ItemStack stack = new ItemStack(MaterialItems.WIRE_CIRCUIT);
		stack.set(BigTechDataComponents.CIRCUIT, this);
		return stack;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": { level: " + this.level() + " }";
	}

	@Override
	public CircuitComponent tick(int externalInput) {
		return this;
	}
}