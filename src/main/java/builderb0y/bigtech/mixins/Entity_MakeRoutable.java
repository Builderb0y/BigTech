package builderb0y.bigtech.mixins;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.mixinterfaces.RoutableEntity;
import builderb0y.bigtech.networking.EntityRoutePacket;

@Mixin(Entity.class)
public abstract class Entity_MakeRoutable implements RoutableEntity {

	public RoutingInfo bigtech_routingInfo;

	@Override
	public RoutingInfo bigtech_getRoutingInfo() {
		return this.bigtech_routingInfo;
	}

	@Override
	public void bigtech_setRoutingInfo(RoutingInfo info, boolean sync) {
		this.bigtech_routingInfo = info;
		if (sync && !this.getWorld().isClient) {
			EntityRoutePacket.INSTANCE.send(
				PlayerLookup.tracking(this.<Entity>as()),
				this.as(),
				info
			);
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void bigtech_resetRoutingInfoOnMove(CallbackInfo callback) {
		if (!this.getWorld().isClient) {
			RoutingInfo info = this.bigtech_getRoutingInfo();
			if (info != null && !(info.pos().equals(this.getBlockPos()) && info.state() == this.getBlockStateAtPos())) {
				this.bigtech_setRoutingInfo(null, info.synced());
			}
		}
	}

	@Shadow public abstract World getWorld();

	@Shadow public abstract BlockPos getBlockPos();

	@Shadow public abstract BlockState getBlockStateAtPos();
}