package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

@Mixin(ServerWorld.class)
public abstract class ServerWorld_SilentBlocks extends World {

	public ServerWorld_SilentBlocks() {
		super(null, null, null, null, false, false, 0L, 0);
	}

	@Inject(method = "syncWorldEvent", at = @At("HEAD"), cancellable = true)
	private void bigtech_cancelSoundsWithWool(PlayerEntity player, int eventId, BlockPos pos, int data, CallbackInfo callback) {
		switch (eventId) {
			case
				WorldEvents.DISPENSER_DISPENSES,
				WorldEvents.DISPENSER_FAILS,
				WorldEvents.DISPENSER_LAUNCHES_PROJECTILE,
				WorldEvents.FIREWORK_ROCKET_SHOOTS,
				WorldEvents.BREWING_STAND_BREWS,
				WorldEvents.CRAFTER_CRAFTS,
				WorldEvents.CRAFTER_FAILS
			-> {
				if (this.getBlockState(pos.down()).isIn(BlockTags.OCCLUDES_VIBRATION_SIGNALS)) {
					callback.cancel();
				}
			}
		}
	}
}