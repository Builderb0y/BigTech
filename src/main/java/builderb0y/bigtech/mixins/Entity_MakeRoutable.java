package builderb0y.bigtech.mixins;

import java.util.ArrayList;
import java.util.Collection;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
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
			Collection<ServerPlayerEntity> tracking = PlayerLookup.tracking(this.<Entity>as());
			if (this.as() instanceof ServerPlayerEntity serverPlayerEntity) {
				tracking = new ArrayList<>(tracking);
				tracking.add(serverPlayerEntity); //players don't track themselves, so I have to special handle them.
			}
			if (!tracking.isEmpty) {
				EntityRoutePacket packet = EntityRoutePacket.from(this.as(), info);
				for (ServerPlayerEntity player : tracking) {
					ServerPlayNetworking.send(player, packet);
				}
			}
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void bigtech_resetRoutingInfoOnMove(CallbackInfo callback) {
		if (!this.getWorld().isClient) {
			RoutingInfo info = this.bigtech_getRoutingInfo();
			if (info != null && !(info.pos.equals(this.blockPos) && info.state == this.blockStateAtPos)) {
				this.bigtech_setRoutingInfo(null, info.synced);
			}
		}
	}

	@Shadow public abstract World getWorld();

	@Shadow public abstract BlockPos getBlockPos();

	@Shadow public abstract BlockState getBlockStateAtPos();
}