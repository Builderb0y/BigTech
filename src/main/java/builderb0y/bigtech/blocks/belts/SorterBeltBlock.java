package builderb0y.bigtech.blocks.belts;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.api.AscenderInteractor;
import builderb0y.bigtech.blockEntities.SorterBeltBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.mixinterfaces.RoutableEntity;
import builderb0y.bigtech.util.WorldHelper;

public class SorterBeltBlock extends DirectionalBeltBlock implements BlockEntityProvider {

	public static final MapCodec<SorterBeltBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public SorterBeltBlock(Settings settings) {
		super(settings);
	}

	@Override
	public int getAscenderPriority(World world, BlockPos pos, BlockState state, Direction face) {
		if (face == Direction.UP) return AscenderInteractor.BELT_TOP;
		if (face == Direction.DOWN) return AscenderInteractor.BLOCKED;
		Direction facing = state.get(Properties.HORIZONTAL_FACING);
		if (face == facing.getOpposite()) return AscenderInteractor.BELT_BACK;
		return AscenderInteractor.BLOCKED;
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
		return world.getBlockEntity(pos) instanceof SorterBeltBlockEntity factory ? factory : null;
	}

	@Override
	public Direction getDirection(World world, BlockPos pos, BlockState state, Entity entity) {
		return entity.<RoutableEntity>as().bigtech_computeRoutingInfo(pos, state, state.get(Properties.HORIZONTAL_FACING), this::computeDirection).direction();
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