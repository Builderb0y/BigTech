package builderb0y.bigtech.asm;

import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.Overwrite;

import builderb0y.bigtech.api.BeaconBeamColorProvider;
import builderb0y.bigtech.mixins.BigTechMixinPlugin;

import static org.objectweb.asm.Opcodes.*;

/**
patches BeaconBlockEntity to make use of {@link BeaconBeamColorProvider}.
by default, the logic looks like this: {@code
	if (!(block instanceof Stainable)) break block16;
	fs = ((Stainable)((Object)block)).getColor().getColorComponents();
}
and I want to replace it with this: {@code
	fs = bigtech_getColor(world, pos, state, beacon);
	if (fs == null) break block16;
}
and as far as I know, there is no way to do this with mixin.
unless you count {@link Overwrite}, which I don't want to use here.
*/
public class BeaconBlockEntityASM {

	public static final ClassMapping
		BEACON_BLOCK_ENTITY_TYPE        = ClassMapping.of("net/minecraft/class_2580"),
		WORLD_TYPE                      = ClassMapping.of("net/minecraft/class_1937"),
		BLOCK_POS_TYPE                  = ClassMapping.of("net/minecraft/class_2338"),
		BLOCK_STATE_TYPE                = ClassMapping.of("net/minecraft/class_2680"),
		STAINABLE_TYPE                  = ClassMapping.of("net/minecraft/class_4275");
	public static final MethodMapping
		BEACON_BLOCK_ENTITY_TICK_METHOD = BEACON_BLOCK_ENTITY_TYPE.method("method_16896", ParamMapping.of(DescMapping.VOID, WORLD_TYPE.desc, BLOCK_POS_TYPE.desc, BLOCK_STATE_TYPE.desc, BEACON_BLOCK_ENTITY_TYPE.desc)),
		BIGTECH_GET_COlOR_METHOD        = BEACON_BLOCK_ENTITY_TYPE.deobfMethod("bigtech_getColor", ParamMapping.of(new DescMapping("[F"), WORLD_TYPE.desc, BLOCK_POS_TYPE.desc, BLOCK_STATE_TYPE.desc, BEACON_BLOCK_ENTITY_TYPE.desc));

	public static void transform(ClassNode clazz) {
		for (MethodNode method : clazz.methods) {
			if (BEACON_BLOCK_ENTITY_TICK_METHOD.matches(method)) {
				transformTick(method);
				return;
			}
		}
	}

	/*
	OLD BYTECODE:
		L19
			LINENUMBER 147 L19
			ALOAD 12
			INSTANCEOF net/minecraft/block/Stainable
			IFEQ L20
		L21
			LINENUMBER 148 L21
			ALOAD 12
			CHECKCAST net/minecraft/block/Stainable
			INVOKEINTERFACE net/minecraft/block/Stainable.getColor ()Lnet/minecraft/util/DyeColor; (itf)
			INVOKEVIRTUAL net/minecraft/util/DyeColor.getColorComponents ()[F
			ASTORE 13
	NEW BYTECODE:
		L19
			ALOAD 0
			ALOAD 7
			ALOAD 11
			ALOAD 3
			INVOKESTATIC BeaconBlockEntity.bigtech_getColor()[F
			ASTORE 13
			ALOAD 13
			IFNULL L20
	*/
	public static void transformTick(MethodNode method) {
		BigTechMixinPlugin.ASM_LOGGER.info("Transforming BeaconBlockEntity.tick()...");
		for (AbstractInsnNode node = method.instructions.first; node != null; node = node.next) {
			if (node.opcode == INSTANCEOF && node.<TypeInsnNode>as().desc.equals(STAINABLE_TYPE.name)) {
				//step 1: find the relevant instructions.
				AbstractInsnNode storeBlock = node.previous;
				while (!(storeBlock.opcode == ASTORE && storeBlock.<VarInsnNode>as().var == 12)) {
					storeBlock = storeBlock.previous;
					if (storeBlock == null) {
						error("Could not locate ASTORE 12 before instanceof Stainable");
						return;
					}
				}
				LabelNode jumpTo;
				if (node.next instanceof JumpInsnNode jump && jump.opcode == IFEQ) {
					jumpTo = jump.label;
				}
				else {
					error("instanceof Stainable not followed by IFEQ");
					return;
				}
				AbstractInsnNode storeColor = node.next.next;
				while (!(storeColor.opcode == ASTORE && storeColor.<VarInsnNode>as().var == 13)) {
					storeColor = storeColor.next;
					if (storeColor == null) {
						error("Could not locate ASTORE 13 after instanceof Stainable");
						return;
					}
				}
				//step 2: apply patches.
				for (AbstractInsnNode toRemove = storeBlock.next; toRemove != storeColor;) {
					AbstractInsnNode next = toRemove.next;
					method.instructions.remove(toRemove);
					toRemove = next;
				}
				method.instructions.insertBefore(storeColor, new VarInsnNode(ALOAD, 0)); //world
				method.instructions.insertBefore(storeColor, new VarInsnNode(ALOAD, 7)); //world, pos
				method.instructions.insertBefore(storeColor, new VarInsnNode(ALOAD, 11)); //world, pos, state
				method.instructions.insertBefore(storeColor, new VarInsnNode(ALOAD, 3)); //world, pos, state, beacon
				method.instructions.insertBefore(storeColor, BIGTECH_GET_COlOR_METHOD.toMethodInsnNode(INVOKESTATIC, false)); //bigtech_getColor(world, pos, state, beacon)
				//fs = bigtech_getColor(world, pos, state, beacon);
				//remaining instructions are inserted *after* storeColor, so they should be read in reverse order.
				method.instructions.insert(storeColor, new JumpInsnNode(IFNULL, jumpTo)); //if (fs == null) goto jumpTo;
				method.instructions.insert(storeColor, new VarInsnNode(ALOAD, 13)); //fs
				BigTechMixinPlugin.ASM_LOGGER.info("Successfully transformed BeaconBlockEntity.tick().");
				return;
			}
		}
	}

	public static void error(String message) {
		BigTechMixinPlugin.ASM_LOGGER.error("Failed to transform BeaconBlockEntity: " + message + "; crystal clusters will not tint their beams correctly.");
	}
}