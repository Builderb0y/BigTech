package builderb0y.bigtech.blocks.belts;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.LightningCableBlock;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class DetectorBeltBlock extends DirectionalBeltBlock {

	public static final Object2ObjectMap<EntityType<?>, EntityCounter<?>> COUNTERS = new Object2ObjectOpenHashMap<>(4);
	static {
		COUNTERS.defaultReturnValue(EntityCounter.DEFAULT);
		registerCounter(EntityType.ITEM, new EntityCounter<>() {

			@Override
			public int getCount(ItemEntity entity) {
				return entity.getStack().getCount();
			}

			@Override
			public int scaleCount(int count) {
				return (count + 3) >> 2;
			}
		});
		registerCounter(EntityType.EXPERIENCE_ORB, new EntityCounter<>() {

			@Override
			public int getCount(ExperienceOrbEntity entity) {
				return entity.getValue();
			}

			@Override
			public int scaleCount(int count) {
				//copy-pasted from ExperienceOrbEntity.getOrbSize(), plus one.
				if (count >= 2477) return 11;
				if (count >= 1237) return 10;
				if (count >=  617) return  9;
				if (count >=  307) return  8;
				if (count >=  149) return  7;
				if (count >=   73) return  6;
				if (count >=   37) return  5;
				if (count >=   17) return  4;
				if (count >=    7) return  3;
				if (count >=    3) return  2;
				return 1;
			}
		});
	}

	public static <E extends Entity> void registerCounter(EntityType<E> type, EntityCounter<E> counter) {
		COUNTERS.put(type, counter);
	}

	@SuppressWarnings("unchecked")
	public static <E extends Entity> EntityCounter<E> getCounter(EntityType<E> type) {
		return (EntityCounter<E>)(COUNTERS.get(type));
	}

	public static final MapCodec<DetectorBeltBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public DetectorBeltBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.POWER, 0)
		);
	}

	@Override
	public void move(World world, BlockPos pos, BlockState state, Entity entity) {
		if (state.get(Properties.POWER) == 0) {
			world.setBlockState(pos, state = state.with(Properties.POWER, this.countEntities(world, pos, state)));
			world.scheduleBlockTick(pos, this, 10);
		}
		super.move(world, pos, state, entity);
	}

	public int countEntities(World world, BlockPos pos, BlockState state) {
		Object2IntOpenHashMap<EntityType<?>> typeToCount = new Object2IntOpenHashMap<>(2);
		for (Entity entity : world.getEntitiesByClass(Entity.class, new Box(pos), (Entity entity) -> {
			return this.canMove(world, pos, state, entity) && this.isOnBelt(world, pos, state, entity);
		})) {
			typeToCount.addTo(entity.getType(), COUNTERS.get(entity.getType()).<EntityCounter<Entity>>as().getCount(entity));
		}
		int sum = 0;
		ObjectIterator<Object2IntMap.Entry<EntityType<?>>> iterator = typeToCount.object2IntEntrySet().fastIterator();
		while (iterator.hasNext()) {
			Object2IntMap.Entry<EntityType<?>> entry = iterator.next();
			sum += COUNTERS.get(entry.getKey()).scaleCount(entry.getIntValue());
		}
		return Math.min(sum, 15);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int count = this.countEntities(world, pos, state);
		if (state.get(Properties.POWER) != count) {
			world.setBlockState(pos, state.with(Properties.POWER, count));
		}
		if (count != 0) {
			world.scheduleBlockTick(pos, this, 10);
		}
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return direction != Direction.DOWN && state.get(Properties.POWER) != 0 ? 15 : 0;
	}

	@Override
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return direction == Direction.UP && state.get(Properties.POWER) != 0 ? 15 : 0;
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(Properties.POWER);
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWER);
	}
}