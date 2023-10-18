package builderb0y.bigtech.datagen.formats;

import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ShapedRecipeBuilder {

	public String group;
	public String[] pattern;
	public Char2ObjectMap<String> ingredients = new Char2ObjectArrayMap<>(6);
	public String result;
	public int count = 1;

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

	public ShapedRecipeBuilder ingredient(char key, String itemOrTag) {
		if (itemOrTag.charAt(0) == '#') {
			this.ingredients.put(key, "\"tag\": \"${itemOrTag.substring(1)}\"");
		}
		else {
			this.ingredients.put(key, "\"item\": \"${itemOrTag}\"");
		}
		return this;
	}

	public ShapedRecipeBuilder itemIngredient(char key, Identifier item) {
		this.ingredients.put(key, "\"item\": \"${item}\"");
		return this;
	}

	public ShapedRecipeBuilder itemIngredient(char key, ItemConvertible item) {
		return this.itemIngredient(key, Registries.ITEM.getId(item.asItem()));
	}

	public ShapedRecipeBuilder tagIngredient(char key, Identifier tag) {
		this.ingredients.put(key, "\"tag\": \"${tag}\"");
		return this;
	}

	public ShapedRecipeBuilder tagIngredient(char key, TagKey<Item> tag) {
		return this.tagIngredient(key, tag.id);
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
		if (this.group != null) builder.append("\t\"group\": \"").append(this.group).append("\",\n");
		builder.append("\t\"pattern\": [\n");
		builder.append("\t\t\"").append(this.pattern[0]).append('"');
		for (int i = 1; i < this.pattern.length; i++) {
			builder.append(",\n\t\t\"").append(this.pattern[i]).append('"');
		}
		builder.append("\n\t],\n");
		builder.append("\t\"key\": {\n");
		ObjectIterator<Char2ObjectMap.Entry<String>> iterator = this.ingredients.char2ObjectEntrySet().iterator();
		Char2ObjectMap.Entry<String> entry = iterator.next();
		builder.append("\t\t\"").append(entry.getCharKey()).append("\": { ").append(entry.getValue()).append(" }");
		while (iterator.hasNext()) {
			entry = iterator.next();
			builder.append(",\n\t\t\"").append(entry.getCharKey()).append("\": { ").append(entry.getValue()).append(" }");
		}
		builder.append("\n\t},\n");
		builder.append("\t\"result\": {\n");
		builder.append("\t\t\"item\": \"").append(this.result).append('"');
		if (this.count != 1) builder.append(",\n\t\t\"count\": ").append(this.count);
		builder.append("\n\t}\n");
		builder.append('}');
		return builder.toString();
	}
}