package builderb0y.bigtech.items;

import java.util.function.Function;

import net.minecraft.item.Item;

import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

public class CopperItemCollection extends CopperRegistrableCollection<Item> {

	public CopperItemCollection(
		Function<Type, String> namer,
		Item copper,
		Item exposed_copper,
		Item weathered_copper,
		Item oxidized_copper,
		Item waxed_copper,
		Item waxed_exposed_copper,
		Item waxed_weathered_copper,
		Item waxed_oxidized_copper
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

	public CopperItemCollection(Function<Type, String> namer, SeparateCopperRegistrableFactory<Item> unwaxedFactory, SeparateCopperRegistrableFactory<Item> waxedFactory) {
		super(namer, unwaxedFactory, waxedFactory);
	}

	public CopperItemCollection(Function<Type, String> namer, MergedCopperRegistrableFactory<Item> factory) {
		super(namer, factory);
	}

	@Override
	public void register(Function<Type, String> namer) {
		BigTechItems.register(namer.apply(Type.COPPER),                 this.                copper);
		BigTechItems.register(namer.apply(Type.EXPOSED_COPPER),         this.        exposed_copper);
		BigTechItems.register(namer.apply(Type.WEATHERED_COPPER),       this.      weathered_copper);
		BigTechItems.register(namer.apply(Type.OXIDIZED_COPPER),        this.       oxidized_copper);
		BigTechItems.register(namer.apply(Type.WAXED_COPPER),           this.          waxed_copper);
		BigTechItems.register(namer.apply(Type.WAXED_EXPOSED_COPPER),   this.  waxed_exposed_copper);
		BigTechItems.register(namer.apply(Type.WAXED_WEATHERED_COPPER), this.waxed_weathered_copper);
		BigTechItems.register(namer.apply(Type.WAXED_OXIDIZED_COPPER),  this. waxed_oxidized_copper);
	}
}