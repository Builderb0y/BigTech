package builderb0y.bigtech.datagen.base;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;

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
		return this.item.translationKey;
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return context.underscoresToCapitals(this.id.path);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(this.id),
			new RetexturedModelBuilder()
			.parent("minecraft:item/generated")
			.itemTexture("layer0", this.id)
			.toString()
		);
	}
}