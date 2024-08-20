package builderb0y.bigtech.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.blockEntities.PulsarBlockEntity;
import builderb0y.bigtech.blockEntities.PulsarBlockEntity.TimeGetter;
import builderb0y.bigtech.slotlessScreens.PulsarEditScreen;
import builderb0y.bigtech.util.WorldHelper;

public class OpenPulsarPacket implements S2CPlayPacket<OpenPulsarPacket.Payload> {

	public static final OpenPulsarPacket INSTANCE = new OpenPulsarPacket();

	public void send(ServerPlayerEntity player, PulsarBlockEntity pulsar) {
		BigTechNetwork.sendToClient(player, new Payload(pulsar.getPos(), pulsar.onTime, pulsar.offTime, pulsar.relativeTo, pulsar.offset));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public Payload decode(RegistryByteBuf buffer) {
		return new Payload(
			buffer.readBlockPos(),
			buffer.readVarInt(),
			buffer.readVarInt(),
			TimeGetter.VALUES[buffer.readUnsignedByte()],
			buffer.readVarInt()
		);
	}

	public static record Payload(
		BlockPos pos,
		int onTime,
		int offTime,
		TimeGetter relativeTo,
		int offset
	)
	implements S2CPayload {

		@Override
		@Environment(EnvType.CLIENT)
		public void process(ClientPlayNetworking.Context context) {
			PulsarBlockEntity pulsar = WorldHelper.getBlockEntity(context.client().world, this.pos, PulsarBlockEntity.class);
			if (pulsar != null) {
				pulsar.onTime = this.onTime;
				pulsar.offTime = this.offTime;
				pulsar.relativeTo = this.relativeTo;
				pulsar.offset = this.offset;
				context.client().setScreen(new PulsarEditScreen(pulsar));
			}
		}

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer
			.writeBlockPos(this.pos)
			.writeVarInt(this.onTime)
			.writeVarInt(this.offTime)
			.writeByte(this.relativeTo.ordinal())
			.writeVarInt(this.offset);
		}
	}
}