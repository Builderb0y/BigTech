package builderb0y.bigtech.datagen.impl.material.redstone;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ArcFurnaceRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class RedstoneAlloyIngotDataGenerator extends BasicItemDataGenerator {

	public RedstoneAlloyIngotDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.REDSTONE_ALLOY_INGOTS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_arc_furnace")),
			new ArcFurnaceRecipeBuilder()
			.input(ConventionalItemTags.COPPER_INGOTS)
			.input(ConventionalItemTags.REDSTONE_DUSTS)
			.slowCoolResult(this.getId())
			.energy(500)
			.coolingRate(10)
			.toString()
		);
	}
}