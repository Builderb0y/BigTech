package builderb0y.bigtech.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.gui.TechnoCrafterAccess;
import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.gui.screenHandlers.TechnoCrafterScreenHandler;

public class TechnoCrafterBlockEntity extends LootableContainerBlockEntity implements TechnoCrafterAccess {

	public boolean interactedRight;
	public SplitStackList stacks;

	public TechnoCrafterBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
		this.stacks = SplitStackList.create();
	}

	public TechnoCrafterBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.TECHNO_CRAFTER, pos, state);
	}

	@Override
	public boolean getInteractionSide() {
		return this.interactedRight;
	}

	@Override
	public void setInteractionSide(boolean interactRight) {
		this.interactedRight = interactRight;
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.techno_crafter");
	}

	@Override
	public DefaultedList<ItemStack> getHeldStacks() {
		return this.stacks.heldStacks();
	}

	@Override
	public void setHeldStacks(DefaultedList<ItemStack> inventory) {
		this.stacks = SplitStackList.create(inventory);
	}

	@Override
	public DefaultedList<ItemStack> getStacks(boolean interactionSide) {
		return this.stacks.getStacks(interactionSide);
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new TechnoCrafterScreenHandler(BigTechScreenHandlerTypes.TECHNO_CRAFTER, syncId, this, playerInventory);
	}

	@Override
	public int size() {
		return 18;
	}

	@Override
	public void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		Inventories.writeNbt(nbt, this.stacks.heldStacks(), registryLookup);
	}

	@Override
	public void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		Inventories.readNbt(nbt, this.stacks.heldStacks(), registryLookup);
	}
}