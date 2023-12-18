package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import builderb0y.bigtech.entities.MinerEntity;

public record ControlMinerPacket(byte input) implements C2SPlayPacket {

	public static ControlMinerPacket parse(PacketByteBuf buffer) {
		return new ControlMinerPacket(buffer.readByte());
	}

	@Override
	public void handle(ServerPlayerEntity player, PacketSender responseSender) {
		if (player.vehicle instanceof MinerEntity miner && miner.getControllingPassenger() == player) {
			miner.dataTracker.set(MinerEntity.INPUT, this.input);
		}
	}

	@Override
	public void write(PacketByteBuf buffer) {
		buffer.writeByte(this.input);
	}

	@Override
	public PacketType<?> getType() {
		return BigTechServerNetwork.CONTROL_MINER;
	}
}