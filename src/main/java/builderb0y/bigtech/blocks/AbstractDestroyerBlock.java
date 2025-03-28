package builderb0y.bigtech.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;

import builderb0y.bigtech.blockEntities.AbstractDestroyerBlockEntity;
import builderb0y.bigtech.util.WorldHelper;

public abstract class AbstractDestroyerBlock extends Block implements BlockEntityProvider {

	public AbstractDestroyerBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.POWERED, Boolean.FALSE)
		);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient) {
			NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
			if (factory != null) player.openHandledScreen(factory);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return world.getBlockEntity(pos) instanceof NamedScreenHandlerFactory factory ? factory : null;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
		super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = world.isReceivingRedstonePower(pos);
		if (powered != shouldBePowered) {
			world.scheduleBlockTick(pos, this, 2);
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = world.isReceivingRedstonePower(pos);
		if (powered != shouldBePowered) {
			world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered), Block.NOTIFY_ALL);
		}
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return (
			super
			.getPlacementState(context)
			.with(Properties.HORIZONTAL_FACING, context.getHorizontalPlayerFacing().getOpposite())
			.with(Properties.POWERED, context.getWorld().isReceivingRedstonePower(context.getBlockPos()))
		);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.HORIZONTAL_FACING, rotation.rotate(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(Properties.HORIZONTAL_FACING, mirror.apply(state.get(Properties.HORIZONTAL_FACING)));
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING, Properties.POWERED);
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	@SuppressWarnings("UseOfDivisionOperator")
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		Inventory inventory = WorldHelper.getBlockEntity(world, pos, Inventory.class);
		if (inventory == null) return 0;
		ItemStack stack = inventory.getStack(0);
		if (stack.isEmpty()) return 0;
		return stack.getCount() * 15 / stack.getMaxCount();
	}
}