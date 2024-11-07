package builderb0y.bigtech.mixins;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.throwables.MixinError;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BlockEntityType.BlockEntityFactory;

@Mixin(BlockEntityType.class)
public interface BlockEntityType_ConstructorAccess {

	@Invoker("<init>")
	public static <B extends BlockEntity> BlockEntityType<B> bigtech_create(BlockEntityFactory<B> factory, Set<Block> blocks) {
		throw new MixinError("Invoker not applied");
	}
}