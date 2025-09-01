package builderb0y.bigtech.circuits;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

import builderb0y.autocodec.annotations.RecordLike;

@RecordLike(value = {}, name = "getInstance")
public class EmptyCircuitComponent implements CircuitComponent {

	public static final EmptyCircuitComponent INSTANCE = new EmptyCircuitComponent();
	public static final PacketCodec<PacketByteBuf, EmptyCircuitComponent> PACKET_CODEC = PacketCodec.unit(INSTANCE);

	public static EmptyCircuitComponent getInstance() {
		return INSTANCE;
	}

	@Override
	public int getOutputLevel(CircuitDirection side) {
		return 0;
	}

	@Override
	public CircuitComponent tick(int externalInputs) {
		return this;
	}

	@Override
	public int getColor(ItemStack stack, int layer) {
		return -1;
	}

	@Override
	public int getComplexity() {
		return EMPTY_COMPLEXITY;
	}

	@Override
	public ItemStack createItemStack() {
		return ItemStack.EMPTY;
	}
}