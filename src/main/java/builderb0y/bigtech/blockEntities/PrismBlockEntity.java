package builderb0y.bigtech.blockEntities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.PersistentBeam;

public class PrismBlockEntity extends BlockEntity {

	public static final int FLAG_MASK = 0b111_111_111___111_101_111___111_111_111;

	public int lenses;

	public PrismBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public PrismBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.PRISM, pos, state);
	}

	public boolean hasAnyLenses() {
		return this.lenses != 0;
	}

	public boolean hasLens(BeamDirection direction) {
		return (this.lenses & direction.flag()) != 0;
	}

	public boolean addLens(BeamDirection direction) {
		if (direction == BeamDirection.CENTER) return false;
		int oldFlags = this.lenses;
		int newFlags = oldFlags | direction.flag();
		if (oldFlags != newFlags) {
			this.lenses = newFlags;
			this.lensesChanged();
			return true;
		}
		return false;
	}

	public boolean removeLens(BeamDirection direction) {
		if (direction == BeamDirection.CENTER) return false;
		int oldFlags = this.lenses;
		int newFlags = oldFlags & ~direction.flag();
		if (oldFlags != newFlags) {
			this.lenses = newFlags;
			this.lensesChanged();
			return true;
		}
		return false;
	}

	public boolean toggleLens(BeamDirection direction) {
		if (direction == BeamDirection.CENTER) return false;
		this.lenses ^= direction.flag();
		this.lensesChanged();
		return true;
	}

	public int countLenses() {
		return Integer.bitCount(this.lenses);
	}

	public void lensesChanged() {
		if (this.world instanceof ServerWorld serverWorld) {
			serverWorld.getChunkManager().markForUpdate(this.pos);
			PersistentBeam.notifyBlockChanged(serverWorld, this.pos, this.getCachedState(), this.getCachedState());
		}
	}

	@Environment(EnvType.CLIENT)
	public void reRender() {
		MinecraftClient.getInstance().worldRenderer.updateBlock(this.world, this.pos, this.getCachedState(), this.getCachedState(),  8);
	}

	@Override
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.lenses = nbt.getInt("lenses") & FLAG_MASK;
		if (this.world != null && this.world.isClient) {
			this.reRender();
		}
	}

	@Override
	public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.putInt("lenses", this.lenses);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		return super.toInitialChunkDataNbt(registryLookup).withInt("lenses", this.lenses);
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
}