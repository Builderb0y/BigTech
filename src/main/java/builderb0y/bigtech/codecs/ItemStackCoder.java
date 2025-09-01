package builderb0y.bigtech.codecs;

import java.lang.annotation.*;

import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import builderb0y.autocodec.coders.AutoCoder;
import builderb0y.autocodec.coders.AutoCoder.NamedCoder;
import builderb0y.autocodec.common.FactoryContext;
import builderb0y.autocodec.common.FactoryException;
import builderb0y.autocodec.data.*;
import builderb0y.autocodec.decoders.DecodeContext;
import builderb0y.autocodec.decoders.DecodeException;
import builderb0y.autocodec.encoders.EncodeContext;
import builderb0y.autocodec.encoders.EncodeException;
import builderb0y.autocodec.reflection.reification.ReifiedType;

/** can't rely on vanilla for anything. */
public class ItemStackCoder extends NamedCoder<ItemStack> {

	public final boolean alwaysEncode;

	public ItemStackCoder(@NotNull ReifiedType<ItemStack> handledType, boolean encode) {
		super(handledType);
		this.alwaysEncode = encode;
	}

	@Override
	@OverrideOnly
	public <T_Encoded> @Nullable ItemStack decode(@NotNull DecodeContext<T_Encoded> context) throws DecodeException {
		if (context.isEmpty()) return ItemStack.EMPTY;
		MapData map = context.forceAsMap();
		StringData id = map.get("id").tryAsString();
		if (id == null) return ItemStack.EMPTY;
		Identifier identifier = Identifier.of(id.value);
		RegistryEntry<Item> item = Registries.ITEM.getEntry(identifier).orElse(null);
		if (item == null || item.value() == Items.AIR) return ItemStack.EMPTY;
		AbstractNumberData count = map.get("count").tryAsNumber();
		int countInt = count != null ? count.intValue() : 1;
		Data componentsData = map.get("components");
		if (componentsData.isEmpty()) {
			return new ItemStack(item, countInt);
		}
		else {
			ComponentChanges components = context.withData(componentsData).decodeWith(context.autoCodec().wrapDFUCodec(ComponentChanges.CODEC));
			return new ItemStack(item, countInt, components);
		}
	}

	@Override
	@OverrideOnly
	public <T_Encoded> @NotNull Data encode(@NotNull EncodeContext<T_Encoded, ItemStack> context) throws EncodeException {
		ItemStack stack = context.object;
		if (stack == null) stack = ItemStack.EMPTY;
		if (!this.alwaysEncode && stack.isEmpty()) return context.empty();
		MapData result = (
			new MapData(3)
			.with("id", new StringData(Registries.ITEM.getId(stack.getItem()).toString()))
			.with("count", new NumberData((byte)(stack.getCount())))
		);
		ComponentChanges changes = stack.getComponentChanges();
		if (!changes.isEmpty()) {
			result.put("components", context.object(changes).encodeWith(context.autoCodec().wrapDFUCodec(ComponentChanges.CODEC)));
		}
		return result;
	}

	public static class Factory extends NamedCoderFactory {

		public static final Factory INSTANCE = new Factory();

		@Override
		@OverrideOnly
		public <T_HandledType> @Nullable AutoCoder<?> tryCreate(@NotNull FactoryContext<T_HandledType> context) throws FactoryException {
			if (context.type.getRawClass() == ItemStack.class) {
				VerifyEmptyable annotation = context.type.getAnnotations().getFirst(VerifyEmptyable.class);
				return new ItemStackCoder(context.type.uncheckedCast(), annotation != null && annotation.alwaysEncode());
			}
			return null;
		}
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface VerifyEmptyable {

		public boolean alwaysEncode() default false;
	}
}