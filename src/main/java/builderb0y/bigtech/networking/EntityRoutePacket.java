package builderb0y.bigtech.networking;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.mixinterfaces.RoutableEntity;
import builderb0y.bigtech.mixinterfaces.RoutableEntity.RoutingInfo;
import builderb0y.bigtech.util.Enums;

public record EntityRoutePacket(int entityID, boolean present, BlockPos pos, BlockState state, Direction direction) implements S2CPlayPacket {

	public EntityRoutePacket(int entityID) {
		this(entityID, false, null, null, null);
	}

	public static EntityRoutePacket from(Entity entity, RoutingInfo info) {
		return (
			info != null
			? new EntityRoutePacket(entity.id, true, info.pos, info.state, info.direction)
			: new EntityRoutePacket(entity.id)
		);
	}

	public static EntityRoutePacket parse(PacketByteBuf buffer) {
		int entityID = buffer.readInt();
		boolean present = buffer.readBoolean();
		if (present) try {
			BlockPos pos = buffer.readBlockPos();
			BlockState state = BlockArgumentParser.block(Registries.BLOCK.readOnlyWrapper, buffer.readString(), false).blockState();
			Direction direction = Enums.DIRECTIONS[buffer.readByte()];
			return new EntityRoutePacket(entityID, true, pos, state, direction);
		}
		catch (CommandSyntaxException exception) {
			throw new RuntimeException(exception);
		}
		else {
			return new EntityRoutePacket(entityID);
		}
	}

	@Override
	public void write(PacketByteBuf buffer) {
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
	public void handle(ClientPlayerEntity player, PacketSender responseSender) {
		System.out.println("Received routing packet: " + this.toRoutingInfo());
		ClientWorld world = MinecraftClient.getInstance().world;

		if (world != null) {
			Entity entity = world.getEntityById(this.entityID);
			if (entity != null) {
				entity.<RoutableEntity>as().bigtech_setRoutingInfo(this.toRoutingInfo(), true);
			}
		}
	}

	@Override
	public PacketType<?> getType() {
		return BigTechClientNetwork.ENTITY_ROUTE;
	}
}