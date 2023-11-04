package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;

import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.mixins.PistonHandlerAccessor;

/**
a block which can fine-tune what it sticks to when moved by a piston.
register blocks with {@link #LOOKUP}.
*/
public interface PistonInteractor {

	public static final BlockApiLookup<PistonInteractor, Void> LOOKUP = BlockApiLookup.get(BigTechMod.modID("piston_interactor"), PistonInteractor.class, Void.class);

	public static PistonInteractor get(World world, BlockPos pos) {
		return LOOKUP.find(world, pos, null);
	}

	public static PistonInteractor get(World world, BlockPos pos, BlockState state) {
		return LOOKUP.find(world, pos, state, null, null);
	}

	/**
	returns true if this block can stick to any adjacent blocks.
	used as a fast path. if this method returns false,
	then {@link #canStickTo} will not be called for this block.
	*/
	public abstract boolean isSticky(
		PistonHandlerInfo handler,
		BlockPos pos,
		BlockState state
	);

	/**
	returns true if this block can stick to the adjacent block.

	@param handler the handler which is responsible for calculating all the blocks to push..
	@param selfPos the position of this block.
	@param selfState the state of this block.
	@param otherPos the position of the adjacent block.
	@param otherState the state of the adjacent block.
	@param face the direction from this position to the adjacent position.
	*/
	public abstract boolean canStickTo(
		PistonHandlerInfo handler,
		BlockPos selfPos,
		BlockState selfState,
		BlockPos otherPos,
		BlockState otherState,
		Direction face
	);

	public static class PistonHandlerInfo {

		public final PistonHandlerAccessor delegate;

		public PistonHandlerInfo(PistonHandlerAccessor delegate) {
			this.delegate = delegate;
		}

		public PistonHandler getActualHandler() {
			return this.delegate.as();
		}

		public World getWorld() {
			return this.delegate.bigtech_getWorld();
		}

		public BlockPos getPistonPosition() {
			return this.delegate.bigtech_getPosFrom();
		}

		public Direction getPistonDirection() {
			return this.delegate.bigtech_getPistonDirection();
		}

		public Direction getMotionDirection() {
			return this.delegate.bigtech_getMotionDirection();
		}

		public boolean isRetracting() {
			return this.delegate.bigtech_isRetracted();
		}
	}
}