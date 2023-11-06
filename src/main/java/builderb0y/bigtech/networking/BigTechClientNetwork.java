package builderb0y.bigtech.networking;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.mixinterfaces.RoutableEntity;

public class BigTechClientNetwork {

	public static final PacketType<?>
		/** updates an entity's routing info {@link RoutableEntity#bigtech_getRoutingInfo()}. */
		ENTITY_ROUTE = register("entity_route", EntityRoutePacket::parse),
		/** spawns a pulse beam. */
		PULSE_BEAM   = register("pulse_beam",     PulseBeamPacket::parse),
		/** syncs the segments of a persistent beam when a chunk is sent to the player. */
		LOAD_BEAM    = register("load_beam",       LoadBeamPacket::parse),
		/** adds new beam segments to chunks already loaded on the client. */
		ADD_BEAM     = register("add_beam",         AddBeamPacket::parse),
		/** removes beam segments from chunks already loaded on the client. */
		REMOVE_BEAM  = register("remove_beam",   RemoveBeamPacket::parse),
		/** registers or unregisters a beam's UUID. */
		TOGGLE_BEAM  = register("toggle_beam",   ToggleBeamPacket::parse);

	public static void send(Collection<ServerPlayerEntity> players, Supplier<? extends S2CPlayPacket> packetSupplier) {
		if (!players.isEmpty) {
			S2CPlayPacket packet = packetSupplier.get();
			players.forEach(player -> ServerPlayNetworking.send(player, packet));
		}
	}

	public static void init() {}

	public static PacketType<?> register(String name, Function<PacketByteBuf, ? extends S2CPlayPacket> parser) {
		Identifier identifier = BigTechMod.modID(name);
		PacketType<? extends S2CPlayPacket> type = PacketType.create(identifier, parser);
		if (!ClientPlayNetworking.registerGlobalReceiver(type, S2CPlayPacket.handler())) {
			throw new IllegalStateException("Packet type already registered: ${identifier}");
		}
		return type;
	}
}