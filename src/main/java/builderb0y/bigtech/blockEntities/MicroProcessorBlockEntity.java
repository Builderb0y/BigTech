package builderb0y.bigtech.blockEntities;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.autocodec.decoders.DecodeException;
import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.circuits.*;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;

public class MicroProcessorBlockEntity extends BlockEntity {

	public static final BlockEntityTicker<MicroProcessorBlockEntity>
		TICKER = (World world, BlockPos pos, BlockState state, MicroProcessorBlockEntity blockEntity) -> blockEntity.tick();

	public Text name = Text.empty();
	public @Nullable MicroProcessorCircuitComponent circuit;
	public boolean active;
	public boolean needWirePreUpdate;
	public boolean selfPowering;

	public MicroProcessorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public MicroProcessorBlockEntity(BlockPos pos, BlockState state) {
		super(BigTechBlockEntityTypes.MICRO_PROCESSOR, pos, state);
	}

	public void setCircuit(MicroProcessorCircuitComponent circuit) {
		this.circuit = circuit;
		this.active = true;
		this.markDirty();
	}

	public Text getName() {
		if (!this.name.getString().isBlank()) return this.name;
		if (this.circuit != null) return this.circuit.getDefaultName();
		return Text.translatable("block.bigtech.micro_processor");
	}

	public void tick() {
		MicroProcessorCircuitComponent oldCircuit = this.circuit;
		if (oldCircuit != null && this.active) {
			this.active = false;
			Direction front = this.getCachedState().get(Properties.HORIZONTAL_FACING);
			BlockRotation rotation = switch (front) {
				case NORTH -> BlockRotation.NONE;
				case EAST  -> BlockRotation.CLOCKWISE_90;
				case SOUTH -> BlockRotation.CLOCKWISE_180;
				case WEST  -> BlockRotation.COUNTERCLOCKWISE_90;
				default    -> throw new IllegalStateException("Vertical micro processor?");
			};
			this.selfPowering = true;
			try {
				this.world.updateNeighbors(this.pos, FunctionalBlocks.MICRO_PROCESSOR);
			}
			finally {
				this.selfPowering = false;
			}
			int externalInput = NeighborIO.assembleLevels(
				this.getRedstoneInput(rotation, Direction.NORTH),
				this.getRedstoneInput(rotation, Direction.SOUTH),
				this.getRedstoneInput(rotation, Direction.WEST),
				this.getRedstoneInput(rotation, Direction.EAST)
			);
			MicroProcessorCircuitComponent nextCircuit = oldCircuit.clone();
			if (this.needWirePreUpdate) {
				nextCircuit.beginRoute(new WireRouter(externalInput), WireRouter.Entry.EMPTY_STACK);
				this.needWirePreUpdate = false;
			}
			nextCircuit = (MicroProcessorCircuitComponent)(
				nextCircuit.tick(externalInput)
			);
			if (!nextCircuit.equals(oldCircuit)) {
				nextCircuit.beginRoute(new WireRouter(externalInput), WireRouter.Entry.EMPTY_STACK);
				nextCircuit.recomputeOutput();
				this.setCircuit(nextCircuit);
			}
			this.world.updateNeighbors(this.pos, FunctionalBlocks.MICRO_PROCESSOR);
		}
	}

	public void wakeup() {
		if (this.circuit != null) {
			this.active = true;
			this.needWirePreUpdate = true;
		}
	}

	public int getRedstoneInput(BlockRotation rotation, Direction side) {
		side = rotation.rotate(side);
		this.selfPowering = true;
		try {
			return this.world.getEmittedRedstonePower(this.pos.offset(side), side);
		}
		finally {
			this.selfPowering = false;
		}
	}

	public int getRedstoneOutput(Direction side) {
		if (this.selfPowering || this.circuit == null) return 0;
		Direction front = this.getCachedState().get(Properties.HORIZONTAL_FACING);
		BlockRotation rotation = switch (front) {
			case NORTH -> BlockRotation.NONE;
			case EAST  -> BlockRotation.COUNTERCLOCKWISE_90;
			case SOUTH -> BlockRotation.CLOCKWISE_180;
			case WEST  -> BlockRotation.CLOCKWISE_90;
			default    -> throw new IllegalStateException("Vertical micro processor?");
		};
		return switch (rotation.rotate(side)) {
			case NORTH -> this.circuit.getOutputLevel(CircuitDirection.FRONT);
			case SOUTH -> this.circuit.getOutputLevel(CircuitDirection.BACK);
			case EAST  -> this.circuit.getOutputLevel(CircuitDirection.RIGHT);
			case WEST  -> this.circuit.getOutputLevel(CircuitDirection.LEFT);
			case UP, DOWN -> 0;
		};
	}

	@Override
	public void readComponents(ComponentsAccess components) {
		super.readComponents(components);
		if (components.get(BigTechDataComponents.CIRCUIT) instanceof MicroProcessorCircuitComponent circuit) {
			this.setCircuit(circuit);
		}
		this.name = components.get(DataComponentTypes.CUSTOM_NAME);
		if (this.name == null) {
			this.name = components.get(DataComponentTypes.ITEM_NAME);
			if (this.name == null) this.name = Text.empty();
		}
	}

	@Override
	public void addComponents(ComponentMap.Builder builder) {
		super.addComponents(builder);
		if (this.circuit != null) {
			builder.add(BigTechDataComponents.CIRCUIT, this.circuit);
		}
		if (!this.name.getString().isBlank()) {
			builder.add(DataComponentTypes.ITEM_NAME, this.name);
		}
	}

	@Override
	public void readData(ReadView view) {
		super.readData(view);
		this.active = view.getBoolean("active", true);
		if (view.read("circuit", CircuitComponent.CODEC).orElse(null) instanceof MicroProcessorCircuitComponent circuit) {
			this.circuit = circuit;
		}
		Text name = tryParseCustomName(view, "name");
		if (name == null) name = Text.empty();
		this.name = name;
	}

	@Override
	public void writeData(WriteView view) {
		super.writeData(view);
		view.putBoolean("active", this.active);
		if (this.circuit != null) view.put(
			"circuit",
			CircuitComponent.CODEC,
			this.circuit
		);
		if (!this.name.getString().isBlank()) {
			view.put("name", TextCodecs.CODEC, this.name);
		}
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(WrapperLookup registries) {
		NbtCompound nbt = super.toInitialChunkDataNbt(registries);
		if (!this.name.getString().isBlank()) {
			nbt.put("name", TextCodecs.CODEC, registries.getOps(NbtOps.INSTANCE), this.name);
		}
		return nbt;
	}
}