package builderb0y.bigtech.circuits;

import java.util.List;

import org.jetbrains.annotations.Range;

import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import builderb0y.autocodec.annotations.AddPseudoField;
import builderb0y.autocodec.annotations.RecordLike;
import builderb0y.autocodec.annotations.VerifyIntRange;
import builderb0y.bigtech.circuits.WireRouter.ComponentLocation;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.items.MaterialItems;
import builderb0y.bigtech.networking.BigTechNetwork;
import builderb0y.bigtech.util.BitField;

@AddPseudoField("horizontal_level")
@AddPseudoField("vertical_level")
@RecordLike(value = { "horizontal_level", "vertical_level" }, name = "getInstance")
public class CrossoverCircuitComponent extends ByteBackedCircuitComponent implements RoutableCircuitComponent {

	public static final BitField
		HORIZONTAL_LEVEL = BitField.create(4, 0),
		VERTICAL_LEVEL   = BitField.create(4, HORIZONTAL_LEVEL.nextOffset());
	public static final Canonicalizer<CrossoverCircuitComponent> CANONICALIZER = new Canonicalizer<>(256, CrossoverCircuitComponent::new);

	public CrossoverCircuitComponent(byte data) {
		super(data);
	}

	public static CrossoverCircuitComponent getInstance(int horizontal_level, int vertical_level) {
		return CANONICALIZER.apply(HORIZONTAL_LEVEL.assemble(horizontal_level) | VERTICAL_LEVEL.assemble(vertical_level));
	}

	public @VerifyIntRange(min = 0L, max = 15L) int horizontal_level() {
		return HORIZONTAL_LEVEL.get(this.data);
	}

	public @VerifyIntRange(min = 0L, max = 15L) int vertical_level() {
		return VERTICAL_LEVEL.get(this.data);
	}

	public CrossoverCircuitComponent withHorizontalLevel(int horizontalLevel) {
		return CANONICALIZER.apply(HORIZONTAL_LEVEL.set(this.data, horizontalLevel));
	}

	public CrossoverCircuitComponent withVerticalLevel(int verticalLevel) {
		return CANONICALIZER.apply(VERTICAL_LEVEL.set(this.data, verticalLevel));
	}

	@Override
	public void appendTooltips(ItemStack stack, List<Text> lines) {
		int horizontalLevel = this.horizontal_level();
		int verticalLevel = this.vertical_level();
		if (verticalLevel > 0) {
			lines.add(
				1,
				Text
				.translatable("tooltip.bigtech.circuit.output.vertical", verticalLevel)
				.styled(CircuitComponent.colorize(verticalLevel))
			);
		}
		if (horizontalLevel > 0) {
			lines.add(
				1,
				Text
				.translatable("tooltip.bigtech.circuit.output.horizontal", horizontalLevel)
				.styled(CircuitComponent.colorize(horizontalLevel))
			);
		}
	}

	@Override
	public CircuitComponent getDefaultState() {
		return CANONICALIZER.apply(0);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": { horizontal_level: " + this.horizontal_level() + ", vertical_level: " + this.vertical_level() + " }";
	}

	@Override
	public void beginRoute(WireRouter router, ComponentLocation[] locationStack) {
		router.spread(new WireRouter.Entry(locationStack, CircuitDirection.FRONT));
		router.spread(new WireRouter.Entry(locationStack, CircuitDirection.RIGHT));
	}

	@Override
	public void route(WireRouter router, WireRouter.Entry context) {
		router.add(context.move(context.inputSide()));
		router.add(context.move(context.inputSide().opposite()));
	}

	@Override
	public int getColor(ItemStack stack, int layer) {
		return switch (layer) {
			case 1 -> RedstoneWireBlock.getWireColor(this.horizontal_level());
			case 2 -> RedstoneWireBlock.getWireColor(this.vertical_level());
			default -> -1;
		};
	}

	@Override
	public @Range(from = 0L, to = 15L) int getOutputLevel(CircuitDirection side) {
		return switch (side) {
			case FRONT, BACK -> this.vertical_level();
			case LEFT, RIGHT -> this.horizontal_level();
		};
	}

	@Override
	public RoutableCircuitComponent postRoute(WireRouter router, WireRouter.Entry context) {
		return switch (context.inputSide()) {
			case FRONT, BACK -> this.withVerticalLevel(router.currentLevel);
			case LEFT, RIGHT -> this.withHorizontalLevel(router.currentLevel);
		};
	}

	@Override
	public int getComplexity() {
		return 15;
	}

	@Override
	public ItemStack createItemStack() {
		ItemStack stack = new ItemStack(MaterialItems.CROSSOVER_CIRCUIT);
		stack.set(BigTechDataComponents.CIRCUIT, this);
		return stack;
	}

	@Override
	public CircuitComponent tick(int externalInput) {
		return this;
	}
}