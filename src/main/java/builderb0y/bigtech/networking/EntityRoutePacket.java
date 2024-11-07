package builderb0y.bigtech.networking;

import java.util.Collection;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.mixinterfaces.RoutableEntity;
import builderb0y.bigtech.mixinterfaces.RoutableEntity.RoutingInfo;
import builderb0y.bigtech.util.Directions;

public class EntityRoutePacket implements S2CPlayPacket<EntityRoutePacket.Payload> {

	public static final EntityRoutePacket INSTANCE = new EntityRoutePacket();

	public void send(Collection<ServerPlayerEntity> tracking, Entity self, RoutingInfo info) {
		Payload payload = null;
		if (self instanceof ServerPlayerEntity player) {
			payload = Payload.from(self, info);
			BigTechNetwork.sendToClient(player, payload);
		}
		if (!tracking.isEmpty()) {
			if (payload == null) payload = Payload.from(self, info);
			for (ServerPlayerEntity player : tracking) {
				BigTechNetwork.sendToClient(player, payload);
			}
		}
	}

	@Override
	public Payload decode(RegistryByteBuf buffer) {
		int entityID = buffer.readInt();
		boolean present = buffer.readBoolean();
		if (present) try {
			BlockPos pos = buffer.readBlockPos();
			BlockState state = BlockArgumentParser.block(Registries.BLOCK, buffer.readString(), false).blockState();
			Direction direction = Directions.ALL[buffer.readByte()];
			return new Payload(entityID, true, pos, state, direction);
		}
		catch (CommandSyntaxException exception) {
			throw new RuntimeException(exception);
		}
		else {
			return new Payload(entityID);
		}
	}

	public static record Payload(int entityID, boolean present, BlockPos pos, BlockState state, Direction direction) implements S2CPayload {

		public Payload(int entityID) {
			this(entityID, false, null, null, null);
		}

		public static Payload from(Entity entity, RoutingInfo info) {
			return (
				info != null
				? new Payload(entity.getId(), true, info.pos(), info.state(), info.direction())
				: new Payload(entity.getId())
			);
		}

		@Override
		public PacketHandler<?> getAssociatedPacket() {
			return INSTANCE;
		}

		@Override
		public void encode(RegistryByteBuf buffer) {
			buffer.writeInt(this.entityID);
			buffer.writeBoolean(this.present);
			if (this.present) {
				buffer.writeBlockPos(this.pos);
				buffer.writeString(BlockArgumentParser.stringifyBlockState(this.state));
				buffer.writeByte(this.direction.ordinal());
			}
		}

		public RoutingInfo toRoutingInfo() {
			return this.present ? new RoutingInfo(this.pos, this.state, this.direction, true) : null;
		}

		@Override
		@Environment(EnvType.CLIENT)
		public void process(ClientPlayNetworking.Context context) {
			ClientWorld world = context.client().world;
			if (world != null) {
				Entity entity = world.getEntityById(this.entityID);
				if (entity != null) {
					entity.<RoutableEntity>as().bigtech_setRoutingInfo(this.toRoutingInfo(), true);
				}
			}
		}
	}
}