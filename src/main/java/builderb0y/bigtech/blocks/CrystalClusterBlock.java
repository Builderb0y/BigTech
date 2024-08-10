package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;

import builderb0y.bigtech.api.BeaconBeamColorProvider;
import builderb0y.bigtech.api.BeamInteractor;
import builderb0y.bigtech.beams.base.SpreadingBeamSegment;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.mixins.ExplosionAccessor;
import builderb0y.bigtech.models.CrystalBakedModel;
import builderb0y.bigtech.particles.SparkleParticleEffect;
import builderb0y.bigtech.registrableCollections.CrystalClusterRegistrableCollection.CrystalClusterColor;

public class CrystalClusterBlock extends Block implements Waterloggable, BeaconBeamColorProvider, BeamInteractor {

	public static final VoxelShape SHAPE = VoxelShapes.cuboid(0.0625D, 0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);

	public static final MapCodec<CrystalClusterBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public final CrystalClusterColor color;

	public CrystalClusterBlock(Settings settings, CrystalClusterColor color) {
		super(settings);
		this.color = color;
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	@Override
	public boolean spreadOut(SpreadingBeamSegment inputSegment, BlockState state) {
		Vector3f color = this.color.colorVector;
		if (inputSegment.color() == null || inputSegment.color().equals(color)) {
			inputSegment.beam().addSegment(inputSegment.withColor(color).extend(inputSegment.distanceRemaining() + 15.0D, inputSegment.direction()));
		}
		else {
			inputSegment.beam().addSegment(inputSegment.terminate());
		}
		return true;
	}

	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		super.onDestroyedByExplosion(world, pos, explosion);
		if (!world.isClient && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
			double
				blockX = pos.getX() + 0.5D,
				blockY = pos.getY() + 0.5D,
				blockZ = pos.getZ() + 0.5D,
				explosionX = explosion.<ExplosionAccessor>as().bigtech_getX(),
				explosionY = explosion.<ExplosionAccessor>as().bigtech_getY(),
				explosionZ = explosion.<ExplosionAccessor>as().bigtech_getZ(),
				explosionPower = explosion.<ExplosionAccessor>as().bigtech_getPower(),
				distanceSquared = MathHelper.squaredMagnitude(blockX - explosionX, blockY - explosionY, blockZ - explosionZ),
				halfDrops = distanceSquared / explosionPower;
			int drops = (int)(world.random.nextDouble() * halfDrops + halfDrops);
			for (int drop = 0; drop < drops; drop++) {
				double
					spawnX = pos.getX() + world.random.nextDouble(),
					spawnY = pos.getY() + world.random.nextDouble(),
					spawnZ = pos.getZ() + world.random.nextDouble(),
					motionX = spawnX - explosionX,
					motionY = spawnY - explosionY,
					motionZ = spawnZ - explosionZ,
					magnitudeSquared = MathHelper.squaredMagnitude(motionX, motionY, motionZ),
					scalar = 0.5D * explosionPower / magnitudeSquared;
				motionX *= scalar;
				motionY *= scalar;
				motionZ *= scalar;
				ItemEntity itemEntity = new ItemEntity(world, spawnX, spawnY, spawnZ, new ItemStack(FunctionalItems.CRYSTAL_DEBRIS), motionX, motionY, motionZ);
				itemEntity.setToDefaultPickupDelay();
				world.spawnEntity(itemEntity);
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getBlockModels().getModel(state);
		if (model instanceof CrystalBakedModel crystal) {
			Vec3d position = crystal.getParticlePosition(crystal.getSeedForPosition(pos), pos, world.random);
			world.addParticle(
				new SparkleParticleEffect(this.color.colorVector, 1.0F),
				position.x,
				position.y,
				position.z,
				0.0D,
				0.0D,
				0.0D
			);
		}
	}

	@Override
	public boolean shouldDropItemsOnExplosion(Explosion explosion) {
		return false;
	}

	@Override
	public int getBeaconColor(World world, BlockPos pos, BlockState state) {
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
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).getDefaultState();
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.WATERLOGGED, context.getWorld().getFluidState(context.getBlockPos()).isEqualAndStill(Fluids.WATER));
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.WATERLOGGED);
	}
}