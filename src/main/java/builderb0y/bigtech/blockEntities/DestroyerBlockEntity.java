package builderb0y.bigtech.blockEntities;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.api.EntityAddedToWorldEvent;
import builderb0y.bigtech.beams.impl.DestroyerBeam;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;
import builderb0y.bigtech.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.screenHandlers.DestroyerScreenHandler;

public class DestroyerBlockEntity extends LootableContainerBlockEntity {

	public static final String IS_SUITABLE_FOR_METHOD_NAME = MixinEnvironment.defaultEnvironment.remappers.mapMethodName("net/minecraft/class_1792", "method_7856", "(Lnet/minecraft/class_2680;)Z");
	public static final ClassValue<Boolean> OVERRIDES_IS_SUITABLE_FOR = new ClassValue<>() {

		@Override
		public Boolean computeValue(Class<?> type) {
			if (!Item.class.isAssignableFrom(type)) {
				throw new IllegalArgumentException("Can only query Item's");
			}
			try {
				return type.getMethod(IS_SUITABLE_FOR_METHOD_NAME, BlockState.class).getDeclaringClass() != Item.class;
			}
			catch (NoSuchMethodException exception) {
				throw new AssertionError("isSuitableFor() does not exist?", exception);
			}
		}
	};
	public static final BlockEntityTicker<DestroyerBlockEntity> SERVER_TICKER = (world, pos, state, blockEntity) -> blockEntity.serverTick();
	public static final ThreadLocal<DestroyerBlockEntity> ACTIVE_DESTROYER = new ThreadLocal<>();
	static {
		EntityAddedToWorldEvent.EVENT.register(entity -> {
			DestroyerBlockEntity blockEntity = ACTIVE_DESTROYER.get();
			if (blockEntity != null) {
				Direction front = blockEntity.cachedState.get(Properties.HORIZONTAL_FACING);
				Direction back = front.opposite;
				BlockPos backPos = blockEntity.pos.offset(back);
				if (entity instanceof ItemEntity itemEntity) {
					ItemStack stack = itemEntity.getStack();
					Storage<ItemVariant> storage = ItemStorage.SIDED.find(entity.world, backPos, front);
					if (storage != null) {
						try (Transaction transaction = Transaction.openOuter()) {
							int moved = (int)(storage.insert(ItemVariant.of(stack), stack.getCount(), transaction));
							if (moved > 0) {
								stack = stack.copyWithCount(stack.getCount() - moved);
								itemEntity.setStack(stack);
								transaction.commit();
								return !stack.isEmpty;
							}
						}
					}
				}
				double width = entity.getType().width;
				double x = backPos.x + 0.5D + front.offsetX * (0.499D - width * 0.5D);
				double y = backPos.y + 0.25D;
				double z = backPos.z + 0.5D + front.offsetZ * (0.499D - width * 0.5D);
				Box box = entity.getType().createSimpleBoundingBox(x, y, z);
				if (!entity.world.getBlockCollisions(entity, box).iterator().hasNext()) {
					entity.refreshPositionAndAngles(x, y, z, entity.yaw, entity.pitch);
					entity.setVelocity(back.offsetX * 0.125D, 0.0D, back.offsetZ * 0.125D);
				}
			}
			return true;
		});
	}

	public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

	public DestroyerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public DestroyerBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.DESTROYER, pos, state);
	}

	public void serverTick() {
		if (this.cachedState.get(Properties.POWERED) && CommonWorldBeamStorage.KEY.get(this.world).getBeam(this.pos) instanceof DestroyerBeam destroyerBeam) {
			DestroyerBlockEntity old = ACTIVE_DESTROYER.get();
			ACTIVE_DESTROYER.set(this);
			try {
				if (destroyerBeam.serverTick(this.inventory.get(0))) {
					this.markDirty();
				}
			}
			finally {
				ACTIVE_DESTROYER.set(old);
			}
		}
	}

	@Override
	public DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}

	@Override
	public void setInvStackList(DefaultedList<ItemStack> list) {
		this.inventory = list;
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.destroyer");
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
		return OVERRIDES_IS_SUITABLE_FOR.get(stack.item.getClass());
	}
}