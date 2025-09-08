package builderb0y.bigtech.blockEntities;

import java.util.stream.IntStream;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.gui.screenHandlers.SorterBeltScreenHandler;
import builderb0y.bigtech.util.FairSharing;
import builderb0y.bigtech.util.Inventories2;
import net.minecraft.inventory.StackWithSlot;

public class SorterBeltBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {

	public static final Object2ObjectMap<EntityType<?>, EntityStackMatcher<?>> MATCHERS = new Object2ObjectOpenHashMap<>(16);
	static {
		MATCHERS.defaultReturnValue((Entity entity, ItemStack stack) -> {
			return stack.getItem() == SpawnEggItem.forEntity(entity.getType()) ? Integer.MAX_VALUE : 0;
		});
		registerMatcher(EntityType.ITEM, (ItemEntity entity, ItemStack inventoryStack) -> {
			ItemStack entityStack = entity.getStack();
			if (inventoryStack.getItem() != entityStack.getItem()) return 0;

			int similarity = 1;
			if (inventoryStack.isDamaged()) {
				if (inventoryStack.getDamage() == entityStack.getDamage()) similarity += 2;
				else return 0;
			}
			else {
				similarity += entityStack.isDamaged() ? 1 : 2;
			}

			ComponentChanges changes = inventoryStack.getComponentChanges();
			if (!changes.isEmpty()) {
				if (changes.equals(entityStack.getComponentChanges())) similarity += 2;
				else return 0;
			}
			else {
				similarity += entityStack.getComponentChanges().isEmpty() ? 2 : 1;
			}

			return similarity;
		});
		registerMatcher(EntityType.EXPERIENCE_ORB, EntityStackMatcher.forItem(Items.EXPERIENCE_BOTTLE));
		registerMatcher(EntityType.PLAYER, (PlayerEntity player, ItemStack stack) -> {
			if (stack.getItem() != Items.PLAYER_HEAD) return 0;
			ProfileComponent profileComponent = stack.get(DataComponentTypes.PROFILE);
			if (profileComponent == null) return 1;
			return profileComponent.gameProfile().equals(player.getGameProfile()) ? Integer.MAX_VALUE : 0;
		});
	}

	public static <E extends Entity> void registerMatcher(EntityType<E> entityType, EntityStackMatcher<E> matcher) {
		MATCHERS.put(entityType, matcher);
	}

	@SuppressWarnings("unchecked")
	public static <E extends Entity> EntityStackMatcher<E> getMatcher(EntityType<E> type) {
		return (EntityStackMatcher<E>)(MATCHERS.get(type));
	}

	public final SorterBeltInventory inventory;
	public int distributionIndex;
	public @Nullable Text customName;
	public ContainerLock lock = ContainerLock.EMPTY;

	public SorterBeltBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.inventory = new SorterBeltInventory(this);
	}

	public SorterBeltBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.SORTER_BELT, pos, state);
	}

	public Direction getDistributionDirection(Entity entity) {
		Direction forward = this.getCachedState().get(Properties.HORIZONTAL_FACING);
		@SuppressWarnings("unchecked")
		EntityStackMatcher<Entity> matcher = (EntityStackMatcher<Entity>)(getMatcher(entity.getType()));
		int  leftMatch = this.getMatch(0,  9, entity, matcher);
		int rightMatch = this.getMatch(9, 18, entity, matcher);

		if (leftMatch > rightMatch) return forward.rotateYCounterclockwise();
		if (rightMatch > leftMatch) return forward.rotateYClockwise();
		if (leftMatch != 0) return this.nextFairSharingDirection(forward);
		return forward;
	}

	public <E extends Entity> int getMatch(int startIndex, int endIndex, E entity, EntityStackMatcher<E> matcher) {
		int match = 0;
		for (int slot = startIndex; slot < endIndex; slot++) {
			ItemStack stack = this.inventory.getStack(slot);
			if (!stack.isEmpty()) {
				int newMatch = matcher.test(entity, stack);
				if (newMatch > match && (match = newMatch) == Integer.MAX_VALUE) break;
			}
		}
		return match;
	}

	public Direction nextFairSharingDirection(Direction forward) {
		return (
			FairSharing.compute2(this.distributionIndex++) != 0
			? forward.rotateYClockwise()
			: forward.rotateYCounterclockwise()
		);
	}

	@Override
	public Text getDisplayName() {
		return this.customName != null ? this.customName : Text.translatable("container.bigtech.sorter_belt");
	}

	@Override
	public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		if (LockableContainerBlockEntity.checkUnlocked(player, this.lock, this.getDisplayName())) {
			return new SorterBeltScreenHandler(BigTechScreenHandlerTypes.SORTER_BELT, syncId, this.inventory, playerInventory);
		}
		else {
			return null;
		}
	}

	@Override
	public void readData(ReadView view) {
		super.readData(view);
		this.distributionIndex = view.getInt("index", 0);
		this.inventory.clear();
		Inventories2.readItems(view, "items").forEach(Inventories2.setter(this.inventory));
		this.lock = ContainerLock.read(view);
		this.customName = tryParseCustomName(view, "CustomName");
	}

	@Override
	public void writeData(WriteView view) {
		super.writeData(view);
		view.putInt("index", this.distributionIndex);
		Inventories2.writeItems(view, "items", Inventories2.stream(this.inventory, true));
		this.lock.write(view);
		if (this.customName != null) view.put("CustomName", TextCodecs.CODEC, this.customName);
	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		if (world != null) this.distributionIndex = world.random.nextInt();
	}

	public static class SorterBeltInventory extends SimpleInventory {

		public final SorterBeltBlockEntity blockEntity;

		public SorterBeltInventory(SorterBeltBlockEntity entity) {
			super(18);
			this.blockEntity = entity;
		}

		@Override
		public int getMaxCountPerStack() {
			return 1;
		}

		@Override
		public void markDirty() {
			this.blockEntity.markDirty();
		}
	}
}