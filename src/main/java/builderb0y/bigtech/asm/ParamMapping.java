package builderb0y.bigtech.asm;

import java.util.Objects;

import org.objectweb.asm.Type;

public class ParamMapping {

	public final String desc;
	public Type lazyType;

	public ParamMapping(String desc) {
		this.desc = Objects.requireNonNull(desc, "desc");
	}

	public static ParamMapping of(DescMapping returnType) {
		return new ParamMapping("()" + returnType.desc);
	}

	public static ParamMapping of(DescMapping returnType, DescMapping paramType) {
		return new ParamMapping('(' + paramType.desc + ')' + returnType.desc);
	}

	public static ParamMapping of(DescMapping returnType, DescMapping... paramTypes) {
		if (paramTypes.length == 0) return of(returnType);
		if (paramTypes.length == 1) return of(returnType, paramTypes[0]);
		StringBuilder builder = new StringBuilder(paramTypes.length * 64 + 64).append('(');
		for (DescMapping paramType : paramTypes) {
			builder.append(paramType.desc);
		}
		return new ParamMapping(builder.append(')').append(returnType.desc).toString());
	}

	public Type getType() {
		if (this.lazyType == null) {
			this.lazyType = Type.getMethodType(this.desc);
		}
		return this.lazyType;
	}

	public boolean matches(String desc) {
		return this.desc.equals(desc);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || (obj instanceof ParamMapping that && this.desc.equals(that.desc));
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