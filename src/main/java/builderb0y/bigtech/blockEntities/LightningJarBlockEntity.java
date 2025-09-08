package builderb0y.bigtech.blockEntities;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;

import builderb0y.bigtech.dataComponents.BigTechDataComponents;

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
				this.world.updateComparators(this.pos, this.getCachedState().getBlock());
			}
		}
	}

	@Override
	public void readData(ReadView view) {
		super.readData(view);
		this.storedEnergy = view.getInt("energy", 0);
	}

	@Override
	public void writeData(WriteView view) {
		super.writeData(view);
		view.putInt("energy", this.storedEnergy);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		return super.toInitialChunkDataNbt(registryLookup).withInt("energy", this.storedEnergy);
	}

	@Override
	public void addComponents(ComponentMap.Builder componentMapBuilder) {
		super.addComponents(componentMapBuilder);
		componentMapBuilder.add(BigTechDataComponents.LIGHTNING_ENERGY, this.storedEnergy);
	}

	@Override
	public void readComponents(ComponentsAccess components) {
		super.readComponents(components);
		this.storedEnergy = components.getOrDefault(BigTechDataComponents.LIGHTNING_ENERGY, 0);
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}
}