package builderb0y.bigtech.asm;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;
import org.spongepowered.asm.mixin.Overwrite;

import builderb0y.bigtech.api.BeaconBeamColorProvider;
import builderb0y.bigtech.mixins.BigTechMixinPlugin;

import static org.objectweb.asm.Opcodes.*;

/**
patches BeaconBlockEntity to make use of {@link BeaconBeamColorProvider}.
by default, the logic looks like this: {@code
	if (blockState.getBlock() instanceof Stainable stainable) {
		int n = stainable.getColor().getEntityColor();
		...
	}
	else {
		...
	}
}
and I want to replace it with this: {@code
	int n = bigtech_getColor(world, pos, state, beacon);
	if (n != 0) {
		...
	}
	else {
		...
	}
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
		BEACON_BLOCK_ENTITY_TICK_METHOD = BEACON_BLOCK_ENTITY_TYPE.method("method_16896", ParamMapping.of(DescMapping.VOID, WORLD_TYPE.getDesc(), BLOCK_POS_TYPE.getDesc(), BLOCK_STATE_TYPE.getDesc(), BEACON_BLOCK_ENTITY_TYPE.getDesc())),
		BIGTECH_GET_COlOR_METHOD        = BEACON_BLOCK_ENTITY_TYPE.deobfMethod("bigtech_getColor", ParamMapping.of(DescMapping.INT, WORLD_TYPE.getDesc(), BLOCK_POS_TYPE.getDesc(), BLOCK_STATE_TYPE.getDesc(), BEACON_BLOCK_ENTITY_TYPE.getDesc()));
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
		L18
			LINENUMBER 142 L18
			ALOAD 11
			INVOKEVIRTUAL net/minecraft/block/BlockState.getBlock ()Lnet/minecraft/block/Block;
			ASTORE 12
		L19
			LINENUMBER 142 L19
			ALOAD 12
			INSTANCEOF net/minecraft/block/Stainable
			IFEQ L20
			ALOAD 12
			CHECKCAST net/minecraft/block/Stainable
			ASTORE 13
		L21
			LINENUMBER 143 L21
			ALOAD 13
			INVOKEINTERFACE net/minecraft/block/Stainable.getColor ()Lnet/minecraft/util/DyeColor; (itf)
			INVOKEVIRTUAL net/minecraft/util/DyeColor.getEntityColor ()I
			ISTORE 14

	NEW BYTECODE:
		L18
			LINENUMBER 142 L18
		L19
			LINENUMBER 142 L19
			ALOAD 0
			ALOAD 7
			ALOAD 11
			ALOAD 3
			INVOKESTATIC BeaconBlockEntity.bigtech_getColor()[F
			ISTORE 14
			ILOAD 14
			IFEQ L20
		L21
	*/
	public static void transformTick(MethodNode method) {
		BigTechMixinPlugin.ASM_LOGGER.info("Transforming BeaconBlockEntity.tick()...");
		for (AbstractInsnNode node = method.instructions.getFirst(); node != null; node = node.getNext()) {
			if (node.getOpcode() == INSTANCEOF && node.<TypeInsnNode>as().desc.equals(STAINABLE_TYPE.name)) {
				//step 1: find the relevant instructions.
				AbstractInsnNode loadState = node.getPrevious();
				while (!(loadState.getOpcode() == ALOAD && loadState.<VarInsnNode>as().var == 11)) {
					loadState = loadState.getPrevious();
					if (loadState == null) {
						error("Could not locate ALOAD 11 before instanceof Stainable");
						return;
					}
				}
				LabelNode jumpTo;
				if (node.getNext() instanceof JumpInsnNode jump && jump.getOpcode() == IFEQ) {
					jumpTo = jump.label;
				}
				else {
					error("instanceof Stainable not followed by IFEQ");
					return;
				}
				AbstractInsnNode storeColor = node.getNext().getNext();
				while (!(storeColor.getOpcode() == ISTORE && storeColor.<VarInsnNode>as().var == 14)) {
					storeColor = storeColor.getNext();
					if (storeColor == null) {
						error("Could not locate ISTORE 14 after instanceof Stainable");
						return;
					}
				}

				//step 2: apply patches.
				for (AbstractInsnNode toRemove = loadState; toRemove != storeColor;) {
					AbstractInsnNode next = toRemove.getNext();
					if (toRemove.getOpcode() >= 0) {
						method.instructions.remove(toRemove);
					}
					toRemove = next;
				}
				method.instructions.insertBefore(storeColor, new VarInsnNode(ALOAD, 0)); //world
				method.instructions.insertBefore(storeColor, new VarInsnNode(ALOAD, 7)); //world, blockPos
				method.instructions.insertBefore(storeColor, new VarInsnNode(ALOAD, 11)); //world, blockPos, blockState
				method.instructions.insertBefore(storeColor, new VarInsnNode(ALOAD, 3)); //world, blockPos, blockState, blockEntity
				method.instructions.insertBefore(storeColor, BIGTECH_GET_COlOR_METHOD.toMethodInsnNode(INVOKESTATIC, false)); //bigtech_getColor(world, blockPos, blockState, blockEntity)
				//now working in reverse order...
				method.instructions.insert(storeColor, new JumpInsnNode(IFEQ, jumpTo)); //if (color == 0) goto jumpTo;
				method.instructions.insert(storeColor, new VarInsnNode(ILOAD, 14)); //color
				BigTechMixinPlugin.ASM_LOGGER.info("Successfully transformed BeaconBlockEntity.tick().");
				return;
			}
		}
	}

	public static void error(String message) {
		BigTechMixinPlugin.ASM_LOGGER.error("Failed to transform BeaconBlockEntity: " + message + "; crystal clusters will not tint their beams correctly.");
	}
}