package builderb0y.bigtech.networking;

import java.util.Collection;
import java.util.UUID;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.Beam;
import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public class ToggleBeamPacket implements S2CPlayPacket<ToggleBeamPacket.Payload> {

	public static final ToggleBeamPacket INSTANCE = new ToggleBeamPacket();

	public void sendAdd(Collection<ServerPlayerEntity> players, PersistentBeam beam) {
		if (!players.isEmpty()) {
			Payload payload = Payload.add(beam);
			for (ServerPlayerEntity player : players) {
				BigTechNetwork.sendToClient(player, payload);
			}
		}
	}

	public void sendRemove(Collection<ServerPlayerEntity> players, PersistentBeam beam) {
		if (!players.isEmpty()) {
			Payload payload = Payload.remove(beam);
			for (ServerPlayerEntity player : players) {
				BigTechNetwork.sendToClient(player, payload);
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Payload decode(RegistryByteBuf buffer) {
		boolean adding = buffer.readBoolean();
		UUID uuid = buffer.readUuid();
		BeamType type = adding ? buffer.readRegistryValue(BeamType.REGISTRY_KEY) : null;
		return new Payload(adding, type, uuid);
	}

	public static record Payload(boolean adding, BeamType type, UUID uuid) implements S2CPayload {

		public static Payload add(PersistentBeam beam) {
			return new Payload(true, beam.getType(), beam.uuid);
		}

		public static Payload remove(PersistentBeam beam) {
			return new Payload(false, null, beam.uuid);
		}

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer.writeBoolean(this.adding).writeUuid(this.uuid);
			if (this.adding) buffer.writeRegistryValue(BeamType.REGISTRY_KEY, this.type);
		}

		@Override
		@Environment(EnvType.CLIENT)
		public void process(ClientPlayNetworking.Context context) {
			ClientPlayerEntity player = context.player();
			CommonWorldBeamStorage storage = CommonWorldBeamStorage.KEY.get(player.getWorld());
			if (this.adding) {
				Beam beam = this.type.factory.create(player.getWorld(), this.uuid);
				if (beam instanceof PersistentBeam persistentBeam) {
					storage.addBeam(persistentBeam);
				}
				else {
					BigTechMod.LOGGER.warn("Received non-persistent beam??? ${BeamType.REGISTRY.getId(this.type)}");
				}
			}
			else {
				storage.removeBeam(this.uuid);
			}
		}
	}
}