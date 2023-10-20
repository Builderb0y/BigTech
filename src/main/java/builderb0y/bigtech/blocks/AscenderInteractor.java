package builderb0y.bigtech.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
a block which pulls entities out of an ascender or descender.
each side of the block has an associated IO type.
the way the logic currently works,
the ascender or descender will first attempt to distribute entities
evenly between all adjacent blocks whose {@link AscenderIOType}
on the side touching the ascender or descender is {@link AscenderIOType#PRIMARY_INPUT}.
if there are no primary inputs for the adjacent blocks on the side touching
the ascender or descender, then the ascender or descender will try to distribute
entities to {@link AscenderIOType#SECONDARY_INPUT}s instead.
if that fails too, then the default direction (up for ascenders,
down for descenders) will be used.
*/
public interface AscenderInteractor {

	public abstract AscenderIOType getAscenderIOType(World world, BlockPos pos, BlockState state, Direction face);

	public static enum AscenderIOType {
		PRIMARY_INPUT,
		SECONDARY_INPUT,
		NO_INPUT;
	}
}