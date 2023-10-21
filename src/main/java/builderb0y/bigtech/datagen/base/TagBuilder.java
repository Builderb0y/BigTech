package builderb0y.bigtech.datagen.base;

import java.util.TreeSet;

import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class TagBuilder extends TreeSet<TagOrItem> {

	public void add(ItemConvertible item) {
		this.add(new TagOrItem(item));
	}

	public void add(TagKey<?> tag) {
		this.add(new TagOrItem(tag));
	}

	public void addElement(Identifier identifier) {
		this.add(new TagOrItem(identifier.toString()));
	}

	public void addTag(Identifier tag) {
		this.add(new TagOrItem("#" + tag));
	}
}