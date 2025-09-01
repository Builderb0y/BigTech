package builderb0y.bigtech.datagen.impl.material;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.impl.functional.arcFurnace.ArcFurnaceRecipesDataGenerator;
import builderb0y.bigtech.items.MaterialItems;

@Dependencies(ArcFurnaceRecipesDataGenerator.class)
public class CastIronIngotDataGenerator extends BasicItemDataGenerator {

	public CastIronIngotDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(ConventionalItemTags.IRON_INGOTS).addElement(this.getId());
		context.getTags(ItemTags.BEACON_PAYMENT_ITEMS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("cast_iron_block_to_ingots")),
			new ShapedRecipeBuilder()
			.pattern("b")
			.whereItem('b', this.getId())
			.result(MaterialItems.CAST_IRON_INGOT)
			.count(9)
			.toString()
		);
	}
}