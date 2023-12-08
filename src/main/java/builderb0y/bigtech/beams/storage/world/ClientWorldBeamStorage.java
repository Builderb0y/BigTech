package builderb0y.bigtech.beams.storage.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.PersistentBeam;

public class ClientWorldBeamStorage extends CommonWorldBeamStorage {

	public ClientWorldBeamStorage(World world) {
		super(world);
	}

	@Override
	public PersistentBeam getBeam(BlockPos pos) {
		throw new UnsupportedOperationException("Beam origins do not exist on the client.");
	}
}