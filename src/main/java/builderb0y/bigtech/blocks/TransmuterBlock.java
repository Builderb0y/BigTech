package builderb0y.bigtech.blocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import builderb0y.bigtech.blockEntities.TransmuterBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.lightning.LightningPulse;
import builderb0y.bigtech.lightning.LightningPulse.LinkedBlockPos;
import builderb0y.bigtech.api.LightningPulseInteractor;
import builderb0y.bigtech.mixins.ServerRecipeManager_PreparedRecipesAccess;
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

	public static final MapCodec<TransmuterBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public TransmuterBlock(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getConductionShape(WorldView world, BlockPos pos, BlockState state, Direction face) {
		return VoxelShapes.fullCube();
	}

	@Override
	public boolean isSink(WorldView world, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void onPulse(ServerWorld world, LinkedBlockPos pos, BlockState state, LightningPulse pulse) {
		TransmuterBlockEntity transmuter = WorldHelper.getBlockEntity(world, pos, TransmuterBlockEntity.class);
		if (transmuter != null) {
			//part of transmute recipe logic requires knowing how much energy the current slot received.
			//however, *that* depends on how many items the transmuter wants to distribute its energy to.
			//the transmuter will only distribute energy to items which are part of a transmute recipe.
			//so, we have to iterate over all slots, check if they're a valid input or not, and count them
			//just to know how much energy each slot should get for the actual crafting procedure.
			//then, we'd normally need to iterate again to do the crafting.
			//I figure I can optimize that somewhat by remembering which recipe was used for which slot
			//during the first iteration, and then re-use that information for the second iteration.
			//additionally, I expect that in practice, transmuters will be filled mostly with many of the same item.
			//as such, if a recipe worked on one item, there is a high likelyhood that it'll work on the next item too.
			//so, that's an opportunity for optimization as well.
			record RecipeSlot(TransmuteRecipe recipe, int slot) {}
			List<RecipeSlot> recipeSlots = new ArrayList<>(15);
			Collection<RecipeEntry<TransmuteRecipe>> allRecipes = world.getRecipeManager().<ServerRecipeManager_PreparedRecipesAccess>as().bigtech_getPreparedRecipes().getAll(BigTechRecipeTypes.TRANSMUTE);
			{
				//first iteration.
				TransmuteRecipe activeRecipe = null;
				for (int slot = 0; slot < 15; slot++) {
					ItemStack stack = transmuter.getStack(slot);
					if (!stack.isEmpty()) {
						if (activeRecipe != null && activeRecipe.input.test(stack)) {
							recipeSlots.add(new RecipeSlot(activeRecipe, slot));
						}
						else for (RecipeEntry<TransmuteRecipe> entry : allRecipes) {
							if (entry.value().input.test(stack)) {
								recipeSlots.add(new RecipeSlot(entry.value(), slot));
								activeRecipe = entry.value();
								break;
							}
						}
					}
				}
			}
			if (!recipeSlots.isEmpty()) {
				TransmuteRecipeInventory inventory = new TransmuteRecipeInventory(world.random);
				inventory.totalEnergy = pulse.getDistributedEnergy();
				inventory.slotEnergy = inventory.totalEnergy / recipeSlots.size();
				//second iteration.
				for (RecipeSlot recipeSlot : recipeSlots) {
					inventory.stack = transmuter.getStack(recipeSlot.slot);
					ItemStack result = recipeSlot.recipe.craft(inventory, world.getRegistryManager());
					if (!result.isEmpty()) {
						transmuter.setStack(recipeSlot.slot, result);
					}
				}
				world.<ServerWorld>as().spawnParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
				world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.BLOCKS, 1.0F, 2.0F - world.random.nextFloat() * 0.25F);
				LightningPulseInteractor.spawnLightningParticles(world, pos, state, pulse);
			}
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient) {
			NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
			if (factory != null) player.openHandledScreen(factory);
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		TransmuterBlockEntity transmuter = WorldHelper.getBlockEntity(world, pos, TransmuterBlockEntity.class);
		if (transmuter != null) {
			int sum = 0;
			for (int index = 0; index < 15; index++) {
				if (!transmuter.getStack(index).isEmpty()) sum++;
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