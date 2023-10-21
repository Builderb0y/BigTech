package builderb0y.bigtech.blocks.belts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

/**
used by the detector belt to determine what redstone signal strength to output for comparators.
example: item entities are counted such that the redstone
signal strength is ceil(total number of items / 4).
so, {@link #getCount(Entity)} would return the number of items in the current entity,
and {@link #scaleCount(int)} would return ceil(count / 4).
note that it would NOT be correct for {@link #getCount(Entity)}
to return ceil(number of items in the current entity / 4),
because this means that 4 *different* item entities on the same belt
would add up to a signal strength of 4, not 1 as intended.

EntityCounter's are registered via {@link DetectorBeltBlock#registerCounter(EntityType, EntityCounter)}.
*/
public interface EntityCounter<E extends Entity> {

	public static final EntityCounter<Entity> DEFAULT = entity -> 1;

	public abstract int getCount(E entity);

	public default int scaleCount(int count) {
		return count;
	}
}