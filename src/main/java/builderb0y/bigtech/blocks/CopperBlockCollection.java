package builderb0y.bigtech.blocks;

import java.util.function.Function;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;

import net.minecraft.block.Block;

import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

public class CopperBlockCollection extends CopperRegistrableCollection<Block> {

	public CopperBlockCollection(
		boolean register,
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
			register,
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

	public CopperBlockCollection(boolean register, SeparateCopperRegistrableFactory<Block> unwaxedFactory, SeparateCopperRegistrableFactory<Block> waxedFactory) {
		super(register, unwaxedFactory, waxedFactory);
	}

	public CopperBlockCollection(boolean register, MergedCopperRegistrableFactory<Block> factory) {
		super(register, factory);
	}

	@Override
	public void register() {
		BigTechBlocks.register(this.                copper);
		BigTechBlocks.register(this.        exposed_copper);
		BigTechBlocks.register(this.      weathered_copper);
		BigTechBlocks.register(this.       oxidized_copper);
		BigTechBlocks.register(this.          waxed_copper);
		BigTechBlocks.register(this.  waxed_exposed_copper);
		BigTechBlocks.register(this.waxed_weathered_copper);
		BigTechBlocks.register(this. waxed_oxidized_copper);

		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.          copper, this.  exposed_copper);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.  exposed_copper, this.weathered_copper);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.weathered_copper, this. oxidized_copper);

		OxidizableBlocksRegistry.registerWaxableBlockPair(this.          copper, this.          waxed_copper);
		OxidizableBlocksRegistry.registerWaxableBlockPair(this.  exposed_copper, this.  waxed_exposed_copper);
		OxidizableBlocksRegistry.registerWaxableBlockPair(this.weathered_copper, this.waxed_weathered_copper);
		OxidizableBlocksRegistry.registerWaxableBlockPair(this. oxidized_copper, this. waxed_oxidized_copper);
	}
}