package builderb0y.bigtech.blockEntities;

import java.util.stream.IntStream;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.gui.TechnoCrafterAccess;
import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.gui.screenHandlers.PlacedTechnoCrafterScreenHandler;

public class TechnoCrafterBlockEntity extends LootableBlockEntityThatReadsAndWritesToNbtProperly implements TechnoCrafterAccess, SidedInventory {

	public static final int[] ACCESSIBLE_SLOTS = IntStream.range(0, 27).toArray();

	public boolean interactedRight;
	public SplitStackList stacks;

	public TechnoCrafterBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
		this.stacks = SplitStackList.createPlaced();
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
		return Text.translatable("container.bigtech.placed_techno_crafter");
	}

	@Override
	public DefaultedList<ItemStack> getHeldStacks() {
		return this.stacks.heldStacks();
	}

	@Override
	public void setHeldStacks(DefaultedList<ItemStack> inventory) {
		this.stacks = SplitStackList.createPlaced(inventory);
	}

	@Override
	public DefaultedList<ItemStack> getStacks(boolean interactionSide) {
		return this.stacks.getStacks(interactionSide);
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new PlacedTechnoCrafterScreenHandler(BigTechScreenHandlerTypes.PLACED_TECHNO_CRAFTER, syncId, this, playerInventory);
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return true;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return true;
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return ACCESSIBLE_SLOTS;
	}
}