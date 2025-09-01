package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;

import net.minecraft.block.PistonBlock;

import builderb0y.autocodec.annotations.AddPseudoField;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.mixinterfaces.VariablePiston;

@AddPseudoField("sticky")
@AddPseudoField("max_blocks")
public class SteelPistonBlock extends PistonBlock {

	public static final MapCodec<SteelPistonBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public SteelPistonBlock(int max_blocks, boolean sticky, Settings settings) {
		super(sticky, settings);
		((VariablePiston)(this)).bigtech_setBlocksToMove(max_blocks);
	}

	public boolean sticky() {
		return ((VariablePiston)(this)).bigtech_isSticky();
	}

	public int max_blocks() {
		return ((VariablePiston)(this)).bigtech_getBlocksToMove();
	}
}