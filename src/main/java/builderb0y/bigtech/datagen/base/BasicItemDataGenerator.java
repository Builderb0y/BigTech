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
		return this.item.getTranslationKey();
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return context.underscoresToCapitals(this.getId().getPath());
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(this.getId()),
			new Models.item.generated()
			.layer0(this.getId())
			.toString()
		);
	}

	@Override
	public void writeItemDefinitions(DataGenContext context) {
		this.writeDefaultItemDefinition(context, context.prefixPath("item/", this.getId()));
	}

	@Override
	public void genTextures(DataGenContext context) {}
}