package builderb0y.bigtech.blocks;

import java.util.SplittableRandom;
import java.util.WeakHashMap;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World.ExplosionSourceType;
import net.minecraft.world.WorldView;

import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;

public class ElectrumCoilBlock extends Block implements LightningPulseInteractor {

	public static final MapCodec<ElectrumCoilBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	public static final WeakHashMap<ServerWorld, Long2ByteOpenHashMap> EMP_AURAS = new WeakHashMap<>();

	static {
		ServerTickEvents.END_WORLD_TICK.register((ServerWorld world) -> {
			Long2ByteOpenHashMap map = EMP_AURAS.get(world);
			if (map != null) {
				BlockPos.Mutable mutablePos = new BlockPos.Mutable();
				for (ObjectIterator<Long2ByteMap.Entry> iterator = map.long2ByteEntrySet().fastIterator(); iterator.hasNext();) {
					Long2ByteMap.Entry entry = iterator.next();
					long key = entry.getLongKey();
					int nextValue = entry.getByteValue() - 1;
					if (nextValue <= 0) iterator.remove();
					else entry.setValue((byte)(nextValue));
					world.updateNeighbors(mutablePos.set(key), FunctionalBlocks.ELECTRUM_COIL);
				}
				if (map.isEmpty()) EMP_AURAS.remove(world);
			}
		});
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public ElectrumCoilBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.FACING, Direction.UP)
		);
	}

	@Override
	public boolean isSink(WorldView world, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		double
			centerX = pos.getX() + 0.5D,
			centerY = pos.getY() + 0.5D,
			centerZ = pos.getZ() + 0.5D;
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		SplittableRandom random = new SplittableRandom(world.random.nextLong());
		int energy = pulse.getDistributedEnergy();
		Long2ByteOpenHashMap auras = EMP_AURAS.computeIfAbsent(world, (ServerWorld $) -> new Long2ByteOpenHashMap(energy));
		double deviation = Math.cbrt(energy) * 0.5D;
		for (int attempt = energy; --attempt >= 0;) {
			int
				x = MathHelper.floor(random.nextGaussian(centerX, deviation)),
				y = MathHelper.floor(random.nextGaussian(centerY, deviation)),
				z = MathHelper.floor(random.nextGaussian(centerZ, deviation)),
				power = random.nextInt(1, 16);
			long key = BlockPos.asLong(x, y, z);
			byte oldPower = auras.get(key);
			if (power > oldPower) {
				auras.put(key, (byte)(power));
				world.updateNeighbors(mutablePos.set(x, y, z), FunctionalBlocks.ELECTRUM_COIL);
			}
		}
		world.setBlockState(pos, Blocks.AIR.getDefaultState());
		world.createExplosion(null, centerX, centerY, centerZ, (float)(deviation * 0.5D), ExplosionSourceType.BLOCK);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return (
			super
			.getPlacementState(context)
			.with(Properties.FACING, context.getSide())
		);
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING);
	}
}