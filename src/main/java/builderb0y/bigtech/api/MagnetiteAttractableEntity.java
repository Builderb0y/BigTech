package builderb0y.bigtech.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;

import builderb0y.bigtech.blocks.MagnetiteBlock;

/**
implement this interface on your entity and call
{@link #attractToNearbyMagnetiteBlocks(double, boolean)}
somewhere inside {@link Entity#tick()} to make
it be attracted to nearby magnetite blocks
and entities wearing magnetite armor.

this interface is implemented via mixin onto the vanilla classes
{@link ProjectileEntity}, {@link ItemEntity}, and {@link ExperienceOrbEntity}.
*/
public interface MagnetiteAttractableEntity {

	/**
	for projectiles, this is 0.25.
	for items and experience orbs, this is 0.075.
	base your own value off of that.
	*/
	public abstract double getMagnetiteAttractionForce();

	/**
	modifies this entity's velocity to move it towards nearby magnetite blocks.
	note that this method only needs to be called on the server side.

	@param markVelocityChanged if true, {@link Entity#velocityDirty} and
	{@link Entity#velocityModified} will be set to true if there were any
	nearby magnetite blocks. in general, if you're calling this method on
	both sides, then you should have this parameter set to false.
	if you're only calling it on the server side, set it to true.

	server-side attraction can be useful if an entity is only conditionally
	attracted to magnetite blocks, and the condition requires server-side data.
	this is the case for magnetite armor, where items and projectiles
	do not get attracted to their owners. the owner is not synced,
	so server-side attraction is used. in this case, the client and server
	are doing different calculations, so the server needs to manually inform
	the client when the velocity changes.

	common-side attraction is useful if an entity is unconditionally
	attracted to magnetite blocks. in this case, the client and server
	are doing the same calculations, so nothing gets out of sync.
	*/
	public default void attractToNearbyMagnetiteBlocks(double range, boolean markVelocityChanged) {
		MagnetiteBlock.attract(this.as(), this.getMagnetiteAttractionForce(), range, markVelocityChanged);
	}

	public default boolean isImmuneToMagnetiteArmor(LivingEntity wearer) {
		return false;
	}
}