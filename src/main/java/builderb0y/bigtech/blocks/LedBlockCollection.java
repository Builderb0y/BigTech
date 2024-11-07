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
		BigTechBlocks.register(this.white    );
		BigTechBlocks.register(this.orange   );
		BigTechBlocks.register(this.magenta  );
		BigTechBlocks.register(this.lightBlue);
		BigTechBlocks.register(this.yellow   );
		BigTechBlocks.register(this.lime     );
		BigTechBlocks.register(this.pink     );
		BigTechBlocks.register(this.gray     );
		BigTechBlocks.register(this.lightGray);
		BigTechBlocks.register(this.cyan     );
		BigTechBlocks.register(this.purple   );
		BigTechBlocks.register(this.blue     );
		BigTechBlocks.register(this.brown    );
		BigTechBlocks.register(this.green    );
		BigTechBlocks.register(this.red      );
		BigTechBlocks.register(this.black    );
	}
}