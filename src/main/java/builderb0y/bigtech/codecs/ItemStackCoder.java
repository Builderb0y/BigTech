package builderb0y.bigtech.codecs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.item.ItemStack;

import builderb0y.autocodec.coders.AutoCoder;
import builderb0y.autocodec.coders.AutoCoder.NamedCoder;
import builderb0y.autocodec.common.FactoryContext;
import builderb0y.autocodec.common.FactoryException;
import builderb0y.autocodec.decoders.DecodeContext;
import builderb0y.autocodec.decoders.DecodeException;
import builderb0y.autocodec.encoders.EncodeContext;
import builderb0y.autocodec.encoders.EncodeException;
import builderb0y.autocodec.reflection.reification.ReifiedType;

public class ItemStackCoder extends NamedCoder<ItemStack> {

	public final AutoCoder<ItemStack> fallback;

	public ItemStackCoder(@NotNull ReifiedType<ItemStack> handledType, AutoCoder<ItemStack> fallback) {
		super(handledType);
		this.fallback = fallback;
	}

	@Override
	@OverrideOnly
	public <T_Encoded> @Nullable ItemStack decode(@NotNull DecodeContext<T_Encoded> context) throws DecodeException {
		if (context.isEmpty()) return ItemStack.EMPTY;
		else return context.decodeWith(this.fallback);
	}

	@Override
	@OverrideOnly
	public <T_Encoded> @NotNull T_Encoded encode(@NotNull EncodeContext<T_Encoded, ItemStack> context) throws EncodeException {
		ItemStack stack = context.object;
		if (stack == null || stack.isEmpty()) return context.empty();
		else return context.encodeWith(this.fallback);
	}

	public static class Factory extends NamedCoderFactory {

		public static final Factory INSTANCE = new Factory();

		@Override
		@OverrideOnly
		public <T_HandledType> @Nullable AutoCoder<?> tryCreate(@NotNull FactoryContext<T_HandledType> context) throws FactoryException {
			if (context.type.getRawClass() == ItemStack.class) {
				AutoCoder<ItemStack> fallback = context.autoCodec.wrapDFUCodec(ItemStack.CODEC);
				return context.type.getAnnotations().has(VerifyEmptyable.class) ? new ItemStackCoder(context.type.uncheckedCast(), fallback) : fallback;
			}
			return null;
		}
	}

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface VerifyEmptyable {}
}