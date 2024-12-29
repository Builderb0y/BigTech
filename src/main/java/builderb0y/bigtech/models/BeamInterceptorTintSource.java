package builderb0y.bigtech.models;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import builderb0y.autocodec.annotations.RecordLike;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.ColorF;

@RecordLike({})
public class BeamInterceptorTintSource implements TintSource {

	public static final MapCodec<BeamInterceptorTintSource> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	public MapCodec<? extends TintSource> getCodec() {
		return CODEC;
	}

	@Override
	public int getTint(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user) {
		NbtCompound nbt = stack.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT).getNbt();
		if (nbt != null) {
			int[] color = nbt.getIntArray("color");
			if (color.length == 3) {
				return ColorF.toInt(
					Float.intBitsToFloat(color[0]),
					Float.intBitsToFloat(color[1]),
					Float.intBitsToFloat(color[2])
				);
			}
		}
		return -1;
	}
}