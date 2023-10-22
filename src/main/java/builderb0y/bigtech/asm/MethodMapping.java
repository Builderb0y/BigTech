package builderb0y.bigtech.asm;

import java.util.Objects;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class MethodMapping {

	public final String owner, name, desc;

	public MethodMapping(String owner, String name, String desc) {
		this.owner = Objects.requireNonNull(owner, "owner");
		this.name  = Objects.requireNonNull(name,  "name" );
		this.desc  = Objects.requireNonNull(desc,  "desc" );
	}

	public static MethodMapping of(ClassMapping owner, String name, ParamMapping desc) {
		return new MethodMapping(owner.name, MixinEnvironment.getDefaultEnvironment().getRemappers().mapMethodName(owner.name, name, desc.desc), desc.desc);
	}

	public MethodNode toMethodNode(int access) {
		return this.toMethodNode(access, null, null);
	}

	public MethodNode toMethodNode(int access, String signature, String[] exceptions) {
		return new MethodNode(access, this.name, this.desc, signature, exceptions);
	}

	public MethodInsnNode toMethodInsnNode(int opcode) {
		return this.toMethodInsnNode(opcode, opcode == Opcodes.INVOKEINTERFACE);
	}

	public MethodInsnNode toMethodInsnNode(int opcode, boolean isInterface) {
		return new MethodInsnNode(opcode, this.owner, this.name, this.desc, isInterface);
	}

	public void applyToMethodInsnNode(MethodInsnNode method, int opcode) {
		this.applyToMethodInsnNode(method, opcode, opcode == Opcodes.INVOKEINTERFACE);
	}

	public void applyToMethodInsnNode(MethodInsnNode method, int opcode, boolean isInterface) {
		method.setOpcode(opcode);
		method.owner = this.owner;
		method.name  = this.name;
		method.desc  = this.desc;
		method.itf   = isInterface;
	}

	public boolean matches(String name, String desc) {
		return this.name.equals(name) && this.desc.equals(desc);
	}

	public boolean matches(String owner, String name, String desc) {
		return this.owner.equals(owner) && this.name.equals(name) && this.desc.equals(desc);
	}

	public boolean matches(MethodNode method) {
		return this.matches(method.name, method.desc);
	}

	public boolean matches(MethodInsnNode method) {
		return this.matches(method.owner, method.name, method.desc);
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || (
			obj instanceof MethodMapping that &&
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
		return "${this.owner}.${this.name}${this.desc}";
	}
}