package builderb0y.bigtech.datagen.base;

import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public interface ItemDataGenerator extends LocalizedDataGenerator {

	public abstract Item getItem();

	@Override
	public default Identifier getId() {
		return Registries.ITEM.getId(this.getItem());
	}

	@Override
	public default void run(DataGenContext context) {
		LocalizedDataGenerator.super.run(context);

		this.writeItemDefinitions(context);
		this.writeItemModels(context);
		this.setupOtherItemTags(context);
		this.writeRecipes(context);
	}

	public abstract void writeItemDefinitions(DataGenContext context);

	public default void writeDefaultItemDefinition(DataGenContext context, Identifier identifier) {
		context.writeToFile(
			context.itemDefinitionPath(this.getId()),
			context.replace(
				//language=json
				"""
				{
					"model": {
						"type": "minecraft:model",
						"model": "%PATH"
					}
				}""",
				Map.of(
					"PATH", identifier.toString()
				)
			)
		);
	}

	public abstract void writeItemModels(DataGenContext context);

	public abstract void setupOtherItemTags(DataGenContext context);

	public abstract void writeRecipes(DataGenContext context);
}