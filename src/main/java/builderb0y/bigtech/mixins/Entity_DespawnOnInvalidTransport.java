package builderb0y.bigtech.mixins;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.entities.BigTechEntityTags;

@Mixin(Entity.class)
public abstract class Entity_DespawnOnInvalidTransport {

	@Shadow public abstract EntityType<?> getType();

	@Shadow public abstract World getWorld();

	@Shadow public abstract BlockPos getBlockPos();

	@Shadow public abstract void discard();

	@Shadow protected boolean firstUpdate;

	@Inject(method = "setPos", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;chunkPos:Lnet/minecraft/util/math/ChunkPos;", opcode = Opcodes.PUTFIELD, shift = Shift.AFTER))
	private void bigtech_despawnOnInvalidTransport(double x, double y, double z, CallbackInfo callback) {
		if (
			!this.firstUpdate &&
			this.getWorld() instanceof ServerWorld serverWorld &&
			this.getType().isIn(BigTechEntityTags.INVALID_TRANSPORT_DESPAWN) &&
			!serverWorld.shouldTickEntityAt(this.getBlockPos()) &&
			this.getWorld().getBlockState(this.getBlockPos()).isIn(BigTechBlockTags.INVALID_TRANSPORT_DESPAWN)
		) {
			this.discard();
		}
	}
}