package builderb0y.bigtech.blocks;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;

import net.minecraft.block.Block;

import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

public class CopperBlockCollection extends CopperRegistrableCollection<Block> {

	public CopperBlockCollection(
		String suffix,
		Block copper,
		Block exposed_copper,
		Block weathered_copper,
		Block oxidized_copper,
		Block waxed_copper,
		Block waxed_exposed_copper,
		Block waxed_weathered_copper,
		Block waxed_oxidized_copper
	) {
		super(
			suffix,
			copper,
			exposed_copper,
			weathered_copper,
			oxidized_copper,
			waxed_copper,
			waxed_exposed_copper,
			waxed_weathered_copper,
			waxed_oxidized_copper
		);
	}

	public CopperBlockCollection(String suffix, SeparateCopperRegistrableFactory<Block> unwaxedFactory, SeparateCopperRegistrableFactory<Block> waxedFactory) {
		super(suffix, unwaxedFactory, waxedFactory);
	}

	public CopperBlockCollection(String suffix, MergedCopperRegistrableFactory<Block> factory) {
		super(suffix, factory);
	}

	@Override
	public void register(String suffix) {
		BigTechBlocks.register(                "copper_" + suffix, this.                copper);
		BigTechBlocks.register(        "exposed_copper_" + suffix, this.        exposed_copper);
		BigTechBlocks.register(      "weathered_copper_" + suffix, this.      weathered_copper);
		BigTechBlocks.register(       "oxidized_copper_" + suffix, this.       oxidized_copper);
		BigTechBlocks.register(          "waxed_copper_" + suffix, this.          waxed_copper);
		BigTechBlocks.register(  "waxed_exposed_copper_" + suffix, this.  waxed_exposed_copper);
		BigTechBlocks.register("waxed_weathered_copper_" + suffix, this.waxed_weathered_copper);
		BigTechBlocks.register( "waxed_oxidized_copper_" + suffix, this. waxed_oxidized_copper);

		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.          copper, this.  exposed_copper);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.  exposed_copper, this.weathered_copper);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.weathered_copper, this. oxidized_copper);

		OxidizableBlocksRegistry.registerWaxableBlockPair(this.          copper, this.          waxed_copper);
		OxidizableBlocksRegistry.registerWaxableBlockPair(this.  exposed_copper, this.  waxed_exposed_copper);
		OxidizableBlocksRegistry.registerWaxableBlockPair(this.weathered_copper, this.waxed_weathered_copper);
		OxidizableBlocksRegistry.registerWaxableBlockPair(this. oxidized_copper, this. waxed_oxidized_copper);
	}
}