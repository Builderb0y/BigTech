package builderb0y.bigtech.blockEntities;

import java.util.stream.IntStream;

import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PlayerHeadItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.screenHandlers.SorterBeltScreenHandler;
import builderb0y.bigtech.util.FairSharing;
import builderb0y.bigtech.util.Inventories2;
import builderb0y.bigtech.util.Inventories2.SlotStack;

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
			if (inventoryStack.isDamaged) {
				if (inventoryStack.damage == entityStack.damage) similarity += 2;
				else return 0;
			}
			else {
				similarity += entityStack.isDamaged ? 1 : 2;
			}

			if (inventoryStack.hasNbt()) {
				if (inventoryStack.nbt.equals(entityStack.nbt)) similarity += 2;
				else return 0;
			}
			else {
				similarity += entityStack.hasNbt() ? 1 : 2;
			}

			return similarity;
		});
		registerMatcher(EntityType.EXPERIENCE_ORB, EntityStackMatcher.forItem(Items.EXPERIENCE_BOTTLE));
		registerMatcher(EntityType.PLAYER, (PlayerEntity player, ItemStack stack) -> {
			if (stack.getItem() != Items.PLAYER_HEAD) return 0;
			NbtCompound nbt = stack.getNbt();
			if (nbt == null || !nbt.contains(PlayerHeadItem.SKULL_OWNER_KEY, NbtElement.COMPOUND_TYPE)) return 1;
			GameProfile profile = NbtHelper.toGameProfile(nbt.getCompound(PlayerHeadItem.SKULL_OWNER_KEY));
			if (profile == null) return 1;
			return profile.equals(player.gameProfile) ? Integer.MAX_VALUE : 0;
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
		Direction forward = this.cachedState.get(Properties.HORIZONTAL_FACING);
		@SuppressWarnings("unchecked")
		EntityStackMatcher<Entity> matcher = (EntityStackMatcher<Entity>)(getMatcher(entity.type));
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
			if (!stack.isEmpty) {
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
		return this.customName != null ? this.customName : BigTechBlocks.SORTER_BELT.name;
	}

	@Override
	public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		if (LockableContainerBlockEntity.checkUnlocked(player, this.lock, this.displayName)) {
			return new SorterBeltScreenHandler(BigTechScreenHandlerTypes.SORTER_BELT, syncId, this.inventory, playerInventory);
		}
		else {
			return null;
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.distributionIndex = nbt.getInt("index");
		this.inventory.readNbtList(nbt.getList("items", NbtElement.COMPOUND_TYPE));
		this.lock = ContainerLock.fromNbt(nbt);
		if (nbt.contains("CustomName", NbtElement.STRING_TYPE)) {
			this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt
		.withInt("index", this.distributionIndex)
		.with("items", this.inventory.toNbtList());
		this.lock.writeNbt(nbt);
		if (this.customName != null) {
			nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
		}
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
		public void readNbtList(NbtList nbtList) {
			this.clear();
			Inventories2.readItems(nbtList).filter(slotStack -> slotStack.slot >= 0 && slotStack.slot < 18).forEach(slotStack -> this.setStack(slotStack.slot, slotStack.stack()));
		}

		@Override
		public NbtList toNbtList() {
			return Inventories2.writeItems(IntStream.range(0, 18).mapToObj(slot -> new SlotStack(slot, this.getStack(slot))).filter(slotStack -> !slotStack.stack.isEmpty));
		}

		@Override
		public void markDirty() {
			this.blockEntity.markDirty();
		}
	}
}