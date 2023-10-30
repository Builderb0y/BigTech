package builderb0y.bigtech.items;

import net.minecraft.item.Item;

import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

public class CopperItemCollection extends CopperRegistrableCollection<Item> {

	public CopperItemCollection(
		String suffix,
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

	public CopperItemCollection(String suffix, SeparateCopperBlockFactory<Item> unwaxedFactory, SeparateCopperBlockFactory<Item> waxedFactory) {
		super(suffix, unwaxedFactory, waxedFactory);
	}

	public CopperItemCollection(String suffix, MergedCopperBlockFactory<Item> factory) {
		super(suffix, factory);
	}

	@Override
	public void register(String suffix) {
		BigTechItems.register(                "copper_" + suffix, this.                copper);
		BigTechItems.register(        "exposed_copper_" + suffix, this.        exposed_copper);
		BigTechItems.register(      "weathered_copper_" + suffix, this.      weathered_copper);
		BigTechItems.register(       "oxidized_copper_" + suffix, this.       oxidized_copper);
		BigTechItems.register(          "waxed_copper_" + suffix, this.          waxed_copper);
		BigTechItems.register(  "waxed_exposed_copper_" + suffix, this.  waxed_exposed_copper);
		BigTechItems.register("waxed_weathered_copper_" + suffix, this.waxed_weathered_copper);
		BigTechItems.register( "waxed_oxidized_copper_" + suffix, this. waxed_oxidized_copper);
	}
}