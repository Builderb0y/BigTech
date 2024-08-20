package builderb0y.bigtech.blockEntities;

import java.util.function.ToLongFunction;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.datagen.impl.PulsarDataGenerator.PulsarTexts;
import builderb0y.bigtech.util.BigTechMath;

public class PulsarBlockEntity extends BlockEntity {

	public static final BlockEntityTicker<PulsarBlockEntity> TICKER = (World world, BlockPos pos, BlockState state, PulsarBlockEntity blockEntity) -> blockEntity.tick();

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
		boolean shouldBePowered = BigTechMath.modulus_BP(this.relativeTo.getTime(this.world) - this.offset, this.onTime + this.offTime) < this.onTime;
		if (powered != shouldBePowered) {
			this.world.setBlockState(this.pos, this.getCachedState().with(Properties.POWERED, shouldBePowered));
		}
	}

	@Override
	public void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		if ((this.onTime = nbt.getInt("onTime")) <= 0) this.onTime = 2;
		if ((this.offTime = nbt.getInt("offTime")) <= 0) this.offTime = 8;
		this.offset = nbt.getInt("offset");
		this.relativeTo = TimeGetter.VALUES[nbt.getByte("relativeTo")];
	}

	@Override
	public void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt
		.withInt("onTime", this.onTime)
		.withInt("offTime", this.offTime)
		.withInt("offset", this.offset)
		.withByte("relativeTo", (byte)(this.relativeTo.ordinal()));
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