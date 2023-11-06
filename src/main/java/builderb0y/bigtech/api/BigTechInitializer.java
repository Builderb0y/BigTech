package builderb0y.bigtech.api;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;

/**
called when big tech is initializing.
this can be used to register implementations of
big tech's interfaces to {@link BlockApiLookup}'s
without needing a runtime dependency on big tech.
*/
public interface BigTechInitializer {

	public abstract void init();
}