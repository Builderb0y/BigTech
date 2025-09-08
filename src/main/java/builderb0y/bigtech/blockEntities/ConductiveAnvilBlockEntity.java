package builderb0y.bigtech.blockEntities;

import it.unimi.dsi.fastutil.objects.AbstractObject2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.mixins.ItemEnchantmentsComponentBuilder_BackingMapAccess;
import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.gui.screenHandlers.ConductiveAnvilScreenHandler;

public class ConductiveAnvilBlockEntity extends LockableContainerBlockEntity {

	public DefaultedList<ItemStack> heldStacks = DefaultedList.ofSize(2, ItemStack.EMPTY);

	public ConductiveAnvilBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	public ConductiveAnvilBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.CONDUCTIVE_ANVIL, pos, state);
	}

	public void onLightningPulse(int energy) {
		ItemStack from = this.heldStacks.get(0);
		ItemStack to   = this.heldStacks.get(1);
		if (EnchantmentHelper.hasEnchantments(from) && isEnchantable(to)) {
			ItemEnchantmentsComponent.Builder fromBuilder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(from));
			ItemEnchantmentsComponent.Builder toBuilder = new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(to));
			while (true) {
				//1: choose a randomly-weighted enchantment to transfer.
				Object2IntMap.Entry<RegistryEntry<Enchantment>> toTransfer = this.chooseRandomEnchantment(fromBuilder);
				if (toTransfer == null) break;

				//2: remove that enchantment from the first item.
				//you will always lose the enchantment regardless
				//of whether or not it can be added to the 2nd item.
				//this is 100% intentional.
				fromBuilder.set(toTransfer.getKey(), toTransfer.getIntValue() - 1);
				//3: check if the enchantment can be added to the 2nd item.
				if (!canApply(toTransfer.getKey(), toBuilder)) break;

				int newLevel = toBuilder.getLevel(toTransfer.getKey()) + 1;
				if (newLevel > toTransfer.getKey().value().getMaxLevel()) break;
				int cost = newLevel * 100;
				if (energy < cost) break;

				//4: add the enchantment to the 2nd item,
				//and consume energy in the process.
				toBuilder.set(toTransfer.getKey(), newLevel);
				energy -= cost;
			}
			this.setEnchantments(0, from, fromBuilder);
			this.setEnchantments(1, to,   toBuilder  );
			this.markDirty();
		}
		//todo: consider damaging or destroying items?
	}

	public static boolean isEnchantable(ItemStack stack) {
		return stack.get(DataComponentTypes.ENCHANTABLE) != null;
	}

	public void setEnchantments(int slot, ItemStack stack, ItemEnchantmentsComponent.Builder enchantments) {
		if (enchantments.<ItemEnchantmentsComponentBuilder_BackingMapAccess>as().bigtech_getEnchantments().isEmpty()) {
			if (stack.isOf(Items.ENCHANTED_BOOK)) {
				ItemStack newStack = stack.copyComponentsToNewStack(Items.BOOK, stack.getCount());
				newStack.remove(DataComponentTypes.STORED_ENCHANTMENTS);
				this.setStack(slot, newStack);
			}
			else {
				EnchantmentHelper.set(stack, enchantments.build());
			}
		}
		else {
			if (stack.isOf(Items.BOOK)) {
				stack = stack.copyComponentsToNewStack(Items.ENCHANTED_BOOK, stack.getCount());
				this.setStack(slot, stack);
			}
			EnchantmentHelper.set(stack, enchantments.build());
		}
	}

	public static boolean canApply(RegistryEntry<Enchantment> from, ItemEnchantmentsComponent.Builder to) {
		for (RegistryEntry<Enchantment> onTo : to.getEnchantments()) {
			if (from.equals(onTo)) continue;
			if (from.value().exclusiveSet().contains(onTo) || onTo.value().exclusiveSet().contains(from)) return false;
		}
		return true;
	}

	public Object2IntMap.@Nullable Entry<RegistryEntry<Enchantment>> chooseRandomEnchantment(ItemEnchantmentsComponent.Builder enchantments) {
		if (
			enchantments
			.<ItemEnchantmentsComponentBuilder_BackingMapAccess>as()
			.bigtech_getEnchantments()
			.isEmpty()
		) {
			return null;
		}
		RegistryEntry<Enchantment> result = null;
		int resultLevel = 0;
		int totalWeight = 0;
		for (
			ObjectIterator<Object2IntMap.Entry<RegistryEntry<Enchantment>>> iterator = (
				enchantments
				.<ItemEnchantmentsComponentBuilder_BackingMapAccess>as()
				.bigtech_getEnchantments()
				.object2IntEntrySet()
				.fastIterator()
			);
			iterator.hasNext();
		) {
			Object2IntMap.Entry<RegistryEntry<Enchantment>> entry = iterator.next();
			int nextWeight = entry.getIntValue();
			if (this.world.random.nextInt(totalWeight += nextWeight) < nextWeight) {
				result = entry.getKey();
				resultLevel = nextWeight;
			}
		}
		if (result == null) return null;
		return new AbstractObject2IntMap.BasicEntry<>(result, resultLevel);
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.conductive_anvil");
	}

	@Override
	public DefaultedList<ItemStack> getHeldStacks() {
		return this.heldStacks;
	}

	@Override
	public void setHeldStacks(DefaultedList<ItemStack> heldStacks) {
		this.heldStacks = heldStacks;
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new ConductiveAnvilScreenHandler(BigTechScreenHandlerTypes.CONDUCTIVE_ANVIL, syncId, this, playerInventory);
	}

	@Override
	public int size() {
		return 2;
	}

	@Override
	public int getMaxCountPerStack() {
		return 1;
	}

	@Override
	public void readData(ReadView view) {
		super.readData(view);
		Inventories.readData(view, this.heldStacks);
	}

	@Override
	public void writeData(WriteView view) {
		super.writeData(view);
		Inventories.writeData(view, this.heldStacks);
	}
}