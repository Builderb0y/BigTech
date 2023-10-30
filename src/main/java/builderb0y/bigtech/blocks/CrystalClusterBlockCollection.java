package builderb0y.bigtech.blocks;

import net.minecraft.block.Block;

import builderb0y.bigtech.registrableCollections.CrystalClusterRegistrableCollection;

public class CrystalClusterBlockCollection extends CrystalClusterRegistrableCollection<Block> {

	public CrystalClusterBlockCollection(
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

	public CrystalClusterBlockCollection(boolean register, CrystalClusterFactory<Block> factory) {
		super(register, factory);
	}

	@Override
	public void register() {
		BigTechBlocks.register(    "red_crystal_cluster", this.red    );
		BigTechBlocks.register( "yellow_crystal_cluster", this.yellow );
		BigTechBlocks.register(  "green_crystal_cluster", this.green  );
		BigTechBlocks.register(   "cyan_crystal_cluster", this.cyan   );
		BigTechBlocks.register(   "blue_crystal_cluster", this.blue   );
		BigTechBlocks.register("magenta_crystal_cluster", this.magenta);
		BigTechBlocks.register(  "black_crystal_cluster", this.black  );
		BigTechBlocks.register(  "white_crystal_cluster", this.white  );
	}
}