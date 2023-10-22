package builderb0y.bigtech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import builderb0y.bigtech.mixins.EntityShapeContext_HeldItemGetter;

public class FrameBlock extends Block implements PistonInteractor {

	public static final VoxelShape COLLISION_SHAPE = VoxelShapes.combineAndSimplify(
		VoxelShapes.fullCube(),
		VoxelShapes.union(
			VoxelShapes.cuboid(0.0D, 0.1875D, 0.1875D, 1.0D, 0.8125D, 0.8125D),
			VoxelShapes.cuboid(0.1875D, 0.0D, 0.1875D, 0.8125D, 1.0D, 0.8125D),
			VoxelShapes.cuboid(0.1875D, 0.1875D, 0.0D, 0.8125D, 0.8125D, 1.0D)
		),
		BooleanBiFunction.ONLY_FIRST
	);

	public final TagKey<Block> sticksTo;

	public FrameBlock(Settings settings, TagKey<Block> sticksTo) {
		super(settings);
		this.sticksTo = sticksTo;
	}

	@Override
	public boolean isSticky(PistonHandlerInfo handler, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean canStickTo(
		PistonHandlerInfo handler,
		BlockPos selfPos,
		BlockState selfState,
		BlockPos otherPos,
		BlockState otherState,
		Direction face
	) {
		return otherState.isIn(this.sticksTo);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context instanceof EntityShapeContext_HeldItemGetter heldItemGetter) {
			ItemStack heldItem = heldItemGetter.bigtech_getHeldItem();
			if (heldItem.item instanceof BlockItem) {
				return VoxelShapes.fullCube();
			}
			if (heldItem.isSuitableFor(state)) {
				return VoxelShapes.fullCube();
			}
		}
		return COLLISION_SHAPE;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return COLLISION_SHAPE;
	}
}