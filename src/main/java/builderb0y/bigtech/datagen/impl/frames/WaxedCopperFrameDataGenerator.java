package builderb0y.bigtech.datagen.impl.frames;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapelessRecipeBuilder;

public class WaxedCopperFrameDataGenerator extends MetalFrameDataGenerator {

	public final Item unwaxedVariant;

	public WaxedCopperFrameDataGenerator(BlockItem blockItem) {
		super(blockItem);
		this.unwaxedVariant = Registries.ITEM.get(BigTechMod.modID(this.id.path.substring("waxed_".length())));
	}

	@Override
	public Identifier getBaseTexture() {
		return Registries.ITEM.getId(this.unwaxedVariant);
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapelessRecipeBuilder()
			.group(BigTechMod.modID("frames"))
			.itemIngredient(this.unwaxedVariant)
			.itemIngredient(Items.HONEYCOMB)
			.result(this.item)
			.toString()
		);
	}
}