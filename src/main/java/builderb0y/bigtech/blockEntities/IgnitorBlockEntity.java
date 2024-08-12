package builderb0y.bigtech.blockEntities;

import java.util.Objects;

import net.fabricmc.fabric.api.registry.FuelRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.damageTypes.BigTechDamageTypes;
import builderb0y.bigtech.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.screenHandlers.IgnitorScreenHandler;

public class IgnitorBlockEntity extends LockableContainerBlockEntity implements SingleStackInventory {

	public static final BlockEntityTicker<IgnitorBlockEntity> SERVER_TICKER = (World world, BlockPos pos, BlockState state, IgnitorBlockEntity blockEntity) -> blockEntity.serverTick();

	public static final int IGNITE_ENTITY_COST = 100;

	//I used to just have an ItemStack here,
	//but then getHeldStacks() was added,
	//so now I need a full DefaultedList.
	public DefaultedList<ItemStack> stacks = DefaultedList.ofSize(1, ItemStack.EMPTY);
	public final Property
		totalBurnTime = Property.create(),
		remainingBurnTime = Property.create();
	public DamageSource damageSource;

	public IgnitorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public IgnitorBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.IGNITOR, pos, state);
	}

	public void setLit(boolean lit) {
		this.world.setBlockState(this.pos, this.getCachedState().with(Properties.LIT, lit));
	}

	public boolean shouldKeepBurning() {
		return this.getCachedState().isOf(FunctionalBlocks.IGNITOR_BEAM) && this.getCachedState().get(Properties.POWERED);
	}

	public void serverTick() {
		int time = this.remainingBurnTime.get();
		if (time > 0) {
			this.remainingBurnTime.set(--time);
			if (time == 0 && !(this.shouldKeepBurning() && this.refuel(1))) {
				this.setLit(false);
			}
			this.markDirty();
		}
		else if (this.shouldKeepBurning() && this.refuel(1)) {
			this.setLit(true);
		}
	}

	public boolean refuel(int amount) {
		if (this.remainingBurnTime.get() >= amount) return true;
		if (!this.getStack().isEmpty()) {
			int burnTime = FuelRegistry.INSTANCE.get(this.getStack().getItem());
			if (burnTime > 0) {
				int consume = Math.min(this.getStack().getCount(), (amount + burnTime - 1) / burnTime);
				assert consume > 0;
				this.getStack().decrement(consume);
				boolean wasUnlit = this.remainingBurnTime.get() <= 0;
				int newTime = this.remainingBurnTime.get() + burnTime * consume;
				this.remainingBurnTime.set(newTime);
				this.totalBurnTime.set(newTime);
				if (wasUnlit) this.setLit(true);
				return newTime >= amount;
			}
		}
		return false;
	}

	public boolean consumeFuel(int amount) {
		if (this.refuel(amount)) {
			int newAmount = this.remainingBurnTime.get() - amount;
			this.remainingBurnTime.set(newAmount);
			if (newAmount == 0) this.setLit(false);
			return true;
		}
		else {
			return false;
		}
	}

	public boolean smeltItem(ItemEntity itemEntity) {
		ItemStack inputStack = itemEntity.getStack();
		SingleStackRecipeInput recipeInput = new SingleStackRecipeInput(inputStack);
		RecipeEntry<SmeltingRecipe> entry = this.world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, recipeInput, this.world).orElse(null);
		if (entry != null) {
			ItemStack primaryOutputStack = entry.value().craft(recipeInput, this.world.getRegistryManager());
			int fuelRequired = entry.value().getCookingTime() << 1;
			if (!primaryOutputStack.isEmpty() && this.consumeFuel(fuelRequired)) {
				DefaultedList<ItemStack> remainder = entry.value().getRemainder(recipeInput);
				itemEntity.setStack(inputStack.copyWithCount(inputStack.getCount() - 1));
				this.world.spawnEntity(new ItemEntity(this.world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), primaryOutputStack));
				for (ItemStack secondaryOutputStack : remainder) {
					if (!secondaryOutputStack.isEmpty()) {
						this.world.spawnEntity(new ItemEntity(this.world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), secondaryOutputStack));
					}
				}
				return true;
			}
		}
		return false;
	}

	public boolean igniteEntity(Entity entity) {
		if (
			!entity.isFireImmune() &&
			this.refuel(IGNITE_ENTITY_COST) &&
			entity.damage(this.damageSource, 2.0F)
		) {
			this.remainingBurnTime.set(this.remainingBurnTime.get() - IGNITE_ENTITY_COST);
			entity.setOnFireFor(8);
			return true;
		}
		else {
			return false;
		}
	}

	public boolean onEntityCollision(Entity entity) {
		return entity instanceof ItemEntity itemEntity ? this.smeltItem(itemEntity) : this.igniteEntity(entity);
	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		if (world != null) {
			this.damageSource = new DamageSource(
				world
				.getRegistryManager()
				.get(RegistryKeys.DAMAGE_TYPE)
				.entryOf(BigTechDamageTypes.IGNITOR),
				this.pos.toCenterPos()
			);
		}
	}

	@Override
	public DefaultedList<ItemStack> getHeldStacks() {
		return this.stacks;
	}

	@Override
	public void setHeldStacks(DefaultedList<ItemStack> inventory) {
		this.stacks = inventory;
	}

	@Override
	public ItemStack getStack() {
		return this.stacks.get(0);
	}

	@Override
	public void setStack(ItemStack stack) {
		this.stacks.set(0, stack);
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.ignitor");
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new IgnitorScreenHandler(BigTechScreenHandlerTypes.IGNITOR, syncId, this, playerInventory, this.totalBurnTime, this.remainingBurnTime);
	}

	@Override
	public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.put("stack", this.getStack().encodeAllowEmpty(registryLookup));
		nbt.putInt("fuel", this.remainingBurnTime.get());
		nbt.putInt("maxFuel", this.remainingBurnTime.get());
	}

	@Override
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.setStack(ItemStack.fromNbtOrEmpty(registryLookup, nbt.getCompound("stack")));
		this.remainingBurnTime.set(nbt.getInt("fuel"));
		this.remainingBurnTime.set(nbt.getInt("maxFuel"));
	}
}