package builderb0y.bigtech.blockEntities;

import java.util.Locale;
import java.util.Objects;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.inventory.SingleStackInventory;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import builderb0y.bigtech.mixinterfaces.SilverIodideProjectile;
import builderb0y.bigtech.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.screenHandlers.SilverIodideCannonScreenHandler;

public class SilverIodideCannonBlockEntity extends LockableContainerBlockEntity implements SingleStackInventory, ExtendedScreenHandlerFactory {

	public ItemStack stack = ItemStack.EMPTY;

	public SilverIodideCannonBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public SilverIodideCannonBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.SILVER_IODIDE_CANNON, pos, state);
	}

	@Override
	public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buffer) {
		buffer.writeBlockPos(this.pos);
	}

	public void fire(PlayerEntity player, boolean moreRainy) {
		if (this.stack.item == Items.FIREWORK_ROCKET) {
			ItemStack split = this.stack.split(1);
			FireworkRocketEntity entity = new FireworkRocketEntity(
				this.world,
				player,
				this.pos.x + 0.5D,
				this.pos.y + 0.5D,
				this.pos.z + 0.5D,
				split
			);
			NbtCompound fireworks = split.getSubNbt(FireworkRocketItem.FIREWORKS_KEY);
			if (fireworks != null) {
				NbtList explosions = fireworks.getList(FireworkRocketItem.EXPLOSIONS_KEY, NbtElement.COMPOUND_TYPE);
				if (!explosions.isEmpty) {
					((SilverIodideProjectile)(entity)).bigtech_setProjectileType(moreRainy ? SilverIodideProjectile.Type.MORE_RAINY : SilverIodideProjectile.Type.LESS_RAINY);
				}
			}
			this.world.spawnEntity(entity);
			this.world.<ServerWorld>as().spawnParticles(ParticleTypes.EXPLOSION, this.pos.x + 0.5D, this.pos.y + 0.5D, this.pos.z + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
			this.world.syncWorldEvent(WorldEvents.DISPENSER_LAUNCHES_PROJECTILE, this.pos, 0);
			this.markDirty();
		}
		else if (!this.stack.isEmpty) {
			ItemStack split = this.stack.split(1);
			ItemEntity entity = new ItemEntity(this.world, this.pos.x + 0.5D, this.pos.y + 1, this.pos.z + 0.5D, split);
			this.world.spawnEntity(entity);
			this.world.syncWorldEvent(WorldEvents.DISPENSER_DISPENSES, this.pos, 0);
			this.markDirty();
		}
		else {
			this.world.syncWorldEvent(WorldEvents.DISPENSER_FAILS, this.pos, 0);
		}
		this.world.emitGameEvent(GameEvent.BLOCK_ACTIVATE, this.pos, GameEvent.Emitter.of(player, this.cachedState));
	}

	@Override
	public Text getContainerName() {
		return Text.translatable("container.bigtech.silver_iodide_cannon");
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new SilverIodideCannonScreenHandler(BigTechScreenHandlerTypes.SILVER_IODIDE_CANNON, syncId, this, playerInventory, this.pos);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.put("stack", this.stack.writeNbt(new NbtCompound()));
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.stack = ItemStack.fromNbt(nbt.getCompound("stack"));
	}

	@Override
	public ItemStack getStack(int slot) {
		Objects.checkIndex(slot, 1);
		return this.stack;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		Objects.checkIndex(slot, 1);
		ItemStack result = this.stack.split(amount);
		this.markDirty();
		return result;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		Objects.checkIndex(slot, 1);
		this.stack = stack;
		this.markDirty();
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
}