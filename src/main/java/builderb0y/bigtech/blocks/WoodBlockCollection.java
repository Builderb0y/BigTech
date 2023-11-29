package builderb0y.bigtech.blocks;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

import net.minecraft.block.Block;

import builderb0y.bigtech.registrableCollections.WoodRegistrableCollection;

public class WoodBlockCollection extends WoodRegistrableCollection<Block> {

	public WoodBlockCollection(
		String suffix,
		Block oak,
		Block spruce,
		Block birch,
		Block jungle,
		Block acacia,
		Block dark_oak,
		Block mangrove,
		Block cherry,
		Block crimson,
		Block warped
	) {
		super(
			suffix,
			oak,
			spruce,
			birch,
			jungle,
			acacia,
			dark_oak,
			mangrove,
			cherry,
			crimson,
			warped
		);
	}

	public WoodBlockCollection(String suffix, WoodRegistrableFactory<Block> factory) {
		super(suffix, factory);
	}

	@Override
	public void register(String suffix) {
		BigTechBlocks.register(     "oak_" + suffix, this.oak);
		BigTechBlocks.register(  "spruce_" + suffix, this.spruce);
		BigTechBlocks.register(   "birch_" + suffix, this.birch);
		BigTechBlocks.register(  "jungle_" + suffix, this.jungle);
		BigTechBlocks.register(  "acacia_" + suffix, this.acacia);
		BigTechBlocks.register("dark_oak_" + suffix, this.dark_oak);
		BigTechBlocks.register("mangrove_" + suffix, this.mangrove);
		BigTechBlocks.register(  "cherry_" + suffix, this.cherry);
		BigTechBlocks.register( "crimson_" + suffix, this.crimson);
		BigTechBlocks.register(  "warped_" + suffix, this.warped);

		FlammableBlockRegistry.getDefaultInstance().add(this.oak,      5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.spruce,   5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.birch,    5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.jungle,   5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.acacia,   5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.dark_oak, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.mangrove, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(this.cherry,   5, 20);
		//FlammableBlockRegistry.getDefaultInstance().add(this.crimson,  5, 20);
		//FlammableBlockRegistry.getDefaultInstance().add(this.warped,   5, 20);
	}
}