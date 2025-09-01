package builderb0y.bigtech.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.render.item.property.select.SelectProperties;
import net.minecraft.client.render.item.property.select.SelectProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.circuits.CircuitRotation;
import builderb0y.bigtech.circuits.RotatableCircuitComponent;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;

@Environment(EnvType.CLIENT)
public class BigTechSelectProperties {

	public static void init() {
		SelectProperties.ID_MAPPER.put(BigTechMod.modID("circuit_rotation"), CircuitRotationSelectProperty.TYPE);
	}

	@Environment(EnvType.CLIENT)
	public static class CircuitRotationSelectProperty implements SelectProperty<CircuitRotation> {

		public static final CircuitRotationSelectProperty INSTANCE = new CircuitRotationSelectProperty();
		public static final Codec<CircuitRotation> CIRCUIT_ROTATION_CODEC = BigTechAutoCodec.AUTO_CODEC.createDFUCodec(CircuitRotation.class);
		public static final SelectProperty.Type<CircuitRotationSelectProperty, CircuitRotation> TYPE = SelectProperty.Type.create(MapCodec.unit(INSTANCE), CIRCUIT_ROTATION_CODEC);

		@Override
		public @Nullable CircuitRotation getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed, ItemDisplayContext displayContext) {
			return stack.get(BigTechDataComponents.CIRCUIT) instanceof RotatableCircuitComponent rotatable ? rotatable.getRotation() : null;
		}

		@Override
		public Codec<CircuitRotation> valueCodec() {
			return CIRCUIT_ROTATION_CODEC;
		}

		@Override
		public Type<? extends SelectProperty<CircuitRotation>, CircuitRotation> getType() {
			return TYPE;
		}
	}
}