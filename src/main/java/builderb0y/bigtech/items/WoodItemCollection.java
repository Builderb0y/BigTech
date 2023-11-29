package builderb0y.bigtech.items;

import net.minecraft.item.Item;

import builderb0y.bigtech.registrableCollections.WoodRegistrableCollection;

public class WoodItemCollection extends WoodRegistrableCollection<Item> {

	public WoodItemCollection(
		String suffix,
		Item oak,
		Item spruce,
		Item birch,
		Item jungle,
		Item acacia,
		Item dark_oak,
		Item mangrove,
		Item cherry,
		Item crimson,
		Item warped
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

	public WoodItemCollection(String suffix, WoodRegistrableFactory<Item> factory) {
		super(suffix, factory);
	}

	@Override
	public void register(String suffix) {
		BigTechItems.register(     "oak_" + suffix, this.oak);
		BigTechItems.register(  "spruce_" + suffix, this.spruce);
		BigTechItems.register(   "birch_" + suffix, this.birch);
		BigTechItems.register(  "jungle_" + suffix, this.jungle);
		BigTechItems.register(  "acacia_" + suffix, this.acacia);
		BigTechItems.register("dark_oak_" + suffix, this.dark_oak);
		BigTechItems.register("mangrove_" + suffix, this.mangrove);
		BigTechItems.register(  "cherry_" + suffix, this.cherry);
		BigTechItems.register( "crimson_" + suffix, this.crimson);
		BigTechItems.register(  "warped_" + suffix, this.warped);
	}
}