package builderb0y.bigtech.asm;

import java.util.Objects;

import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class ClassMapping {

	public final String name;
	public DescMapping lazyDesc;

	public ClassMapping(String name) {
		this.name = Objects.requireNonNull(name, "name");
	}

	public static ClassMapping of(String name) {
		return new ClassMapping(MixinEnvironment.getDefaultEnvironment().getRemappers().map(name));
	}

	public DescMapping getDesc() {
		if (this.lazyDesc == null) {
			this.lazyDesc = new DescMapping(
				this.name.length() == 1 &&
				switch (this.name.charAt(0)) {
					case 'B', 'S', 'I', 'J', 'F', 'D', 'C', 'Z', 'V' -> true;
					default -> false;
				}
				? this.name
				: "L${this.name};"
			);
		}
		return this.lazyDesc;
	}

	public Type getType() {
		return this.desc.type;
	}

	public FieldMapping field(String name, DescMapping desc) {
		return FieldMapping.of(this, name, desc);
	}

	public FieldMapping deobfField(String name, DescMapping desc) {
		return new FieldMapping(this.name, name, desc.desc);
	}

	public MethodMapping method(String name, ParamMapping desc) {
		return MethodMapping.of(this, name, desc);
	}

	public MethodMapping deobfMethod(String name, ParamMapping desc) {
		return new MethodMapping(this.name, name, desc.desc);
	}

	public boolean matches(String name) {
		return this.name.equals(name);
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || (obj instanceof ClassMapping that && this.name.equals(that.name));
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public String toString() {
		return this.name;
	}
}