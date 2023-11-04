package builderb0y.bigtech.networking;

import java.util.UUID;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.Beam;
import builderb0y.bigtech.beams.BeamType;
import builderb0y.bigtech.beams.PersistentBeam;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public record TogglePersistentBeamPacket(boolean adding, UUID uuid, BeamType type) implements S2CPlayPacket {

	public static final PacketType<TogglePersistentBeamPacket> TYPE = PacketType.create(BigTechMod.modID("toggle_persistent_beam"), TogglePersistentBeamPacket::parse);

	public static TogglePersistentBeamPacket parse(PacketByteBuf buffer) {
		boolean adding = buffer.readBoolean();
		UUID uuid = buffer.readUuid();
		BeamType type = adding ? buffer.readRegistryValue(BeamType.REGISTRY) : null;
		return new TogglePersistentBeamPacket(adding, uuid, type);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void handle(ClientPlayerEntity player, PacketSender responseSender) {
		CommonWorldBeamStorage storage = CommonWorldBeamStorage.KEY.get(player.world);
		if (this.adding) {
			Beam beam = this.type.factory.create(player.world, this.uuid);
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

	@Override
	public void write(PacketByteBuf buffer) {
		buffer.writeBoolean(this.adding).writeUuid(this.uuid);
		if (this.adding) buffer.writeRegistryValue(BeamType.REGISTRY, this.type);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}
}