package builderb0y.bigtech.asm;

import java.util.Objects;

import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class DescMapping {

	public static final DescMapping
		BYTE    = new DescMapping("B"),
		SHORT   = new DescMapping("S"),
		INT     = new DescMapping("I"),
		LONG    = new DescMapping("J"),
		FLOAT   = new DescMapping("F"),
		DOUBLE  = new DescMapping("D"),
		CHAR    = new DescMapping("C"),
		BOOLEAN = new DescMapping("Z"),
		VOID    = new DescMapping("V");

	public final String desc;
	public Type lazyType;

	public DescMapping(String desc) {
		this.desc = Objects.requireNonNull(desc);
	}

	public static DescMapping of(String desc) {
		return new DescMapping(MixinEnvironment.getDefaultEnvironment().getRemappers().mapDesc(desc));
	}

	public DescMapping array(int dimensions) {
		return dimensions == 0 ? this : new DescMapping("[".repeat(dimensions) + this.desc);
	}

	public DescMapping array() {
		return new DescMapping('[' + this.desc);
	}

	public Type getType() {
		if (this.lazyType == null) {
			this.lazyType = Type.getType(this.desc);
		}
		return this.lazyType;
	}

	public boolean matches(String desc) {
		return this.desc.equals(desc);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || (obj instanceof DescMapping that && this.desc.equals(that.desc));
	}

	@Override
	public int hashCode() {
		return this.desc.hashCode();
	}

	@Override
	public String toString() {
		return this.desc;
	}
}