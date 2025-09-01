package builderb0y.bigtech.items;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import builderb0y.bigtech.circuits.CircuitComponent;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;

@Environment(EnvType.CLIENT)
public class CircuitTintSource implements TintSource {

	public static final MapCodec<CircuitTintSource> CODEC = BigTechAutoCodec.callerMapCodec();

	public final int index;

	public CircuitTintSource(int index) {
		this.index = index;
	}

	@Override
	public int getTint(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user) {
		CircuitComponent circuit = stack.get(BigTechDataComponents.CIRCUIT);
		return circuit != null ? circuit.getColor(stack, this.index) : -1;
	}

	@Override
	public MapCodec<? extends TintSource> getCodec() {
		return CODEC;
	}
}