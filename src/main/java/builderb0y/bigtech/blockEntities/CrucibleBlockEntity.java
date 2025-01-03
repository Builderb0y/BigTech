package builderb0y.bigtech.blockEntities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.mixins.ServerRecipeManager_PreparedRecipesAccess;
import builderb0y.bigtech.recipes.ArcFurnaceRecipe;
import builderb0y.bigtech.recipes.ArcFurnaceRecipeInput;
import builderb0y.bigtech.recipes.BigTechRecipeTypes;
import builderb0y.bigtech.util.FluidConstants2;

public class CrucibleBlockEntity extends BlockEntity implements SidedInventory {

	public static final BlockEntityTicker<CrucibleBlockEntity>
		TICKER = (World world, BlockPos pos, BlockState state, CrucibleBlockEntity blockEntity) -> blockEntity.tick();

	public static final int BASE_SIZE = 5;
	public static final int[] BASE_SLOTS = IntStream.range(0, BASE_SIZE).toArray();

	public @Nullable CrucibleProgress progress;
	public DefaultedList<ItemStack> stacks = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);

	public CrucibleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public CrucibleBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.CRUCIBLE, pos, state);
	}

	public List<ItemStack> compact() {
		int count = this.countItems();
		return switch (count) {
			case 0 -> Collections.emptyList();
			case 1 -> {
				for (int slot = 0; slot < this.size(); slot++) {
					ItemStack stack = this.stacks.get(slot);
					if (!stack.isEmpty()) yield Collections.singletonList(stack);
				}
				throw new AssertionError();
			}
			default -> {
				ArrayList<ItemStack> list = new ArrayList<>(count);
				for (int slot = 0; slot < this.size(); slot++) {
					ItemStack stack = this.stacks.get(slot);
					if (!stack.isEmpty()) list.add(stack);
				}
				yield list;
			}
		};
	}

	public void syncAndSave() {
		if (this.world instanceof ServerWorld serverWorld) {
			serverWorld.getChunkManager().markForUpdate(this.getPos());
			this.markDirty();
		}
	}

	public void craft(ServerWorld serverWorld, boolean fastCool) {
		if (this.progress == null) return;
		ItemStack output = (fastCool ? this.progress.fastCoolResult : this.progress.slowCoolResult).copy();
		this.clear();
		this.addItem(output, output.getCount());
		this.progress = null;
		this.syncAndSave();
	}

	public void tick() {
		if (this.progress != null && (this.progress.heat -= this.progress.coolingRate) <= 0) {
			if (this.progress.cooling && this.world instanceof ServerWorld serverWorld) {
				this.craft(serverWorld, false);
			}
			this.progress = null;
		}
	}

	public void onLightningPulse(ServerWorld serverWorld, LightningPulse pulse) {
		if (this.progress == null) {
			ArcFurnaceRecipe recipe = serverWorld.getRecipeManager().getFirstMatch(BigTechRecipeTypes.ARC_FURNACE, new ArcFurnaceRecipeInput(this.compact(), false), serverWorld).map(RecipeEntry::value).orElse(null);
			if (recipe != null) this.progress = CrucibleProgress.fromRecipe(recipe);
			else return;
		}
		this.progress.heat += pulse.getDistributedEnergy();
		serverWorld.spawnParticles(ParticleTypes.FLASH, this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
		serverWorld.playSound(null, this.pos, serverWorld.random.nextBoolean() ? SoundEvents.ENTITY_FIREWORK_ROCKET_TWINKLE : SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.BLOCKS, 4.0F, 2.0F - serverWorld.random.nextFloat() * 0.5F);
		if (this.progress.heat >= this.progress.maxHeat) {
			this.progress.cooling = true;
		}
		this.syncAndSave();
	}

	public int addItem(ItemStack stack, int maxCount) {
		int added = 0;
		for (int slot = 0; slot < this.size() && added < maxCount && !stack.isEmpty(); slot++) {
			if (this.stacks.get(slot).isEmpty()) {
				this.stacks.set(slot, stack.split(1));
				added++;
			}
		}
		if (added != 0) this.syncAndSave();
		return added;
	}

	public ItemStack removeItem() {
		if (this.progress == null) {
			for (int slot = 0; slot < this.size(); slot++) {
				ItemStack taken = this.stacks.get(slot);
				if (!taken.isEmpty()) {
					this.stacks.set(slot, ItemStack.EMPTY);
					this.syncAndSave();
					return taken;
				}
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public int size() {
		return BASE_SIZE;
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return BASE_SLOTS;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction side) {
		if (this.world instanceof ServerWorld serverWorld) {
			for (RecipeEntry<ArcFurnaceRecipe> entry : serverWorld.getRecipeManager().<ServerRecipeManager_PreparedRecipesAccess>as().bigtech_getPreparedRecipes().getAll(BigTechRecipeTypes.ARC_FURNACE)) {
				if (ItemStack.areItemsAndComponentsEqual(stack, entry.value().slow_cool_result) || ItemStack.areItemsAndComponentsEqual(stack, entry.value().fast_cool_result)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
		if (this.world instanceof ServerWorld serverWorld) {
			for (RecipeEntry<ArcFurnaceRecipe> entry : serverWorld.getRecipeManager().<ServerRecipeManager_PreparedRecipesAccess>as().bigtech_getPreparedRecipes().getAll(BigTechRecipeTypes.ARC_FURNACE)) {
				for (Ingredient input : entry.value().inputs) {
					if (input.test(stack)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public int getMaxCountPerStack() {
		return 1;
	}

	public int countItems() {
		int count = 0;
		for (int slot = 0; slot < this.size(); slot++) {
			if (!this.stacks.get(slot).isEmpty()) count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		for (int slot = 0; slot < this.size(); slot++) {
			if (!this.stacks.get(slot).isEmpty()) return false;
		}
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		return slot >= 0 && slot < this.stacks.size() ? this.stacks.get(slot) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack stack = this.stacks.get(slot).split(amount);
		if (!stack.isEmpty()) this.syncAndSave();
		return stack;
	}

	@Override
	public ItemStack removeStack(int slot) {
		ItemStack stack = this.stacks.set(slot, ItemStack.EMPTY);
		if (!stack.isEmpty()) this.syncAndSave();
		return stack;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.stacks.set(slot, stack);
		this.syncAndSave();
	}

	@Override
	public void clear() {
		this.stacks.clear();
		this.syncAndSave();
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(WrapperLookup registries) {
		NbtCompound nbt = super.toInitialChunkDataNbt(registries);
		this.writeNbt(nbt, registries);
		return nbt;
	}

	@Override
	public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public void readNbt(NbtCompound nbt, WrapperLookup registries) {
		super.readNbt(nbt, registries);
		this.stacks.clear();
		Inventories.readNbt(nbt, this.stacks, registries);
		if (nbt.get("progress") instanceof NbtCompound compound) {
			this.progress = CrucibleProgress.fromNbt(compound, registries);
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt, WrapperLookup registries) {
		super.writeNbt(nbt, registries);
		Inventories.writeNbt(nbt, this.stacks, registries);
		if (this.progress != null) nbt.put("progress", this.progress.toNbt(registries));
	}

	public Storage<FluidVariant> fluidStorage(ServerWorld serverWorld) {
		if (this.progress != null) {
			return new CrucibleFluidStorage(
				new HeatSnapshot(serverWorld, this, this.progress)
			);
		}
		else {
			return Storage.empty();
		}
	}

	public static class HeatSnapshot extends SnapshotParticipant<Integer> {

		public final ServerWorld serverWorld;
		public final CrucibleBlockEntity crucible;
		public final CrucibleProgress progress;
		public int heat;

		public HeatSnapshot(ServerWorld serverWorld, CrucibleBlockEntity crucible, CrucibleProgress progress) {
			this.serverWorld = serverWorld;
			this.crucible = crucible;
			this.progress = progress;
			this.heat = progress.heat;
		}

		public void setHeat(TransactionContext transaction, int heat) {
			this.updateSnapshots(transaction);
			this.heat = heat;
		}

		public void addHeat(TransactionContext transaction, int heat) {
			this.setHeat(transaction, this.heat + heat);
		}

		public void subtractHeat(TransactionContext transaction, int heat) {
			this.setHeat(transaction, this.heat - heat);
		}

		@Override
		public Integer createSnapshot() {
			return this.heat;
		}

		@Override
		public void readSnapshot(Integer snapshot) {
			this.heat = snapshot;
		}

		@Override
		public void onFinalCommit() {
			if (this.heat < this.progress.heat) {
				this.serverWorld.syncWorldEvent(WorldEvents.LAVA_EXTINGUISHED, this.crucible.pos, 0);
			}
			if (this.heat == 0) {
				this.crucible.craft(this.serverWorld, true);
			}
			this.progress.heat = this.heat;
			this.crucible.markDirty();
		}
	}

	public static class CrucibleFluidStorage implements InsertionOnlyStorage<FluidVariant> {

		public final HeatSnapshot snapshot;

		public CrucibleFluidStorage(HeatSnapshot snapshot) {
			this.snapshot = snapshot;
		}

		@Override
		public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
			if (maxAmount <= 0L) return 0L;
			if (!resource.isOf(Fluids.WATER)) return 0L;
			CrucibleProgress progress = this.snapshot.progress;
			int coolingRate = progress.coolingRate;
			long scaled = maxAmount * coolingRate / FluidConstants2.MILLIBUCKET;
			int inserted = (int)(Math.min(scaled, this.snapshot.heat));
			this.snapshot.subtractHeat(transaction, inserted);
			return FluidConstants2.MILLIBUCKET * inserted / coolingRate;
		}
	}
}