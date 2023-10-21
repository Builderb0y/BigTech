package builderb0y.bigtech.datagen.base;

import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public record TagOrItem(String asString) implements Comparable<TagOrItem> {

	public TagOrItem(ItemConvertible item) {
		this(Registries.ITEM.getId(item.asItem()).toString());
	}

	public TagOrItem(TagKey<?> tag) {
		this("#" + tag.id);
	}

	public boolean isTag() {
		return !this.asString.isEmpty && this.asString.charAt(0) == '#';
	}

	public boolean isItem() {
		return this.asString.isEmpty || this.asString.charAt(0) != '#';
	}

	public String id() {
		return this.isTag ? this.asString.substring(1) : this.asString;
	}

	@Override
	public int compareTo(TagOrItem that) {
		return this.asString.compareTo(that.asString);
	}

	@Override
	public String toString() {
		return this.asString;
	}
}