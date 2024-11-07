package builderb0y.bigtech.items;

import net.minecraft.item.Item;

import builderb0y.bigtech.registrableCollections.LedRegistrableCollection;

public class LedItemCollection extends LedRegistrableCollection<Item> {

	public LedItemCollection(
		boolean register,
		Item white,
		Item orange,
		Item magenta,
		Item lightBlue,
		Item yellow,
		Item lime,
		Item pink,
		Item gray,
		Item lightGray,
		Item cyan,
		Item purple,
		Item blue,
		Item brown,
		Item green,
		Item red,
		Item black
	) {
		super(
			register,
			white,
			orange,
			magenta,
			lightBlue,
			yellow,
			lime,
			pink,
			gray,
			lightGray,
			cyan,
			purple,
			blue,
			brown,
			green,
			red,
			black
		);
	}

	public LedItemCollection(boolean register, LedFactory<Item> factory) {
		super(register, factory);
	}

	@Override
	public void register() {
		BigTechItems.register(this.white    );
		BigTechItems.register(this.orange   );
		BigTechItems.register(this.magenta  );
		BigTechItems.register(this.lightBlue);
		BigTechItems.register(this.yellow   );
		BigTechItems.register(this.lime     );
		BigTechItems.register(this.pink     );
		BigTechItems.register(this.gray     );
		BigTechItems.register(this.lightGray);
		BigTechItems.register(this.cyan     );
		BigTechItems.register(this.purple   );
		BigTechItems.register(this.blue     );
		BigTechItems.register(this.brown    );
		BigTechItems.register(this.green    );
		BigTechItems.register(this.red      );
		BigTechItems.register(this.black    );
	}
}