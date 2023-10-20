package builderb0y.bigtech.datagen.formats;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ShapelessRecipeBuilder {

	public CraftingRecipeCategory category;
	public String group;
	public List<String> ingredients = new ArrayList<>(9);
	public String result;
	public int count = 1;

	public ShapelessRecipeBuilder category(CraftingRecipeCategory category) {
		this.category = category;
		return this;
	}

	public ShapelessRecipeBuilder group(String group) {
		this.group = group;
		return this;
	}

	public ShapelessRecipeBuilder group(Identifier group) {
		return this.group(group.toString());
	}

	public ShapelessRecipeBuilder ingredient(String itemOrTag) {
		if (itemOrTag.charAt(0) == '#') {
			this.ingredients.add("\"tag\": \"${itemOrTag.substring(1)}\"");
		}
		else {
			this.ingredients.add("\"item\": \"${itemOrTag}\"");
		}
		return this;
	}

	public ShapelessRecipeBuilder itemIngredient(Identifier item) {
		this.ingredients.add("\"item\": \"${item}\"");
		return this;
	}

	public ShapelessRecipeBuilder itemIngredient(ItemConvertible item) {
		return this.itemIngredient(Registries.ITEM.getId(item.asItem()));
	}

	public ShapelessRecipeBuilder tagIngredient(Identifier tag) {
		this.ingredients.add("\"tag\": \"${tag}\"");
		return this;
	}

	public ShapelessRecipeBuilder tagIngredient(TagKey<Item> tag) {
		return this.tagIngredient(tag.id);
	}

	public ShapelessRecipeBuilder result(String result) {
		this.result = result;
		return this;
	}

	public ShapelessRecipeBuilder result(Identifier result) {
		return this.result(result.toString());
	}

	public ShapelessRecipeBuilder result(ItemConvertible item) {
		return this.result(Registries.ITEM.getId(item.asItem()));
	}

	public ShapelessRecipeBuilder count(int count) {
		this.count = count;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(512);
		builder.append("{\n");
		builder.append("\t\"type\": \"minecraft:crafting_shapeless\",\n");
		if (this.category != null) builder.append("\t\"category\": \"").append(this.category.asString()).append("\",\n");
		if (this.group != null) builder.append("\t\"group\": \"").append(this.group).append("\",\n");
		builder.append("\t\"ingredients\": [\n");
		List<String> ingredients = this.ingredients;
		for (int index = 0, size = ingredients.size(); index < size; index++) {
			String ingredient = ingredients.get(index);
			builder.append("\t\t{ ").append(ingredient).append(" }");
			if (index != size - 1) builder.append(',');
			builder.append('\n');
		}
		builder.append("\t],\n");
		builder.append("\t\"result\": {\n");
		builder.append("\t\t\"item\": \"").append(this.result).append('"');
		if (this.count != 1) builder.append(",\n\t\t\"count\": ").append(this.count);
		builder.append("\n\t}\n");
		builder.append('}');
		return builder.toString();
	}
}