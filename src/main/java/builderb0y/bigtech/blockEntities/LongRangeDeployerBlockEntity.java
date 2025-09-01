package builderb0y.bigtech.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.AbstractNbtNumber;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.gui.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.gui.screenHandlers.LongRangeDeployerScreenHandler;

public class LongRangeDeployerBlockEntity extends AbstractDeployerBlockEntity {

	public Property everywhere = Property.create();

	public LongRangeDeployerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public LongRangeDeployerBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.LONG_RANGE_DEPLOYER, pos, state);
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new LongRangeDeployerScreenHandler(BigTechScreenHandlerTypes.LONG_RANGE_DEPLOYER, syncId, this, playerInventory, this.everywhere);
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.long_range_deployer");
	}

	@Override
	public void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		if (nbt.get("everywhere") instanceof AbstractNbtNumber everywhere) this.everywhere.set(everywhere.intValue());
	}

	@Override
	public void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.putBoolean("everywhere", this.everywhere.get() != 0);
	}
}