package builderb0y.bigtech.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blockEntities.PulsarBlockEntity;
import builderb0y.bigtech.blockEntities.PulsarBlockEntity.TimeGetter;
import builderb0y.bigtech.util.WorldHelper;

public class UpdatePulsarPacket implements C2SPlayPacket<UpdatePulsarPacket.Payload> {

	public static final UpdatePulsarPacket INSTANCE = new UpdatePulsarPacket();

	public void send(BlockPos pos, int onTime, int offTime, TimeGetter relativeTo, int offset) {
		BigTechNetwork.sendToServer(new Payload(pos, onTime, offTime, relativeTo, offset));
	}

	@Override
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
	implements C2SPayload {

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

		@Override
		public void process(ServerPlayNetworking.Context context) {
			if (this.onTime > 0 && this.onTime <= 1_000_000 && this.offTime > 0 && this.offTime <= 1_000_000 && this.offset >= 0 && this.offset <= 1_000_000) {
				if (context.player().canInteractWithBlockAt(this.pos, 4.0F)) {
					PulsarBlockEntity pulsar = WorldHelper.getBlockEntity(context.player().getWorld(), this.pos, PulsarBlockEntity.class);
					if (pulsar != null) {
						pulsar.onTime = this.onTime;
						pulsar.offTime = this.offTime;
						pulsar.relativeTo = this.relativeTo;
						pulsar.offset = this.offset;
					}
				}
				else {
					BigTechMod.LOGGER.warn(context.player() + " tried to edit a pulsar without being near it.");
				}
			}
			else {
				context.responseSender().disconnect(Text.literal("Attempted to update pulsar with invalid values"));
			}
		}
	}
}