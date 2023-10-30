package builderb0y.bigtech.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;

import builderb0y.bigtech.api.BeaconBeamColorProvider;
import builderb0y.bigtech.items.BigTechItems;
import builderb0y.bigtech.mixins.ExplosionAccessor;
import builderb0y.bigtech.registrableCollections.CrystalClusterRegistrableCollection.CrystalClusterColor;

public class CrystalClusterBlock extends Block implements Waterloggable, BeaconBeamColorProvider {

	public static final VoxelShape SHAPE = VoxelShapes.cuboid(0.0625D, 0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);

	public final CrystalClusterColor color;

	public CrystalClusterBlock(Settings settings, CrystalClusterColor color) {
		super(settings);
		this.color = color;
		this.defaultState = this.defaultState.with(Properties.WATERLOGGED, Boolean.FALSE);
		BeaconBeamColorProvider.LOOKUP.registerForBlocks((world, pos, state, blockEntity, context) -> this, this);
	}

	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		super.onDestroyedByExplosion(world, pos, explosion);
		if (!world.isClient && world.gameRules.getBoolean(GameRules.DO_TILE_DROPS)) {
			double
				blockX = pos.x + 0.5D,
				blockY = pos.y + 0.5D,
				blockZ = pos.z + 0.5D,
				explosionX = ((ExplosionAccessor)(explosion)).bigtech_getX(),
				explosionY = ((ExplosionAccessor)(explosion)).bigtech_getY(),
				explosionZ = ((ExplosionAccessor)(explosion)).bigtech_getZ(),
				explosionPower = ((ExplosionAccessor)(explosion)).bigtech_getPower(),
				distanceSquared = MathHelper.squaredMagnitude(blockX - explosionX, blockY - explosionY, blockZ - explosionZ),
				halfDrops = distanceSquared / explosionPower;
			int drops = (int)(world.random.nextDouble() * halfDrops + halfDrops);
			for (int drop = 0; drop < drops; drop++) {
				double
					spawnX = pos.x + world.random.nextDouble(),
					spawnY = pos.y + world.random.nextDouble(),
					spawnZ = pos.z + world.random.nextDouble(),
					motionX = spawnX - explosionX,
					motionY = spawnY - explosionY,
					motionZ = spawnZ - explosionZ,
					magnitudeSquared = MathHelper.squaredMagnitude(motionX, motionY, motionZ),
					speed = Math.sqrt(magnitudeSquared),
					targetSpeed = 2.0D * explosionPower / magnitudeSquared,
					scalar = targetSpeed / speed;
				motionX *= scalar;
				motionY *= scalar;
				motionZ *= scalar;
				ItemEntity itemEntity = new ItemEntity(world, spawnX, spawnY, spawnZ, new ItemStack(BigTechItems.CRYSTAL_DEBRIS), motionX, motionY, motionZ);
				itemEntity.setToDefaultPickupDelay();
				world.spawnEntity(itemEntity);
			}
		}
	}

	@Override
	public boolean shouldDropItemsOnExplosion(Explosion explosion) {
		return false;
	}

	@Override
	public float[] getBeaconColor(World world, BlockPos pos, BlockState state) {
		return this.color.color;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(Properties.WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).defaultState;
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.WATERLOGGED, context.world.getFluidState(context.blockPos).isEqualAndStill(Fluids.WATER));
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.WATERLOGGED);
	}
}