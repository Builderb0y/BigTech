package builderb0y.bigtech.blocks;

import java.util.List;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.compat.computercraft.ComputercraftCompat;

public class RadioBlock extends Block {

	public static final MapCodec<RadioBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public static final VoxelShape
		NORTH_SOUTH_SHAPE = VoxelShapes.cuboidUnchecked(0.25D, 0.0D, 0.375D, 0.75D, 0.25D, 0.625D),
		EAST_WEST_SHAPE   = VoxelShapes.cuboidUnchecked(0.375D, 0.0D, 0.25D, 0.625D, 0.25D, 0.75D);

	public RadioBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.HORIZONTAL_FACING, context.getHorizontalPlayerFacing());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch (state.get(Properties.HORIZONTAL_FACING).getAxis()) {
			case X -> EAST_WEST_SHAPE;
			case Y -> throw new IllegalStateException("vertical radio?");
			case Z -> NORTH_SOUTH_SHAPE;
		};
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
		super.appendTooltip(stack, context, tooltip, options);
		if (ComputercraftCompat.INSTALLED) {
			tooltip.add(Text.translatable("tooltip.bigtech.radio.usage"));
		}
		else {
			tooltip.add(Text.translatable("tooltip.bigtech.radio.computercraft_not_installed"));
		}
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.HORIZONTAL_FACING);
	}
}