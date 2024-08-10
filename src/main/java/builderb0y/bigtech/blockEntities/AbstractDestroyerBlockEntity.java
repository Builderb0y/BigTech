package builderb0y.bigtech.blockEntities;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.api.EntityAddedToWorldEvent;
import builderb0y.bigtech.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.screenHandlers.DestroyerScreenHandler;

public abstract class AbstractDestroyerBlockEntity extends LootableContainerBlockEntity {

	public static final ThreadLocal<AbstractDestroyerBlockEntity> ACTIVE_DESTROYER = new ThreadLocal<>();
	static {
		EntityAddedToWorldEvent.EVENT.register((Entity entity) -> {
			AbstractDestroyerBlockEntity blockEntity = ACTIVE_DESTROYER.get();
			if (blockEntity != null) {
				Direction front = blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING);
				Direction back = front.getOpposite();
				BlockPos backPos = blockEntity.getPos().offset(back);
				if (entity instanceof ItemEntity itemEntity) {
					ItemStack stack = itemEntity.getStack();
					Storage<ItemVariant> storage = ItemStorage.SIDED.find(entity.getWorld(), backPos, front);
					if (storage != null) {
						try (Transaction transaction = Transaction.openOuter()) {
							int moved = (int)(storage.insert(ItemVariant.of(stack), stack.getCount(), transaction));
							if (moved > 0) {
								stack = stack.copyWithCount(stack.getCount() - moved);
								itemEntity.setStack(stack);
								transaction.commit();
								return !stack.isEmpty();
							}
						}
					}
				}
				double width = entity.getType().getWidth();
				double x = backPos.getX() + 0.5D + front.getOffsetX() * (0.499D - width * 0.5D);
				double y = backPos.getY() + 0.25D;
				double z = backPos.getZ() + 0.5D + front.getOffsetZ() * (0.499D - width * 0.5D);
				Box box = entity.getType().getSpawnBox(x, y, z);
				if (!entity.getWorld().getBlockCollisions(entity, box).iterator().hasNext()) {
					entity.refreshPositionAndAngles(x, y, z, entity.getYaw(), entity.getPitch());
					entity.setVelocity(back.getOffsetX() * 0.125D, 0.0D, back.getOffsetZ() * 0.125D);
				}
			}
			return true;
		});
	}
	public static final BlockEntityTicker<AbstractDestroyerBlockEntity> SERVER_TICKER = (World world, BlockPos pos, BlockState state, AbstractDestroyerBlockEntity blockEntity) -> blockEntity.tick();

	public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

	public AbstractDestroyerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public void tick() {
		if (this.getCachedState().get(Properties.POWERED)) {
			AbstractDestroyerBlockEntity old = ACTIVE_DESTROYER.get();
			ACTIVE_DESTROYER.set(this);
			try {
				this.doTick();
			}
			finally {
				ACTIVE_DESTROYER.set(old);
			}
		}
	}

	public abstract void doTick();

	@Override
	public DefaultedList<ItemStack> getHeldStacks() {
		return this.inventory;
	}

	@Override
	public void setHeldStacks(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new DestroyerScreenHandler(BigTechScreenHandlerTypes.DESTROYER, syncId, this, playerInventory);
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return stack.get(DataComponentTypes.TOOL) != null;
	}
}