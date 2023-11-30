package builderb0y.bigtech.blockEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.random.RandomGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.damageTypes.BigTechDamageTypes;
import builderb0y.bigtech.lightning.LightningPulse;

public class TeslaCoilBlockEntity extends BlockEntity {

	public static final BlockEntityTicker<TeslaCoilBlockEntity>
		CLIENT_TICKER = (world, pos, state, blockEntity) -> blockEntity.tickClient(),
		SERVER_TICKER = (world, pos, state, blockEntity) -> blockEntity.tickServer();

	public Target[] targets = new Target[8];
	public RandomGenerator random = new SplittableRandom();
	public DamageSource damageSource;

	public TeslaCoilBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public TeslaCoilBlockEntity(BlockPos pos, BlockState state) {
		this(BigTechBlockEntityTypes.TESLA_COIL, pos, state);
	}

	public void onLightningPulse() {
		Direction direction = this.cachedState.get(Properties.FACING);
		if (this.checkStructure() && this.world.isSkyVisible(this.pos.offset(direction, 3))) {
			LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, this.world);
			entity.refreshPositionAndAngles(
				this.pos.x + 0.5D + direction.offsetX * 2.5D,
				this.pos.y + 0.5D + direction.offsetY * 2.5D,
				this.pos.z + 0.5D + direction.offsetZ * 2.5D,
				0.0F,
				0.0F
			);
			entity.cosmetic = true;
			this.world.spawnEntity(entity);
		}
	}

	public void tickServer() {
		if (this.cachedState.get(Properties.POWERED) && this.checkStructure()) {
			Direction facing = this.cachedState.get(Properties.FACING);
			Orientation orientation = Orientation.from(facing);
			List<LivingEntity> entities = this.world.getNonSpectatingEntities(LivingEntity.class, orientation.box.offset(this.pos).expand(this.random.nextDouble(2.0D, 3.0D)));
			if (!entities.isEmpty) {
				LightningPulse.shockEntity(
					entities.get(
						this.random.nextInt(entities.size())
					),
					1.0F,
					this.damageSource
				);
			}
		}
	}

	public void tickClient() {
		System.arraycopy(this.targets, 0, this.targets, 1, 7);
		this.targets[0] = (
			this.cachedState.get(Properties.POWERED) && this.checkStructure()
			? this.newTarget()
			: null
		);
	}

	public Target newTarget() {
		Direction facing = this.cachedState.get(Properties.FACING);
		Orientation orientation = Orientation.from(facing);
		Box ironBox = orientation.box.offset(this.pos);
		List<LivingEntity> entities = this.world.getNonSpectatingEntities(LivingEntity.class, ironBox.expand(this.random.nextDouble(1.0D, 3.0D)));
		if (!entities.isEmpty) {
			Box entityBox = entities.get(this.random.nextInt(entities.size())).boundingBox;
			double endX = this.random.nextDouble(entityBox.minX, entityBox.maxX);
			double endY = this.random.nextDouble(entityBox.minY, entityBox.maxY);
			double endZ = this.random.nextDouble(entityBox.minZ, entityBox.maxZ);
			double startX = MathHelper.clamp(endX, ironBox.minX, ironBox.maxX);
			double startY = MathHelper.clamp(endY, ironBox.minY, ironBox.maxY);
			double startZ = MathHelper.clamp(endZ, ironBox.minZ, ironBox.maxZ);
			return new Target(startX, startY, startZ, endX, endY, endZ, this.random.nextLong());
		}
		else {
			Vec3i ironOffset = orientation.ironOffsets.get(this.random.nextInt() & 7);
			double centerX = this.pos.x + ironOffset.x + 0.5D;
			double centerY = this.pos.y + ironOffset.y + 0.5D;
			double centerZ = this.pos.z + ironOffset.z + 0.5D;
			double unitX = this.random.nextDouble() - 0.5D;
			double unitY = this.random.nextDouble() - 0.5D;
			double unitZ = this.random.nextDouble() - 0.5D;
			double scalar = 1.0D / Math.max(Math.max(Math.abs(unitX), Math.abs(unitY)), Math.abs(unitZ));
			unitX *= scalar;
			unitY *= scalar;
			unitZ *= scalar;
			double startX = centerX + unitX * 0.5D;
			double startY = centerY + unitY * 0.5D;
			double startZ = centerZ + unitZ * 0.5D;
			double length = this.random.nextDouble(1.0D, 3.0D);
			double endX = centerX + unitX * length;
			double endY = centerY + unitY * length;
			double endZ = centerZ + unitZ * length;
			return new Target(startX, startY, startZ, endX, endY, endZ, this.random.nextLong());
		}
	}

	public boolean checkStructure() {
		Direction primaryDirection = this.cachedState.get(Properties.FACING);
		Orientation orientation = Orientation.from(primaryDirection);
		BlockPos.Mutable mutablePos = this.pos.mutableCopy();
		BlockState tmpState;
		for (Vec3i offset : orientation.copperOffsetsExcludingSelf) {
			tmpState = this.world.getBlockState(mutablePos.set(this.pos, offset));
			if (!tmpState.isOf(FunctionalBlocks.COPPER_COIL) || tmpState.get(Properties.FACING) != primaryDirection) return false;
		}
		for (Vec3i offset : orientation.ironOffsets) {
			tmpState = this.world.getBlockState(mutablePos.set(this.pos, offset));
			if (!tmpState.isOf(Blocks.IRON_BLOCK)) return false;
		}
		return true;
	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		if (world != null) {
			this.damageSource = new DamageSource(
				world
				.registryManager
				.get(RegistryKeys.DAMAGE_TYPE)
				.entryOf(BigTechDamageTypes.TESLA_COIL),
				this.pos.toCenterPos()
			);
		}
	}

	public static class Target {

		public double startX, startY, startZ, endX, endY, endZ;
		public long seed;

		public Target(
			double startX,
			double startY,
			double startZ,
			double endX,
			double endY,
			double endZ,
			long seed
		) {
			this.startX = startX;
			this.startY = startY;
			this.startZ = startZ;
			this.endX   = endX;
			this.endY   = endY;
			this.endZ   = endZ;
			this.seed   = seed;
		}
	}

	public static enum Orientation {
		UP   (Direction.UP   ),
		DOWN (Direction.DOWN ),
		NORTH(Direction.NORTH),
		SOUTH(Direction.SOUTH),
		EAST (Direction.EAST ),
		WEST (Direction.WEST );

		public final Direction direction;
		public final BlockBox blockBox;
		public final Box box;
		public final List<Vec3i> copperOffsetsIncludingSelf, copperOffsetsExcludingSelf, ironOffsets;

		Orientation(Direction direction) {
			this.direction = direction;
			this.copperOffsetsIncludingSelf = new ArrayList<>(3);
			this.copperOffsetsExcludingSelf = new ArrayList<>(2);
			this.copperOffsetsIncludingSelf.add(new Vec3i(0, 0, 0));
			int
				offsetX = direction.offsetX,
				offsetY = direction.offsetY,
				offsetZ = direction.offsetZ;
			Vec3i tmp = new Vec3i(offsetX, offsetY, offsetZ);
			this.copperOffsetsIncludingSelf.add(tmp);
			this.copperOffsetsExcludingSelf.add(tmp);
			offsetX <<= 1;
			offsetY <<= 1;
			offsetZ <<= 1;
			tmp = new Vec3i(offsetX, offsetY, offsetZ);
			this.copperOffsetsIncludingSelf.add(tmp);
			this.copperOffsetsExcludingSelf.add(tmp);

			int
				minX = -1,
				minY = -1,
				minZ = -1,
				maxX = +1,
				maxY = +1,
				maxZ = +1;
			switch (direction.axis) {
				case X -> minX = maxX = 0;
				case Y -> minY = maxY = 0;
				case Z -> minZ = maxZ = 0;
			}
			minX += offsetX;
			minY += offsetY;
			minZ += offsetZ;
			maxX += offsetX;
			maxY += offsetY;
			maxZ += offsetZ;
			this.blockBox = new BlockBox(minX, minY, minZ, maxX, maxY, maxZ);
			this.box = new Box(minX, minY, minZ, maxX + 1, maxY + 1, maxZ + 1);

			this.ironOffsets = new ArrayList<>(8);
			for (int z = minZ; z <= maxZ; z++) {
				for (int x = minX; x <= maxX; x++) {
					for (int y = minY; y <= maxY; y++) {
						if (x == offsetX && y == offsetY && z == offsetZ) continue;
						this.ironOffsets.add(new Vec3i(x, y, z));
					}
				}
			}
			assert this.copperOffsetsIncludingSelf.size() == 3;
			assert this.copperOffsetsExcludingSelf.size() == 2;
			assert this.ironOffsets.size() == 8;
		}

		public static Orientation from(Direction direction) {
			return switch (direction) {
				case UP    -> UP;
				case DOWN  -> DOWN;
				case NORTH -> NORTH;
				case SOUTH -> SOUTH;
				case EAST  -> EAST;
				case WEST  -> WEST;
			};
		}
	}
}