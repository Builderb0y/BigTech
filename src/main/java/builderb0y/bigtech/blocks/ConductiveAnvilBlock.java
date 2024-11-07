package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.ConductiveAnvilBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;
import builderb0y.bigtech.util.WorldHelper;

public class ConductiveAnvilBlock extends AnvilBlock implements LightningPulseInteractor, BlockEntityProvider {

	public static final MapCodec<ConductiveAnvilBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public ConductiveAnvilBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isSink(WorldView world, BlockPos pos, BlockState state) {
		ConductiveAnvilBlockEntity anvil = WorldHelper.getBlockEntity(world, pos, ConductiveAnvilBlockEntity.class);
		return anvil != null && !anvil.isEmpty();
	}

	@Override
	public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		LightningPulseInteractor.shockEntitiesAround(world, pos, state, pulse);
		LightningPulseInteractor.spawnLightningParticles(world, pos, state, pulse);
		ConductiveAnvilBlockEntity anvil = WorldHelper.getBlockEntity(world, pos, ConductiveAnvilBlockEntity.class);
		if (anvil != null) anvil.onLightningPulse(pulse.getDistributedEnergy());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient) {
			NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
			if (factory != null) player.openHandledScreen(factory);
		}
		return ActionResult.SUCCESS;
	}

	@Nullable
	@Override
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		return WorldHelper.getBlockEntity(world, pos, ConductiveAnvilBlockEntity.class);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!BigTechBlockEntityTypes.CONDUCTIVE_ANVIL.supports(newState)) {
			ConductiveAnvilBlockEntity anvil = WorldHelper.getBlockEntity(world, pos, ConductiveAnvilBlockEntity.class);
			if (anvil != null) {
				ItemScatterer.spawn(world, pos, anvil);
				world.removeBlockEntity(pos);
			}
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ConductiveAnvilBlockEntity(BigTechBlockEntityTypes.CONDUCTIVE_ANVIL, pos, state);
	}
}