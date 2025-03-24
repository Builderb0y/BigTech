package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockSetType;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.autocodec.annotations.AddPseudoField;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

@AddPseudoField("block_set_type")
public class SteelPressurePlateBlock extends PressurePlateBlock {

	public static final MapCodec<SteelPressurePlateBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public SteelPressurePlateBlock(BlockSetType block_set_type, Settings settings) {
		super(block_set_type, settings);
	}

	public BlockSetType block_set_type() {
		return this.blockSetType;
	}

	@Override
	public int getRedstoneOutput(World world, BlockPos pos) {
		return getEntityCount(world, BOX.offset(pos), PlayerEntity.class) > 0 ? 15 : 0;
	}
}