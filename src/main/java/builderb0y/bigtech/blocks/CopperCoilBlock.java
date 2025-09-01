package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;

import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.TeslaCoilBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;
import builderb0y.bigtech.util.WorldHelper;

public class CopperCoilBlock extends Block implements BlockEntityProvider, LightningPulseInteractor {

	public static final MapCodec<CopperCoilBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public CopperCoilBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.FACING, Direction.UP)
			.with(Properties.POWERED, Boolean.FALSE)
		);
	}

	@Override
	public boolean isSink(WorldView world, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void spreadOut(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		this.forceSpreadOut(world, pos, state, pulse);
	}

	@Override
	public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		TeslaCoilBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, TeslaCoilBlockEntity.class);
		if (blockEntity != null) blockEntity.onLightningPulse();
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TeslaCoilBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type == BigTechBlockEntityTypes.TESLA_COIL ? (world.isClient ? TeslaCoilBlockEntity.CLIENT_TICKER : TeslaCoilBlockEntity.SERVER_TICKER).as() : null;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
		super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
		boolean powered = state.get(Properties.POWERED);
		boolean shouldBePowered = world.isReceivingRedstonePower(pos);
		if (powered != shouldBePowered) {
			world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered));
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return (
			super
			.getPlacementState(context)
			.with(Properties.FACING, context.getSide())
			.with(Properties.POWERED, context.getWorld().isReceivingRedstonePower(context.getBlockPos()))
		);
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING, Properties.POWERED);
	}
}