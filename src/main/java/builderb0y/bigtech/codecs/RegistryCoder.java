package builderb0y.bigtech.codecs;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registry;

import builderb0y.autocodec.AutoCodec;
import builderb0y.autocodec.coders.AutoCoder;
import builderb0y.autocodec.coders.KeyDispatchCoder.DispatchCoder;
import builderb0y.autocodec.coders.KeyDispatchCoder.Dispatchable;
import builderb0y.bigtech.BigTechMod;

public class RegistryCoder<T_Decoded extends Dispatchable<T_Decoded>> extends DispatchCoder<T_Decoded> {

	public final AutoCodec autoCodec;
	public final Registry<AutoCoder<? extends T_Decoded>> registry;
	public final Map<AutoCoder<? extends T_Decoded>, PacketCodec<? super RegistryByteBuf, ? extends T_Decoded>> packetCodecs;
	public final PacketCodec<RegistryByteBuf, T_Decoded> packetCodec = PacketCodec.ofStatic(this::encodeToPacket, this::decodeFromPacket);

	public RegistryCoder(AutoCodec autoCodec, String toString, String keyName, Registry<AutoCoder<? extends T_Decoded>> registry) {
		super(toString, autoCodec.wrapDFUCodec(registry.getCodec()), keyName);
		this.autoCodec = autoCodec;
		this.registry = registry;
		this.packetCodecs = new HashMap<>();
	}

	public <T_Sub extends T_Decoded> void register(String name, Class<T_Sub> clazz, PacketCodec<? super RegistryByteBuf, T_Sub> packetCodec) {
		AutoCoder<T_Sub> coder = this.autoCodec.createCoder(clazz);
		Registry.register(this.registry, BigTechMod.modID(name), coder);
		this.packetCodecs.put(coder, packetCodec);
	}

	public T_Decoded decodeFromPacket(RegistryByteBuf buffer) {
		int id = buffer.readVarInt();
		AutoCoder<? extends T_Decoded> coder = this.registry.get(id);
		PacketCodec<? super RegistryByteBuf, ? extends T_Decoded> packetCodec = this.packetCodecs.get(coder);
		return packetCodec.decode(buffer);
	}

	@SuppressWarnings("unchecked")
	public void encodeToPacket(RegistryByteBuf buffer, T_Decoded object) {
		AutoCoder<? extends T_Decoded> coder = object.getCoder();
		int id = this.registry.getRawId(coder);
		buffer.writeVarInt(id);
		PacketCodec<? super RegistryByteBuf, ? extends T_Decoded> packetCodec = this.packetCodecs.get(coder);
		(
			(PacketCodec<? super RegistryByteBuf, T_Decoded>)(
				packetCodec
			)
		)
		.encode(buffer, object);
	}
}