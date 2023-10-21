package builderb0y.bigtech.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
a block which pulls entities out of an ascender or descender.
each side of the block has an associated IO type.
the way the logic currently works,
the ascender or descender will sort all adjacent blocks by priority,
and distribute between all blocks with the highest priority.
by this I mean that if 2 adjacent blocks are tied for the highest priority,
then the ascender will distribute between both of them.
if no adjacent blocks have a positive priority, then the default
direction (up for ascenders, down for descenders) is used.
*/
public interface AscenderInteractor {

	public static final int
		BELT_BACK           = 256,
		ASCENDER_TOP_BOTTOM = 192,
		BELT_SIDE           = 128,
		BELT_TOP            =  64,
		BLOCKED             =   0;

	public abstract int getAscenderPriority(World world, BlockPos pos, BlockState state, Direction face);
}