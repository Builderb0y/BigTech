package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import builderb0y.bigtech.blockEntities.BuildingBlockEntity;
import builderb0y.bigtech.blockEntities.BuildingBlockEntity.PlaceContext;
import builderb0y.bigtech.blocks.FunctionalBlocks;

@Mixin(BlockItem.class)
public abstract class BlockItem_UseBuildingBlock {

	@Shadow protected abstract boolean place(ItemPlacementContext context, BlockState state);

	@Shadow protected abstract boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state);

	@Shadow private static void copyComponentsToBlockEntity(World world, BlockPos pos, ItemStack stack) {}

	@Shadow protected abstract SoundEvent getPlaceSound(BlockState state);

	@Shadow public abstract Block getBlock();

	@Shadow protected abstract boolean canPlace(ItemPlacementContext context, BlockState state);

	@Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;"))
	private void bigtech_captureOldState(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> callback, @Share("oldState") LocalRef<BlockState> oldState) {
		oldState.set(context.getWorld().getBlockState(context.getBlockPos()));
	}

	@Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "FIELD", target = "Lnet/minecraft/util/ActionResult;SUCCESS:Lnet/minecraft/util/ActionResult$Success;", opcode = Opcodes.GETSTATIC))
	private void bigtech_placeAdditional(
		ItemPlacementContext context,
		CallbackInfoReturnable<ActionResult> callback,
		@Local(ordinal = 0) BlockState toPlace,
		@Local(ordinal = 1) BlockState originallyPlaced,
		@Share("oldState") LocalRef<BlockState> oldState
	) {
		if (
			context.getPlayer() instanceof ServerPlayerEntity serverPlayer &&
			!serverPlayer.isSneaking() &&
			this.getBlock() != FunctionalBlocks.BUILDING_BLOCK
		) {
			BuildingBlockEntity.forWorld(
				serverPlayer,
				new PlaceContext(
					context.getBlockPos(),
					oldState.get(),
					originallyPlaced,
					context.getPlayerLookDirection(),
					context.getSide()
				)
			)
			.forEach((PlaceContext placement) -> {
				ItemStack stack = context.getStack();
				if (stack.getItem() == (Object)(this)) { //will be air when empty.
					ServerWorld world = serverPlayer.getWorld();
					BlockPos pos = placement.pos();
					if (placement.matches(world)) {
						AutomaticItemPlacementContext vanilla = placement.toVanilla(world, stack);
						BlockState to = placement.to().withIfExists(Properties.WATERLOGGED, oldState.get().getOrEmpty(Properties.WATERLOGGED).orElse(Boolean.FALSE));
						if (this.canPlace(vanilla, to) && this.place(vanilla, to)) {
							BlockState placed = world.getBlockState(pos);
							if (placed.getBlock() == toPlace.getBlock()) {
								this.postPlacement(pos, world, serverPlayer, stack, placed);
								copyComponentsToBlockEntity(world, pos, stack);
								placed.getBlock().onPlaced(world, pos, placed, serverPlayer, stack);
								Criteria.PLACED_BLOCK.trigger(serverPlayer, pos, stack);
							}
							BlockSoundGroup sound = placed.getSoundGroup();
							world.playSound(
								null,
								pos,
								this.getPlaceSound(placed),
								SoundCategory.BLOCKS,
								sound.getVolume() * 0.5F + 0.5F,
								sound.getPitch() * 0.8F
							);
							world.emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(serverPlayer, placed));
							stack.decrementUnlessCreative(1, serverPlayer);
						}
					}
				}
			});
		}
	}
}