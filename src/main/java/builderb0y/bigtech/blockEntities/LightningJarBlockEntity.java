package builderb0y.bigtech.blockEntities;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class LightningJarBlockEntity extends BlockEntity {

	public int storedEnergy;

	public LightningJarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public LightningJarBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.LIGHTNING_JAR, pos, state);
	}

	public void setStoredEnergyAndSync(int storedEnergy) {
		if (this.storedEnergy != storedEnergy) {
			this.storedEnergy = storedEnergy;
			if (this.world instanceof ServerWorld serverWorld) {
				serverWorld.getChunkManager().markForUpdate(this.pos);
				this.world.updateComparators(this.pos, this.cachedState.getBlock());
			}
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.storedEnergy = nbt.getInt("energy");
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("energy", this.storedEnergy);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return super.toInitialChunkDataNbt().withInt("energy", this.storedEnergy);
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
}