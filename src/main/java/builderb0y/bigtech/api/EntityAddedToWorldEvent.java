package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.world.entity.EntityLike;

public interface EntityAddedToWorldEvent {

	public static final Event<EntityAddedToWorldEvent> EVENT = EventFactory.createArrayBacked(EntityAddedToWorldEvent.class, subscribers -> entity -> {
		for (EntityAddedToWorldEvent subscriber : subscribers) {
			if (!subscriber.onEntityAddedToWorld(entity)) return false;
		}
		return true;
	});

	/**
	called when an entity is added to a world via
	{@link ServerEntityManager#addEntity(EntityLike, boolean)}.
	if this method returns false, the entity will not be spawned in the world.
	notes:
		* this event is NOT fired for entities loaded from disk.
		* this event is NOT fired on the logical client.
		* if this method returns false, no other callbacks will be invoked.
	*/
	public abstract boolean onEntityAddedToWorld(Entity entity);
}