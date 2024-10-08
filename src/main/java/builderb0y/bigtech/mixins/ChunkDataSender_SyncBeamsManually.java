package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.network.ChunkDataSender;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;

import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.chunk.CommonChunkBeamStorage;
import builderb0y.bigtech.networking.LoadBeamPacket;

/**
it would appear that cardinal components syncs beams whenever a block changes.
this is *massively* undesirable in my case.
so I'm duplicating its logic for syncing on chunk load, AND NOWHEN ELSE.
*/
@Mixin(ChunkDataSender.class)
public class ChunkDataSender_SyncBeamsManually {

	@Inject(method = "sendChunkData", at = @At("RETURN"))
	private static void bigtech_loadBeams(
		ServerPlayNetworkHandler handler,
		ServerWorld world,
		WorldChunk chunk,
		CallbackInfo callback
	) {
		CommonChunkBeamStorage chunkStorage = ChunkBeamStorageHolder.KEY.get(chunk).require();
		if (!chunkStorage.isEmpty()) {
			LoadBeamPacket.INSTANCE.send(chunk.getPos().x, chunk.getPos().z, chunkStorage, handler.player);
		}
	}
}