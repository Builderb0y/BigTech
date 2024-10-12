package builderb0y.bigtech.codecs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;

import builderb0y.autocodec.coders.AutoCoder.NamedCoder;
import builderb0y.autocodec.decoders.DecodeContext;
import builderb0y.autocodec.decoders.DecodeException;
import builderb0y.autocodec.encoders.EncodeContext;
import builderb0y.autocodec.encoders.EncodeException;

/** {@link NbtCompound#CODEC} does not handle nulls correctly. */
public class NbtAutoCoder extends NamedCoder<NbtCompound> {

	public static final NbtAutoCoder INSTANCE = new NbtAutoCoder("NbtAutoCoder.INSTANCE");

	public NbtAutoCoder(@NotNull String toString) {
		super(toString);
	}

	@Override
	public @Nullable <T_Encoded> NbtCompound decode(@NotNull DecodeContext<T_Encoded> context) throws DecodeException {
		if (context.isEmpty()) return null;
		NbtElement element = context.ops.convertTo(NbtOps.INSTANCE, context.input);
		if (element instanceof NbtCompound compound) return compound;
		throw new DecodeException(() -> "Not a compound: " + element);
	}

	@Override
	public <T_Encoded> @NotNull T_Encoded encode(@NotNull EncodeContext<T_Encoded, NbtCompound> context) throws EncodeException {
		if (context.object == null) return context.empty();
		return NbtOps.INSTANCE.convertTo(context.ops, context.object);
	}
}