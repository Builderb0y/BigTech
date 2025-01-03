package builderb0y.bigtech.datagen.base;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.component.ComponentType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public abstract class BasicBlockDataGenerator extends UnplaceableBlockDataGenerator implements BlockItemDataGenerator {

	public final BlockItem blockItem;

	public BasicBlockDataGenerator(BlockItem blockItem) {
		this.blockItem = blockItem;
	}

	@Override
	public BlockItem getBlockItem() {
		return this.blockItem;
	}

	public void writeBlockEntityComponentCopyingLootTableJson(DataGenContext context, ComponentType<?>... types) {
		context.writeToFile(
			context.blockLootTablePath(this.getId()),
			context.replace(
				//language=json
				"""
				{
					"type": "minecraft:block",
					"pools": [{
						"rolls": 1,
						"entries": [{
							"type": "minecraft:item",
							"name": "%BLOCK"
						}],
						"conditions": [{
							"condition": "minecraft:survives_explosion"
						}],
						"functions": [{
							"function": "minecraft:copy_components",
							"source": "block_entity",
							"include": [
								%COMPONENTS
							]
						}]
					}]
				}""",
				Map.of(
					"BLOCK",
					this.getId().toString(),

					"COMPONENTS",
					Arrays
					.stream(types)
					.map(Registries.DATA_COMPONENT_TYPE::getId)
					.map(Identifier::toString)
					.map((String s) -> '"' + s + '"')
					.collect(Collectors.joining(",\n\t\t\t\t"))
				)
			)
		);
	}

	public Identifier getItemModelParent(DataGenContext context) {
		return this.getId();
	}

	@Override
	public Identifier getDroppedItem() {
		return this.getId();
	}

	@Override
	public void writeItemDefinitions(DataGenContext context) {
		this.writeDefaultItemDefinition(context, context.prefixPath("block/", this.getItemModelParent(context)));
	}

	@Override
	public void writeItemModels(DataGenContext context) {}
}