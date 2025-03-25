package builderb0y.bigtech.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;

import builderb0y.bigtech.datagen.base.UseDataGen;
import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection.CrystalColor;

import static builderb0y.bigtech.blocks.BigTechBlocks.*;

public class MaterialBlocks {

	@UseDataGen(void.class)
	public static final CrystalBlockCollection CRYSTAL_CLUSTERS = new CrystalBlockCollection(
		true,
		(CrystalColor color) -> {
			return new CrystalClusterBlock(
				settings(color.prefix + "crystal_cluster")
				.mapColor(color.closestDyeColor)
				.strength(0.3F)
				.sounds(BlockSoundGroup.AMETHYST_CLUSTER)
				.nonOpaque()
				.allowsSpawning(Blocks::never)
				.solidBlock(Blocks::never)
				.suffocates(Blocks::never)
				.blockVision(Blocks::never)
				.luminance((BlockState state) -> 7),
				color
			);
		}
	);
	@UseDataGen(void.class)
	public static final CrystallineSandBlock CRYSTALLINE_SAND = register(
		new CrystallineSandBlock(
			copySettings(Blocks.SAND, "crystalline_sand")
		)
	);
	@UseDataGen(void.class)
	public static final Block SMOOTH_OBSIDIAN = register(
		new Block(
			copySettings(Blocks.OBSIDIAN, "smooth_obsidian")
			.hardness(20.0F)
			//did you know that pistons hard-code the check for
			//obsidian instead of using the piston behavior on the block?
			//in fact, obsidian doesn't even specify a piston behavior at all,
			//so copying its properties won't automatically make it unpushable.
			.pistonBehavior(PistonBehavior.BLOCK)
		)
	);
	@UseDataGen(void.class)
	public static final Block CAST_IRON_BLOCK = register(
		new Block(
			copySettings(Blocks.IRON_BLOCK, "cast_iron_block")
		)
	);
	@UseDataGen(void.class)
	public static final Block STEEL_BLOCK = register(
		new Block(
			copySettings(Blocks.IRON_BLOCK, "steel_block")
			.mapColor(MapColor.GRAY)
		)
	);
	@UseDataGen(void.class)
	public static final Block SILVER_ORE = register(
		new Block(
			copySettings(Blocks.GOLD_ORE, "silver_ore")
		)
	);
	@UseDataGen(void.class)
	public static final Block DEEPSLATE_SILVER_ORE = register(
		new Block(
			copySettings(Blocks.DEEPSLATE_GOLD_ORE, "deepslate_silver_ore")
		)
	);
	@UseDataGen(void.class)
	public static final Block RAW_SILVER_BLOCK = register(
		new Block(
			copySettings(Blocks.RAW_GOLD_BLOCK, "raw_silver_block")
			.mapColor(MapColor.LIGHT_BLUE_GRAY)
		)
	);
	@UseDataGen(void.class)
	public static final Block SILVER_BLOCK = register(
		new Block(
			copySettings(Blocks.GOLD_BLOCK, "silver_block")
			.mapColor(MapColor.LIGHT_BLUE_GRAY)
		)
	);
	@UseDataGen(void.class)
	public static final Block ELECTRUM_BLOCK = register(
		new Block(
			copySettings(Blocks.GOLD_BLOCK, "electrum_block")
			.mapColor(MapColor.PALE_YELLOW)
		)
	);

	public static void init() {}
}