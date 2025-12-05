package builderb0y.bigtech.blockEntities;

import java.util.*;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.ReadView.TypedListReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.storage.WriteView.ListAppender;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Uuids;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.BigTechProperties;
import builderb0y.bigtech.blocks.BuildingBlock.BuildingBlockMode;
import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.util.Symmetry;
import builderb0y.bigtech.util.WorldHelper;

public class BuildingBlockEntity extends BlockEntity {

	public static final BlockEntityTicker<BuildingBlockEntity>
		TICKER = (World world, BlockPos pos, BlockState state, BuildingBlockEntity blockEntity) -> blockEntity.tick();

	public static final ThreadLocal<Boolean> PLACING = ThreadLocal.withInitial(() -> Boolean.FALSE);
	public static final WeakHashMap<ServerWorld, Set<BlockPos>> ACTIVE_INSTANCES = new WeakHashMap<>();
	static {
		ServerTickEvents.END_WORLD_TICK.register((ServerWorld world) -> {
			Set<BlockPos> positions = ACTIVE_INSTANCES.get(world);
			if (positions != null) positions.removeIf((BlockPos pos) -> !(
				world.isPosLoaded(pos) &&
				world.getBlockEntity(pos) instanceof BuildingBlockEntity building &&
				building.parent == null
			));
		});
		UseBlockCallback.EVENT.register((PlayerEntity player, World world, Hand hand, BlockHitResult hit) -> {
			if (PLACING.get()) return ActionResult.PASS;
			Set<BlockPos> startingPoints;
			ItemStack stack;
			if (
				player instanceof ServerPlayerEntity serverPlayer &&
				!player.isSpectator() &&
				!player.isSneaking() &&
				(stack = player.getStackInHand(hand)).getItem() instanceof BlockItem &&
				!stack.isOf(FunctionalItems.BUILDING_BLOCK) &&
				(startingPoints = ACTIVE_INSTANCES.get(world)) != null &&
				!startingPoints.isEmpty()
			) {
				PlacementData startData = new PlacementData(
					serverPlayer,
					serverPlayer.getPos(),
					serverPlayer.getYaw(),
					hit
				);
				List<PlacementData> queue = new ArrayList<>();
				for (BlockPos start : startingPoints) {
					if (
						world.isPosLoaded(start) &&
						world.getBlockEntity(start) instanceof BuildingBlockEntity building &&
						building.isOwnedBy(player) &&
						building.isInRange(startData.hit.getBlockPos())
					) {
						building
						.symmetrify(startData)
						.forEachOrdered(queue::add);
					}
				}
				if (!queue.isEmpty()) {
					if (stack.getCount() >= queue.size() || player.isCreative()) {
						PLACING.set(Boolean.TRUE);
						try {
							for (PlacementData placement : queue) {
								placement.prepare();
								placement.place(hand);
							}
						}
						finally {
							PLACING.set(Boolean.FALSE);
							startData.prepare();
						}
						return ActionResult.SUCCESS;
					}
					else {
						serverPlayer.networkHandler.sendPacket(
							serverPlayer.getInventory().createSlotSetPacket(
								switch (hand) {
									case MAIN_HAND -> serverPlayer.getInventory().getSelectedSlot();
									case OFF_HAND -> PlayerInventory.OFF_HAND_SLOT;
								}
							)
						);
						return ActionResult.FAIL;
					}
				}
			}
			return ActionResult.PASS;
		});
		PlayerBlockBreakEvents.AFTER.register((
			World world,
			PlayerEntity player,
			BlockPos pos,
			BlockState state,
			@Nullable BlockEntity blockEntity
		) -> {
			if (
				!PLACING.get() &&
				player instanceof ServerPlayerEntity serverPlayer &&
				!player.isSneaking() &&
				!state.isOf(FunctionalBlocks.BUILDING_BLOCK)
			) {
				Set<BlockPos> startPositions = ACTIVE_INSTANCES.get(world);
				if (startPositions != null) {
					List<PlacementData> finalPositions = new ArrayList<>();
					PlacementData startData = new PlacementData(
						serverPlayer,
						serverPlayer.getPos(),
						serverPlayer.getYaw(),
						new BlockHitResult(
							Vec3d.ofCenter(pos),
							Direction.UP,
							pos,
							false
						)
					);
					for (BlockPos start : startPositions) {
						if (
							world.isPosLoaded(start) &&
							world.getBlockEntity(start) instanceof BuildingBlockEntity building &&
							building.isOwnedBy(player) &&
							building.isInRange(pos)
						) {
							building
							.symmetrify(startData)
							.forEachOrdered(finalPositions::add);
						}
					}
					if (!finalPositions.isEmpty()) {
						PLACING.set(Boolean.TRUE);
						try {
							Vec3d center = Vec3d.ofCenter(pos);
							for (PlacementData placement : finalPositions) {
								if (world.getBlockState(placement.hit.getBlockPos()).getBlock() == state.getBlock()) {
									Vec3d offset = center.subtract(placement.hit.getPos());
									placement.prepare();
									WorldHelper.runEntitySpawnAction(
										placement::breakBlock,
										(Entity entity) -> {
											entity.setPosition(entity.getPos().add(offset));
											return true;
										}
									);
								}
							}
						}
						finally {
							PLACING.set(Boolean.FALSE);
							startData.prepare();
						}
					}
				}
			}
		});
	}
	public static record PlacementData(
		ServerPlayerEntity player,
		Vec3d playerPos,
		float playerYaw,
		BlockHitResult hit
	) {

		public PlacementData subtract(BlockPos pos) {
			if (pos.equals(BlockPos.ORIGIN)) return this;
			Vec3d center = Vec3d.ofBottomCenter(pos);
			return new PlacementData(
				this.player,
				this.playerPos.subtract(center),
				this.playerYaw,
				new BlockHitResult(
					this.hit.getPos().subtract(center),
					this.hit.getSide(),
					this.hit.getBlockPos().subtract(pos),
					this.hit.isInsideBlock()
				)
			);
		}

		public PlacementData add(BlockPos pos) {
			if (pos.equals(BlockPos.ORIGIN)) return this;
			Vec3d center = Vec3d.ofBottomCenter(pos);
			return new PlacementData(
				this.player,
				this.playerPos.add(center),
				this.playerYaw,
				new BlockHitResult(
					this.hit.getPos().add(center),
					this.hit.getSide(),
					this.hit.getBlockPos().add(pos),
					this.hit.isInsideBlock()
				)
			);
		}

		public PlacementData symmetrify(Symmetry symmetry) {
			if (symmetry == Symmetry.IDENTITY) return this;
			return new PlacementData(
				this.player,
				symmetry.modifyEntityPos(this.playerPos),
				symmetry.modifyYaw(this.playerYaw),
				new BlockHitResult(
					symmetry.modifyEntityPos(this.hit.getPos()),
					symmetry.modifyDirection(this.hit.getSide()),
					symmetry.modifyBlockPos(this.hit.getBlockPos()),
					this.hit.isInsideBlock()
				)
			);
		}

		public PlacementData symmetrifyAround(BlockPos origin, Symmetry symmetry) {
			if (symmetry == Symmetry.IDENTITY) return this;
			return this.subtract(origin).symmetrify(symmetry).add(origin);
		}

		public void prepare() {
			this.player.setPosition(this.playerPos);
			this.player.setYaw(this.playerYaw);
		}

		public void place(Hand hand) {
			this.player.interactionManager.interactBlock(
				this.player,
				this.player.getWorld(),
				this.player.getStackInHand(hand),
				hand,
				this.hit
			);
		}

		public void breakBlock() {
			this.player.interactionManager.tryBreakBlock(this.hit.getBlockPos());
		}
	}

	public @Nullable UUID owner;
	public @Nullable BlockPos parent;
	public Set<BlockPos> children = new ObjectOpenHashSet<>();
	public byte iterX = -1, iterY = -1, iterZ = -1, levels = 0;

	public BuildingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public BuildingBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.BUILDING_BLOCK, pos, state);
	}

	public void setLevels(int levels) {
		if (this.levels != levels) {
			this.levels = (byte)(levels);
			if (this.world instanceof ServerWorld serverWorld) {
				serverWorld.getChunkManager().markForUpdate(this.pos);
			}
		}
	}

	public boolean isOwnedBy(PlayerEntity player) {
		return this.owner != null && this.owner.equals(player.getGameProfile().getId());
	}

	public boolean isInRange(BlockPos pos) {
		return pos.getChebyshevDistance(this.pos) <= this.getRange();
	}

	public Stream<PlacementData> symmetrify(PlacementData placement) {
		BuildingBlockMode mode = this.getCachedState().get(BigTechProperties.BUILDING_BLOCK_MODE);
		Stream<PlacementData> stream;
		if (mode == BuildingBlockMode.STACK) {
			if (this.parent != null) {
				stream = Stream.of(placement, placement.add(this.pos.subtract(this.parent)));
			}
			else {
				stream = Stream.of(placement);
			}
		}
		else {
			stream = Arrays.stream(mode.symmetries).map((Symmetry symmetry) -> (
				placement.symmetrifyAround(this.pos, symmetry)
			));
		}
		if (this.children != null && !this.children.isEmpty()) {
			stream = stream.flatMap((PlacementData placementData) -> (
				this.children.stream().flatMap((BlockPos childPos) -> {
					if (
						this.isInRange(childPos) &&
						this.world.isPosLoaded(childPos) &&
						this.world.getBlockEntity(childPos) instanceof BuildingBlockEntity building &&
						building.isOwnedBy(placementData.player)
					) {
						return building.symmetrify(placementData);
					}
					else {
						return Stream.empty();
					}
				})
			));
		}
		return stream;
	}

	public boolean isBeamVisible() {
		return this.levels > 0;
	}

	public int getRange() {
		return this.levels << 4;
	}

	public void tick() {
		if (this.parent == null) {
			this.tickLevels();
			this.tickLevels();
			if (this.world instanceof ServerWorld serverWorld) {
				ACTIVE_INSTANCES.computeIfAbsent(serverWorld, (ServerWorld world) -> new ObjectOpenHashSet<>()).add(this.pos);
			}
		}
		else {
			if (this.world.isPosLoaded(this.parent) && this.world.getBlockEntity(this.parent) instanceof BuildingBlockEntity building) {
				building.children.add(this.pos);
				this.setLevels(building.levels);
			}
			else {
				this.setLevels(0);
			}
		}
		this.children.removeIf((BlockPos pos) -> !(
			this.world.isPosLoaded(pos) &&
			this.world.getBlockEntity(pos) instanceof BuildingBlockEntity building &&
			this.pos.equals(building.parent)
		));
	}

	public void tickLevels() {
		BlockPos iterPos = this.pos.add(this.iterX, this.iterY, this.iterZ);
		if (this.world.getBlockState(iterPos).isIn(BlockTags.BEACON_BASE_BLOCKS)) {
			if (++this.iterX > -this.iterY) {
				if (++this.iterZ > -this.iterY) {
					if (--this.iterY < -4) {
						this.iterY = -1;
					}
					this.setLevels((byte)(Math.max(this.levels, -this.iterY)));
					this.iterZ = this.iterY;
				}
				this.iterX = this.iterY;
			}
		}
		else {
			this.setLevels((byte)(Math.min(this.levels, ~this.iterY)));
			this.iterX = -1;
			this.iterY = -1;
			this.iterZ = -1;
		}
	}

	@Override
	public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(WrapperLookup registries) {
		return super.toInitialChunkDataNbt(registries).withByte("levels", this.levels);
	}

	@Override
	public void readData(ReadView view) {
		super.readData(view);
		UUID owner = view.read("owner", Uuids.CODEC).orElse(null);
		if (owner != null) this.owner = owner;
		Byte levels = view.read("levels", Codec.BYTE).orElse(null);
		if (levels != null) this.levels = levels.byteValue();
		this.parent = view.read("parent", BlockPos.CODEC).orElse(null);
		this.children.clear();
		TypedListReadView<BlockPos> children = view.getOptionalTypedListView("children", BlockPos.CODEC).orElse(null);
		if (children != null) children.stream().forEach(this.children::add);
	}

	@Override
	public void writeData(WriteView view) {
		super.writeData(view);
		if (this.owner != null) view.put("owner", Uuids.CODEC, this.owner);
		if (this.parent != null) view.put("parent", BlockPos.CODEC, this.parent);
		if (!this.children.isEmpty()) {
			ListAppender<BlockPos> children = view.getListAppender("children", BlockPos.CODEC);
			for (BlockPos child : this.children) {
				children.add(child);
			}
		}
	}
}