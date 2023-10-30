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

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.mixinterfaces.RoutableEntity;
import builderb0y.bigtech.mixinterfaces.RoutableEntity.RoutingInfo;
import builderb0y.bigtech.util.Enums;

public record EntityRouteSyncPacket(int entityID, boolean present, BlockPos pos, BlockState state, Direction direction) implements S2CPlayPacket {

	public static final PacketType<EntityRouteSyncPacket> TYPE = PacketType.create(BigTechMod.modID("entity_route_sync"), EntityRouteSyncPacket::parse);

	public EntityRouteSyncPacket(int entityID) {
		this(entityID, false, null, null, null);
	}

	public static EntityRouteSyncPacket from(Entity entity, RoutingInfo info) {
		return (
			info != null
			? new EntityRouteSyncPacket(entity.id, true, info.pos, info.state, info.direction)
			: new EntityRouteSyncPacket(entity.id)
		);
	}

	public static EntityRouteSyncPacket parse(PacketByteBuf buffer) {
		int entityID = buffer.readInt();
		boolean present = buffer.readBoolean();
		if (present) try {
			BlockPos pos = buffer.readBlockPos();
			BlockState state = BlockArgumentParser.block(Registries.BLOCK.readOnlyWrapper, buffer.readString(), false).blockState();
			Direction direction = Enums.DIRECTIONS[buffer.readByte()];
			return new EntityRouteSyncPacket(entityID, true, pos, state, direction);
		}
		catch (CommandSyntaxException exception) {
			throw new RuntimeException(exception);
		}
		else {
			return new EntityRouteSyncPacket(entityID);
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
				((RoutableEntity)(entity)).bigtech_setRoutingInfo(this.toRoutingInfo(), true);
			}
		}
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}
}