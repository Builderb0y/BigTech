package builderb0y.bigtech.blockEntities;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.ReadView.TypedListReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.storage.WriteView.ListAppender;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.BigTechProperties;
import builderb0y.bigtech.blocks.BuildingBlock.BuildingBlockMode;
import builderb0y.bigtech.blocks.FunctionalBlocks;
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
				PLACING.set(Boolean.TRUE);
				try {
					forWorld(serverPlayer, new BreakContext(pos, state)).forEach((BreakContext context) -> {
						BlockState beforeBreak = serverPlayer.getWorld().getBlockState(context.pos);
						Vec3d offset = new Vec3d(context.pos.subtract(pos));
						if (Symmetrifiable.matches(context.expected, beforeBreak) && (serverPlayer.isCreative() || serverPlayer.canHarvest(beforeBreak))) {
							WorldHelper.runEntitySpawnAction(
								() -> serverPlayer.interactionManager.tryBreakBlock(context.pos),
								(Entity entity) -> {
									entity.setPosition(entity.getPos().add(offset));
									return true;
								}
							);
						}
					});
				}
				finally {
					PLACING.set(Boolean.FALSE);
				}
			}
		});
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

	public static interface Symmetrifiable<S extends Symmetrifiable<S>> {

		public abstract S add(BlockPos pos);

		public abstract S subtract(BlockPos pos);

		public abstract S symmetrify(Symmetry symmetry);

		@SuppressWarnings("unchecked")
		public default S symmetrifyAround(BlockPos origin, Symmetry symmetry) {
			if (symmetry == Symmetry.IDENTITY) return (S)(this);
			return this.subtract(origin).symmetrify(symmetry).add(origin);
		}

		public static boolean matches(ServerWorld world, BlockPos pos, BlockState expected) {
			return matches(expected, world.getBlockState(pos));
		}

		public static boolean matches(BlockState expected, BlockState actual) {
			return removeFluids(expected) == removeFluids(actual);
		}

		public static BlockState removeFluids(BlockState blockState) {
			FluidState fluidState = blockState.getFluidState();
			if (!fluidState.isEmpty()) {
				if (fluidState.getBlockState() == blockState) {
					return Blocks.AIR.getDefaultState();
				}
				else {
					return blockState.withIfExists(Properties.WATERLOGGED, Boolean.FALSE);
				}
			}
			return blockState;
		}
	}

	public static record PlaceContext(
		BlockPos pos,
		BlockState from,
		BlockState to,
		Direction playerFacing,
		Direction clickedSide
	)
	implements Symmetrifiable<PlaceContext> {

		@Override
		public PlaceContext add(BlockPos pos) {
			if (pos.equals(BlockPos.ORIGIN)) return this;
			return new PlaceContext(this.pos.add(pos), this.from, this.to, this.playerFacing, this.clickedSide);
		}

		@Override
		public PlaceContext subtract(BlockPos pos) {
			if (pos.equals(BlockPos.ORIGIN)) return this;
			return new PlaceContext(this.pos.subtract(pos), this.from, this.to, this.playerFacing, this.clickedSide);
		}

		@Override
		public PlaceContext symmetrify(Symmetry symmetry) {
			if (symmetry == Symmetry.IDENTITY) return this;
			return new PlaceContext(
				symmetry.modifyBlockPos(this.pos),
				symmetry.modifyBlockState(this.from),
				symmetry.modifyBlockState(this.to),
				symmetry.modifyDirection(this.playerFacing),
				symmetry.modifyDirection(this.clickedSide)
			);
		}

		public boolean matches(ServerWorld world) {
			return Symmetrifiable.matches(world, this.pos, this.from);
		}

		public AutomaticItemPlacementContext toVanilla(ServerWorld world, ItemStack stack) {
			return new AutomaticItemPlacementContext(world, this.pos, this.playerFacing, stack, this.clickedSide) {

				@Override
				public boolean canReplaceExisting() {
					return this.canReplaceExisting; //fix stack overflow when placing certain specific blocks, like slabs.
				}
			};
		}
	}

	public static record BreakContext(
		BlockPos pos,
		BlockState expected
	)
	implements Symmetrifiable<BreakContext> {

		@Override
		public BreakContext add(BlockPos pos) {
			if (pos.equals(BlockPos.ORIGIN)) return this;
			return new BreakContext(this.pos.add(pos), this.expected);
		}

		@Override
		public BreakContext subtract(BlockPos pos) {
			if (pos.equals(BlockPos.ORIGIN)) return this;
			return new BreakContext(this.pos.subtract(pos), this.expected);
		}

		@Override
		public BreakContext symmetrify(Symmetry symmetry) {
			if (symmetry == Symmetry.IDENTITY) return this;
			return new BreakContext(
				symmetry.modifyBlockPos(this.pos),
				symmetry.modifyBlockState(this.expected)
			);
		}

		public boolean matches(ServerWorld world) {
			return Symmetrifiable.matches(world, this.pos, this.expected);
		}
	}

	public static <S extends Symmetrifiable<S>> Stream<S> forWorld(ServerPlayerEntity player, S first) {
		ServerWorld world = player.getWorld();
		Set<BlockPos> positions = ACTIVE_INSTANCES.get(world);
		if (positions != null && !positions.isEmpty()) {
			return positions.stream().flatMap((BlockPos startingPosition) -> {
				if (
					world.isPosLoaded(startingPosition) &&
					world.getBlockEntity(startingPosition) instanceof BuildingBlockEntity building &&
					building.isOwnedBy(player) &&
					building.isInRange(startingPosition)
				) {
					return building.symmetrify(player, first);
				}
				else {
					return Stream.empty();
				}
			});
		}
		else {
			return Stream.empty();
		}
	}

	public <S extends Symmetrifiable<S>> Stream<S> symmetrify(ServerPlayerEntity player, S first) {
		BuildingBlockMode mode = this.getCachedState().get(BigTechProperties.BUILDING_BLOCK_MODE);
		Stream<S> stream;
		if (mode == BuildingBlockMode.STACK) {
			if (this.parent != null) {
				stream = Stream.of(first, first.add(this.pos.subtract(this.parent)));
			}
			else {
				stream = Stream.of(first);
			}
		}
		else {
			stream = Arrays.stream(mode.symmetriesIncludingIdentity).map((Symmetry symmetry) -> (
				first.symmetrifyAround(this.pos, symmetry)
			));
		}
		if (this.children != null && !this.children.isEmpty()) {
			stream = stream.flatMap((S placementData) -> (
				this.children.stream().flatMap((BlockPos childPos) -> {
					if (
						this.isInRange(childPos) &&
						this.world.isPosLoaded(childPos) &&
						this.world.getBlockEntity(childPos) instanceof BuildingBlockEntity building &&
						building.isOwnedBy(player)
					) {
						return building.symmetrify(player, placementData);
					}
					else {
						return Stream.of(placementData);
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
		return this.levels > 0 ? 8 << this.levels : 0;
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