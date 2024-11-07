package builderb0y.bigtech.blocks;

import net.minecraft.block.Block;

import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection;

public class CrystalBlockCollection extends CrystalRegistrableCollection<Block> {

	public CrystalBlockCollection(
		boolean register,
		Block red,
		Block yellow,
		Block green,
		Block cyan,
		Block blue,
		Block magenta,
		Block black,
		Block white
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

	public CrystalBlockCollection(boolean register, CrystalClusterFactory<Block> factory) {
		super(register, factory);
	}

	@Override
	public void register() {
		BigTechBlocks.register(this.red    );
		BigTechBlocks.register(this.yellow );
		BigTechBlocks.register(this.green  );
		BigTechBlocks.register(this.cyan   );
		BigTechBlocks.register(this.blue   );
		BigTechBlocks.register(this.magenta);
		BigTechBlocks.register(this.black  );
		BigTechBlocks.register(this.white  );
	}
}