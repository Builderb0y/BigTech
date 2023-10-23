package builderb0y.bigtech.mixins;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.FrameBlock;
import builderb0y.bigtech.util.Enums;

@Mixin(value = PlayerEntity.class, priority = 2000)
public abstract class PlayerEntity_HookIntoIsClimbing extends LivingEntity {

	@Shadow @Final private PlayerAbilities abilities;

	public PlayerEntity_HookIntoIsClimbing(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Dynamic("isClimbing() is not, by default, overridden by PlayerEntity. A different mixin overrides it.")
	@Inject(method = "isClimbing()Z", at = @At("HEAD"), cancellable = true)
	private void bigtech_ladderTweaks(CallbackInfoReturnable<Boolean> callback) {
		if (this.abilities.flying) {
			callback.setReturnValue(Boolean.FALSE);
			return;
		}
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		for (Direction direction : Enums.HORIZONTAL_DIRECTIONS) {
			BlockState state = this.world.getBlockState(mutablePos.set(this.blockPos, direction));
			if (state.getBlock() instanceof FrameBlock && state.isIn(BlockTags.CLIMBABLE)) {
				callback.setReturnValue(Boolean.TRUE);
				return;
			}
		}
	}
}