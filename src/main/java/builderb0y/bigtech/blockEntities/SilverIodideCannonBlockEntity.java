package builderb0y.bigtech.blockEntities;

import java.util.Objects;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworksComponent;
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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import builderb0y.bigtech.mixinterfaces.SilverIodideProjectile;
import builderb0y.bigtech.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.screenHandlers.SilverIodideCannonScreenHandler;

public class SilverIodideCannonBlockEntity extends LockableContainerBlockEntity implements SingleStackInventory, ExtendedScreenHandlerFactory<BlockPos> {

	//I used to just have an ItemStack here,
	//but then getHeldStacks() was added,
	//so now I need a full DefaultedList.
	public DefaultedList<ItemStack> stacks = DefaultedList.ofSize(1, ItemStack.EMPTY);

	public SilverIodideCannonBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	public SilverIodideCannonBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.SILVER_IODIDE_CANNON, pos, state);
	}

	@Override
	public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
		return this.pos;
	}

	public void fire(PlayerEntity player, boolean moreRainy) {
		World world = this.world;
		BlockPos pos = this.pos;
		ItemStack stack = this.getStack();
		if (stack.getItem() == Items.FIREWORK_ROCKET) {
			ItemStack split = stack.split(1);
			FireworkRocketEntity entity = new FireworkRocketEntity(
				world,
				player,
				pos.getX() + 0.5D,
				pos.getY() + 0.5D,
				pos.getZ() + 0.5D,
				split
			);
			FireworksComponent fireworks = split.get(DataComponentTypes.FIREWORKS);
			if (fireworks != null && !fireworks.explosions().isEmpty()) {
				entity.<SilverIodideProjectile>as().bigtech_setProjectileType(moreRainy ? SilverIodideProjectile.Type.MORE_RAINY : SilverIodideProjectile.Type.LESS_RAINY);
			}
			world.spawnEntity(entity);
			world.<ServerWorld>as().spawnParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
			world.syncWorldEvent(WorldEvents.DISPENSER_LAUNCHES_PROJECTILE, pos, 0);
			this.markDirty();
		}
		else if (!stack.isEmpty()) {
			ItemStack split = stack.split(1);
			ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D, split);
			world.spawnEntity(entity);
			world.syncWorldEvent(WorldEvents.DISPENSER_DISPENSES, pos, 0);
			this.markDirty();
		}
		else {
			world.syncWorldEvent(WorldEvents.DISPENSER_FAILS, pos, 0);
		}
		world.emitGameEvent(GameEvent.BLOCK_ACTIVATE, pos, GameEvent.Emitter.of(player, this.getCachedState()));
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
	public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.put("stack", this.getStack().encodeAllowEmpty(registryLookup));
	}

	@Override
	public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.setStack(ItemStack.fromNbtOrEmpty(registryLookup, nbt.getCompound("stack")));
	}

	@Override
	public DefaultedList<ItemStack> getHeldStacks() {
		return this.stacks;
	}

	@Override
	public void setHeldStacks(DefaultedList<ItemStack> stacks) {
		this.stacks = stacks;
	}

	@Override
	public ItemStack getStack() {
		return this.stacks.get(0);
	}

	@Override
	public void setStack(ItemStack stack) {
		this.stacks.set(0, stack);
	}
}