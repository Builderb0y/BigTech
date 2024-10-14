package builderb0y.bigtech.blockEntities;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.gui.TechnoCrafterAccess;
import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.gui.screenHandlers.TechnoCrafterScreenHandler;

public class TechnoCrafterBlockEntity extends LootableBlockEntityThatReadsAndWritesToNbtProperly implements TechnoCrafterAccess, ExtendedScreenHandlerFactory<Byte> {

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
	public Byte getScreenOpeningData(ServerPlayerEntity player) {
		return -1;
	}
}