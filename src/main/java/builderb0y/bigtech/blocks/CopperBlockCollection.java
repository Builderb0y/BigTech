package builderb0y.bigtech.blocks;

import java.util.function.Function;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;

import net.minecraft.block.Block;

import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

public class CopperBlockCollection extends CopperRegistrableCollection<Block> {

	public CopperBlockCollection(
		Function<Type, String> namer,
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
			namer,
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

	public CopperBlockCollection(Function<Type, String> namer, SeparateCopperRegistrableFactory<Block> unwaxedFactory, SeparateCopperRegistrableFactory<Block> waxedFactory) {
		super(namer, unwaxedFactory, waxedFactory);
	}

	public CopperBlockCollection(Function<Type, String> namer, MergedCopperRegistrableFactory<Block> factory) {
		super(namer, factory);
	}

	@Override
	public void register(Function<Type, String> namer) {
		BigTechBlocks.register(namer.apply(Type.COPPER),                 this.                copper);
		BigTechBlocks.register(namer.apply(Type.EXPOSED_COPPER),         this.        exposed_copper);
		BigTechBlocks.register(namer.apply(Type.WEATHERED_COPPER),       this.      weathered_copper);
		BigTechBlocks.register(namer.apply(Type.OXIDIZED_COPPER),        this.       oxidized_copper);
		BigTechBlocks.register(namer.apply(Type.WAXED_COPPER),           this.          waxed_copper);
		BigTechBlocks.register(namer.apply(Type.WAXED_EXPOSED_COPPER),   this.  waxed_exposed_copper);
		BigTechBlocks.register(namer.apply(Type.WAXED_WEATHERED_COPPER), this.waxed_weathered_copper);
		BigTechBlocks.register(namer.apply(Type.WAXED_OXIDIZED_COPPER),  this. waxed_oxidized_copper);

		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.          copper, this.  exposed_copper);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.  exposed_copper, this.weathered_copper);
		OxidizableBlocksRegistry.registerOxidizableBlockPair(this.weathered_copper, this. oxidized_copper);

		OxidizableBlocksRegistry.registerWaxableBlockPair(this.          copper, this.          waxed_copper);
		OxidizableBlocksRegistry.registerWaxableBlockPair(this.  exposed_copper, this.  waxed_exposed_copper);
		OxidizableBlocksRegistry.registerWaxableBlockPair(this.weathered_copper, this.waxed_weathered_copper);
		OxidizableBlocksRegistry.registerWaxableBlockPair(this. oxidized_copper, this. waxed_oxidized_copper);
	}
}