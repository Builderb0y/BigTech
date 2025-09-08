package builderb0y.bigtech.blockEntities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.ComponentMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
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
	public void readData(ReadView view) {
		super.readData(view);
		int[] color = view.getOptionalIntArray("color").orElse(null);
		if (color != null && color.length == 3) {
			this.color = new Vector3f(
				Float.intBitsToFloat(color[0]),
				Float.intBitsToFloat(color[1]),
				Float.intBitsToFloat(color[2])
			);
		}
		else {
			this.color = null;
		}
		this.locked = view.getBoolean("locked", false);
		if (this.world != null && this.world.isClient) {
			this.reRender();
		}
	}

	@Override
	public void writeData(WriteView view) {
		super.writeData(view);
		if (this.color != null) view.putIntArray("color", new int[] {
			Float.floatToRawIntBits(this.color.x),
			Float.floatToRawIntBits(this.color.y),
			Float.floatToRawIntBits(this.color.z)
		});
		view.putBoolean("locked", this.locked);
	}

	@Environment(EnvType.CLIENT)
	public void reRender() {
		MinecraftClient.getInstance().worldRenderer.updateBlock(this.world, this.pos, this.getCachedState(), this.getCachedState(),  8);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		NbtCompound nbt = super.toInitialChunkDataNbt(registryLookup);
		if (this.color != null) {
			nbt.putFloatArray("color", new float[] { this.color.x, this.color.y, this.color.z });
		}
		else {
			//readNbt() won't be called if we return an empty compound.
			nbt.putBoolean("not_empty", true);
		}
		return nbt;
	}

	@Override
	public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
}