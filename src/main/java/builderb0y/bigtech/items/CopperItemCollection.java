package builderb0y.bigtech.items;

import java.util.function.Function;

import net.minecraft.item.Item;

import builderb0y.bigtech.registrableCollections.CopperRegistrableCollection;

public class CopperItemCollection extends CopperRegistrableCollection<Item> {

	public CopperItemCollection(
		boolean register,
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

	public CopperItemCollection(boolean register, SeparateCopperRegistrableFactory<Item> unwaxedFactory, SeparateCopperRegistrableFactory<Item> waxedFactory) {
		super(register, unwaxedFactory, waxedFactory);
	}

	public CopperItemCollection(boolean register, MergedCopperRegistrableFactory<Item> factory) {
		super(register, factory);
	}

	@Override
	public void register() {
		BigTechItems.register(this.                copper);
		BigTechItems.register(this.        exposed_copper);
		BigTechItems.register(this.      weathered_copper);
		BigTechItems.register(this.       oxidized_copper);
		BigTechItems.register(this.          waxed_copper);
		BigTechItems.register(this.  waxed_exposed_copper);
		BigTechItems.register(this.waxed_weathered_copper);
		BigTechItems.register(this. waxed_oxidized_copper);
	}
}