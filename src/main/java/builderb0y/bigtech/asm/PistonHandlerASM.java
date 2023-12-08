package builderb0y.bigtech.asm;

import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.piston.PistonHandler;

import builderb0y.bigtech.mixins.BigTechMixinPlugin;

import static org.objectweb.asm.Opcodes.*;

/**
transformations applied in this file:

	{@link PistonHandler#calculatePush}:
		before:
			if (!PistonHandler.isBlockSticky(this.world.getBlockState(blockPos)) || this.tryMoveAdjacentBlock(blockPos)) continue;
		after:
			if (!PistonHandlerASM.isBlockSticky(this.world.getBlockState(blockPos), blockPos, this.world) || this.tryMoveAdjacentBlock(blockPos)) continue;

	{@link PistonHandler#tryMove}:
		before:
			while (PistonHandler.isBlockSticky(blockState)) {
		after:
			while (PistonHandlerASM.isBlockSticky(blockState, pos, this.world)) {
		before:
			if (blockState.isAir() || !PistonHandler.isAdjacentBlockStuck(blockState22, blockState) || !PistonBlock.isMovable(blockState, this.world, blockPos, this.motionDirection, false, this.motionDirection.getOpposite()) || blockPos.equals(this.posFrom)) break;
		after:
			if (blockState.isAir() || !PistonHandlerASM.isAdjacentBlockStuck(blockState22, blockState, pos.offset(this.motionDirection.getOpposite(), i - 1, blockPos, this.world, this.motionDirection.getOpposite()) || !PistonBlock.isMovable(blockState, this.world, blockPos, this.motionDirection, false, this.motionDirection.getOpposite()) || blockPos.equals(this.posFrom)) break;
		before:
			if (!PistonHandler.isBlockSticky(this.world.getBlockState(blockPos3)) || this.tryMoveAdjacentBlock(blockPos3)) continue;
		after:
			if (!PistonHandlerASM.isBlockSticky(this.world.getBlockState(blockPos3), blockPos3, this.world) || this.tryMoveAdjacentBlock(blockPos3)) continue;

	{@link PistonHandler#tryMoveAdjacentBlock}:
		before:
			if (direction.getAxis() == this.motionDirection.getAxis() || !PistonHandler.isAdjacentBlockStuck(blockState2 = this.world.getBlockState(blockPos = pos.offset(direction)), blockState) || this.tryMove(blockPos, direction)) continue;
		after:
			if (direction.getAxis() == this.motionDirection.getAxis() || !PistonHandlerASM.isAdjacentBlockStuck(blockState, blockState2 = this.world.getBlockState(blockPos = pos.offset(direction)), pos, blockPos, this.world, direction) || this.tryMove(blockPos, direction)) continue;

reason for using ASM here:

	the existing {@link PistonHandler#isBlockSticky isBlockSticky} and
	{@link PistonHandler#isAdjacentBlockStuck isAdjacentBlockStuck} methods
	do not have parameters for world, position, or direction, and I kind of need those.
	at the very least, I need the direction, but it never hurts
	to provide more useful stuff in case anyone else wants it.

	{@link Redirect @Redirect}
	can capture the parameters of the enclosing method,
	but it can't capture other local variables.
	since position and direction are local variables in some of the methods I'm modifying,
	{@link Redirect} can't access them.

	{@link Inject @Inject} can
	capture local variables, but it can't change a method call like
	{@link Redirect @Redirect} can.

	as far as I'm aware, {@link ModifyVariable}
	can't modify the return value of a method after it's been
	called while said return value is still on the top of the stack.
	it also can't capture locals. but it does allow capturing parameters.

	I asked if there was a solution I was overlooking
	on the fabric discord, but got no response.
	so now I'm using ASM.
	if anyone wants to tell me a better solution, feel free.
*/
public class PistonHandlerASM {

	public static final ClassMapping
		PISTON_HANDLER_TYPE = ClassMapping.of("net/minecraft/class_2674"),
		BLOCK_POS_TYPE      = ClassMapping.of("net/minecraft/class_2338"),
		VEC3I_TYPE          = ClassMapping.of("net/minecraft/class_2382"),
		BLOCK_STATE_TYPE    = ClassMapping.of("net/minecraft/class_2680"),
		DIRECTION_TYPE      = ClassMapping.of("net/minecraft/class_2350"),
		WORLD_TYPE          = ClassMapping.of("net/minecraft/class_1937");

	public static final MethodMapping
		CALCULATE_PUSH_METHOD                 = PISTON_HANDLER_TYPE.method("method_11537", ParamMapping.of(DescMapping.BOOLEAN)),
		TRY_MOVE_METHOD                       = PISTON_HANDLER_TYPE.method("method_11540", ParamMapping.of(DescMapping.BOOLEAN, BLOCK_POS_TYPE.desc, DIRECTION_TYPE.desc)),
		TRY_MOVE_ADJACENT_BLOCK_METHOD        = PISTON_HANDLER_TYPE.method("method_11538", ParamMapping.of(DescMapping.BOOLEAN, BLOCK_POS_TYPE.desc)),
		IS_BLOCK_STICKY_METHOD                = PISTON_HANDLER_TYPE.method("method_23367", ParamMapping.of(DescMapping.BOOLEAN, BLOCK_STATE_TYPE.desc)),
		IS_ADJACENT_BLOCK_STUCK_METHOD        = PISTON_HANDLER_TYPE.method("method_23675", ParamMapping.of(DescMapping.BOOLEAN, BLOCK_STATE_TYPE.desc, BLOCK_STATE_TYPE.desc)),
		GET_OPPOSITE_METHOD                   =      DIRECTION_TYPE.method("method_10153", ParamMapping.of(DIRECTION_TYPE.desc)),
		OFFSET_METHOD                         =      BLOCK_POS_TYPE.method("method_10079", ParamMapping.of(BLOCK_POS_TYPE.desc, DIRECTION_TYPE.desc, DescMapping.INT)),
		CUSTOM_IS_BLOCK_STICKY_METHOD         = PISTON_HANDLER_TYPE.deobfMethod("bigtech_isBlockSticky",        ParamMapping.of(DescMapping.BOOLEAN, BLOCK_STATE_TYPE.desc, BLOCK_POS_TYPE.desc, PISTON_HANDLER_TYPE.desc)),
		CUSTOM_IS_ADJACENT_BLOCK_STUCK_METHOD = PISTON_HANDLER_TYPE.deobfMethod("bigtech_isAdjacentBlockStuck", ParamMapping.of(DescMapping.BOOLEAN, BLOCK_STATE_TYPE.desc, BLOCK_STATE_TYPE.desc, BLOCK_POS_TYPE.desc, BLOCK_POS_TYPE.desc, DIRECTION_TYPE.desc, PISTON_HANDLER_TYPE.desc));

	public static final FieldMapping
		MOTION_DIRECTION_FIELD = PISTON_HANDLER_TYPE.field("field_12243", DIRECTION_TYPE.desc);

	public static void transform(ClassNode classNode) {
		BigTechMixinPlugin.ASM_LOGGER.info("Transforming PistonHandler to add new context for blocks to use when deciding whether or not to be sticky.");
		for (MethodNode methodNode : classNode.methods) {
			BigTechMixinPlugin.ASM_LOGGER.debug("Found method " + methodNode.name + methodNode.desc);
			if (CALCULATE_PUSH_METHOD.matches(methodNode)) {
				transformCalculatePush(methodNode);
			}
			else if (TRY_MOVE_METHOD.matches(methodNode)) {
				transformTryMove(methodNode);
			}
			else if (TRY_MOVE_ADJACENT_BLOCK_METHOD.matches(methodNode)) {
				transformTryMoveAdjacentBlock(methodNode);
			}
		}
	}

	public static void transformCalculatePush(MethodNode methodNode) {
		BigTechMixinPlugin.ASM_LOGGER.debug("Transforming calculatePush()");
		int isBlockStickyCalls = 0;
		for (AbstractInsnNode instruction = methodNode.instructions.getFirst(); instruction != null; instruction = instruction.getNext()) {
			if (instruction instanceof MethodInsnNode call && IS_BLOCK_STICKY_METHOD.matches(call)) {
				BigTechMixinPlugin.ASM_LOGGER.debug("Found call to isBlockSticky()");
				if (isBlockStickyCalls == 0) {
					addIsBlockStickyContext(methodNode.instructions, call, 3);
				}
				isBlockStickyCalls++;
			}
		}
		if (isBlockStickyCalls != 1) {
			BigTechMixinPlugin.ASM_LOGGER.warn("calculatePush() had " + isBlockStickyCalls + " calls to isBlockSticky(); expected 1.");
		}
	}

	public static void transformTryMove(MethodNode methodNode) {
		BigTechMixinPlugin.ASM_LOGGER.debug("Transforming tryMove()");
		int isBlockStickyCalls = 0;
		int isAdjacentBlockStuckCalls = 0;
		for (AbstractInsnNode instruction = methodNode.instructions.getFirst(); instruction != null; instruction = instruction.getNext()) {
			if (instruction instanceof MethodInsnNode call) {
				if (IS_BLOCK_STICKY_METHOD.matches(call)) {
					BigTechMixinPlugin.ASM_LOGGER.debug("Found call to isBlockSticky()");
					if (isBlockStickyCalls == 0) {
						addIsBlockStickyContext(methodNode.instructions, call, 1);
					}
					else if (isBlockStickyCalls == 1) {
						addIsBlockStickyContext(methodNode.instructions, call, 10);
					}
					isBlockStickyCalls++;
				}
				else if (IS_ADJACENT_BLOCK_STUCK_METHOD.matches(call)) {
					BigTechMixinPlugin.ASM_LOGGER.debug("Found call to isAdjacentBlockStuck()");
					if (isAdjacentBlockStuckCalls == 0) {
						transformFirstTryMoveCallToIsAdjacentBlockStuck(methodNode.instructions, call);
					}
					isAdjacentBlockStuckCalls++;
				}
			}
		}
		if (isBlockStickyCalls != 2) {
			BigTechMixinPlugin.ASM_LOGGER.warn("tryMove() had " + isBlockStickyCalls + " calls to isBlockSticky(); expected 2.");
		}
		if (isAdjacentBlockStuckCalls != 1) {
			BigTechMixinPlugin.ASM_LOGGER.warn("tryMove() had " + isAdjacentBlockStuckCalls + " calls to isAdjacentBlockStuck(); expected 1.");
		}
	}

	public static void transformFirstTryMoveCallToIsAdjacentBlockStuck(InsnList instructions, MethodInsnNode call) {
		BigTechMixinPlugin.ASM_LOGGER.debug("Transforming call to isAdjacentBlockStuck() from tryMove()");
		//state and otherState are already on the stack.

		//pos
		instructions.insertBefore(call, new VarInsnNode(ALOAD, 1)); //pos
		instructions.insertBefore(call, new VarInsnNode(ALOAD, 0)); //pos, this
		instructions.insertBefore(call, MOTION_DIRECTION_FIELD.toFieldInsnNode(GETFIELD)); //pos, this.motionDirection
		instructions.insertBefore(call, GET_OPPOSITE_METHOD.toMethodInsnNode(INVOKEVIRTUAL)); //pos, this.motionDirection.getOpposite()
		instructions.insertBefore(call, new VarInsnNode(ILOAD, 4)); //pos, this.motionDirection.getOpposite(), i
		instructions.insertBefore(call, new InsnNode(ICONST_1)); //pos, this.motionDirection.getOpposite(), i, 1
		instructions.insertBefore(call, new InsnNode(ISUB)); //pos, this.motionDirection.getOpposite(), i - 1
		instructions.insertBefore(call, OFFSET_METHOD.toMethodInsnNode(INVOKEVIRTUAL)); //pos.offset(this.motionDirection.getOpposite(), i - 1)

		//otherPos
		instructions.insertBefore(call, new VarInsnNode(ALOAD, 5)); //otherPos

		//direction
		instructions.insertBefore(call, new VarInsnNode(ALOAD, 0)); //this
		instructions.insertBefore(call, MOTION_DIRECTION_FIELD.toFieldInsnNode(GETFIELD)); //this.motionDirection
		instructions.insertBefore(call, GET_OPPOSITE_METHOD.toMethodInsnNode(INVOKEVIRTUAL)); //this.motionDirection.getOpposite()

		//handler
		instructions.insertBefore(call, new VarInsnNode(ALOAD, 0));

		//custom invoke
		CUSTOM_IS_ADJACENT_BLOCK_STUCK_METHOD.applyToMethodInsnNode(call, INVOKESTATIC);
	}

	public static void transformTryMoveAdjacentBlock(MethodNode methodNode) {
		BigTechMixinPlugin.ASM_LOGGER.debug("Transforming tryMoveAdjacentBlock()");
		int isAdjacentBlockStuckCalls = 0;
		for (AbstractInsnNode instruction = methodNode.instructions.getFirst(); instruction != null; instruction = instruction.getNext()) {
			if (instruction instanceof MethodInsnNode call && IS_ADJACENT_BLOCK_STUCK_METHOD.matches(call)) {
				BigTechMixinPlugin.ASM_LOGGER.debug("Found call to isAdjacentBlockStuck()");
				if (isAdjacentBlockStuckCalls == 0) {
					transformFirstTryMoveAdjacentBlockCallToIsAdjacentBlockStuck(methodNode.instructions, call);
				}
				isAdjacentBlockStuckCalls++;
			}
		}
		if (isAdjacentBlockStuckCalls != 1) {
			BigTechMixinPlugin.ASM_LOGGER.warn("tryMoveAdjacentBlock() had " + isAdjacentBlockStuckCalls + " calls to isAdjacentBlockStuck(); expected 1.");
		}
	}

	public static void transformFirstTryMoveAdjacentBlockCallToIsAdjacentBlockStuck(InsnList instructions, MethodInsnNode call) {
		BigTechMixinPlugin.ASM_LOGGER.debug("Transforming call to isAdjacentBlockStuck() from tryMoveAdjacentBlock()");

		//state and otherState are already on the stack, but in the wrong order.
		instructions.insertBefore(call, new InsnNode(SWAP));

		//pos
		instructions.insertBefore(call, new VarInsnNode(ALOAD, 1)); //pos

		//otherPos
		instructions.insertBefore(call, new VarInsnNode(ALOAD, 7)); //otherPos

		//face
		instructions.insertBefore(call, new VarInsnNode(ALOAD, 6)); //face

		//handler
		instructions.insertBefore(call, new VarInsnNode(ALOAD, 0)); //this

		//custom invoke
		CUSTOM_IS_ADJACENT_BLOCK_STUCK_METHOD.applyToMethodInsnNode(call, INVOKESTATIC);
	}

	public static void addIsBlockStickyContext(InsnList instructions, MethodInsnNode call, int blockPosVar) {
		BigTechMixinPlugin.ASM_LOGGER.debug("Adding context to isBlockSticky() call");
		instructions.insertBefore(call, new VarInsnNode(ALOAD, blockPosVar)); //blockPos
		instructions.insertBefore(call, new VarInsnNode(ALOAD, 0)); //this
		CUSTOM_IS_BLOCK_STICKY_METHOD.applyToMethodInsnNode(call, INVOKESTATIC);
	}
}