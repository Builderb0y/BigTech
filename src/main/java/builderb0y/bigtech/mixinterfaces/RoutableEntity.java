package builderb0y.bigtech.mixinterfaces;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface RoutableEntity {

	public abstract RoutingInfo bigtech_getRoutingInfo();

	public abstract void bigtech_setRoutingInfo(RoutingInfo info, boolean sync);

	public default RoutingInfo bigtech_computeRoutingInfo(BlockPos pos, BlockState state, Direction defaultDirection, RoutingInfoFunction function) {
		RoutingInfo info = this.bigtech_getRoutingInfo();
		if (info == null || !info.pos.equals(pos) || info.state != state) {
			Direction direction = function.compute(
				this.<Entity>as().getWorld(),
				pos,
				state,
				this.as()
			);
			this.bigtech_setRoutingInfo(
				info = new RoutingInfo(pos, state, direction, direction != defaultDirection),
				info.synced
			);
		}
		return info;
	}

	public static record RoutingInfo(BlockPos pos, BlockState state, Direction direction, boolean synced) {

		public RoutingInfo {
			pos = pos.toImmutable();
		}
	}

	@FunctionalInterface
	public static interface RoutingInfoFunction {

		public abstract Direction compute(World world, BlockPos pos, BlockState state, Entity entity);
	}
}