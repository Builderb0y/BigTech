package builderb0y.bigtech.mixins;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntry_FixVanillaBug {

	@Inject(method = "sendPackets", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", shift = Shift.AFTER), cancellable = true)
	private void bigtech_cancelInvalidPackets(ServerPlayerEntity player, Consumer<Packet<ClientPlayPacketListener>> sender, CallbackInfo callback) {
		callback.cancel();
	}
}