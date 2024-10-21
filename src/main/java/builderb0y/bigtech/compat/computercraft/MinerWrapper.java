package builderb0y.bigtech.compat.computercraft;

import java.util.List;
import java.util.Map;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import builderb0y.bigtech.entities.MinerEntity;
import builderb0y.bigtech.util.BigTechMath;

@SuppressWarnings("unused")
public final class MinerWrapper {

	public final BlockPos radioPos;
	public final MinerEntity miner;
	public boolean running;
	public float targetForwardSpeed, targetSidewaysSpeed, targetPitch;
	public MinerInventoryWrapper inventory;

	public MinerWrapper(BlockPos pos, MinerEntity miner) {
		this.radioPos = pos.toImmutable();
		this.miner = miner;
	}

	public double distanceSquared() {
		Vec3d pos = this.miner.getPos();
		return MathHelper.squaredMagnitude(
			(this.radioPos.getX() + 0.5D) - (pos.getX()),
			(this.radioPos.getY() + 0.5D) - (pos.getY() + 1.0D),
			(this.radioPos.getZ() + 0.5D) - (pos.getZ())
		);
	}

	public boolean isInRange() {
		return this.distanceSquared() < MinerEntity.RADIO_RANGE * MinerEntity.RADIO_RANGE;
	}

	public void checkRangeExceptConnectedAndRunning() throws LuaException {
		if (this.miner.isRemoved()) {
			throw new LuaException("Miner was unloaded or destroyed");
		}
		if (!this.isInRange()) {
			throw new LuaException("Miner out of range");
		}
	}

	public void checkRangeExceptRunning() throws LuaException {
		this.checkRangeExceptConnectedAndRunning();
		if (this.miner.radioInput.get() != this) {
			throw new LuaException("Connection to miner lost");
		}
	}

	public void checkRange() throws LuaException {
		this.checkRangeExceptRunning();
		if (!this.running) {
			throw new LuaException("Engine not running");
		}
	}

	@LuaFunction
	public boolean connect() throws LuaException {
		this.checkRangeExceptConnectedAndRunning();
		while (true) {
			MinerWrapper old = this.miner.radioInput.get();
			if (old == this) {
				return true;
			}
			else if (old == null || old.equals(this)) {
				if (this.miner.radioInput.compareAndSet(old, this)) {
					return true;
				}
			}
			else {
				return false;
			}
		}
	}

	@LuaFunction
	public boolean disconnect() {
		this.running = false;
		while (true) {
			MinerWrapper old = this.miner.radioInput.get();
			if (this.equals(old)) {
				if (this.miner.radioInput.compareAndSet(old, null)) {
					return true;
				}
			}
			else {
				return false;
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof MinerWrapper that && (
			this.miner == that.miner &&
			this.radioPos.equals(that.radioPos)
		);
	}

	@Override
	public int hashCode() {
		return this.miner.hashCode() * 31 + this.radioPos.hashCode();
	}

	@Override
	public String toString() {
		return "Radio at " + this.radioPos + " controlling " + this.miner;
	}

	@LuaFunction
	public boolean isConnected() {
		return (
			!this.miner.isRemoved() &&
			this.isInRange() &&
			this.miner.radioInput.get() == this
		);
	}

	@LuaFunction
	public void startEngine() throws LuaException {
		this.checkRangeExceptRunning();
		this.running = true;
	}

	@LuaFunction
	public void stopEngine() throws LuaException {
		this.checkRangeExceptRunning();
		this.running = false;
	}

	@LuaFunction
	public boolean isEngineRunning() throws LuaException {
		this.checkRangeExceptRunning();
		return this.running;
	}

	@LuaFunction
	public boolean isBeingRidden() throws LuaException {
		this.checkRangeExceptRunning();
		List<Entity> list = this.miner.getPassengerList();
		return !list.isEmpty() && list.getFirst() instanceof PlayerEntity;
	}

	@LuaFunction
	public int getNumber() throws LuaException {
		this.checkRangeExceptRunning();
		return this.miner.number;
	}

	@LuaFunction
	public Map<String, Double> locate() throws LuaException {
		this.checkRangeExceptRunning();
		Vec3d pos = this.miner.getPos();
		return Map.of("x", pos.x, "y", pos.y, "z", pos.z);
	}

	@LuaFunction
	public double getDistanceToRadio() throws LuaException {
		this.checkRangeExceptRunning();
		return Math.sqrt(this.distanceSquared());
	}

	@LuaFunction
	public double getHorizontalDistanceTo(double x, double z) throws LuaException {
		this.checkRangeExceptRunning();
		Vec3d pos = this.miner.getPos();
		return Math.sqrt(BigTechMath.square(pos.x - x, pos.z - z));
	}

	@LuaFunction
	public double getDistanceTo(double x, double y, double z) throws LuaException {
		this.checkRangeExceptRunning();
		Vec3d pos = this.miner.getPos();
		return Math.sqrt(BigTechMath.square(pos.x - x, pos.y - y, pos.z - z));
	}

	@LuaFunction
	public double getYaw() throws LuaException {
		this.checkRangeExceptRunning();
		return Math.toRadians(BigTechMath.modulus_BP(this.miner.getYaw(), 360.0F));
	}

	@LuaFunction
	public double getYawTo(double x, double z) throws LuaException {
		this.checkRangeExceptRunning();
		Vec3d pos = this.miner.getPos();
		//I have a lot of swear words for whoever decided
		//how minecraft's coordinate system would work.
		double angle = Math.atan2(pos.x - x, z - pos.z);
		//adding 0.0 will convert -0.0 to +0.0.
		angle += angle < 0.0D ? Math.TAU : 0.0D;
		return angle;
	}

	@LuaFunction
	public double getFacingX() throws LuaException {
		this.checkRangeExceptRunning();
		return -Math.sin(Math.toRadians(this.miner.getYaw()));
	}

	@LuaFunction
	public double getFacingZ() throws LuaException {
		this.checkRangeExceptRunning();
		return Math.cos(Math.toRadians(this.miner.getYaw()));
	}

	@LuaFunction
	public int getRemainingFuel() throws LuaException {
		this.checkRangeExceptRunning();
		return this.miner.fuelTicks.get();
	}

	@LuaFunction
	public double getTargetForwardSpeed() throws LuaException {
		this.checkRange();
		return this.targetForwardSpeed;
	}

	@LuaFunction
	public void setTargetForwardSpeed(double targetForwardSpeed) throws LuaException {
		this.checkRange();
		if      (targetForwardSpeed >  1.0D) targetForwardSpeed =  1.0D;
		else if (targetForwardSpeed < -1.0D) targetForwardSpeed = -1.0D;
		else if (Double.isNaN(targetForwardSpeed)) targetForwardSpeed = 0.0D;
		this.targetForwardSpeed = (float)(targetForwardSpeed);
	}

	@LuaFunction
	public double getTargetTurningSpeed() throws LuaException {
		this.checkRange();
		return this.targetSidewaysSpeed;
	}

	@LuaFunction
	public void setTargetTurningSpeed(double targetTurningSpeed) throws LuaException {
		this.checkRange();
		if      (targetTurningSpeed >  1.0D) targetTurningSpeed =  1.0D;
		else if (targetTurningSpeed < -1.0D) targetTurningSpeed = -1.0D;
		else if (Double.isNaN(targetTurningSpeed)) targetTurningSpeed = 0.0D;
		this.targetSidewaysSpeed = (float)(targetTurningSpeed);
	}

	@LuaFunction
	public void setTargetPitch(double targetPitch) throws LuaException {
		this.checkRange();
		if      (targetPitch >  1.0D) targetPitch =  1.0D;
		else if (targetPitch < -1.0D) targetPitch = -1.0D;
		else if (Double.isNaN(targetPitch)) targetPitch = 0.0D;
		this.targetPitch = (float)(targetPitch);
	}

	@LuaFunction
	public void stopMoving() throws LuaException {
		this.checkRangeExceptRunning();
		this.targetForwardSpeed  = 0.0F;
		this.targetSidewaysSpeed = 0.0F;
	}

	@LuaFunction
	public MinerInventoryWrapper getInventory() throws LuaException {
		return this.inventory == null ? this.inventory = new MinerInventoryWrapper(this) : this.inventory;
	}
}