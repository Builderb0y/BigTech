package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.SilverIodideCannonBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.WorldHelper;

public class SilverIodideCannonBlock extends Block implements BlockEntityProvider {

	public static final VoxelShape
		FULL_SHAPE = VoxelShapes.cuboid(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D),
		HOLLOW_SHAPE = VoxelShapes.combine(
			FULL_SHAPE,
			VoxelShapes.cuboid(0.3125D, 0.0625D, 0.3125D, 0.6875D, 1.0D, 0.6875D),
			BooleanBiFunction.ONLY_FIRST
		);

	public static final MapCodec<SilverIodideCannonBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public SilverIodideCannonBlock(Settings settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SilverIodideCannonBlockEntity(pos, state);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return HOLLOW_SHAPE;
	}

	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return FULL_SHAPE;
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
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!BigTechBlockEntityTypes.SILVER_IODIDE_CANNON.supports(newState)) {
			SilverIodideCannonBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, SilverIodideCannonBlockEntity.class);
			if (blockEntity != null) ItemScatterer.spawn(world, pos, blockEntity);
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return world.getBlockEntity(pos) instanceof SilverIodideCannonBlockEntity blockEntity ? blockEntity : null;
	}
}