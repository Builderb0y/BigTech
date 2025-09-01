package builderb0y.bigtech.datagen.formats;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.TagOrItem;

public class ArcFurnaceRecipeBuilder {

	public List<TagOrItem>
		inputs = new ArrayList<>();
	public String
		slowCoolResult,
		fastCoolResult;
	public int
		slowCoolQuantity = 1,
		fastCoolQuantity = 1,
		energy,
		coolingRate;

	public ArcFurnaceRecipeBuilder input(TagOrItem input) {
		this.inputs.add(input);
		return this;
	}

	public ArcFurnaceRecipeBuilder input(String input) {
		this.inputs.add(new TagOrItem(input));
		return this;
	}

	public ArcFurnaceRecipeBuilder input(ItemConvertible item) {
		this.inputs.add(new TagOrItem(item));
		return this;
	}

	public ArcFurnaceRecipeBuilder itemInput(Identifier id) {
		this.inputs.add(new TagOrItem(id.toString()));
		return this;
	}

	public ArcFurnaceRecipeBuilder tagInput(Identifier tag) {
		this.inputs.add(new TagOrItem("#" + tag));
		return this;
	}

	public ArcFurnaceRecipeBuilder input(TagKey<Item> tag) {
		this.inputs.add(new TagOrItem(tag));
		return this;
	}

	public ArcFurnaceRecipeBuilder slowCoolResult(String result) {
		this.slowCoolResult = result;
		return this;
	}

	public ArcFurnaceRecipeBuilder slowCoolResult(Identifier identifier) {
		this.slowCoolResult = identifier.toString();
		return this;
	}

	public ArcFurnaceRecipeBuilder slowCoolResult(ItemConvertible item) {
		this.slowCoolResult = Registries.ITEM.getId(item.asItem()).toString();
		return this;
	}

	public ArcFurnaceRecipeBuilder fastCoolResult(String result) {
		this.fastCoolResult = result;
		return this;
	}

	public ArcFurnaceRecipeBuilder fastCoolResult(Identifier identifier) {
		this.fastCoolResult = identifier.toString();
		return this;
	}

	public ArcFurnaceRecipeBuilder fastCoolResult(ItemConvertible item) {
		this.fastCoolResult = Registries.ITEM.getId(item.asItem()).toString();
		return this;
	}

	public ArcFurnaceRecipeBuilder slowCoolQuantity(int slowCoolQuantity) {
		this.slowCoolQuantity = slowCoolQuantity;
		return this;
	}

	public ArcFurnaceRecipeBuilder fastCoolQuantity(int fastCoolQuantity) {
		this.fastCoolQuantity = fastCoolQuantity;
		return this;
	}

	public ArcFurnaceRecipeBuilder energy(int energy) {
		this.energy = energy;
		return this;
	}

	public ArcFurnaceRecipeBuilder coolingRate(int coolingRate) {
		this.coolingRate = coolingRate;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(512).append("{\n");
		builder.append("\t\"type\": \"bigtech:arc_furnace\",\n");
		builder.append("\t\"inputs\": [");
		switch (this.inputs.size()) {
			case 0 -> throw new IllegalStateException("No inputs");
			case 1 -> builder.append(" \"").append(this.inputs.get(0)).append("\" ],\n");
			default -> {
				builder.append("\n\t\t\"").append(this.inputs.get(0)).append('"');
				for (int index = 1; index < this.inputs.size(); index++) {
					builder.append(",\n\t\t\"").append(this.inputs.get(index)).append('"');
				}
				builder.append("\n\t],\n");
			}
		}
		if (this.slowCoolResult == null) throw new IllegalStateException("No slow cool result");
		builder.append("\t\"slow_cool_result\": { \"id\": \"").append(this.slowCoolResult).append('"');
		if (this.slowCoolQuantity != 1) builder.append(", \"count\": ").append(this.slowCoolQuantity);
		builder.append(" },\n");
		if (this.fastCoolResult != null) {
			builder.append("\t\"fast_cool_result\": { \"id\": \"").append(this.fastCoolResult).append('"');
			if (this.fastCoolQuantity != 1) builder.append(", \"count\": ").append(this.fastCoolQuantity);
			builder.append(" },\n");
		}
		builder.append("\t\"energy\": ").append(this.energy).append(",\n");
		builder.append("\t\"cooling_rate\": ").append(this.coolingRate).append('\n');
		builder.append('}');
		return builder.toString();
	}
}