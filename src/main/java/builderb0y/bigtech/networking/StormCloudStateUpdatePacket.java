package builderb0y.bigtech.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import builderb0y.bigtech.entities.StormCloudEntity;

public class StormCloudStateUpdatePacket implements S2CPlayPacket<StormCloudStateUpdatePacket.Payload> {

	public static final StormCloudStateUpdatePacket INSTANCE = new StormCloudStateUpdatePacket();

	public static void send(StormCloudEntity cloud) {
		Payload payload = new Payload(cloud.getId(), cloud.radiusSquared);
		for (ServerPlayerEntity player : PlayerLookup.tracking(cloud)) {
			BigTechNetwork.sendToClient(player, payload);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Payload decode(RegistryByteBuf buffer) {
		return new Payload(buffer.readInt(), buffer.readInt());
	}

	public static record Payload(int entityID, int radiusSquared) implements S2CPayload {

		@Override
		@Environment(EnvType.CLIENT)
		public void process(ClientPlayNetworking.Context context) {
			if (context.player().getWorld().getEntityById(this.entityID) instanceof StormCloudEntity cloud) {
				cloud.radiusSquared = this.radiusSquared;
			}
		}

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer.writeInt(this.entityID).writeInt(this.radiusSquared);
		}
	}
}