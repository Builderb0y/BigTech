package builderb0y.bigtech.items;

import net.minecraft.item.Item;

import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection;

public class CrystalItemCollection extends CrystalRegistrableCollection<Item> {

	public CrystalItemCollection(
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

	public CrystalItemCollection(boolean register, CrystalClusterFactory<Item> factory) {
		super(register, factory);
	}

	@Override
	public void register() {
		BigTechItems.register(this.red    );
		BigTechItems.register(this.yellow );
		BigTechItems.register(this.green  );
		BigTechItems.register(this.cyan   );
		BigTechItems.register(this.blue   );
		BigTechItems.register(this.magenta);
		BigTechItems.register(this.black  );
		BigTechItems.register(this.white  );
	}
}