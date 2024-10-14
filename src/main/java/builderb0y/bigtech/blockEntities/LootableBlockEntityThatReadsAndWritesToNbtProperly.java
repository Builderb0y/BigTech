package builderb0y.bigtech.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
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
	public void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.getHeldStacks().clear();
		if (!this.readLootTable(nbt)) {
			Inventories.readNbt(nbt, this.getHeldStacks(), registryLookup);
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		if (!this.writeLootTable(nbt)) {
			Inventories.writeNbt(nbt, this.getHeldStacks(), registryLookup);
		}
	}
}