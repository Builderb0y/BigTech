package builderb0y.bigtech.blocks;

import net.minecraft.block.Block;

import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection;

public class CrystalBlockCollection extends CrystalRegistrableCollection<Block> {

	public CrystalBlockCollection(
		String suffix,
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

	public CrystalBlockCollection(String suffix, CrystalClusterFactory<Block> factory) {
		super(suffix, factory);
	}

	@Override
	public void register(String suffix) {
		BigTechBlocks.register(    "red_" + suffix, this.red    );
		BigTechBlocks.register( "yellow_" + suffix, this.yellow );
		BigTechBlocks.register(  "green_" + suffix, this.green  );
		BigTechBlocks.register(   "cyan_" + suffix, this.cyan   );
		BigTechBlocks.register(   "blue_" + suffix, this.blue   );
		BigTechBlocks.register("magenta_" + suffix, this.magenta);
		BigTechBlocks.register(  "black_" + suffix, this.black  );
		BigTechBlocks.register(  "white_" + suffix, this.white  );
	}
}