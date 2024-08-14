package builderb0y.bigtech.blocks;

import net.minecraft.block.Block;

import builderb0y.bigtech.registrableCollections.LedRegistrableCollection;

public class LedBlockCollection extends LedRegistrableCollection<Block> {

	public LedBlockCollection(
		boolean register,
		Block white,
		Block orange,
		Block magenta,
		Block lightBlue,
		Block yellow,
		Block lime,
		Block pink,
		Block gray,
		Block lightGray,
		Block cyan,
		Block purple,
		Block blue,
		Block brown,
		Block green,
		Block red,
		Block black
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

	public LedBlockCollection(boolean register, LedFactory<Block> factory) {
		super(register, factory);
	}

	@Override
	public void register() {
		BigTechBlocks.register("white_led",      this.white    );
		BigTechBlocks.register("orange_led",     this.orange   );
		BigTechBlocks.register("magenta_led",    this.magenta  );
		BigTechBlocks.register("light_blue_led", this.lightBlue);
		BigTechBlocks.register("yellow_led",     this.yellow   );
		BigTechBlocks.register("lime_led",       this.lime     );
		BigTechBlocks.register("pink_led",       this.pink     );
		BigTechBlocks.register("gray_led",       this.gray     );
		BigTechBlocks.register("light_gray_led", this.lightGray);
		BigTechBlocks.register("cyan_led",       this.cyan     );
		BigTechBlocks.register("purple_led",     this.purple   );
		BigTechBlocks.register("blue_led",       this.blue     );
		BigTechBlocks.register("brown_led",      this.brown    );
		BigTechBlocks.register("green_led",      this.green    );
		BigTechBlocks.register("red_led",        this.red      );
		BigTechBlocks.register("black_led",      this.black    );
	}
}