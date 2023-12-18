package builderb0y.bigtech.networking;

import java.util.function.Function;

import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;

public class BigTechServerNetwork {

	public static final PacketType<?>
		/** sent every tick while the player is controlling a miner. */
		CONTROL_MINER = register("control_miner", ControlMinerPacket::parse);

	public static PacketType<?> register(String name, Function<PacketByteBuf, ? extends C2SPlayPacket> parser) {
		Identifier identifier = BigTechMod.modID(name);
		PacketType<? extends C2SPlayPacket> type = PacketType.create(identifier, parser);
		if (!ServerPlayNetworking.registerGlobalReceiver(type, C2SPlayPacket.handler())) {
			throw new IllegalStateException("Packet type already registerd: ${identifier}");
		}
		return type;
	}

	public static void init() {}
}