package builderb0y.bigtech.datagen.formats;

import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.datagen.formats.TableFormats.KeyedRecipeIngredient;
import builderb0y.bigtech.datagen.tables.Table;

public class ShapedRecipeBuilder {

	public CraftingRecipeCategory category;
	public String group;
	public String[] pattern;
	public Char2ObjectMap<String> ingredients = new Char2ObjectArrayMap<>(6);
	public String result;
	public int count = 1;

	public ShapedRecipeBuilder category(CraftingRecipeCategory category) {
		this.category = category;
		return this;
	}

	public ShapedRecipeBuilder group(String group) {
		this.group = group;
		return this;
	}

	public ShapedRecipeBuilder group(Identifier group) {
		return this.group(group.toString());
	}

	public ShapedRecipeBuilder pattern(String... pattern) {
		this.pattern = pattern;
		return this;
	}

	public ShapedRecipeBuilder where(char key, String itemOrTag) {
		this.ingredients.put(key, itemOrTag);
		return this;
	}

	public ShapedRecipeBuilder where(char key, TagOrItem tagOrItem) {
		return this.where(key, tagOrItem.toString());
	}

	public ShapedRecipeBuilder whereItem(char key, Identifier item) {
		this.ingredients.put(key, item.toString());
		return this;
	}

	public ShapedRecipeBuilder where(char key, ItemConvertible item) {
		return this.whereItem(key, Registries.ITEM.getId(item.asItem()));
	}

	public ShapedRecipeBuilder whereTag(char key, Identifier tag) {
		this.ingredients.put(key, "#" + tag);
		return this;
	}

	public ShapedRecipeBuilder where(char key, TagKey<Item> tag) {
		return this.whereTag(key, tag.id);
	}

	public ShapedRecipeBuilder result(String result) {
		this.result = result;
		return this;
	}

	public ShapedRecipeBuilder result(Identifier result) {
		return this.result(result.toString());
	}

	public ShapedRecipeBuilder result(ItemConvertible item) {
		return this.result(Registries.ITEM.getId(item.asItem()));
	}

	public ShapedRecipeBuilder count(int count) {
		this.count = count;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(512);
		builder.append("{\n");
		builder.append("\t\"type\": \"minecraft:crafting_shaped\",\n");
		if (this.category != null) builder.append("\t\"category\": \"").append(this.category.asString()).append("\",\n");
		if (this.group != null) builder.append("\t\"group\": \"").append(this.group).append("\",\n");
		builder.append("\t\"pattern\": [\n");
		builder.append("\t\t\"").append(this.pattern[0]).append('"');
		for (int i = 1; i < this.pattern.length; i++) {
			builder.append(",\n\t\t\"").append(this.pattern[i]).append('"');
		}
		builder.append("\n\t],\n");
		builder.append("\t\"key\": {\n");
		Table<KeyedRecipeIngredient> table = new Table<>(KeyedRecipeIngredient.FORMAT);
		this.ingredients.char2ObjectEntrySet().stream().map(KeyedRecipeIngredient::create).forEachOrdered(table.rows::add);
		builder.append(table);
		builder.append("\n\t},\n");
		builder.append("\t\"result\": {\n");
		builder.append("\t\t\"item\": \"").append(this.result).append('"');
		if (this.count != 1) builder.append(",\n\t\t\"count\": ").append(this.count);
		builder.append("\n\t}\n");
		builder.append('}');
		return builder.toString();
	}
}