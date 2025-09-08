package builderb0y.bigtech.blockEntities;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.gui.screenHandlers.SpawnerInterceptorScreenHandler;
import builderb0y.bigtech.util.Inventories2;

public class SpawnerInterceptorBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Nameable {

	public SpawnerInterceptorInventory inventory = new SpawnerInterceptorInventory();
	public ContainerLock lock = ContainerLock.EMPTY;
	public @Nullable Text customName;

	public SpawnerInterceptorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public SpawnerInterceptorBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.SPAWNER_INTERCEPTOR, pos, state);
	}

	public boolean intercept(EntityType<?> type) {
		if (!this.inventory.egg.isOf(Items.EGG)) {
			return false;
		}
		Item toAdd = SpawnEggItem.forEntity(type);
		if (toAdd == null) {
			return false;
		}
		if (this.inventory.spawnEgg.isEmpty()) {
			this.inventory.egg.decrement(1);
			this.inventory.spawnEgg = new ItemStack(toAdd);
			this.markDirty();
			return true;
		}
		else if (this.inventory.spawnEgg.isOf(toAdd) && this.inventory.spawnEgg.getCount() < this.inventory.spawnEgg.getMaxCount()) {
			this.inventory.egg.decrement(1);
			this.inventory.spawnEgg.increment(1);
			this.markDirty();
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void readData(ReadView view) {
		super.readData(view);
		this.lock = ContainerLock.read(view);
		this.customName = tryParseCustomName(view, "CustomName");
		this.inventory.clear();
		Inventories2.readItems(view, "Items").forEach(Inventories2.setter(this.inventory));
	}

	@Override
	public void writeData(WriteView view) {
		super.writeData(view);
		this.lock.write(view);
		if (this.customName != null) {
			view.put("CustomName", TextCodecs.CODEC, this.customName);
		}
		Inventories2.writeItems(view, "Items", Inventories2.stream(this.inventory, true));
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new SpawnerInterceptorScreenHandler(BigTechScreenHandlerTypes.SPAWNER_INTERCEPTOR, syncId, this.inventory, playerInventory);
	}

	@Override
	public Text getName() {
		return this.customName != null ? this.customName : Text.translatable("container.bigtech.spawner_interceptor");
	}

	@Override
	public Text getDisplayName() {
		return this.getName();
	}

	public class SpawnerInterceptorInventory implements SidedInventory {

		public static final int[] SLOTS = { 0, 1 };

		public ItemStack
			egg = ItemStack.EMPTY,
			spawnEgg = ItemStack.EMPTY;

		@Override
		public int[] getAvailableSlots(Direction side) {
			return SLOTS;
		}

		@Override
		public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
			return slot == 0 && stack.isOf(Items.EGG);
		}

		@Override
		public boolean canExtract(int slot, ItemStack stack, Direction dir) {
			return slot == 1;
		}

		@Override
		public int size() {
			return 2;
		}

		@Override
		public boolean isEmpty() {
			return this.egg.isEmpty() && this.spawnEgg.isEmpty();
		}

		@Override
		public ItemStack getStack(int slot) {
			return switch (slot) {
				case 0 -> this.egg;
				case 1 -> this.spawnEgg;
				default -> throw new IndexOutOfBoundsException("Slot ${slot} is out of bounds for inventory of size 2");
			};
		}

		@Override
		public ItemStack removeStack(int slot, int amount) {
			this.markDirty();
			return switch (slot) {
				case 0 -> this.egg.split(amount);
				case 1 -> this.spawnEgg.split(amount);
				default -> throw new IndexOutOfBoundsException("Slot ${slot} is out of bounds for inventory of size 2");
			};
		}

		@Override
		public ItemStack removeStack(int slot) {
			this.markDirty();
			return switch (slot) {
				case 0 -> this.egg.copyAndEmpty();
				case 1 -> this.spawnEgg.copyAndEmpty();
				default -> throw new IndexOutOfBoundsException("Slot ${slot} is out of bounds for inventory of size 2");
			};
		}

		@Override
		public void setStack(int slot, ItemStack stack) {
			this.markDirty();
			switch (slot) {
				case 0 -> this.egg = stack;
				case 1 -> this.spawnEgg = stack;
				default -> throw new IndexOutOfBoundsException("Slot ${slot} is out of bounds for inventory of size 2");
			}
		}

		@Override
		public void markDirty() {
			SpawnerInterceptorBlockEntity.this.markDirty();
		}

		@Override
		public boolean canPlayerUse(PlayerEntity player) {
			return Inventory.canPlayerUse(SpawnerInterceptorBlockEntity.this, player);
		}

		@Override
		public void clear() {
			this.egg = this.spawnEgg = ItemStack.EMPTY;
			this.markDirty();
		}
	}
}