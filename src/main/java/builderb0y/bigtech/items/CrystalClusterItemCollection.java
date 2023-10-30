package builderb0y.bigtech.items;

import net.minecraft.item.Item;

import builderb0y.bigtech.registrableCollections.CrystalClusterRegistrableCollection;

public class CrystalClusterItemCollection extends CrystalClusterRegistrableCollection<Item> {

	public CrystalClusterItemCollection(
		boolean register,
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
			register,
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

	public CrystalClusterItemCollection(boolean register, CrystalClusterFactory<Item> factory) {
		super(register, factory);
	}

	@Override
	public void register() {
		BigTechItems.register(    "red_crystal_cluster", this.red    );
		BigTechItems.register( "yellow_crystal_cluster", this.yellow );
		BigTechItems.register(  "green_crystal_cluster", this.green  );
		BigTechItems.register(   "cyan_crystal_cluster", this.cyan   );
		BigTechItems.register(   "blue_crystal_cluster", this.blue   );
		BigTechItems.register("magenta_crystal_cluster", this.magenta);
		BigTechItems.register(  "black_crystal_cluster", this.black  );
		BigTechItems.register(  "white_crystal_cluster", this.white  );
	}
}