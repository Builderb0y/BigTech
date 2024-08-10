package builderb0y.bigtech.blocks.belts;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.api.AscenderInteractor;
import builderb0y.bigtech.blocks.BigTechProperties;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class DirectorBeltBlock extends RedstoneReceivingBeltBlock {

	public static final MapCodec<DirectorBeltBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public DirectorBeltBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(BigTechProperties.DIRECTOR_BELT_MODE, DirectorBeltMode.LEFT_RIGHT)
			.with(Properties.INVERTED, Boolean.FALSE)
		);
	}

	@Override
	public int getAscenderPriority(World world, BlockPos pos, BlockState state, Direction face) {
		if (face == Direction.UP) return AscenderInteractor.BELT_TOP;
		if (face == Direction.DOWN) return AscenderInteractor.BLOCKED;
		Direction facing = state.get(Properties.FACING);
		if (face == facing.getOpposite()) return AscenderInteractor.BELT_BACK;
		return AscenderInteractor.BLOCKED;
	}

	@Override
	public Direction getDirection(World world, BlockPos pos, BlockState state, Entity entity) {
		Direction forward = state.get(Properties.HORIZONTAL_FACING);
		DirectorBeltMode mode = state.get(BigTechProperties.DIRECTOR_BELT_MODE);
		return state.get(Properties.POWERED) == state.get(Properties.INVERTED) ? mode.getFirstDirection(forward) : mode.getSecondDirection(forward);
	}

	@Override
	public ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			if (player.isSneaking()) {
				world.setBlockState(pos, state.cycle(BigTechProperties.DIRECTOR_BELT_MODE));
			}
			else {
				world.setBlockState(pos, state.with(Properties.INVERTED, !state.get(Properties.INVERTED)));
			}
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.5F);
		}
		return ItemActionResult.SUCCESS;
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.INVERTED, BigTechProperties.DIRECTOR_BELT_MODE);
	}

	public static enum DirectorBeltMode implements StringIdentifiable {
		LEFT_RIGHT("left_right", "right_left"),
		LEFT_FRONT("left_front", "front_left"),
		RIGHT_FRONT("right_front", "front_right");

		public final String regularName, invertedName;

		DirectorBeltMode(String regularName, String invertedName) {
			this. regularName =  regularName;
			this.invertedName = invertedName;
		}

		@Override
		public String asString() {
			return this.regularName;
		}

		public Direction getFirstDirection(Direction front) {
			return switch (this) {
				case  LEFT_RIGHT -> front.rotateYCounterclockwise();
				case  LEFT_FRONT -> front.rotateYCounterclockwise();
				case RIGHT_FRONT -> front.rotateYClockwise();
			};
		}

		public Direction getSecondDirection(Direction front) {
			return switch (this) {
				case  LEFT_RIGHT -> front.rotateYClockwise();
				case  LEFT_FRONT -> front;
				case RIGHT_FRONT -> front;
			};
		}
	}
}