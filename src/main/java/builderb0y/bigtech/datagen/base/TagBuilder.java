package builderb0y.bigtech.datagen.base;

import java.util.TreeSet;

import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class TagBuilder extends TreeSet<TagOrItem> {

	public void addAll(TagOrItem... tagsOrItems) {
		for (TagOrItem tagOrItem : tagsOrItems) {
			this.add(tagOrItem);
		}
	}

	public void add(RegistryEntry<?> entry) {
		this.add(new TagOrItem(entry));
	}

	public void addAll(RegistryEntry<?>... entries) {
		for (RegistryEntry<?> entry : entries) {
			this.add(new TagOrItem(entry));
		}
	}

	public void add(ItemConvertible item) {
		this.add(new TagOrItem(item));
	}

	public void addAll(ItemConvertible... items) {
		for (ItemConvertible item : items) {
			this.add(new TagOrItem(item));
		}
	}

	public void add(TagKey<?> tag) {
		this.add(new TagOrItem(tag));
	}

	public void addAll(TagKey<?>... tags) {
		for (TagKey<?> tag : tags) {
			this.add(new TagOrItem(tag));
		}
	}

	public void addElement(Identifier identifier) {
		this.add(new TagOrItem(identifier.toString()));
	}

	public void addTag(Identifier tag) {
		this.add(new TagOrItem("#" + tag));
	}
}