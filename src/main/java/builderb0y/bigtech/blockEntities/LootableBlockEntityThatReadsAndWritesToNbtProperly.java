package builderb0y.bigtech.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;

public abstract class LootableBlockEntityThatReadsAndWritesToNbtProperly extends LootableContainerBlockEntity {

	public LootableBlockEntityThatReadsAndWritesToNbtProperly(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	@Override
	public int size() {
		return this.getHeldStacks().size();
	}

	@Override
	public void readData(ReadView view) {
		super.readData(view);
		this.getHeldStacks().clear();
		if (!this.readLootTable(view)) {
			Inventories.readData(view, this.getHeldStacks());
		}
	}

	@Override
	public void writeData(WriteView view) {
		super.writeData(view);
		if (!this.writeLootTable(view)) {
			Inventories.writeData(view, this.getHeldStacks());
		}
	}
}