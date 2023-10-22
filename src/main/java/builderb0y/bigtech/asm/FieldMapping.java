package builderb0y.bigtech.asm;

import java.util.Objects;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class FieldMapping {

	public final String owner, name, desc;

	public FieldMapping(String owner, String name, String desc) {
		this.owner = Objects.requireNonNull(owner, "owner");
		this.name  = Objects.requireNonNull(name,  "name" );
		this.desc  = Objects.requireNonNull(desc,  "desc" );
	}

	public static FieldMapping of(ClassMapping owner, String name, DescMapping desc) {
		return new FieldMapping(owner.name, MixinEnvironment.getDefaultEnvironment().getRemappers().mapFieldName(owner.name, name, desc.desc), desc.desc);
	}

	public FieldNode toFieldNode(int access) {
		return this.toFieldNode(access, null, null);
	}

	public FieldNode toFieldNode(int access, String signature, Object value) {
		return new FieldNode(access, this.name, this.desc, signature, value);
	}

	public FieldInsnNode toFieldInsnNode(int opcode) {
		return new FieldInsnNode(opcode, this.owner, this.name, this.desc);
	}

	public void applyToFieldInsnNode(FieldInsnNode field, int opcode) {
		field.setOpcode(opcode);
		field.owner = this.owner;
		field.name  = this.name;
		field.desc  = this.desc;
	}

	public boolean matches(String name, String desc) {
		return this.name.equals(name) && this.desc.equals(desc);
	}

	public boolean matches(String owner, String name, String desc) {
		return this.owner.equals(owner) && this.name.equals(name) && this.desc.equals(desc);
	}

	public boolean matches(FieldNode field) {
		return this.matches(field.name, field.desc);
	}

	public boolean matches(FieldInsnNode field) {
		return this.matches(field.owner, field.name, field.desc);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || (
			obj instanceof FieldMapping that &&
			this.owner.equals(that.owner) &&
			this.name .equals(that.name ) &&
			this.desc .equals(that.desc )
		);
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + this.owner.hashCode();
		hash = hash * 31 + this.name .hashCode();
		hash = hash * 31 + this.desc .hashCode();
		return hash;
	}

	@Override
	public String toString() {
		return "${this.owner}.${this.name} : ${this.desc}";
	}
}