package builderb0y.bigtech.datagen.base;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

public abstract class UnplaceableBlockDataGenerator implements BlockDataGenerator {

	public Identifier lazyId;

	@Override
	public Identifier getId() {
		return this.lazyId == null ? this.lazyId = BlockDataGenerator.super.getId() : this.lazyId;
	}

	@Override
	public String getLangKey(DataGenContext context) {
		return this.getBlock().getTranslationKey();
	}

	@Override
	public String getLangValue(DataGenContext context) {
		return context.underscoresToCapitals(this.getId().getPath());
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
			this.writeVariantBlockStateJson(context);
		}
		else {
			this.writeDefaultBlockstateJson(context, this.getId());
		}
	}

	public void writeDefaultBlockstateJson(DataGenContext context, Identifier blockModel) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			context.replace(
				//language=json
				"""
				{
					"variants": {
						"": { "model": "%MODEL" }
					}
				}""",
				Map.of("MODEL", context.prefixPath("block/", blockModel).toString())
			)
		);
	}

	public void writeVariantBlockStateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				BlockStateJsonVariant
				.streamStatesSorted(this.getBlock())
				.map((BlockState state) -> this.createVariant(context, state))
				::iterator
			)
			.toString()
		);
	}

	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		context.error(new UnsupportedOperationException("${this.getId()} (${this.getClass()}) has more properties than just waterlogged, and therefore should override createVariant(), but it does not."));
		return new BlockStateJsonVariant(state, context.prefixPath("block/", this.getId()).toString(), null, null);
	}

	public abstract Identifier getDroppedItem();

	@Override
	public void writeLootTableJson(DataGenContext context) {
		context.writeToFile(
			context.blockLootTablePath(this.getId()),
			this.getDroppedItem() != null
			? context.replace(
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
						}]
					}]
				}""",
				Map.of("BLOCK", this.getDroppedItem().toString())
			)
			: """
			{ "type": "minecraft:block" }"""
		);
	}

	@Override
	public void genTextures(DataGenContext context) {}
}