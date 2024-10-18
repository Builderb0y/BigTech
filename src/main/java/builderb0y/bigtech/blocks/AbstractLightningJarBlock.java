package builderb0y.bigtech.blocks;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.api.LightningStorageItem;
import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.blockEntities.LightningJarBlockEntity;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;
import builderb0y.bigtech.util.Directions;
import builderb0y.bigtech.util.WorldHelper;

public abstract class AbstractLightningJarBlock extends Block implements BlockEntityProvider, LightningPulseInteractor {

	public AbstractLightningJarBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.POWERED, Boolean.FALSE)
		);
	}

	public abstract int getCapacity();

	public abstract int getPulseSteps();

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
		super.appendTooltip(stack, context, tooltip, options);
		Integer stored = stack.get(BigTechDataComponents.LIGHTNING_ENERGY);
		if (stored != null) {
			tooltip.add(Text.translatable("bigtech.lightning_jar.stored", stored, this.getCapacity(), stored * 100 / this.getCapacity()));
		}
	}

	@Override
	public abstract VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context);

	@Override
	public ActionResult interactWithBattery(World world, BlockPos pos, BlockState state, PlayerEntity player, ItemStack stack, LightningStorageItem battery) {
		LightningJarBlockEntity jar = WorldHelper.getBlockEntity(world, pos, LightningJarBlockEntity.class);
		if (jar != null) {
			int charge = battery.getCharge(stack);
			int maxCharge = battery.getMaxCharge(stack);
			int jarCharge = jar.storedEnergy;
			int maxJarCharge = this.getCapacity();
			if (charge > 0 && jarCharge < maxJarCharge) {
				if (!world.isClient) {
					int transferred = Math.min(charge, maxJarCharge - jarCharge);
					jar.setStoredEnergyAndSync(jarCharge + transferred);
					if (player == null || !player.isCreative()) {
						battery.setCharge(stack, charge - transferred);
					}
				}
				return ActionResult.SUCCESS;
			}
			else if (jarCharge > 0 && charge < maxCharge) {
				if (!world.isClient) {
					int transferred = Math.min(jarCharge, maxCharge - charge);
					jar.setStoredEnergyAndSync(jarCharge - transferred);
					if (player == null || !player.isCreative()) {
						battery.setCharge(stack, charge + transferred);
					}
				}
				return ActionResult.SUCCESS;
			}
			else {
				return ActionResult.FAIL;
			}
		}
		else {
			return ActionResult.FAIL;
		}
	}

	@Override
	public boolean isSink(World world, BlockPos pos, BlockState state) {
		return !state.get(Properties.POWERED);
	}

	@Override
	public boolean canConductIn(WorldAccess world, BlockPos pos, BlockState state, @Nullable Direction side) {
		return side == Direction.DOWN && !state.get(Properties.POWERED);
	}

	@Override
	public boolean canConductOut(WorldAccess world, BlockPos pos, BlockState state, Direction side) {
		return side == Direction.DOWN;
	}

	@Override
	public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		if (!state.get(Properties.POWERED)) {
			LightningJarBlockEntity jar = WorldHelper.getBlockEntity(world, pos, LightningJarBlockEntity.class);
			if (jar != null) {
				jar.setStoredEnergyAndSync(Math.min(jar.storedEnergy + pulse.getDistributedEnergy(), this.getCapacity()));
				this.spawnLightningParticles(world, pos, state, pulse);
			}
		}
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		LightningJarBlockEntity jar = WorldHelper.getBlockEntity(world, pos, LightningJarBlockEntity.class);
		if (jar != null) {
			double chance = ((double)(jar.storedEnergy)) / ((double)(this.getCapacity()));
			if (world.random.nextDouble() < chance) {
				ParticleUtil.spawnParticle(
					world,
					pos,
					Directions.ALL[random.nextInt(6)],
					ParticleTypes.ELECTRIC_SPARK,
					new Vec3d(
						random.nextDouble(-0.125D, 0.125D),
						random.nextDouble(-0.125D, 0.125D),
						random.nextDouble(-0.125D, 0.125D)
					),
					0.55D
				);
			}
		}
	}

	@Override
	public ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!player.getStackInHand(hand).isEmpty()) {
			return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}
		if (!world.isClient) {
			LightningJarBlockEntity jar = WorldHelper.getBlockEntity(world, pos, LightningJarBlockEntity.class);
			if (jar != null) {
				player.sendMessage(Text.translatable("bigtech.lightning_jar.stored", jar.storedEnergy, this.getCapacity(), jar.storedEnergy * 100 / this.getCapacity()), true);
			}
		}
		return ItemActionResult.SUCCESS;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean moved) {
		super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, moved);
		world.scheduleBlockTick(pos, this, 2);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		boolean powered = state.get(Properties.POWERED);
		int power = world.getReceivedRedstonePower(pos);
		boolean shouldBePowered = power != 0;
		if (powered != shouldBePowered) {
			world.setBlockState(pos, state.with(Properties.POWERED, shouldBePowered));
			if (shouldBePowered) {
				LightningJarBlockEntity jar = WorldHelper.getBlockEntity(world, pos, LightningJarBlockEntity.class);
				if (jar != null && jar.storedEnergy > 0) {
					int subtracted = Math.min(this.getCapacity() * power / 15, jar.storedEnergy);
					LightningPulse pulse = new LightningPulse(world, pos.down(), subtracted, this.getPulseSteps());
					pulse.run();
					this.spawnLightningParticles(world, pos, state, pulse);
					jar.setStoredEnergyAndSync(jar.storedEnergy - subtracted);
				}
			}
		}
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		LightningJarBlockEntity jar = WorldHelper.getBlockEntity(world, pos, LightningJarBlockEntity.class);
		if (jar != null) {
			int result = Math.floorDiv((jar.storedEnergy - 1) * 14, (this.getCapacity() - 1)) + 1;
			return Math.min(Math.max(result, 0), 15);
		}
		else {
			return 0;
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new LightningJarBlockEntity(pos, state);
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.POWERED);
	}
}