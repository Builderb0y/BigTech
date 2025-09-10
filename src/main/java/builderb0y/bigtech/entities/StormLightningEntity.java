package builderb0y.bigtech.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.world.World;

public class StormLightningEntity extends LightningEntity {

	public static final TrackedData<Long>
		START_X = DataTracker.registerData(StormLightningEntity.class, TrackedDataHandlerRegistry.LONG),
		START_Y = DataTracker.registerData(StormLightningEntity.class, TrackedDataHandlerRegistry.LONG),
		START_Z = DataTracker.registerData(StormLightningEntity.class, TrackedDataHandlerRegistry.LONG);

	public StormLightningEntity(EntityType<? extends LightningEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder
		.add(START_X, 0L)
		.add(START_Y, 0L)
		.add(START_Z, 0L)
		;
	}

	public double getStartX() {
		return Double.longBitsToDouble(this.dataTracker.get(START_X));
	}

	public void setStartX(double x) {
		this.dataTracker.set(START_X, Double.doubleToRawLongBits(x));
	}

	public double getStartY() {
		return Double.longBitsToDouble(this.dataTracker.get(START_Y));
	}

	public void setStartY(double y) {
		this.dataTracker.set(START_Y, Double.doubleToRawLongBits(y));
	}

	public double getStartZ() {
		return Double.longBitsToDouble(this.dataTracker.get(START_Z));
	}

	public void setStartZ(double z) {
		this.dataTracker.set(START_Z, Double.doubleToRawLongBits(z));
	}
}