package builderb0y.bigtech.datagen.formats;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.datagen.formats.TableFormats.UnkeyedRecipeIngredient;
import builderb0y.bigtech.datagen.tables.Table;

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
		this.ingredients.add(itemOrTag);
		return this;
	}

	public ShapelessRecipeBuilder ingredient(TagOrItem tagOrItem) {
		this.ingredients.add(tagOrItem.toString());
		return this;
	}

	public ShapelessRecipeBuilder itemIngredient(Identifier item) {
		this.ingredients.add(item.toString());
		return this;
	}

	public ShapelessRecipeBuilder ingredient(ItemConvertible item) {
		return this.itemIngredient(Registries.ITEM.getId(item.asItem()));
	}

	public ShapelessRecipeBuilder tagIngredient(Identifier tag) {
		this.ingredients.add("#" + tag);
		return this;
	}

	public ShapelessRecipeBuilder ingredient(TagKey<Item> tag) {
		return this.tagIngredient(tag.id());
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
		Table<UnkeyedRecipeIngredient> table = new Table<>(UnkeyedRecipeIngredient.FORMAT);
		this.ingredients.stream().map(UnkeyedRecipeIngredient::create).forEachOrdered(table.rows::add);
		builder.append(table);
		builder.append("\n\t],\n");
		builder.append("\t\"result\": {\n");
		builder.append("\t\t\"id\": \"").append(this.result).append('"');
		if (this.count != 1) builder.append(",\n\t\t\"count\": ").append(this.count);
		builder.append("\n\t}\n");
		builder.append('}');
		return builder.toString();
	}
}