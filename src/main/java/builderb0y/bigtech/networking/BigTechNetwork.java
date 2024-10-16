package builderb0y.bigtech.networking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import it.unimi.dsi.fastutil.objects.Reference2ByteMap;
import it.unimi.dsi.fastutil.objects.Reference2ByteOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.mixinterfaces.RoutableEntity;
import builderb0y.bigtech.networking.C2SPlayPacket.C2SPayload;
import builderb0y.bigtech.networking.PacketHandler.BigTechPayload;
import builderb0y.bigtech.networking.S2CPlayPacket.S2CPayload;

@EnvironmentInterface(value = EnvType.CLIENT, itf = ClientPlayNetworking.PlayPayloadHandler.class)
public class BigTechNetwork implements
	ClientPlayNetworking.PlayPayloadHandler<PacketHandler.BigTechPayload<?>>,
	ServerPlayNetworking.PlayPayloadHandler<PacketHandler.BigTechPayload<?>>,
	PacketCodec<RegistryByteBuf, PacketHandler.BigTechPayload<?>>
{

	public static final Logger LOGGER = LoggerFactory.getLogger(BigTechMod.MODNAME + "/Network");
	public static final Identifier NETWORK_ID = BigTechMod.modID("network");
	public static final CustomPayload.Id<PacketHandler.BigTechPayload<?>> ID = new CustomPayload.Id<>(BigTechMod.modID("payload"));
	public static final BigTechNetwork INSTANCE = new BigTechNetwork();

	public final List<PacketHandler<?>> idToHandler = new ArrayList<>();
	public final Reference2ByteMap<PacketHandler<?>> handlerToId = new Reference2ByteOpenHashMap<>();

	public BigTechNetwork() {
		this.handlerToId.defaultReturnValue((byte)(-1));

		//server-to-client packets

		/** updates an entity's routing info {@link RoutableEntity#bigtech_getRoutingInfo()}. */
		this.register(EntityRoutePacket.INSTANCE);
		/** spawns a pulse beam. */
		this.register(  PulseBeamPacket.INSTANCE);
		/** syncs the segments of a persistent beam when a chunk is sent to the player. */
		this.register(   LoadBeamPacket.INSTANCE);
		/** adds new beam segments to chunks already loaded on the client. */
		this.register(    AddBeamPacket.INSTANCE);
		/** removes beam segments from chunks already loaded on the client. */
		this.register( RemoveBeamPacket.INSTANCE);
		/** registers or unregisters a beam's UUID. */
		this.register( ToggleBeamPacket.INSTANCE);
		/** opens a screen to configure a pulsar when a player right clicks on one. */
		this.register( OpenPulsarPacket.INSTANCE);

		//client-to-server packets

		/** sent every tick while the player is controlling a miner. */
		this.register(          ControlMinerPacket.INSTANCE);
		/** sent when a player attempts to fire a silver iodide cannon. */
		this.register(SilverIodideCannonFirePacket.INSTANCE);
		/** sent when a player clicks "done" in a pulsar config screen. */
		this.register(          UpdatePulsarPacket.INSTANCE);
		/** sent when a player clicks one of the widgets in the dislocator screen. */
		this.register(    DislocatorSetDepthPacket.INSTANCE);
	}

	public static void init() {
		LOGGER.debug("Initializing common network...");
		PayloadTypeRegistry.playC2S().register(ID, INSTANCE);
		PayloadTypeRegistry.playS2C().register(ID, INSTANCE);
		ServerPlayNetworking.registerGlobalReceiver(ID, INSTANCE);
		LOGGER.debug("Done initializing common network.");
	}

	@Environment(EnvType.CLIENT)
	public static void initClient() {
		LOGGER.debug("Initializing client network...");
		ClientPlayNetworking.registerGlobalReceiver(ID, INSTANCE);
		LOGGER.debug("Done initializing client network.");
	}

	@Environment(EnvType.CLIENT)
	public static void sendToServer(C2SPayload payload) {
		ClientPlayNetworking.send(payload);
	}

	public static void sendToClient(ServerPlayerEntity player, S2CPayload payload) {
		ServerPlayNetworking.send(player, payload);
	}

	public byte nextId() {
		int id = this.idToHandler.size();
		if (id < 255) return (byte)(id);
		else throw new IllegalStateException("Too many packet handlers registered on " + this);
	}

	public void register(PacketHandler<?> handler) {
		byte id = this.nextId();
		this.idToHandler.add(handler);
		this.handlerToId.put(handler, id);
	}

	public byte getId(PacketHandler<?> handler) {
		byte id = this.handlerToId.getByte(handler);
		if (id != -1) return id;
		else throw new IllegalStateException(handler + " not registered on " + this);
	}

	public PacketHandler<?> getHandler(byte id) {
		int unsignedId = Byte.toUnsignedInt(id);
		if (unsignedId < this.idToHandler.size()) return this.idToHandler.get(unsignedId);
		else return null;
	}

	@Override
	@Environment(EnvType.CLIENT)
	@SuppressWarnings("unchecked")
	public void receive(PacketHandler.BigTechPayload payload, ClientPlayNetworking.Context context) {
		payload.process(context);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void receive(PacketHandler.BigTechPayload payload, ServerPlayNetworking.Context context) {
		payload.process(context);
	}

	@Override
	public PacketHandler.BigTechPayload<?> decode(RegistryByteBuf buffer) {
		PacketHandler<?> handler = this.getHandler(buffer.readByte());
		BigTechPayload<?> result = handler.decode(buffer);
		if (buffer.readableBytes() > 0) {
			System.out.println("buffer fail!");
		}
		return result;
	}

	@Override
	public void encode(RegistryByteBuf buffer, PacketHandler.BigTechPayload value) {
		buffer.writeByte(this.getId(value.getAssociatedPacket()));
		value.encode(buffer);
	}
}