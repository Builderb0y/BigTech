package builderb0y.bigtech.items;

import net.minecraft.item.Item;

import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection;

public class CrystalItemCollection extends CrystalRegistrableCollection<Item> {

	public CrystalItemCollection(
		String suffix,
		Item red,
		Item yellow,
		Item green,
		Item cyan,
		Item blue,
		Item magenta,
		Item black,
		Item white
	) {
		super(
			suffix,
			red,
			yellow,
			green,
			cyan,
			blue,
			magenta,
			black,
			white
		);
	}

	public CrystalItemCollection(String suffix, CrystalClusterFactory<Item> factory) {
		super(suffix, factory);
	}

	@Override
	public void register(String suffix) {
		BigTechItems.register(    "red_" + suffix, this.red    );
		BigTechItems.register( "yellow_" + suffix, this.yellow );
		BigTechItems.register(  "green_" + suffix, this.green  );
		BigTechItems.register(   "cyan_" + suffix, this.cyan   );
		BigTechItems.register(   "blue_" + suffix, this.blue   );
		BigTechItems.register("magenta_" + suffix, this.magenta);
		BigTechItems.register(  "black_" + suffix, this.black  );
		BigTechItems.register(  "white_" + suffix, this.white  );
	}
}