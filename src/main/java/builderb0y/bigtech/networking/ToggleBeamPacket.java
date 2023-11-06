package builderb0y.bigtech.networking;

import java.util.UUID;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.Beam;
import builderb0y.bigtech.beams.base.BeamType;
import builderb0y.bigtech.beams.base.PersistentBeam;
import builderb0y.bigtech.beams.storage.world.CommonWorldBeamStorage;

public record ToggleBeamPacket(boolean adding, BeamType type, UUID uuid) implements S2CPlayPacket {

	public static ToggleBeamPacket add(PersistentBeam beam) {
		return new ToggleBeamPacket(true, beam.type, beam.uuid);
	}

	public static ToggleBeamPacket remove(PersistentBeam beam) {
		return new ToggleBeamPacket(false, null, beam.uuid);
	}

	public static ToggleBeamPacket parse(PacketByteBuf buffer) {
		boolean adding = buffer.readBoolean();
		UUID uuid = buffer.readUuid();
		BeamType type = adding ? buffer.readRegistryValue(BeamType.REGISTRY) : null;
		return new ToggleBeamPacket(adding, type, uuid);
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
		return BigTechClientNetwork.TOGGLE_BEAM;
	}
}