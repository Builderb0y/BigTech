package builderb0y.bigtech.blockEntities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class BeamInterceptorBlockEntity extends BlockEntity {

	public @Nullable Vector3f color;
	public boolean locked;

	public BeamInterceptorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public BeamInterceptorBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.BEAM_INTERCEPTOR, pos, state);
	}

	public void setColorAndSync(Vector3f color) {
		this.color = color;
		this.markDirty();
		if (this.world instanceof ServerWorld serverWorld) {
			serverWorld.getChunkManager().markForUpdate(this.pos);
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (this.color != null) nbt.putFloatArray("color", new float[] { this.color.x, this.color.y, this.color.z });
		nbt.putBoolean("locked", this.locked);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		float[] color = nbt.getFloatArray("color");
		if (color.length == 3) {
			this.color = new Vector3f(color);
		}
		else {
			this.color = null;
		}
		this.locked = nbt.getBoolean("locked");
		if (this.world != null && this.world.isClient) {
			this.reRender();
		}
	}

	@Environment(EnvType.CLIENT)
	public void reRender() {
		MinecraftClient.getInstance().worldRenderer.updateBlock(this.world, this.pos, this.cachedState, this.cachedState,  8);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound nbt = super.toInitialChunkDataNbt();
		if (this.color != null) {
			nbt.putFloatArray("color", new float[] { this.color.x, this.color.y, this.color.z });
		}
		else {
			//readNbt() won't be called if we return an empty compound.
			nbt.putBoolean("not_empty", true);
		}
		return nbt;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
}