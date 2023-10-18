package builderb0y.bigtech.datagen.base;

import java.util.Map;

import net.minecraft.item.Item;

public abstract class BasicItemDataGenerator implements ItemDataGenerator {

	public final Item item;

	public BasicItemDataGenerator(Item item) {
		this.item = item;
	}

	@Override
	public Item getItem() {
		return this.item;
	}

	@Override
	public String getLangKey(DataGenContext context) {
		return this.item.getTranslationKey();
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return context.underscoresToCapitals(this.id.path);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(this.id),
			context.replace(
				"""
				{
					"parent": "minecraft:item/generated",
					"textures": {
						"layer0": "TEX"
					}
				}
				""",
				Map.of("TEX", this.id.toString())
			)
		);
	}
}