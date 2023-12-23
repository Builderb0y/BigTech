package builderb0y.bigtech.mixins;

import java.util.LinkedList;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkProvider;
import net.minecraft.world.chunk.light.ChunkBlockLightProvider;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.minecraft.world.chunk.light.LightStorage;

import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.chunk.CommonChunkBeamStorage;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;

@Mixin(ChunkBlockLightProvider.class)
public abstract class ChunkBlockLightProvider_AllowBeamsToEmitLight extends ChunkLightProvider {

	public ChunkBlockLightProvider_AllowBeamsToEmitLight(ChunkProvider chunkProvider, LightStorage lightStorage) {
		super(chunkProvider, lightStorage);
	}

	@ModifyExpressionValue(method = "getLightSourceLuminance", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getLuminance()I"))
	private int bigtech_getLightLevelFromBeams(int oldValue, @Local(argsOnly = true, index = 1) long packedPosition) {
		if (oldValue >= 15) return oldValue;
		int x = BlockPos.unpackLongX(packedPosition);
		int y = BlockPos.unpackLongY(packedPosition);
		int z = BlockPos.unpackLongZ(packedPosition);
		if (this.chunkProvider.getChunk(x >> 4, z >> 4) instanceof Chunk chunk) {
			CommonChunkBeamStorage chunkStorage = ChunkBeamStorageHolder.KEY.get(chunk).get();
			if (chunkStorage != null) {
				CommonSectionBeamStorage sectionStorage = chunkStorage.get(y >> 4);
				if (sectionStorage != null) {
					LinkedList<BeamSegment> segments = sectionStorage.checkSegments(x, y, z);
					if (segments != null) {
						for (BeamSegment segment : segments) {
							oldValue = Math.max(oldValue, ((PersistentBeam)(segment.beam)).getLightLevel(segment));
							if (oldValue >= 15) return oldValue;
						}
					}
				}
			}
		}
		return oldValue;
	}
}