package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;

import builderb0y.bigtech.api.BeamInteractor.BeamCallback;
import builderb0y.bigtech.beams.BeamUtil;
import builderb0y.bigtech.beams.base.*;
import builderb0y.bigtech.beams.impl.RedstoneBeam;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.blockEntities.AssemblerBlockEntity;
import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.Directions;
import builderb0y.bigtech.util.WorldHelper;

public class AssemblerBlock extends BlockWithEntity implements BeamCallback {

	public static final MapCodec<AssemblerBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public AssemblerBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(BigTechProperties.ACTIVE, Boolean.FALSE)
			.with(Properties.POWERED, Boolean.FALSE)
		);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new AssemblerBlockEntity(BigTechBlockEntityTypes.ASSEMBLER, pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return type == BigTechBlockEntityTypes.ASSEMBLER && !world.isClient ? AssemblerBlockEntity.TICKER.as() : null;
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
		return WorldHelper.getBlockEntity(world, pos, AssemblerBlockEntity.class);
	}

	public boolean shouldBeActivatedBy(PersistentBeam beam, BlockPos pos) {
		if (beam instanceof RedstoneBeam) {
			for (Direction outDirection : Directions.HORIZONTAL) {
				if (BeamUtil.hasSegmentLeadingInto(beam, pos, BeamDirection.from(outDirection), BeamUtil.VISIBLE)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean shouldBeActivated(World world, BlockPos pos) {
		for (Direction outDirection : Directions.HORIZONTAL) {
			if (BeamUtil.hasSegmentLeadingInto(world, pos, BeamDirection.from(outDirection), (BeamSegment segment) -> segment.beam() instanceof RedstoneBeam)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onBeamAdded(ServerWorld world, BlockPos pos, BlockState state, PersistentBeam beam) {
		if (this.shouldBeActivatedBy(beam, pos)) {
			world.scheduleBlockTick(pos, this, 2);
			AssemblerBlockEntity assembler = WorldHelper.getBlockEntity(world, pos, AssemblerBlockEntity.class);
			if (assembler != null) {
				assembler.beamCount++;
				assembler.markDirty();
			}
		}
	}

	@Override
	public void onBeamRemoved(ServerWorld world, BlockPos pos, BlockState state, PersistentBeam beam) {
		if (this.shouldBeActivatedBy(beam, pos)) {
			world.scheduleBlockTick(pos, this, 2);
			AssemblerBlockEntity assembler = WorldHelper.getBlockEntity(world, pos, AssemblerBlockEntity.class);
			if (assembler != null) {
				assembler.beamCount--;
				assembler.markDirty();
			}
		}
	}

	@Override
	public void onBeamPulse(ServerWorld world, BlockPos pos, BlockState state, PulseBeam beam) {

	}

	@Override
	public boolean spreadOut(ServerWorld world, BlockPos pos, BlockState state, SpreadingBeamSegment segment) {
		if (segment.beam() instanceof RedstoneBeam && (segment.direction().flag() & 0b000_000_000___010_101_010___000_000_000) != 0) {
			segment.beam().addSegment(world, segment.terminate());
			return true;
		}
		return false;
	}

	public boolean checkActive(ServerWorld world, BlockPos pos, BlockState state) {
		AssemblerBlockEntity blockEntity = WorldHelper.getBlockEntity(world, pos, AssemblerBlockEntity.class);
		if (blockEntity == null) {
			return false;
		}
		ColorAccumulator accumulator = new ColorAccumulator();
		accumulator.acceptAll(pos, ChunkBeamStorageHolder.KEY.get(world.getChunk(pos)).require().get(pos.getY() >> 4));
		Vector3f sum = accumulator.getColor();
		blockEntity.setColorAndSync(sum, accumulator.count);
		return sum != null;
	}

	public BlockState updateActive(ServerWorld world, BlockPos pos, BlockState state) {
		boolean shouldBeActive = this.checkActive(world, pos, state);
		return state.with(BigTechProperties.ACTIVE, shouldBeActive);
	}

	public BlockState updatePowered(ServerWorld world, BlockPos pos, BlockState state) {
		boolean shouldBePowered = world.isReceivingRedstonePower(pos);
		return state.with(Properties.POWERED, shouldBePowered);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		BlockState newState = state;
		newState = this.updateActive(world, pos, newState);
		newState = this.updatePowered(world, pos, newState);
		if (state != newState) {
			world.setBlockState(pos, newState);
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
		super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
		world.scheduleBlockTick(pos, FunctionalBlocks.ASSEMBLER, 2);
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		AssemblerBlockEntity assembler = WorldHelper.getBlockEntity(world, pos, AssemblerBlockEntity.class);
		if (assembler != null) {
			float fullness = 0.0F;
			int width = assembler.width();
			int height = assembler.height();
			int minX = 5 - width;
			int minY = 5 - height;
			for (int y = minY; y < 5; y++) {
				for (int x = minX; x < 5; x++) {
					ItemStack stack = assembler.inventory.getStack(y * 5 + x);
					if (!stack.isEmpty()) {
						fullness += ((float)(stack.getCount())) / ((float)(stack.getMaxCount()));
					}
				}
			}
			return MathHelper.lerpPositive(fullness / (width * height), 0, 15);
		}
		return 0;
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BigTechProperties.ACTIVE, Properties.POWERED);
	}
}