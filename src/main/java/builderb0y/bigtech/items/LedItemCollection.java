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
		BigTechItems.register("white_led",      this.white    );
		BigTechItems.register("orange_led",     this.orange   );
		BigTechItems.register("magenta_led",    this.magenta  );
		BigTechItems.register("light_blue_led", this.lightBlue);
		BigTechItems.register("yellow_led",     this.yellow   );
		BigTechItems.register("lime_led",       this.lime     );
		BigTechItems.register("pink_led",       this.pink     );
		BigTechItems.register("gray_led",       this.gray     );
		BigTechItems.register("light_gray_led", this.lightGray);
		BigTechItems.register("cyan_led",       this.cyan     );
		BigTechItems.register("purple_led",     this.purple   );
		BigTechItems.register("blue_led",       this.blue     );
		BigTechItems.register("brown_led",      this.brown    );
		BigTechItems.register("green_led",      this.green    );
		BigTechItems.register("red_led",        this.red      );
		BigTechItems.register("black_led",      this.black    );
	}
}