package builderb0y.bigtech.circuits;

import java.util.Arrays;

import com.mojang.serialization.DynamicOps;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryOps;
import net.minecraft.text.Text;

import builderb0y.autocodec.annotations.*;
import builderb0y.autocodec.coders.AutoCoder;
import builderb0y.autocodec.common.FactoryContext;
import builderb0y.autocodec.data.Data;
import builderb0y.autocodec.data.DataOps;
import builderb0y.autocodec.data.MapData;
import builderb0y.autocodec.decoders.DecodeException;
import builderb0y.autocodec.encoders.EncodeException;
import builderb0y.autocodec.fixers.AutoFixer;
import builderb0y.autocodec.fixers.DataFixContext;
import builderb0y.autocodec.fixers.DataFixException;
import builderb0y.autocodec.reflection.reification.ReifiedType;
import builderb0y.bigtech.circuits.WireRouter.ComponentLocation;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.codecs.ItemStackCoder.VerifyEmptyable;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.util.BitField;

@AddPseudoField("width")
@AddPseudoField("height")
@UseFixer(name = "fixer", in = MicroProcessorCircuitComponent.class, usage = MemberUsage.METHOD_IS_FACTORY)
public class MicroProcessorCircuitComponent implements RotatableCircuitComponent, RoutableCircuitComponent, Cloneable {

	public static final BitField
		WIDTH    = BitField.create(3, 0),
		HEIGHT   = BitField.create(3, WIDTH.nextOffset()),
		ROTATION = BitField.create(2, HEIGHT.nextOffset());
	public static final PacketCodec<RegistryByteBuf, MicroProcessorCircuitComponent>
		PACKET_CODEC = PacketCodec.of(MicroProcessorCircuitComponent::writeToPacket, MicroProcessorCircuitComponent::readFromPacket);
	public static final Text[]
		RECURSION_NAMES = {
			Text.translatable("block.bigtech.micro_processor"),
			Text.translatable("block.bigtech.milli_processor"),
			Text.translatable("block.bigtech.processor"),
			Text.translatable("block.bigtech.kilo_processor"),
			Text.translatable("block.bigtech.mega_processor"),
			Text.translatable("block.bigtech.giga_processor"),
			Text.translatable("block.bigtech.tera_processor"),
			Text.translatable("block.bigtech.peta_processor"),
			Text.translatable("block.bigtech.over_processor"),
		};

	@Hidden
	public byte data;
	public short outputs;
	public CircuitStack[] stacks;

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.stacks);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || (
			obj instanceof MicroProcessorCircuitComponent that &&
			this.data == that.data &&
			Arrays.equals(this.stacks, that.stacks)
		);
	}

	/** constructor for normal people. */
	@Hidden
	public MicroProcessorCircuitComponent(int width, int height) {
		this.data = (byte)(WIDTH.assemble(width - 1) | HEIGHT.assemble(height - 1));
		this.stacks = new CircuitStack[width * height];
		for (int index = 0; index < this.stacks.length; index++) {
			this.stacks[index] = new CircuitStack(ItemStack.EMPTY);
		}
	}

	/** constructor for AutoCodec. */
	public MicroProcessorCircuitComponent(
		int width,
		int height,
		short outputs,
		CircuitRotation rotation,
		CircuitStack[] stacks
	) {
		if (stacks.length != width * height) {
			throw new IllegalArgumentException("Components array is wrong length: expected " + width + " * " + height + " = " + (width * height) + ", got " + stacks.length);
		}
		this.data = (byte)(
			WIDTH.assemble(width - 1) |
			HEIGHT.assemble(height - 1) |
			ROTATION.assemble(rotation.ordinal())
		);
		this.outputs = outputs;
		this.stacks = stacks;
	}

	/** constructor for networking. */
	@Hidden
	public MicroProcessorCircuitComponent(
		byte data,
		short outputs,
		CircuitStack[] stacks
	) {
		this.data = data;
		this.outputs = outputs;
		this.stacks = stacks;
		int width = this.width();
		int height = this.height();
		if (stacks.length != width * height) {
			throw new IllegalArgumentException("Components array is wrong length: expected " + width + " * " + height + " = " + (width * height) + ", got " + stacks.length);
		}
	}

	public static MicroProcessorCircuitComponent readFromPacket(RegistryByteBuf buffer) {
		byte data = buffer.readByte();
		short outputs = buffer.readShort();
		CircuitStack[] stacks = new CircuitStack[(WIDTH.get(data) + 1) * (HEIGHT.get(data) + 1)];
		for (int index = 0; index < stacks.length; index++) {
			stacks[index] = new CircuitStack(ItemStack.OPTIONAL_PACKET_CODEC.decode(buffer));
		}
		return new MicroProcessorCircuitComponent(data, outputs, stacks);
	}

	public void writeToPacket(RegistryByteBuf buffer) {
		buffer.writeByte(this.data).writeShort(this.outputs);
		for (CircuitStack stack : this.stacks) {
			ItemStack.OPTIONAL_PACKET_CODEC.encode(buffer, stack.getStack());
		}
	}

	@VerifyIntRange(min = 1L, max = 8L)
	public int width() {
		return WIDTH.get(this.data) + 1;
	}

	@VerifyIntRange(min = 1L, max = 8L)
	public int height() {
		return HEIGHT.get(this.data) + 1;
	}

	@Override
	public CircuitRotation getRotation() {
		return CircuitRotation.VALUES[ROTATION.get(this.data)];
	}

	@Override
	public CircuitComponent withRotation(CircuitRotation rotation) {
		if (this.getRotation() == rotation) return this;
		MicroProcessorCircuitComponent clone = this.shallowClone();
		clone.data = (byte)(ROTATION.set(clone.data, rotation.ordinal()));
		return clone;
	}

	public ItemStack getStack(int x, int y) {
		return this.stacks[y * this.width() + x].getStack();
	}

	public void setStack(int x, int y, ItemStack stack) {
		int index = y * this.width() + x;
		this.stacks[index] = this.stacks[index].withStack(stack);
	}

	public CircuitComponent getComponent(int x, int y) {
		return this.stacks[y * this.width() + x].getCircuit();
	}

	public void setComponent(int x, int y, CircuitComponent component) {
		int index = y * this.width() + x;
		this.stacks[index] = this.stacks[index].withCircuit(component);
	}

	@Override
	public int getRawOutputLevel(CircuitDirection side) {
		return NeighborIO.getLevel(this.outputs, side);
	}

	public int getOutputLevel(int x, int y, CircuitDirection side) {
		return this.getComponent(x, y).getOutputLevel(side);
	}

	public int getInputLevel(int x, int y, CircuitDirection side, int externalInputs) {
		fallback: {
			switch (side) {
				case FRONT -> { if (--y <  0            ) break fallback; }
				case BACK  -> { if (++y >= this.height()) break fallback; }
				case LEFT  -> { if (--x <  0            ) break fallback; }
				case RIGHT -> { if (++x >= this.width() ) break fallback; }
			}
			return this.getComponent(x, y).getOutputLevel(side.opposite());
		}
		return NeighborIO.getLevel(externalInputs, side);
	}

	public int collectInputs(int x, int y, int externalInputs) {
		return NeighborIO.assembleLevels(
			this.getInputLevel(x, y, CircuitDirection.FRONT, externalInputs),
			this.getInputLevel(x, y, CircuitDirection.BACK, externalInputs),
			this.getInputLevel(x, y, CircuitDirection.LEFT, externalInputs),
			this.getInputLevel(x, y, CircuitDirection.RIGHT, externalInputs)
		);
	}

	public void recomputeOutput() {
		int width = this.width(), height = this.height(), result = 0, io;

		io = NeighborIO.getLevel(result, CircuitDirection.FRONT);
		for (int x = 0; x < width && io < 15; x++) {
			io = Math.max(io, this.getOutputLevel(x, 0, CircuitDirection.FRONT));
		}
		result = NeighborIO.setLevel(result, CircuitDirection.FRONT, io);

		io = NeighborIO.getLevel(result, CircuitDirection.BACK);
		for (int x = 0; x < width && io < 15; x++) {
			io = Math.max(io, this.getOutputLevel(x, height - 1, CircuitDirection.BACK));
		}
		result = NeighborIO.setLevel(result, CircuitDirection.BACK, io);

		io = NeighborIO.getLevel(result, CircuitDirection.LEFT);
		for (int y = 0; y < height && io < 15; y++) {
			io = Math.max(io, this.getOutputLevel(0, y, CircuitDirection.LEFT));
		}
		result = NeighborIO.setLevel(result, CircuitDirection.LEFT, io);

		io = NeighborIO.getLevel(result, CircuitDirection.RIGHT);
		for (int y = 0; y < height && io < 15; y++) {
			io = Math.max(io, this.getOutputLevel(width - 1, y, CircuitDirection.RIGHT));
		}
		result = NeighborIO.setLevel(result, CircuitDirection.RIGHT, io);

		this.outputs = (short)(result);
	}

	@Override
	public CircuitComponent rawTick(int externalInput) {
		MicroProcessorCircuitComponent next = this.clone();
		int width = this.width(), height = this.height();
		for (int innerY = 0; innerY < height; innerY++) {
			for (int innerX = 0; innerX < width; innerX++) {
				CircuitComponent inner = this.getComponent(innerX, innerY);
				CircuitComponent replacement = inner.tick(this.collectInputs(innerX, innerY, externalInput));
				if (replacement != inner) {
					if (replacement instanceof MicroProcessorCircuitComponent processor) {
						processor.recomputeOutput();
					}
					next.setComponent(innerX, innerY, replacement);
				}
			}
		}
		return next;
	}

	@Override
	public int getComplexity() {
		int complexity = GATE_COMPLEXITY;
		for (CircuitStack stack : this.stacks) {
			complexity += stack.getCircuit().getComplexity();
		}
		return complexity;
	}

	@Override
	public void beginRoute(WireRouter router, ComponentLocation[] locationStack) {
		int width = this.width(), height = this.height();
		for (int innerY = 0; innerY < height; innerY++) {
			for (int innerX = 0; innerX < width; innerX++) {
				if (this.getComponent(innerX, innerY) instanceof RoutableCircuitComponent routable) {
					ComponentLocation[] nextStack = Arrays.copyOf(locationStack, locationStack.length + 1);
					nextStack[locationStack.length] = new ComponentLocation(this, innerX, innerY);
					routable.beginRoute(router, nextStack);
				}
			}
		}
	}

	@Override
	public void route(WireRouter router, WireRouter.Entry context) {
		int width = this.width(), height = this.height();
		router.currentLevel = (byte)(Math.max(router.currentLevel, this.getOutputLevel(context.inputSide())));
		switch (this.rotateToLocal(context.inputSide())) {
			case FRONT -> {
				for (int x = 0; x < width; x++) {
					router.add(context.recurse(this, x, 0));
				}
			}
			case RIGHT -> {
				for (int y = 0; y < height; y++) {
					router.add(context.recurse(this, width - 1, y));
				}
			}
			case BACK -> {
				for (int x = 0; x < width; x++) {
					router.add(context.recurse(this, x, height - 1));
				}
			}
			case LEFT -> {
				for (int y = 0; y < height; y++) {
					router.add(context.recurse(this, 0, y));
				}
			}
		}
	}

	@Override
	public RoutableCircuitComponent postRoute(WireRouter router, WireRouter.Entry context) {
		return this;
	}

	@Override
	public int getColor(ItemStack stack, int layer) {
		return -1;
	}

	public int getRecursionDepth() {
		int depth = -1;
		for (CircuitStack stack : this.stacks) {
			if (stack.getCircuit() instanceof MicroProcessorCircuitComponent microProcessor) {
				depth = Math.max(depth, microProcessor.getRecursionDepth());
			}
		}
		return depth + 1;
	}

	public Text getDefaultName() {
		return RECURSION_NAMES[Math.min(this.getRecursionDepth(), RECURSION_NAMES.length - 1)];
	}

	@Override
	public ItemStack createItemStack() {
		ItemStack stack = new ItemStack(FunctionalItems.MICRO_PROCESSOR);
		stack.set(BigTechDataComponents.CIRCUIT, this);
		return stack;
	}

	public MicroProcessorCircuitComponent shallowClone() {
		try {
			return (MicroProcessorCircuitComponent)(super.clone());
		}
		catch (CloneNotSupportedException exception) {
			throw new AssertionError(exception);
		}
	}

	@Override
	public MicroProcessorCircuitComponent clone() {
		try {
			MicroProcessorCircuitComponent clone = (MicroProcessorCircuitComponent)(super.clone());
			clone.stacks = clone.stacks.clone();
			return clone;
		}
		catch (CloneNotSupportedException exception) {
			throw new AssertionError(exception);
		}
	}

	public static AutoFixer<MicroProcessorCircuitComponent> fixer(FactoryContext<MicroProcessorCircuitComponent> context) {
		AutoCoder<CircuitStack[]> stackArrayCoder = context.type(ReifiedType.from(CircuitStack[].class)).forceCreateCoder();
		AutoCoder<CircuitComponent[]> componentArrayCoder = context.type(ReifiedType.from(CircuitComponent[].class)).forceCreateCoder();

		return new AutoFixer<>() {

			@Override
			public <T_Encoded> @NotNull DataFixContext<T_Encoded> fixData(@NotNull DataFixContext<T_Encoded> context) throws DataFixException {
				MapData map = context.forceAsMap();
				Data componentsData = map.remove("components");
				if (!componentsData.isEmpty()) try {
					CircuitComponent[] components = context.withData(componentsData).decodeWith(componentArrayCoder);
					int length = components.length;
					CircuitStack[] stacks = new CircuitStack[length];
					for (int index = 0; index < length; index++) {
						stacks[index] = new CircuitStack(components[index]);
					}
					DynamicOps<Data> ops = DataOps.UNCOMPRESSED;
					if (context.ops instanceof RegistryOps<?> registryOps) {
						ops = registryOps.withDelegate(ops);
					}
					map.put("stacks", context.autoCodec().encode(stackArrayCoder, stacks, ops));
				}
				catch (EncodeException | DecodeException exception) {
					throw new DataFixException(exception);
				}
				return context;
			}
		};
	}

	@Wrapper
	public static class CircuitStack {

		public static final CircuitStack EMPTY = new CircuitStack(EmptyCircuitComponent.INSTANCE);

		@UseGetter("getStack")
		public @VerifyEmptyable(alwaysEncode = true) ItemStack stack;
		public CircuitComponent circuit;
		public boolean changed;

		public CircuitStack(ItemStack stack) {
			this.stack = stack.copyWithCount(1);
			this.circuit = stack.get(BigTechDataComponents.CIRCUIT);
			if (this.circuit == null) this.circuit = EmptyCircuitComponent.INSTANCE;
		}

		@Hidden
		public CircuitStack(CircuitComponent component) {
			this.stack = component.createItemStack();
			this.circuit = component;
		}

		@Hidden
		public CircuitStack(ItemStack stack, CircuitComponent circuit, boolean changed) {
			this.stack   = stack;
			this.circuit = circuit;
			this.changed = changed;
		}

		public ItemStack getStack() {
			if (this.changed) {
				if (!this.stack.isEmpty()) {
					this.stack = this.stack.copy();
					this.stack.set(BigTechDataComponents.CIRCUIT, this.circuit);
				}
				this.changed = false;
			}
			return this.stack;
		}

		public CircuitStack withStack(ItemStack stack) {
			return new CircuitStack(stack);
		}

		public CircuitComponent getCircuit() {
			return this.circuit;
		}

		public CircuitStack withCircuit(CircuitComponent circuit) {
			if (this.circuit == circuit) {
				return this;
			}
			else {
				return new CircuitStack(this.stack, circuit, true);
			}
		}

		@Override
		public int hashCode() {
			return this.getCircuit().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return this == obj || (
				obj instanceof CircuitStack that &&
				this.getCircuit().equals(that.getCircuit())
			);
		}

		@Override
		public String toString() {
			return this.getStack() + " " + this.getCircuit();
		}
	}
}