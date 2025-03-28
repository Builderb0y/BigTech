package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.CrucibleBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.WorldHelper;

public class CrucibleBlock extends BlockWithEntity implements FluidFillable {

	@SuppressWarnings("rawtypes")
	public static final MapCodec CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(
		VoxelShapes.fullCube(),
		VoxelShapes.union(
			Block.createCuboidShape(0.0, 0.0, 4.0, 16.0,  3.0, 12.0),
			Block.createCuboidShape(4.0, 0.0, 0.0, 12.0,  3.0, 16.0),
			Block.createCuboidShape(2.0, 0.0, 2.0, 14.0,  3.0, 14.0),
			Block.createCuboidShape(2.0, 4.0, 2.0, 14.0, 16.0, 14.0)
		),
		BooleanBiFunction.ONLY_FIRST
	);

	public CrucibleBlock(Settings settings) {
		super(settings);
		FluidStorage.SIDED.registerForBlocks(
			(
				World world,
				BlockPos pos,
				BlockState state,
				@Nullable BlockEntity blockEntity,
				@Nullable Direction direction
			)
			-> {
				if (blockEntity instanceof CrucibleBlockEntity crucible && world instanceof ServerWorld serverWorld) {
					return crucible.fluidStorage(serverWorld);
				}
				return null;
			},
			this
		);
	}

	@Override
	public Item asItem() {
		return Items.CAULDRON;
	}

	@Override
	public ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world instanceof ServerWorld serverWorld) {
			CrucibleBlockEntity crucible = WorldHelper.getBlockEntity(world, pos, CrucibleBlockEntity.class);
			if (crucible != null) {
				if (stack.isEmpty()) {
					ItemStack removed = crucible.removeItem();
					if (!(player.isInCreativeMode() && player.getInventory().contains(removed))) {
						player.getInventory().insertStack(removed);
					}
				}
				else if (crucible.progress != null) {
					this.tryWaterInteraction(serverWorld, crucible, stack, player, hand);
				}
				else {
					crucible.addItem(stack.splitUnlessCreative(1, player), 1);
				}
			}
		}
		return ActionResult.SUCCESS;
	}

	public boolean tryWaterInteraction(ServerWorld serverWorld, CrucibleBlockEntity crucible, ItemStack stack, PlayerEntity player, Hand hand) {
		Storage<FluidVariant> itemStorage = FluidStorage.ITEM.find(stack, ContainerItemContext.forPlayerInteraction(player, hand));
		if (itemStorage != null) {
			Storage<FluidVariant> blockStorage = crucible.fluidStorage(serverWorld);
			try (Transaction transaction = Transaction.openOuter()) {
				FluidVariant water = FluidVariant.of(Fluids.WATER);
				long extracted = itemStorage.extract(water, Long.MAX_VALUE, transaction);
				if (extracted > 0L) {
					blockStorage.insert(water, extracted, transaction);
					transaction.commit();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canFillWithFluid(@Nullable LivingEntity filler, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		if (fluid == Fluids.WATER) {
			CrucibleBlockEntity crucible = WorldHelper.getBlockEntity(world, pos, CrucibleBlockEntity.class);
			if (crucible != null) {
				return crucible.progress != null;
			}
		}
		return false;
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		if (fluidState.getFluid() == Fluids.WATER && world instanceof ServerWorld serverWorld) {
			CrucibleBlockEntity crucible = WorldHelper.getBlockEntity(world, pos, CrucibleBlockEntity.class);
			if (crucible != null) {
				Storage<FluidVariant> blockStorage = crucible.fluidStorage(serverWorld);
				try (Transaction transaction = Transaction.openOuter()) {
					FluidVariant water = FluidVariant.of(Fluids.WATER);
					blockStorage.insert(water, FluidConstants.BUCKET, transaction);
					transaction.commit();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		CrucibleBlockEntity crucible = WorldHelper.getBlockEntity(world, pos, CrucibleBlockEntity.class);
		if (crucible != null) {
			return crucible.getComparatorOutput();
		}
		return 0;
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CrucibleBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type == BigTechBlockEntityTypes.CRUCIBLE ? CrucibleBlockEntity.TICKER.as() : null;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return OUTLINE_SHAPE;
	}

	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.fullCube();
	}
}