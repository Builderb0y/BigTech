package builderb0y.bigtech.blockEntities;

import java.util.function.ToLongFunction;

import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.datagen.impl.functional.PulsarDataGenerator.PulsarTexts;
import builderb0y.bigtech.util.BigTechMath;
import builderb0y.bigtech.util.Directions;

public class PulsarBlockEntity extends BlockEntity {

	public static final BlockEntityTicker<PulsarBlockEntity> TICKER = (World world, BlockPos pos, BlockState state, PulsarBlockEntity blockEntity) -> blockEntity.tick();

	public static final int
		MIN_ON_TIME  =  1,
		MAX_ON_TIME  =  1_000_000,
		MIN_OFF_TIME =  1,
		MAX_OFF_TIME =  1_000_000,
		MIN_OFFSET   = -1_000_000,
		MAX_OFFSET   =  1_000_000;

	public int onTime = 2, offTime = 8, offset = 0;
	public TimeGetter relativeTo = TimeGetter.WORLD_AGE;

	public PulsarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public PulsarBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.PULSAR, pos, state);
	}

	public void tick() {
		boolean powered = this.getCachedState().get(Properties.POWERED);
		boolean shouldBePowered = BigTechMath.modulus_BP(this.relativeTo.getTime(this.world) - this.offset, this.onTime + this.offTime) < this.onTime && !this.isStrongPowered();
		if (powered != shouldBePowered) {
			this.world.setBlockState(this.pos, this.getCachedState().with(Properties.POWERED, shouldBePowered));
		}
	}

	public boolean isStrongPowered() {
		BlockPos.Mutable pos = new BlockPos.Mutable();
		for (Direction direction : Directions.ALL) {
			BlockState state = this.world.getBlockState(pos.set(this.pos, direction));
			if (!(state.getBlock() instanceof RedstoneWireBlock) && state.getStrongRedstonePower(this.world, pos, direction) != 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void readData(ReadView view) {
		super.readData(view);
		if ((this.onTime = view.getInt("onTime", 2)) <= 0) this.onTime = 2;
		if ((this.offTime = view.getInt("offTime", 8)) <= 0) this.offTime = 8;
		this.offset = view.getInt("offset", 0);
		this.relativeTo = view.getArray("relativeTo", TimeGetter.VALUES).orElse(TimeGetter.WORLD_AGE);
	}

	@Override
	public void writeData(WriteView view) {
		super.writeData(view);
		view.putInt("onTime", this.onTime);
		view.putInt("offTime", this.offTime);
		view.putInt("offset", this.offset);
		view.putByte("relativeTo", (byte)(this.relativeTo.ordinal()));
	}

	public static enum TimeGetter {
		WORLD_AGE(PulsarTexts.WORLD_AGE, World::getTime),
		DAY_NIGHT(PulsarTexts.DAY_NIGHT, World::getTimeOfDay);

		public static final TimeGetter[] VALUES = values();

		public final String translationKey;
		public final ToLongFunction<World> timeGetter;

		TimeGetter(String translationKey, ToLongFunction<World> timeGetter) {
			this.translationKey = translationKey;
			this.timeGetter = timeGetter;
		}

		public long getTime(World world) {
			return this.timeGetter.applyAsLong(world);
		}
	}
}