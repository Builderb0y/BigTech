package builderb0y.bigtech.datagen.impl.material.photovoltaic;

import net.minecraft.item.Item;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.impl.material.CommonFlatItemDataGenerator;

@Dependencies(CommonFlatItemDataGenerator.class)
public abstract class PhotovoltaicCellDataGenerator extends BasicItemDataGenerator {

	public PhotovoltaicCellDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(this.getId()),
			new RetexturedModelBuilder()
			.itemParent(BigTechMod.modID("photovoltaic_cell"))
			.itemTexture("top", this.getId())
			.itemTexture("back", BigTechMod.modID("photovoltaic_cell_back"))
			.itemTexture("particle", this.getId())
			.toString()
		);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}
}