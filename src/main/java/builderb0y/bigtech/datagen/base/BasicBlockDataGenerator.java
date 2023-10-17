package builderb0y.bigtech.datagen.base;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import net.minecraft.item.BlockItem;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

public abstract class BasicBlockDataGenerator implements BlockItemDataGenerator {

	public final BlockItem blockItem;

	public BasicBlockDataGenerator(BlockItem blockItem) {
		this.blockItem = blockItem;
	}

	@Override
	public BlockItem getBlockItem() {
		return this.blockItem;
	}

	@Override
	public String getLangKey(DataGenContext context) {
		return this.blockItem.getTranslationKey();
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return context.underscoresToCapitals(this.getID().getPath());
	}

	public boolean hasMorePropertiesThan(Property<?> expected) {
		Collection<Property<?>> actual = this.getBlock().getStateManager().getProperties();
		return switch (actual.size()) {
			case 0 -> false;
			case 1 -> actual.iterator().next() != expected;
			default -> true;
		};
	}

	public boolean hasMorePropertiesThan(Property<?>... expected) {
		return switch (expected.length) {
			case 0 -> !this.getBlock().getStateManager().getProperties().isEmpty();
			case 1 -> this.hasMorePropertiesThan(expected[0]);
			default -> {
				Collection<Property<?>> actual = this.getBlock().getStateManager().getProperties();
				if (actual.size() > expected.length) yield true;
				actual = new HashSet<>(actual);
				for (Property<?> property : expected) {
					actual.remove(property);
				}
				yield !actual.isEmpty();
			}
		};
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		if (this.hasMorePropertiesThan(Properties.WATERLOGGED)) {
			context.error(new IllegalStateException("Should override writeBlockstateJson() to handle multiple states: " + this));
		}
		this.writeDefaultBlockstateJson(context, this.getID());
	}

	public void writeDefaultBlockstateJson(DataGenContext context, Identifier blockModel) {
		context.writeToFile(
			context.blockstatePath(this.getID()),
			context.replace(
				"""
				{
					"variants": {
						"": { "model": "%MODEL" }
					}
				}
				""",
				Map.of("MODEL", context.prefixPath("block/", blockModel).toString())
			)
		);
	}

	@Override
	public void writeLootTableJson(DataGenContext context) {
		context.writeToFile(
			context.blockLootTablePath(this.getID()),
			context.replace(
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
						}]
					}]
				}
				""",
				Map.of("BLOCK", this.getID().toString())
			)
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(this.getID()),
			context.replace(
				"""
				{
					"parent": "%PARENT"
				}
				""",
				Map.of("PARENT", context.prefixPath("block/", this.getID()).toString())
			)
		);
	}
}