package builderb0y.bigtech.blocks;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

import net.minecraft.block.Block;

import builderb0y.bigtech.registrableCollections.WoodRegistrableCollection;

public class WoodBlockCollection extends WoodRegistrableCollection<Block> {

	public WoodBlockCollection(
		boolean register,
		Block oak,
		Block spruce,
		Block birch,
		Block jungle,
		Block acacia,
		Block dark_oak,
		Block mangrove,
		Block cherry,
		Block pale_oak,
		Block crimson,
		Block warped
	) {
		super(
			register,
			oak,
			spruce,
			birch,
			jungle,
			acacia,
			dark_oak,
			mangrove,
			cherry,
			pale_oak,
			crimson,
			warped
		);
	}

	public WoodBlockCollection(boolean register, WoodRegistrableFactory<Block> factory) {
		super(register, factory);
	}

	@Override
	public void register() {
		BigTechBlocks.register(this.oak);
		BigTechBlocks.register(this.spruce);
		BigTechBlocks.register(this.birch);
		BigTechBlocks.register(this.jungle);
		BigTechBlocks.register(this.acacia);
		BigTechBlocks.register(this.dark_oak);
		BigTechBlocks.register(this.mangrove);
		BigTechBlocks.register(this.cherry);
		BigTechBlocks.register(this.pale_oak);
		BigTechBlocks.register(this.crimson);
		BigTechBlocks.register(this.warped);

		FlammableBlockRegistry.getDefaultInstance().add(this.oak,      5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.spruce,   5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.birch,    5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.jungle,   5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.acacia,   5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.dark_oak, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.mangrove, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.cherry,   5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.pale_oak, 5, 20);
		//FlammableBlockRegistry.getDefaultInstance().add(this.crimson,  5, 20);
		//FlammableBlockRegistry.getDefaultInstance().add(this.warped,   5, 20);
	}
}