package builderb0y.bigtech.blocks.belts;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.SorterBeltBlockEntity;
import builderb0y.bigtech.api.AscenderInteractor;
import builderb0y.bigtech.mixinterfaces.RoutableEntity;
import builderb0y.bigtech.util.WorldHelper;

public class SorterBeltBlock extends DirectionalBeltBlock implements BlockEntityProvider {

	public SorterBeltBlock(Settings settings) {
		super(settings);
	}

	@Override
	public int getAscenderPriority(World world, BlockPos pos, BlockState state, Direction face) {
		if (face == Direction.UP) return AscenderInteractor.BELT_TOP;
		if (face == Direction.DOWN) return AscenderInteractor.BLOCKED;
		Direction facing = state.get(Properties.FACING);
		if (face == facing.opposite) return AscenderInteractor.BELT_BACK;
		return AscenderInteractor.BLOCKED;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.onPlaced(world, pos, state, placer, stack);
		if (stack.hasCustomName()) {
			SorterBeltBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, SorterBeltBlockEntity.class);
			if (blockEntity != null) {
				blockEntity.customName = stack.getName();
			}
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
			if (factory != null) player.openHandledScreen(factory);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	@SuppressWarnings("deprecation")
	public @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity instanceof SorterBeltBlockEntity factory ? factory : null;
	}

	@Override
	public Direction getDirection(World world, BlockPos pos, BlockState state, Entity entity) {
		return ((RoutableEntity)(entity)).bigtech_computeRoutingInfo(pos, state, state.get(Properties.HORIZONTAL_FACING), this::computeDirection).direction;
	}

	public Direction computeDirection(World world, BlockPos pos, BlockState state, Entity entity) {
		if (world.isClient) return state.get(Properties.HORIZONTAL_FACING);
		SorterBeltBlockEntity sorter = WorldHelper.getBlockEntity(world, pos, SorterBeltBlockEntity.class);
		return sorter != null ? sorter.getDistributionDirection(entity) : state.get(Properties.HORIZONTAL_FACING);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SorterBeltBlockEntity(pos, state);
	}
}