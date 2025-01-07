package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class FerromagneticTranslatorBlock extends Block implements MagneticBlock {

	public static final MapCodec<FerromagneticTranslatorBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public FerromagneticTranslatorBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(Properties.FACING, Direction.UP));
	}

	@Override
	public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.FACING, context.getPlayerLookDirection().getOpposite());
	}

	@Override
	public void attractEntity(
		World world,
		BlockPos pos,
		BlockState state,
		Entity entity,
		double dx,
		double dy,
		double dz,
		double force,
		Vector3d accumulatedVelocity
	) {
		Direction direction = state.get(Properties.FACING);
		accumulatedVelocity.add(direction.getOffsetX() * force, direction.getOffsetY() * force, direction.getOffsetZ() * force);
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING);
	}
}