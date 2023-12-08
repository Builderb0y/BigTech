package builderb0y.bigtech.mixins;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.beams.base.PersistentBeam;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntity_NotifyBeamsOnLevelChange {

	@Inject(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/block/entity/BeaconBlockEntity;level:I"))
	private static void bigtech_beforeLevelChange(World world, BlockPos pos, BlockState state, BeaconBlockEntity blockEntity, CallbackInfo callback, @Share("bigtech_oldLevel") LocalIntRef level) {
		level.set(((BeaconBlockEntity_LevelGetter)(blockEntity)).bigtech_getLevel());
	}

	@Inject(method = "tick", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/block/entity/BeaconBlockEntity;level:I", shift = Shift.AFTER))
	private static void bigtech_afterLevelChange(World world, BlockPos pos, BlockState state, BeaconBlockEntity blockEntity, CallbackInfo callback, @Share("bigtech_oldLevel") LocalIntRef level) {
		if (level.get() != ((BeaconBlockEntity_LevelGetter)(blockEntity)).bigtech_getLevel()) {
			PersistentBeam.notifyBlockChanged(world, pos, state, state);
		}
	}
}