package builderb0y.bigtech.items;

import net.minecraft.item.Item;

import builderb0y.bigtech.registrableCollections.WoodRegistrableCollection;

public class WoodItemCollection extends WoodRegistrableCollection<Item> {

	public WoodItemCollection(
		boolean register,
		Item oak,
		Item spruce,
		Item birch,
		Item jungle,
		Item acacia,
		Item dark_oak,
		Item mangrove,
		Item cherry,
		Item pale_oak,
		Item crimson,
		Item warped
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

	public WoodItemCollection(boolean register, WoodRegistrableFactory<Item> factory) {
		super(register, factory);
	}

	@Override
	public void register() {
		BigTechItems.register(this.oak);
		BigTechItems.register(this.spruce);
		BigTechItems.register(this.birch);
		BigTechItems.register(this.jungle);
		BigTechItems.register(this.acacia);
		BigTechItems.register(this.dark_oak);
		BigTechItems.register(this.mangrove);
		BigTechItems.register(this.cherry);
		BigTechItems.register(this.pale_oak);
		BigTechItems.register(this.crimson);
		BigTechItems.register(this.warped);
	}
}