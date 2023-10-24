package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;

/**
a block which pulls entities out of an ascender or descender.
each side of the block has an associated priority.
the way the logic currently works,
the ascender or descender will sort all adjacent blocks by priority,
and distribute between all blocks with the highest priority.
by this I mean that if 2 adjacent blocks are tied for the highest priority,
then the ascender will distribute between both of them.
if no adjacent blocks have a positive priority, then the default
direction (up for ascenders, down for descenders) is used.
register blocks with {@link #LOOKUP}.
*/
public interface AscenderInteractor {

	public static final BlockApiLookup<AscenderInteractor, Void> LOOKUP = BlockApiLookup.get(BigTechMod.modID("ascender_interactor"), AscenderInteractor.class, Void.class);

	public static AscenderInteractor get(World world, BlockPos pos) {
		return LOOKUP.find(world, pos, null);
	}

	public static AscenderInteractor get(World world, BlockPos pos, BlockState state) {
		return LOOKUP.find(world, pos, state, null, null);
	}

	public static AscenderInteractor get(World world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		return LOOKUP.find(world, pos, state, blockEntity, null);
	}

	public static final int
		BELT_BACK           = 256,
		ASCENDER_TOP_BOTTOM = 192,
		BELT_SIDE           = 128,
		BELT_TOP            =  64,
		BLOCKED             =   0;

	public abstract int getAscenderPriority(World world, BlockPos pos, BlockState state, Direction face);
}