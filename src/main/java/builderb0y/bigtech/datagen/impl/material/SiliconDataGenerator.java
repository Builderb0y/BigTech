package builderb0y.bigtech.datagen.impl.material;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.items.BigTechItemTags;

public class SiliconDataGenerator extends BasicItemDataGenerator {

	public SiliconDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.SILICON).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(context.suffixPath(this.getId(), "_stonecutting")),
			//language=json
			"""
			{
				"type": "minecraft:stonecutting",
				"ingredient": "bigtech:silicon_block",
				"result": {
					"id": "bigtech:silicon",
					"count": 16
				}
			}
			"""
		);
	}
}