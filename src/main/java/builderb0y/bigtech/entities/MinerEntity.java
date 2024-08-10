package builderb0y.bigtech.entities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.entity.vehicle.VehicleInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes.BoxConsumer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import builderb0y.bigtech.api.EntityAddedToWorldEvent;
import builderb0y.bigtech.beams.impl.DestructionManager;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.networking.ControlMinerPacket;
import builderb0y.bigtech.screenHandlers.BigTechScreenHandlerTypes;
import builderb0y.bigtech.screenHandlers.MinerScreenHandler;
import builderb0y.bigtech.util.WorldHelper;

public class MinerEntity extends VehicleEntity implements VehicleInventory, SidedInventory, RideableInventory {

	public static final int
		FORWARD   = 1 << 0,
		BACKWARD  = 1 << 1,
		LEFT      = 1 << 2,
		RIGHT     = 1 << 3,
		SPRINTING = 1 << 4;
	public static final int
		STORAGE_START  = 0,
		STORAGE_SIZE   = 6 * 9,
		STORAGE_END    = STORAGE_START + STORAGE_SIZE,
		FUEL_START     = STORAGE_END,
		FUEL_SIZE      = 1,
		FUEL_END       = FUEL_START + FUEL_SIZE,
		SMELTING_START = FUEL_END,
		SMELTING_SIZE  = 1,
		SMELTING_END   = SMELTING_START + SMELTING_SIZE,
		TOTAL_SIZE     = SMELTING_END;
	public static final int
		SLOWDOWN_THRESHOLD = 200;

	public static final ThreadLocal<MinerEntity> MINING_MINER = new ThreadLocal<>();
	static {
		EntityAddedToWorldEvent.EVENT.register((Entity entity) -> {
			if (entity instanceof ItemEntity itemEntity) {
				MinerEntity miner = MINING_MINER.get();
				if (miner != null) {
					ItemStack stack = itemEntity.getStack();
					for (int slot = STORAGE_START; slot < STORAGE_END; slot++) {
						ItemStack existing = miner.getStack(slot);
						if (existing.isEmpty()) {
							miner.setStack(slot, stack);
							itemEntity.setStack(ItemStack.EMPTY);
							return false;
						}
						else if (ItemStack.areItemsAndComponentsEqual(existing, stack)) {
							int transferred = Math.min(stack.getCount(), existing.getMaxCount() - existing.getCount());
							existing.increment(transferred);
							stack.decrement(transferred);
							if (stack.isEmpty()) {
								itemEntity.setStack(ItemStack.EMPTY);
								return false;
							}
						}
					}
					itemEntity.setStack(stack.copy());
				}
			}
			return true;
		});
	}

	public static final TrackedData<Byte> INPUT = DataTracker.registerData(MinerEntity.class, TrackedDataHandlerRegistry.BYTE);
	public static final TrackedData<Float> FUEL_FRACTION = DataTracker.registerData(MinerEntity.class, TrackedDataHandlerRegistry.FLOAT);

	public short number;
	public float angularMomentum;

	public double lerpX, lerpY, lerpZ;
	public float lerpYaw;
	public int lerpTicks;

	public Map<BlockPos, BlockState> lastTickBreakingBlocks = Collections.emptyMap();

	public Property fuelTicks = Property.create();
	public int smeltingTicks;
	public SmeltingRecipe activeSmeltingRecipe;

	public MinerEntity(EntityType<?> type, World world) {
		super(type, world);
		this.number = (short)(world.random.nextInt(1000));
	}

	@Override
	public float getStepHeight() {
		return 0.6F;
	}

	@Override
	public void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(INPUT, (byte)(0)).add(FUEL_FRACTION, 0.0F);
	}

	public static void trySpawnAt(World world, BlockPos pos, BlockState chestState) {
		ChestType type = chestState.get(ChestBlock.CHEST_TYPE);
		Direction backDirection = chestState.get(ChestBlock.FACING).getOpposite();
		Direction sideDirection;
		switch (type) {
			default     -> { return; }
			case SINGLE -> { return; }
			case LEFT   -> sideDirection = backDirection.rotateYCounterclockwise();
			case RIGHT  -> sideDirection = backDirection.rotateYClockwise();
		}
		BlockPos chestPos1 = pos, chestPos2, terracottaPos1, terracottaPos2, topIronPos1, topIronPos2, bottomIronPos1, bottomIronPos2;
		if (
			world.getBlockState(chestPos2 = chestPos1.offset(sideDirection)).getBlock() != Blocks.CHEST ||
			world.getBlockState(terracottaPos1 = chestPos1.down()).getBlock() != Blocks.YELLOW_TERRACOTTA ||
			world.getBlockState(terracottaPos2 = chestPos2.down()).getBlock() != Blocks.YELLOW_TERRACOTTA ||
			world.getBlockState(topIronPos1 = chestPos1.offset(backDirection)).getBlock() != Blocks.IRON_BLOCK ||
			world.getBlockState(topIronPos2 = chestPos2.offset(backDirection)).getBlock() != Blocks.IRON_BLOCK ||
			world.getBlockState(bottomIronPos1 = topIronPos1.down()).getBlock() != Blocks.IRON_BLOCK ||
			world.getBlockState(bottomIronPos2 = topIronPos2.down()).getBlock() != Blocks.IRON_BLOCK
		) return;

		MinerEntity miner = new MinerEntity(BigTechEntityTypes.MINER, world);
		miner.refreshPositionAndAngles(
			terracottaPos1.getX() + 0.5D + backDirection.getOffsetX() * 0.5D + sideDirection.getOffsetX() * 0.5D,
			terracottaPos1.getY(),
			terracottaPos1.getZ() + 0.5D + backDirection.getOffsetZ() * 0.5D + sideDirection.getOffsetZ() * 0.5D,
			backDirection.asRotation(),
			0.0F
		);
		ChestBlockEntity chest1 = WorldHelper.getBlockEntity(world, chestPos1, ChestBlockEntity.class);
		ChestBlockEntity chest2 = WorldHelper.getBlockEntity(world, chestPos2, ChestBlockEntity.class);
		Text name;
		if (chest1 != null && (name = chest1.getCustomName()) != null) {
			miner.setCustomName(name.copy());
		}
		if (chest2 != null) for (int slot = 0; slot < 27; slot++) {
			miner.setStack(slot, chest2.removeStack(slot));
		}
		if (chest1 != null) for (int slot = 0; slot < 27; slot++) {
			miner.setStack(slot + 27, chest1.removeStack(slot));
		}
		world.removeBlock(chestPos1,      false);
		world.removeBlock(chestPos2,      false);
		world.removeBlock(terracottaPos1, false);
		world.removeBlock(terracottaPos2, false);
		world.removeBlock(topIronPos1,    false);
		world.removeBlock(topIronPos2,    false);
		world.removeBlock(bottomIronPos1, false);
		world.removeBlock(bottomIronPos2, false);
		world.spawnEntity(miner);
	}

	public boolean isPressingForward (byte input) { return (input & FORWARD  ) != 0; }
	public boolean isPressingBackward(byte input) { return (input & BACKWARD ) != 0; }
	public boolean isPressingLeft    (byte input) { return (input & LEFT     ) != 0; }
	public boolean isPressingRight   (byte input) { return (input & RIGHT    ) != 0; }
	public boolean isPressingSprint  (byte input) { return (input & SPRINTING) != 0; }

	public float getTargetForwardMomentum(byte input) {
		float targetForwardMomentum = 0.0F;
		if (this.isPressingForward (input)) targetForwardMomentum += this.isPressingSprint(input) ? 0.375F : 0.25F;
		if (this.isPressingBackward(input)) targetForwardMomentum -= this.isPressingSprint(input) ? 0.25F  : 0.1875F;
		return targetForwardMomentum * this.dataTracker.get(FUEL_FRACTION);
	}

	public float getTargetAngularMomentum(byte input) {
		float targetAngularMomentum = 0.0F;
		if (this.isPressingLeft (input)) targetAngularMomentum -= 4.0F;
		if (this.isPressingRight(input)) targetAngularMomentum += 4.0F;
		return targetAngularMomentum * this.dataTracker.get(FUEL_FRACTION);
	}

	@Environment(EnvType.CLIENT)
	public static byte encodeInput(ClientPlayerEntity client) {
		int input = 0;
		if (client.input.pressingForward) input |= FORWARD;
		if (client.input.pressingBack   ) input |= BACKWARD;
		if (client.input.pressingLeft   ) input |= LEFT;
		if (client.input.pressingRight  ) input |= RIGHT;
		if (client.isSprinting()        ) input |= SPRINTING;
		return (byte)(input);
	}

	@Environment(EnvType.CLIENT)
	public void updateInput(PlayerEntity player) {
		byte input = encodeInput((ClientPlayerEntity)(player));
		this.dataTracker.set(INPUT, input);
		ControlMinerPacket.INSTANCE.send(input);
	}

	@Override
	public boolean canSprintAsVehicle() {
		return true;
	}

	public void updateFuel(PlayerEntity controller) {
		if (controller == null) {
			this.smeltingTicks = 0;
			return;
		}
		if (this.fuelTicks.get() > 0) this.fuelTicks.set(this.fuelTicks.get() - (controller.isSprinting() ? 2 : 1));
		if (this.fuelTicks.get() < SLOWDOWN_THRESHOLD) {
			ItemStack stack = this.getStack(FUEL_START);
			int fuel;
			if (!stack.isEmpty() && (fuel = FuelRegistry.INSTANCE.get(stack.getItem())) > 0) {
				stack.decrement(1);
				this.fuelTicks.set(this.fuelTicks.get() + fuel);
			}
		}
		if (this.fuelTicks.get() > 0) {
			ItemStack toSmelt = this.getStack(SMELTING_START);
			if (!toSmelt.isEmpty()) {
				SingleStackRecipeInput inventory = new SingleStackRecipeInput(toSmelt);
				if (this.activeSmeltingRecipe == null || !this.activeSmeltingRecipe.matches(inventory, this.getWorld())) {
					this.activeSmeltingRecipe = this.getWorld().getRecipeManager().getFirstMatch(RecipeType.SMELTING, inventory, this.getWorld()).map(RecipeEntry::value).orElse(null);
				}
				if (this.activeSmeltingRecipe != null && ++this.smeltingTicks >= this.activeSmeltingRecipe.getCookingTime()) {
					ItemStack smelted = this.activeSmeltingRecipe.craft(inventory, this.getWorld().getRegistryManager());
					if (!smelted.isEmpty()) {
						int available = 0;
						for (int slot = STORAGE_END; --slot >= STORAGE_START; ) {
							ItemStack existing = this.getStack(slot);
							if (existing.isEmpty()) {
								available = smelted.getCount();
								break;
							}
							else if (ItemStack.areItemsAndComponentsEqual(existing, smelted)) {
								available += existing.getMaxCount() - existing.getCount();
							}
						}
						if (available >= smelted.getCount()) {
							for (int slot = STORAGE_END; --slot >= STORAGE_START; ) {
								ItemStack existing = this.getStack(slot);
								if (existing.isEmpty()) {
									this.setStack(slot, smelted);
									break;
								}
								else if (ItemStack.areItemsAndComponentsEqual(existing, smelted)) {
									int transferred = Math.min(smelted.getCount(), existing.getMaxCount() - existing.getCount());
									existing.increment(transferred);
									smelted.decrement(transferred);
								}
							}
							this.smeltingTicks = 0;
							toSmelt.decrement(1);
						}
					}
				}
			}
			else {
				this.smeltingTicks = 0;
			}
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (this.getDamageWobbleTicks() > 0) {
			this.setDamageWobbleTicks(this.getDamageWobbleTicks() - 1);
		}

		if (this.getDamageWobbleStrength() > 0.0F) {
			this.setDamageWobbleStrength(this.getDamageWobbleStrength() - 1.0F);
		}

		PlayerEntity controller = this.getControllingPassenger();

		this.updateFuel(controller);
		if (!this.getWorld().isClient) {
			this.dataTracker.set(FUEL_FRACTION, ((float)(Math.min(this.fuelTicks.get(), SLOWDOWN_THRESHOLD))) / ((float)(SLOWDOWN_THRESHOLD)));
		}

		if (controller != null) {
			if (this.getWorld().isClient) {
				this.updateInput(controller);
			}
		}
		else {
			this.dataTracker.set(INPUT, (byte)(0));
		}

		byte input = this.dataTracker.get(INPUT);
		float slipperiness = this.computeSlipperiness();
		this.angularMomentum = MathHelper.lerp(slipperiness, this.getTargetAngularMomentum(input), this.angularMomentum);
		if (Math.abs(this.angularMomentum) < 1.0E-3F) {
			this.angularMomentum = 0.0F;
		}
		else {
			this.setYaw(this.getYaw() + this.angularMomentum);
		}
		double newForwardSpeed = this.getTargetForwardMomentum(input);
		double velocityX = -Math.sin(Math.toRadians(this.getYaw())) * newForwardSpeed;
		double velocityZ =  Math.cos(Math.toRadians(this.getYaw())) * newForwardSpeed;
		this.setVelocity(
			MathHelper.lerp(slipperiness, velocityX, this.getVelocity().x),
			(this.getVelocity().y - 0.08D) * 0.99D,
			MathHelper.lerp(slipperiness, velocityZ, this.getVelocity().z)
		);
		this.move(MovementType.SELF, this.getVelocity());

		if (this.isOnGround() && newForwardSpeed > 0.0D && this.horizontalCollision) {
			float adjustment = ((float)(Math.sin(this.getYaw() * (Math.PI / 45.0D)))) * -4.0F;
			this.angularMomentum = MathHelper.lerp(slipperiness, adjustment, this.angularMomentum);
		}

		Map<BlockPos, BlockState> currBreakingPositions = newForwardSpeed > 0.0D ? this.computeBreakingPositions() : Collections.emptyMap();
		if (!this.getWorld().isClient) {
			Map<BlockPos, BlockState> prevBreakingPositions = this.lastTickBreakingBlocks;
			DestructionManager manager = DestructionManager.forWorld(this.getWorld());
			//remove positions which we are no longer breaking.
			prevBreakingPositions.forEach((BlockPos pos, BlockState state) -> {
				if (currBreakingPositions.get(pos) != state) {
					manager.resetProgress(pos);
				}
			});
			MinerEntity old = MINING_MINER.get();
			try {
				MINING_MINER.set(this);
				float breakSpeed = this.isPressingSprint(input) ? 1.5F : 1.0F;
				currBreakingPositions.forEach((BlockPos pos, BlockState state) -> {
					manager.increaseDamage(pos, state, breakSpeed, FunctionalItems.MINER_TOOL.getDefaultStack());
				});
			}
			finally {
				MINING_MINER.set(old);
			}
			this.lastTickBreakingBlocks = currBreakingPositions;
		}
		else {
			this.spawnBreakingParticles(currBreakingPositions);
		}
		if (this.isLogicalSideForUpdatingMovement()) {
			this.lerpTicks = 0;
			this.updateTrackedPosition(this.getX(), this.getY(), this.getZ());
		}
		else if (this.lerpTicks > 0) {
			this.lerpPosAndRotation(this.lerpTicks, this.lerpX, this.lerpY, this.lerpZ, this.lerpYaw, this.getPitch());
			this.lerpTicks--;
		}
	}

	//welcome to over-engineered code.
	public float computeSlipperiness() {
		class Accumulator implements BoxConsumer {

			public float sum;
			public float area;
			public float currentSlipperiness;
			public BlockPos pos;

			@Override
			public void consume(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
				maxY += this.pos.getY();
				Box minerBox = MinerEntity.this.getBoundingBox();
				if (MathHelper.approximatelyEquals(maxY, minerBox.minY)) {
					minX = Math.max(minX + this.pos.getX(), minerBox.minX);
					maxX = Math.min(maxX + this.pos.getX(), minerBox.maxX);
					minZ = Math.max(minZ + this.pos.getZ(), minerBox.minZ);
					maxZ = Math.min(maxZ + this.pos.getZ(), minerBox.maxZ);
					float intersectedArea = (float)(Math.max(maxX - minX, 0.0D) * Math.max(maxZ - minZ, 0.0D));
					this.area += intersectedArea;
					this.sum += intersectedArea * this.currentSlipperiness;
				}
			}

			public float get() {
				return this.area == 0.0F ? 1.0F : this.sum / this.area;
			}
		}
		if (!this.isOnGround()) return 1.0F;
		Accumulator accumulator = new Accumulator();
		int minX = MathHelper.floor(this.getBoundingBox().minX);
		int minZ = MathHelper.floor(this.getBoundingBox().minZ);
		int maxX = MathHelper.ceil(this.getBoundingBox().maxX) - 1;
		int maxZ = MathHelper.ceil(this.getBoundingBox().maxZ) - 1;
		int y = MathHelper.floor(this.getY() - 0.0001D);
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		accumulator.pos = mutablePos;
		for (int z = minZ; z <= maxZ; z++) {
			for (int x = minX; x <= maxX; x++) {
				BlockState state = this.getWorld().getBlockState(mutablePos.set(x, y, z));
				accumulator.currentSlipperiness = state.getBlock().getSlipperiness();
				VoxelShape shape = state.getCollisionShape(this.getWorld(), mutablePos, ShapeContext.of(this));
				shape.forEachBox(accumulator);
			}
		}
		return accumulator.get();
	}

	public Map<BlockPos, BlockState> computeBreakingPositions() {
		Map<BlockPos, BlockState> positions = new HashMap<>(8);
		double sin = Math.sin(Math.toRadians(this.getYaw()));
		double cos = Math.cos(Math.toRadians(this.getYaw()));
		double baseX = this.getX() - sin * 1.125D;
		double baseY = this.getY();
		double baseZ = this.getZ() + cos * 1.125D;
		sin *= 0.5D;
		cos *= 0.5D;
		this.addPosition(positions, baseX - cos, baseY + 0.5D, baseZ - sin);
		this.addPosition(positions, baseX + cos, baseY + 0.5D, baseZ + sin);
		this.addPosition(positions, baseX - cos, baseY + 1.5D, baseZ - sin);
		this.addPosition(positions, baseX + cos, baseY + 1.5D, baseZ + sin);
		return positions;
	}

	public void addPosition(Map<BlockPos, BlockState> positions, double x, double y, double z) {
		BlockPos pos = BlockPos.ofFloored(x, y, z);
		if (positions.containsKey(pos)) return;
		BlockState state = this.getWorld().getBlockState(pos);
		VoxelShape shape = state.getCollisionShape(this.getWorld(), pos, ShapeContext.of(this));
		if (shape.isEmpty()) return;
		Box box = shape.getBoundingBox();
		if (
			this.getBoundingBox().intersects(
				box.minX + pos.getX() - 0.0001D,
				box.minY + pos.getY(),
				box.minZ + pos.getZ() - 0.0001D,
				box.maxX + pos.getX() + 0.0001D,
				box.maxY + pos.getY(),
				box.maxZ + pos.getZ() + 0.0001D
			)
		) {
			positions.put(pos, state);
		}
	}

	@Environment(EnvType.CLIENT)
	public void spawnBreakingParticles(Map<BlockPos, BlockState> positions) {
		Direction facing = this.getHorizontalFacing().getOpposite();
		for (BlockPos pos : positions.keySet()) {
			MinecraftClient.getInstance().particleManager.addBlockBreakingParticles(pos, facing);
		}
	}

	@Override
	public void move(MovementType movementType, Vec3d movement) {
		super.move(movementType, movement);
		this.getWorld().getEntitiesByClass(Entity.class, this.getBoundingBox(), (Entity entity) -> {
			return entity.getRootVehicle() != this && !entity.isSpectator() && entity.getPistonBehavior() != PistonBehavior.IGNORE;
		})
		.forEach((Entity entity) -> entity.move(MovementType.PISTON, movement));
	}

	@Override
	public boolean canAddPassenger(Entity passenger) {
		return this.getPassengerList().size() < 2;
	}

	@Override
	public Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
		return (
			switch (this.getPassengerList().indexOf(passenger)) {
				case 0 -> new Vec3d( 0.5F, 0.5F, -0.3125F);
				case 1 -> new Vec3d(-0.5F, 0.5F, -0.3125F);
				default -> super.getPassengerAttachmentPos(passenger, dimensions, scaleFactor);
			}
		)
		.rotateY(this.getYaw() * ((float)(Math.PI / -180.0D)));
	}

	public int removedPassengerIndex = -1;

	@Override
	public void removePassenger(Entity passenger) {
		this.removedPassengerIndex = this.getPassengerList().indexOf(passenger);
		super.removePassenger(passenger);
	}

	@Override
	public Vec3d updatePassengerForDismount(LivingEntity passenger) {
		return switch (this.removedPassengerIndex) {
			case 0 -> {
				Vec3d position = getPassengerDismountOffset(this.getWidth() * 1.4142135623730951D /* sqrt(2) */, passenger.getWidth(), this.getYaw() - 90.0F);
				if (!this.getWorld().getBlockCollisions(passenger, passenger.getType().getSpawnBox(this.getX() + position.x, this.getY() + position.y, this.getZ() + position.z)).iterator().hasNext()) {
					yield this.getPos().add(position);
				}
				yield this.getPos();
			}
			case 1 -> {
				Vec3d position = getPassengerDismountOffset(this.getWidth() * 1.4142135623730951D /* sqrt(2) */, passenger.getWidth(), this.getYaw() + 90.0F);
				if (!this.getWorld().getBlockCollisions(passenger, passenger.getType().getSpawnBox(this.getX() + position.x, this.getY() + position.y, this.getZ() + position.z)).iterator().hasNext()) {
					yield this.getPos().add(position);
				}
				yield this.getPos();
			}
			default -> {
				yield this.getPos();
			}
		};
	}

	@Nullable
	@Override
	public PlayerEntity getControllingPassenger() {
		return this.getFirstPassenger() instanceof PlayerEntity player ? player : null;
	}

	@Override
	public void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
		super.updatePassengerPosition(passenger, positionUpdater);
		passenger.horizontalCollision = false; //fix passenger randomly stopping sprinting.
		passenger.setYaw(passenger.getYaw() + this.getYaw() - this.prevYaw);
		passenger.setBodyYaw(this.getYaw());
		this.clampPassengerYaw(passenger);
	}

	@Override
	public void onPassengerLookAround(Entity passenger) {
		super.onPassengerLookAround(passenger);
		this.clampPassengerYaw(passenger);
	}

	public void clampPassengerYaw(Entity passenger) {
		float offset = MathHelper.wrapDegrees(passenger.getYaw() - this.getYaw());
		float clampedOffset = MathHelper.clamp(offset, -105.0F, 105.0F);
		passenger.prevYaw += clampedOffset - offset;
		passenger.setYaw(passenger.getYaw() + clampedOffset - offset);
		passenger.setHeadYaw(passenger.getYaw());
	}

	@Override
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps) {
		this.lerpX = x;
		this.lerpY = y;
		this.lerpZ = z;
		this.lerpYaw = yaw;
		this.lerpTicks = 10;
	}

	@Override
	public double getLerpTargetX() {
		return this.lerpTicks > 0 ? this.lerpX : this.getX();
	}

	@Override
	public double getLerpTargetY() {
		return this.lerpTicks > 0 ? this.lerpY : this.getY();
	}

	@Override
	public double getLerpTargetZ() {
		return this.lerpTicks > 0 ? this.lerpZ : this.getZ();
	}

	@Override
	public float getLerpTargetYaw() {
		return this.lerpTicks > 0 ? this.lerpYaw : this.getYaw();
	}

	@Override
	public boolean isCollidable() {
		return true;
	}

	@Override
	public boolean canHit() {
		return !this.isRemoved() && !this.isThePlayerRiding();
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if (!this.getWorld().isClient) {
			if (player.isSneaking()) player.openHandledScreen(this);
			else player.startRiding(this);
		}
		return ActionResult.SUCCESS;
	}

	public boolean isThePlayerRiding() {
		return this.getWorld().isClient && this.isThePlayerRiding0();
	}

	@Environment(EnvType.CLIENT)
	public boolean isThePlayerRiding0() {
		Entity entity = MinecraftClient.getInstance().cameraEntity;
		return entity != null && entity.getVehicle() == this;
	}

	@Override
	public void slowMovement(BlockState state, Vec3d multiplier) {
		//miners cannot be slowed by simple cobwebs.
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		if (nbt.contains("number", NbtElement.NUMBER_TYPE)) {
			this.number = nbt.getShort("number");
		}
		this.inventory.clear();
		Inventories.readNbt(nbt, this.inventory, this.getWorld().getRegistryManager());
		this.angularMomentum = nbt.getFloat("angular_momentum");
		if (!Float.isFinite(this.angularMomentum)) this.angularMomentum = 0.0F;
		this.fuelTicks.set(nbt.getInt("fuel_ticks"));
		this.smeltingTicks = nbt.getInt("smelting_ticks");
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putShort("number", this.number);
		Inventories.writeNbt(nbt, this.inventory, this.getWorld().getRegistryManager());
		nbt.putFloat("angular_momentum", this.angularMomentum);
		nbt.putInt("fuel_ticks", this.fuelTicks.get());
		nbt.putInt("smelting_ticks", this.smeltingTicks);
	}

	@Override
	public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
		return new EntitySpawnS2CPacket(this, entityTrackerEntry, this.number);
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		this.number = (short)(packet.getEntityData());
	}

	//////////////////////////////// inventory stuff ////////////////////////////////

	public static final int[]
		INVENTORY_SLOTS = slotRange(STORAGE_START, STORAGE_END),
		FUEL_SLOTS      = { FUEL_START, SMELTING_START };

	public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(TOTAL_SIZE, ItemStack.EMPTY);
	public RegistryKey<LootTable> lootTableId;
	public long lootTableSeed;

	public static int[] slotRange(int start, int end) {
		int length = end - start;
		int[] result = new int[length];
		for (int index = 0; index < length; index++) {
			result[index] = index + start;
		}
		return result;
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return side.getAxis() == Axis.Y ? INVENTORY_SLOTS : FUEL_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		if (slot >= STORAGE_START && slot < STORAGE_END) {
			return true;
		}
		if (slot >= FUEL_START && slot < FUEL_END) {
			return AbstractFurnaceBlockEntity.canUseAsFuel(stack);
		}
		if (slot >= SMELTING_START && slot < SMELTING_END) {
			return this.getWorld().getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SingleStackRecipeInput(stack), this.getWorld()).isPresent();
		}
		return false;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return slot >= STORAGE_START && slot < STORAGE_END;
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		if (slot >= STORAGE_START && slot < STORAGE_END) {
			return true;
		}
		if (slot >= FUEL_START && slot < FUEL_END) {
			return AbstractFurnaceBlockEntity.canUseAsFuel(stack);
		}
		if (slot >= SMELTING_START && slot < SMELTING_END) {
			return this.getWorld().getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SingleStackRecipeInput(stack), this.getWorld()).isPresent();
		}
		return false;
	}

	@Nullable
	@Override
	public RegistryKey<LootTable> getLootTable() {
		return this.lootTableId;
	}

	@Override
	public void setLootTable(@Nullable RegistryKey<LootTable> lootTable) {
		this.lootTableId = lootTable;
	}

	@Override
	public long getLootTableSeed() {
		return this.lootTableSeed;
	}

	@Override
	public void setLootTableSeed(long lootTableSeed) {
		this.lootTableSeed = lootTableSeed;
	}

	@Override
	public DefaultedList<ItemStack> getInventory() {
		return this.inventory;
	}

	@Override
	public void resetInventory() {
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
	}

	@Override
	public int size() {
		return TOTAL_SIZE;
	}

	@Override
	public ItemStack getStack(int slot) {
		return this.inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		return Inventories.splitStack(this.inventory, slot, amount);
	}

	@Override
	public ItemStack removeStack(int slot) {
		return this.inventory.set(slot, ItemStack.EMPTY);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		this.inventory.set(slot, stack);
	}

	@Override
	public void markDirty() {}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		if (this.lootTableId != null && player.isSpectator()) {
			return null;
		}
		else {
			this.generateInventoryLoot(playerInventory.player);
			return new MinerScreenHandler(BigTechScreenHandlerTypes.MINER, syncId, this, playerInventory, this.fuelTicks);
		}
	}

	@Override
	public void clear() {
		this.clearInventory();
	}

	@Override
	public void openInventory(PlayerEntity player) {
		player.openHandledScreen(this);
	}

	@Override
	public void killAndDropSelf(DamageSource source) {
		this.kill();
		if (this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			this.dropStack(new ItemStack(Items.CHEST));
			this.dropStack(new ItemStack(Items.CHEST));
			this.dropStack(new ItemStack(Items.YELLOW_TERRACOTTA));
			this.dropStack(new ItemStack(Items.YELLOW_TERRACOTTA));
			this.dropStack(new ItemStack(Items.IRON_BLOCK));
			this.dropStack(new ItemStack(Items.IRON_BLOCK));
			this.dropStack(new ItemStack(Items.IRON_BLOCK));
			this.dropStack(new ItemStack(Items.IRON_BLOCK));
			ItemScatterer.spawn(this.getWorld(), this, this);
		}
	}

	@Override
	public Item asItem() {
		return Items.AIR;
	}
}