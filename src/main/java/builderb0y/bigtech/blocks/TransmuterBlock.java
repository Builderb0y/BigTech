package builderb0y.bigtech.blocks;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.TransmuterBlockEntity;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;
import builderb0y.bigtech.lightning.LightningPulseInteractor;
import builderb0y.bigtech.recipes.BigTechRecipeTypes;
import builderb0y.bigtech.recipes.TransmuteRecipe;
import builderb0y.bigtech.recipes.TransmuteRecipeInventory;
import builderb0y.bigtech.util.WorldHelper;

public class TransmuterBlock extends BlockWithEntity implements LightningPulseInteractor {

	public static final VoxelShape SHAPE = VoxelShapes.union(
		VoxelShapes.cuboid(0.0625D, 0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D),
		VoxelShapes.combine(
			VoxelShapes.fullCube(),
			VoxelShapes.union(
				VoxelShapes.cuboid(0.0D, 0.0D, 0.1875D, 1.0D, 1.0D, 0.8125D),
				VoxelShapes.cuboid(0.0D, 0.1875D, 0.0D, 1.0D, 0.8125D, 1.0D),
				VoxelShapes.cuboid(0.1875D, 0.0D, 0.0D, 0.8125D, 1.0D, 1.0D)
			),
			BooleanBiFunction.ONLY_FIRST
		)
	);

	public TransmuterBlock(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isSink(World world, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void onPulse(World world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		TransmuterBlockEntity transmuter = WorldHelper.getBlockEntity(world, pos, TransmuterBlockEntity.class);
		if (transmuter != null) {
			int nonEmptySlots = (int)(transmuter.items.stream().filter(stack -> !stack.isEmpty).count());
			if (nonEmptySlots > 0) {
				List<RecipeEntry<TransmuteRecipe>> allRecipes = world.getRecipeManager().listAllOfType(BigTechRecipeTypes.TRANSMUTE);
				TransmuteRecipeInventory inventory = new TransmuteRecipeInventory(world.random);
				inventory.totalEnergy = pulse.distributedEnergy;
				inventory.slotEnergy = inventory.totalEnergy / nonEmptySlots;
				TransmuteRecipe activeRecipe = null;
				for (int slot = 0; slot < 15; slot++) {
					inventory.stack = transmuter.getStack(slot);
					found:
					if (activeRecipe == null || !activeRecipe.matches(inventory, world)) {
						for (RecipeEntry<TransmuteRecipe> recipe : allRecipes) {
							if (recipe.value().matches(inventory, world)) {
								activeRecipe = recipe.value();
								break found;
							}
						}
						activeRecipe = null;
					}
					if (activeRecipe != null) {
						ItemStack result = activeRecipe.craft(inventory, world.registryManager);
						if (!result.isEmpty) {
							transmuter.setStack(slot, result);
						}
					}
				}
				((ServerWorld)(world)).spawnParticles(ParticleTypes.EXPLOSION, pos.x + 0.5D, pos.y + 0.5D, pos.z + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
				world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0F, 2.0F - world.random.nextFloat() * 0.25F);
				this.spawnLightningParticles(world, pos, state, pulse);
			}
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.onPlaced(world, pos, state, placer, stack);
		if (stack.hasCustomName()) {
			TransmuterBlockEntity transmuter = WorldHelper.getBlockEntity(world, pos, TransmuterBlockEntity.class);
			if (transmuter != null) {
				transmuter.customName = stack.getName();
			}
		}
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.isOf(newState.getBlock())) {
			return;
		}
		TransmuterBlockEntity transmuter = WorldHelper.getBlockEntity(world, pos, TransmuterBlockEntity.class);
		if (transmuter != null) ItemScatterer.spawn(world, pos, transmuter);
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
			if (factory != null) player.openHandledScreen(factory);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		TransmuterBlockEntity transmuter = WorldHelper.getBlockEntity(world, pos, TransmuterBlockEntity.class);
		if (transmuter != null) {
			int sum = 0;
			for (int index = 0; index < 15; index++) {
				if (!transmuter.getStack(index).isEmpty) sum++;
			}
			return sum;
		}
		return 0;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TransmuterBlockEntity(pos, state);
	}
}