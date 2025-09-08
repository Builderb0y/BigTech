package builderb0y.bigtech.blockEntities;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.circuits.CircuitComponent;
import builderb0y.bigtech.circuits.MicroProcessorCircuitComponent;
import builderb0y.bigtech.circuits.EmptyCircuitComponent;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.gui.screenHandlers.AssemblerScreenHandler;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.util.BitField;

public class AssemblerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<Text> {

	public static final int
		PROPERTY_WIDTH = 0,
		PROPERTY_HEIGHT = 1,
		PROPERTY_PROGRESS = 2,
		PROPERTY_COMPLEXITY = 3,
		SYNCED_PROPERTY_COUNT = 4;
	public static final BlockEntityTicker<AssemblerBlockEntity>
		TICKER = (World world, BlockPos pos, BlockState state, AssemblerBlockEntity blockEntity) -> {
			blockEntity.serverTick();
		};
	public static final BitField
		WIDTH  = BitField.create(3, 0),
		HEIGHT = BitField.create(3, WIDTH.nextOffset());

	public Text customName, outputName = Text.empty();
	public Vector3f color;
	public int beamCount;
	public byte size = (byte)(WIDTH.assemble(5) | HEIGHT.assemble(5));
	public RecipeData recipe;
	public PropertyDelegate syncedProperties = new PropertyDelegate() {

		@Override
		public int size() {
			return 4;
		}

		@Override
		public int get(int index) {
			return switch (index) {
				case PROPERTY_WIDTH -> AssemblerBlockEntity.this.width();
				case PROPERTY_HEIGHT -> AssemblerBlockEntity.this.height();
				case PROPERTY_PROGRESS -> AssemblerBlockEntity.this.getRecipeData().progress;
				case PROPERTY_COMPLEXITY -> AssemblerBlockEntity.this.getRecipeData().complexity;
				default -> throw new IndexOutOfBoundsException(index);
			};
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
				case PROPERTY_WIDTH -> AssemblerBlockEntity.this.setWidth((byte)(value));
				case PROPERTY_HEIGHT -> AssemblerBlockEntity.this.setHeight((byte)(value));
				case PROPERTY_PROGRESS -> AssemblerBlockEntity.this.getRecipeData().progress = value;
				case PROPERTY_COMPLEXITY -> AssemblerBlockEntity.this.getRecipeData().complexity = value;
				default -> throw new IndexOutOfBoundsException(index);
			}
		}
	};
	public MainInventory inventory = this.new MainInventory();

	public AssemblerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public AssemblerBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.ASSEMBLER, pos, state);
	}

	public int width() {
		return WIDTH.get(this.size);
	}

	public int height() {
		return HEIGHT.get(this.size);
	}

	public void setWidth(int width) {
		if (width >= 1 && width <= 5) {
			this.size = (byte)(WIDTH.set(this.size, width));
			this.sizeChanged();
		}
	}

	public void setHeight(int height) {
		if (height >= 1 && height <= 5) {
			this.size = (byte)(HEIGHT.set(this.size, height));
			this.sizeChanged();
		}
	}

	public void setSize(int width, int height) {
		if (width >= 1 && width <= 5 && height >= 1 && height <= 5) {
			this.size = (byte)(WIDTH.assemble(width) | HEIGHT.assemble(height));
			this.sizeChanged();
		}
	}

	public void sizeChanged() {
		if (this.recipe != null) this.recipe.update();
		this.markDirty();
	}

	public void setOutputName(Text outputName) {
		this.outputName = outputName;
		this.outputNameChanged();
	}

	public void outputNameChanged() {
		if (this.recipe != null) {
			this.recipe.output.set(
				DataComponentTypes.ITEM_NAME,
				!this.outputName.getString().isBlank()
				? this.outputName
				: (
					(MicroProcessorCircuitComponent)(
						this.recipe.output.get(BigTechDataComponents.CIRCUIT)
					)
				)
				.getDefaultName()
			);
		}
		this.markDirty();
	}

	@Override
	public Text getDisplayName() {
		return this.customName != null ? this.customName : Text.translatable("container.bigtech.assembler");
	}

	@Override
	public Text getScreenOpeningData(ServerPlayerEntity player) {
		return this.outputName;
	}

	@Override
	public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new AssemblerScreenHandler(syncId, this, playerInventory);
	}

	public void serverTick() {
		RecipeData recipe = this.getRecipeData();
		if (this.beamCount > 0) {
			if (recipe.canCraft()) {
				recipe.progress += this.beamCount;
				while (recipe.progress >= recipe.complexity && recipe.canCraft()) {
					if (this.craft()) {
						recipe.progress -= recipe.complexity;
					}
					else {
						recipe.progress = recipe.complexity;
						break;
					}
				}
			}
			else {
				recipe.progress = 0;
			}
			this.markDirty();
		}
		else if (recipe.progress > 0 && !recipe.canCraft()) {
			recipe.progress = 0;
			this.markDirty();
		}
	}

	public boolean craft() {
		if (this.insertResult()) {
			this.consumeInput();
			return true;
		}
		else {
			return false;
		}
	}

	public boolean insertResult() {
		ItemStack result = this.getRecipeData().output;
		ItemStack existing = this.inventory.getStack(25);
		if (existing.isEmpty()) {
			this.inventory.setStack(25, result.copy());
			return true;
		}
		else if (ItemStack.areItemsAndComponentsEqual(existing, result) && existing.getCount() < existing.getMaxCount()) {
			existing.increment(1);
			return true;
		}
		else {
			return false;
		}
	}

	public void consumeInput() {
		int consumeSlots = this.getRecipeData().inputSlots;
		for (int slot = 0; slot < 25; slot++) {
			if ((consumeSlots & (1 << slot)) != 0) {
				this.inventory.decrement(slot);
			}
		}
	}

	public RecipeData getRecipeData() {
		if (this.recipe == null) {
			this.recipe = new RecipeData();
		}
		return this.recipe;
	}

	public void setColorAndSync(Vector3f color, int beamCount) {
		this.color = color;
		this.beamCount = beamCount;
		this.markDirty();
		if (this.world instanceof ServerWorld serverWorld) {
			serverWorld.getChunkManager().markForUpdate(this.pos);
		}
	}

	@Override
	public void readComponents(ComponentsAccess components) {
		super.readComponents(components);
		this.customName = components.get(DataComponentTypes.CUSTOM_NAME);
	}

	@Override
	public void addComponents(ComponentMap.Builder builder) {
		super.addComponents(builder);
		if (this.customName != null) builder.add(DataComponentTypes.CUSTOM_NAME, this.customName);
	}

	@Override
	public void readData(ReadView view) {
		this.recipe = null;
		super.readData(view);
		this.customName = tryParseCustomName(view, "custom_name");
		this.outputName = tryParseCustomName(view, "output_name");
		if (this.outputName == null) this.outputName = Text.empty();
		Inventories.readData(view, this.inventory.items);
		int[] colorBits = view.getOptionalIntArray("color").orElse(null);
		if (colorBits != null && colorBits.length == 3) {
			this.color = new Vector3f(
				Float.intBitsToFloat(colorBits[0]),
				Float.intBitsToFloat(colorBits[1]),
				Float.intBitsToFloat(colorBits[2])
			);
		}
		else {
			this.color = null;
		}
		if (this.world != null && this.world.isClient) {
			this.reRender();
		}
		int width = view.getByte("width", (byte)(0));
		if (width > 0 && width <= 8) this.setWidth(width);
		int height = view.getByte("height", (byte)(0));
		if (height > 0 && height <= 8) this.setHeight(height);
		this.getRecipeData().progress = view.getInt("progress", 0);
		this.beamCount = view.getInt("beam_count", 0);
		this.recipe.update();
	}

	@Override
	public void writeData(WriteView view) {
		super.writeData(view);
		if (this.customName != null) view.put("custom_name", TextCodecs.CODEC, this.customName);
		if (this.outputName != null) view.put("output_name", TextCodecs.CODEC, this.outputName);
		Inventories.writeData(view, this.inventory.items);
		if (this.color != null) view.putIntArray("color", new int[] {
			Float.floatToRawIntBits(this.color.x),
			Float.floatToRawIntBits(this.color.y),
			Float.floatToRawIntBits(this.color.z)
		});
		view.putByte("width", (byte)(this.width()));
		view.putByte("height", (byte)(this.height()));
		if (this.recipe != null) view.putInt("progress", this.recipe.progress);
		view.putInt("beam_count", this.beamCount);
	}

	@Environment(EnvType.CLIENT)
	public void reRender() {
		MinecraftClient.getInstance().worldRenderer.updateBlock(this.world, this.pos, this.getCachedState(), this.getCachedState(),  8);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(WrapperLookup registries) {
		NbtCompound nbt = super.toInitialChunkDataNbt(registries);
		if (this.color != null) {
			nbt.putFloatArray("color", new float[] { this.color.x, this.color.y, this.color.z });
		}
		else {
			//readNbt() won't be called if we return an empty compound.
			nbt.putBoolean("not_empty", true);
		}
		return nbt;
	}

	@Override
	public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	public void slotChanged(int slot, ItemStack oldStack, ItemStack newStack) {
		if (slot < 25) {
			if (this.recipe != null) {
				this.recipe.update(slot, oldStack, newStack);
			}
			if (this.world != null) {
				this.world.updateComparators(this.pos, FunctionalBlocks.ASSEMBLER);
			}
		}
		this.markDirty();
	}

	@Override
	public void onBlockReplaced(BlockPos pos, BlockState oldState) {
		for (ItemStack stack : this.inventory.items) {
			ItemScatterer.spawn(this.world, pos.getX(), pos.getY(), pos.getZ(), this.inventory.defaultify(stack, true));
		}
		super.onBlockReplaced(pos, oldState);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void setCachedState(BlockState state) {
		super.setCachedState(state);
		if (this.recipe != null) {
			this.recipe.powered = state.get(Properties.POWERED);
			this.recipe.update();
		}
	}

	public class MainInventory implements Inventory {

		public final DefaultedList<ItemStack> items = DefaultedList.ofSize(26, ItemStack.EMPTY);
		public ItemStack clickingStack = ItemStack.EMPTY;
		public boolean clicking;

		public ItemStack defaultify(ItemStack stack, boolean copy) {
			CircuitComponent circuit = stack.get(BigTechDataComponents.CIRCUIT);
			if (circuit != null) {
				CircuitComponent defaultState = circuit.getDefaultState();
				if (defaultState != circuit) {
					if (copy) stack = stack.copy();
					stack.set(BigTechDataComponents.CIRCUIT, defaultState);
				}
			}
			return stack;
		}

		public void beginExport(int clickedSlot) {
			if (this.clicking) {
				throw new IllegalStateException("Recursive click!");
			}
			this.clicking = true;
			ItemStack stack = this.items.get(clickedSlot);
			this.clickingStack = stack.copy();
			this.defaultify(stack, false);
		}

		public void endExport(int clickedSlot) {
			if (!this.clicking) {
				throw new IllegalStateException("Must call beginExport() first");
			}
			this.clicking = false;
			ItemStack generic = this.items.get(clickedSlot);
			ItemStack specific = this.clickingStack;
			ItemStack defaultified = this.defaultify(specific, true);
			if (ItemStack.areItemsAndComponentsEqual(generic, defaultified)) {
				ItemStack newStack = generic.copy();
				newStack.set(BigTechDataComponents.CIRCUIT, specific.get(BigTechDataComponents.CIRCUIT));
				this.setStack(clickedSlot, newStack);
			}
		}

		@Override
		public int size() {
			return this.items.size();
		}

		@Override
		public boolean isEmpty() {
			for (ItemStack item : this.items) {
				if (!item.isEmpty()) return false;
			}
			return true;
		}

		@Override
		public ItemStack getStack(int slot) {
			return this.items.get(slot);
		}

		public void decrement(int slot) {
			ItemStack oldStack = this.items.get(slot);
			if (oldStack.isEmpty()) return;
			ItemStack newStack = oldStack.copy();
			newStack.decrement(1);
			this.items.set(slot, newStack);
			AssemblerBlockEntity.this.slotChanged(slot, oldStack, newStack);
		}

		@Override
		public ItemStack removeStack(int slot, int amount) {
			ItemStack oldStack = this.items.get(slot);
			if (oldStack.isEmpty()) return ItemStack.EMPTY;
			ItemStack newStack = oldStack.copy();
			ItemStack result = newStack.split(amount);
			this.items.set(slot, newStack);
			AssemblerBlockEntity.this.slotChanged(slot, oldStack, newStack);
			return result;
		}

		@Override
		public ItemStack removeStack(int slot) {
			ItemStack removed = this.items.set(slot, ItemStack.EMPTY);
			if (!removed.isEmpty()) {
				AssemblerBlockEntity.this.slotChanged(slot, removed, net.minecraft.item.ItemStack.EMPTY);
			}
			return removed;
		}

		@Override
		public void setStack(int slot, ItemStack stack) {
			ItemStack old = this.items.set(slot, stack);
			AssemblerBlockEntity.this.slotChanged(slot, old, stack);
		}

		@Override
		public void markDirty() {
			RecipeData recipe = AssemblerBlockEntity.this.recipe;
			if (recipe != null) recipe.update();
			AssemblerBlockEntity.this.markDirty();
		}

		@Override
		public boolean canPlayerUse(PlayerEntity player) {
			return Inventory.canPlayerUse(AssemblerBlockEntity.this, player);
		}

		@Override
		public void clear() {
			this.items.clear();
		}
	}

	public static class MainStorage implements SlottedStorage<ItemVariant> {

		public final List<SingleSlotStorage<ItemVariant>> slots;

		public MainStorage(AssemblerBlockEntity assembler) {
			@SuppressWarnings("unchecked")
			SingleSlotStorage<ItemVariant>[] slots = new SingleSlotStorage[26];
			for (int index = 0; index < 25; index++) {
				slots[index] = assembler.new InputSlotStorage(index);
			}
			slots[25] = assembler.new OutputSlotStorage();
			this.slots = Arrays.asList(slots);
		}

		@Override
		public int getSlotCount() {
			return 26;
		}

		@Override
		public SingleSlotStorage<ItemVariant> getSlot(int slot) {
			return this.slots.get(slot);
		}

		@Override
		public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
			long amount = 0;
			for (int slot = 0; slot < 25; slot++) {
				amount += this.slots.get(slot).insert(resource, maxAmount - amount, transaction);
				if (amount == maxAmount) break;
			}
			return amount;
		}

		@Override
		public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
			return this.slots.get(25).extract(resource, maxAmount, transaction);
		}

		@Override
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Iterator<StorageView<ItemVariant>> iterator() {
			//parent method returns Iterator<StorageView<ItemVariant>> directly,
			//not Iterator<? extends StorageView<ItemVariant>>.
			return (Iterator)(this.slots.iterator());
		}
	}

	public class InputSlotStorage extends SnapshotParticipant<ItemStack> implements SingleSlotStorage<ItemVariant> {

		public final int slot;
		public ItemStack stack;

		public InputSlotStorage(int slot) {
			this.slot = slot;
			this.stack = AssemblerBlockEntity.this.inventory.getStack(slot);
		}

		@Override
		public ItemStack createSnapshot() {
			return this.stack;
		}

		@Override
		public void readSnapshot(ItemStack snapshot) {
			this.stack = snapshot;
		}

		@Override
		public boolean supportsExtraction() {
			return false;
		}

		@Override
		public long extract(ItemVariant variant, long l, TransactionContext context) {
			return 0L;
		}

		@Override
		public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
			ItemStack stack = this.stack;
			if (stack.getItem() != resource.getItem()) {
				return 0L;
			}
			CircuitComponent circuit = stack.get(BigTechDataComponents.CIRCUIT);
			if (circuit == null) {
				return 0L;
			}
			ItemStack defaultStack = stack;
			CircuitComponent defaultCircuit = circuit.getDefaultState();
			if (defaultCircuit != circuit) {
				defaultStack = defaultStack.copy();
				defaultStack.set(BigTechDataComponents.CIRCUIT, defaultCircuit);
			}
			circuit = resource.getComponentMap().get(BigTechDataComponents.CIRCUIT);
			if (circuit != null) {
				defaultCircuit = circuit.getDefaultState();
				if (defaultCircuit != circuit) {
					resource = resource.withComponentChanges(ComponentChanges.builder().add(BigTechDataComponents.CIRCUIT, defaultCircuit).build());
				}
			}
			if (resource.matches(defaultStack)) {
				int inserted = (int)(Math.min(stack.getMaxCount() - stack.getCount(), maxAmount));
				if (inserted > 0) {
					this.updateSnapshots(transaction);
					this.stack = stack.copyWithCount(stack.getCount() + inserted);
					return inserted;
				}
			}
			return 0L;
		}

		@Override
		public void onFinalCommit() {
			AssemblerBlockEntity.this.inventory.setStack(this.slot, this.stack);
		}

		@Override
		public boolean isResourceBlank() {
			return this.stack.isEmpty();
		}

		@Override
		public ItemVariant getResource() {
			return ItemVariant.of(this.stack);
		}

		@Override
		public long getAmount() {
			return this.stack.getCount();
		}

		@Override
		public long getCapacity() {
			return this.stack.getMaxCount();
		}

		@Override
		public Iterator<StorageView<ItemVariant>> iterator() {
			return SingleSlotStorage.super.iterator();
		}
	}

	public class OutputSlotStorage extends SnapshotParticipant<ItemStack> implements SingleSlotStorage<ItemVariant> {

		public ItemStack stack = AssemblerBlockEntity.this.inventory.getStack(25);

		@Override
		public ItemStack createSnapshot() {
			return this.stack;
		}

		@Override
		public void readSnapshot(ItemStack snapshot) {
			this.stack = snapshot;
		}

		@Override
		public boolean supportsInsertion() {
			return false;
		}

		@Override
		public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
			return 0L;
		}

		@Override
		public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
			if (maxAmount > 0L) {
				ItemStack stack = this.stack;
				int count = stack.getCount();
				if (count > 0 && resource.matches(stack)) {
					this.updateSnapshots(transaction);
					int extracted = (int)(Math.min(count, maxAmount));
					this.stack = stack.copyWithCount(stack.getCount() - extracted);
					return extracted;
				}
			}
			return 0L;
		}

		@Override
		public void onFinalCommit() {
			AssemblerBlockEntity.this.inventory.setStack(25, this.stack);
		}

		@Override
		public boolean isResourceBlank() {
			return this.stack.isEmpty();
		}

		@Override
		public ItemVariant getResource() {
			return ItemVariant.of(this.stack);
		}

		@Override
		public long getAmount() {
			return this.stack.getCount();
		}

		@Override
		public long getCapacity() {
			return this.stack.getMaxCount();
		}
	}

	public class RecipeData {

		public boolean powered;
		public int inputSlots;
		public int blockingSlots;
		public int complexity;
		public int progress;
		public ItemStack output;

		public RecipeData() {
			this.powered = AssemblerBlockEntity.this.getCachedState().get(Properties.POWERED);
			this.update();
		}

		public boolean canCraft() {
			return this.inputSlots != 0 && this.blockingSlots == 0;
		}

		public void update() {
			this.inputSlots    = 0;
			this.blockingSlots = 0;
			this.complexity    = 0;
			this.progress      = 0;
			int width  = AssemblerBlockEntity.this.width();
			int height = AssemblerBlockEntity.this.height();
			int minX   = 5 - width;
			int minY   = 5 - height;
			MicroProcessorCircuitComponent grid = new MicroProcessorCircuitComponent(width, height);
			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 5; x++) {
					int slot = y * 5 + x;
					ItemStack stack = AssemblerBlockEntity.this.inventory.getStack(slot);
					int count = stack.getCount();
					if (count > 0) {
						if (x < minX || y < minY) {
							this.blockingSlots |= 1 << slot;
							continue;
						}
						CircuitComponent component = stack.get(BigTechDataComponents.CIRCUIT);
						this.inputSlots |= 1 << slot;
						if ((count == 1 && !this.powered) || component == null) {
							this.blockingSlots |= 1 << slot;
						}
						if (component != null) {
							this.complexity += component.getComplexity();
							grid.setStack(x - minX, y - minY, stack);
						}
					}
				}
			}
			grid.recomputeOutput();
			this.output = new ItemStack(FunctionalItems.MICRO_PROCESSOR);
			this.output.set(BigTechDataComponents.CIRCUIT, grid);
			Text name = AssemblerBlockEntity.this.outputName;
			this.output.set(DataComponentTypes.ITEM_NAME, name.getString().isBlank() ? grid.getDefaultName() : name);
		}

		public void update(int slot, ItemStack oldStack, ItemStack newStack) {
			int minX = 5 - AssemblerBlockEntity.this.width();
			int minY = 5 - AssemblerBlockEntity.this.height();
			int x = slot % 5;
			int y = slot / 5;
			int count = newStack.getCount();
			if (count == 0) {
				this.inputSlots &= ~(1 << slot);
				this.blockingSlots &= ~(1 << slot);
			}
			else {
				this.inputSlots |= 1 << slot;
				if (x < minX || y < minY || (count == 1 && !this.powered) || !newStack.contains(BigTechDataComponents.CIRCUIT)) {
					this.blockingSlots |= 1 << slot;
				}
				else {
					this.blockingSlots &= ~(1 << slot);
				}
			}
			this.complexity += (
				newStack.getOrDefault(BigTechDataComponents.CIRCUIT, EmptyCircuitComponent.INSTANCE).getComplexity() -
				oldStack.getOrDefault(BigTechDataComponents.CIRCUIT, EmptyCircuitComponent.INSTANCE).getComplexity()
			);
			if (x >= minX && y >= minY && !ItemStack.areItemsAndComponentsEqual(oldStack, newStack)) {
				MicroProcessorCircuitComponent grid = ((MicroProcessorCircuitComponent)(this.output.get(BigTechDataComponents.CIRCUIT))).clone();
				grid.setStack(x - minX, y - minY, newStack);
				grid.recomputeOutput();
				this.output.set(BigTechDataComponents.CIRCUIT, grid);
				this.progress = 0;
			}
		}
	}
}