package builderb0y.bigtech.circuits;

import java.util.List;
import java.util.Locale;
import java.util.function.UnaryOperator;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import org.jetbrains.annotations.Range;

import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import builderb0y.autocodec.annotations.MemberUsage;
import builderb0y.autocodec.annotations.UseCoder;
import builderb0y.autocodec.coders.AutoCoder;
import builderb0y.autocodec.coders.KeyDispatchCoder.Dispatchable;
import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.codecs.RegistryCoder;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;

@UseCoder(name = "CODER", in = CircuitComponent.class, usage = MemberUsage.FIELD_CONTAINS_HANDLER)
public interface CircuitComponent extends Dispatchable<CircuitComponent> {

	public static final int
		EMPTY_COMPLEXITY = 5,
		WIRE_COMPLEXITY  = 10,
		GATE_COMPLEXITY  = 20;

	public static final RegistryCoder<CircuitComponent> CODER = new RegistryCoder<>(
		//using /give for items with circuit components re-parses the
		//component data every time the user types another character.
		//since the data is not usually correct until the user
		//is finished typing, this results in a lot of log spam.
		//so, we use the silent codec here, not he regular one.
		BigTechAutoCodec.SILENT_CODEC,
		"CircuitComponentCoder",
		"circuit_type",
		FabricRegistryBuilder.createDefaulted(
			RegistryKey.<AutoCoder<? extends CircuitComponent>>ofRegistry(BigTechMod.modID("circuit_type")),
			BigTechMod.modID("empty")
		)
		.attribute(RegistryAttribute.SYNCED)
		.buildAndRegister()
	);
	public static final Codec<CircuitComponent> CODEC = CODER.autoCodec.createDFUCodec(CODER);
	public static final Object INITIALIZER = new Object() {{
		CODER.register("empty",           EmptyCircuitComponent.class,      EmptyCircuitComponent.PACKET_CODEC);
		CODER.register("source",         SourceCircuitComponent.class,     SourceCircuitComponent.CANONICALIZER.packetCodec);
		CODER.register("wire",             WireCircuitComponent.class,       WireCircuitComponent.CANONICALIZER.packetCodec);
		CODER.register("crossover",   CrossoverCircuitComponent.class,  CrossoverCircuitComponent.CANONICALIZER.packetCodec);
		CODER.register("not",           NotGateCircuitComponent.class,    NotGateCircuitComponent.CANONICALIZER.packetCodec);
		CODER.register("diode",           DiodeCircuitComponent.class,      DiodeCircuitComponent.CANONICALIZER.packetCodec);
		CODER.register("comparator", ComparatorCircuitComponent.class, ComparatorCircuitComponent.CANONICALIZER.packetCodec);
		CODER.register("subtracter", SubtracterCircuitComponent.class, SubtracterCircuitComponent.CANONICALIZER.packetCodec);
		CODER.register("processor",                 MicroProcessorCircuitComponent.class,                MicroProcessorCircuitComponent.PACKET_CODEC);
		ItemTooltipCallback.EVENT.register((ItemStack stack, TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) -> {
			CircuitComponent component = stack.get(BigTechDataComponents.CIRCUIT);
			if (component != null) component.appendTooltips(stack, lines);
		});
	}};

	public default void appendTooltips(ItemStack stack, List<Text> lines) {
		int line = 1;
		for (CircuitDirection direction : CircuitDirection.VALUES) {
			int output = this.getOutputLevel(direction);
			if (output > 0) {
				lines.add(
					line++,
					Text
					.translatable(
						"tooltip.bigtech.circuit.output." +
						direction.name().toLowerCase(Locale.ROOT),
						output
					)
					.styled(colorize(output))
				);
			}
		}
	}

	public static UnaryOperator<Style> colorize(int level) {
		return (Style style) -> style.withColor(RedstoneWireBlock.getWireColor(level));
	}

	public abstract int getColor(ItemStack stack, int layer);

	public abstract @Range(from = 0L, to = 15L) int getOutputLevel(CircuitDirection side);

	public abstract CircuitComponent tick(int externalInput);

	public default CircuitComponent getDefaultState() {
		return this;
	}

	public default CircuitComponent cycle(boolean backward) {
		return this;
	}

	public default int getComplexity() {
		return GATE_COMPLEXITY;
	}

	public abstract ItemStack createItemStack();

	@Override
	public default AutoCoder<? extends CircuitComponent> getCoder() {
		return CODER.autoCodec.createCoder(this.getClass());
	}
}