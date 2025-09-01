package builderb0y.bigtech.datagen.impl.functional;

import java.util.Map;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.FunctionalBlocks;
import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.FunctionalItems;

@Dependencies(SteelPistonDataGenerator.BaseModel.class)
public class SteelPistonDataGenerator extends BasicBlockDataGenerator {

	public SteelPistonDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	public static class BaseModel implements DataGenerator {

		@Override
		public void run(DataGenContext context) {
			context.writeToFile(
				context.blockModelPath(BigTechMod.modID("steel_piston_base")),
				//language=json
				"""
				{
					"parent": "block/piston_extended",
					"textures": {
						"bottom": "bigtech:block/steel_piston_bottom",
						"side": "bigtech:block/steel_piston_side",
						"inside": "bigtech:block/steel_piston_inner"
					}
				}"""
			);
		}
	}

	public boolean isSticky() {
		return this.getBlock() == FunctionalBlocks.STICKY_STEEL_PISTON;
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		Direction facing = state.get(Properties.FACING);
		boolean extended = state.get(Properties.EXTENDED);
		return new BlockStateJsonVariant(
			state,
			extended
			? "bigtech:block/steel_piston_base"
			: this.isSticky()
			? "bigtech:block/sticky_steel_piston"
			: "bigtech:block/steel_piston",
			BlockStateJsonVariant.xFromHorizontal(facing),
			BlockStateJsonVariant.yFromNorth(facing)
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
			context.replace(
				//language=json
				"""
				{
					"parent": "minecraft:block/template_piston",
					"textures": {
						"bottom": "bigtech:block/steel_piston_bottom",
						"platform": "minecraft:block/%TOP",
						"side": "bigtech:block/steel_piston_side"
					}
				}""",
				Map.of("TOP", this.isSticky() ? "piston_top_sticky" : "piston_top")
			)
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_inventory")),
			context.replace(
				//language=json
				"""
				{
					"parent": "minecraft:block/cube_bottom_top",
					"textures": {
						"bottom": "bigtech:block/steel_piston_bottom",
						"side": "bigtech:block/steel_piston_side",
						"top": "minecraft:block/%TOP"
					}
				}""",
				Map.of("TOP", this.isSticky() ? "piston_top_sticky" : "piston_top")
			)
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.getId(), "_inventory");
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//wood is fine.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		if (this.isSticky()) {
			context.writeToFile(
				context.recipePath(this.getId()),
				new ShapedRecipeBuilder()
				.category(CraftingRecipeCategory.REDSTONE)
				.pattern("s", "p")
				.where('s', ConventionalItemTags.SLIME_BALLS)
				.where('p', FunctionalItems.STICKY_STEEL_PISTON)
				.result(this.getId())
				.toString()
			);
		}
		else {
			context.writeToFile(
				context.recipePath(this.getId()),
				new ShapedRecipeBuilder()
				.category(CraftingRecipeCategory.REDSTONE)
				.pattern("ppp", "dsd", "drd")
				.where('p', ItemTags.PLANKS)
				.where('d', Items.COBBLED_DEEPSLATE)
				.where('s', BigTechItemTags.STEEL_INGOTS)
				.where('r', ConventionalItemTags.REDSTONE_DUSTS)
				.result(this.getId())
				.toString()
			);
		}
	}
}