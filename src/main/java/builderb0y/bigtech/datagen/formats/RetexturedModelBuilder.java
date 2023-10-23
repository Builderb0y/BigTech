package builderb0y.bigtech.datagen.formats;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.formats.TableFormats.RetextureEntry;
import builderb0y.bigtech.datagen.tables.Table;

public class RetexturedModelBuilder {

	public String parent;
	public Map<String, String> textures = new HashMap<>(4);
	public boolean jmxl;

	public RetexturedModelBuilder jmxl(boolean jmxl) {
		this.jmxl = jmxl;
		return this;
	}

	public RetexturedModelBuilder parent(String parent) {
		this.parent = parent;
		return this;
	}

	public RetexturedModelBuilder parent(Identifier parent) {
		return this.parent(parent.toString());
	}

	public RetexturedModelBuilder blockParent(Identifier parent) {
		return this.parent("${parent.namespace}:block/${parent.path}");
	}

	public RetexturedModelBuilder itemParent(Identifier parent) {
		return this.parent("${parent.namespace}:item/${parent.path}");
	}

	public RetexturedModelBuilder texture(String key, String texture) {
		this.textures.put(key, texture);
		return this;
	}

	public RetexturedModelBuilder texture(String key, Identifier texture) {
		return this.texture(key, texture.toString());
	}

	public RetexturedModelBuilder blockTexture(String key, Identifier texture) {
		return this.texture(key, "${texture.namespace}:block/${texture.path}");
	}

	public RetexturedModelBuilder itemTexture(String key, Identifier texture) {
		return this.texture(key, "${texture.namespace}:item/${texture.path}");
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(256);
		builder.append("{\n");
		if (this.jmxl) builder.append("\t\"jmxl\": true,\n");
		builder.append("\t\"parent\": \"").append(this.parent).append('"');
		if (!this.textures.isEmpty()) {
			builder.append(",\n\t\"textures\": {\n");
			Table<RetextureEntry> table = new Table<>(RetextureEntry.FORMAT);
			this.textures.entrySet().stream().map(RetextureEntry::new).forEachOrdered(table.rows::add);
			builder.append(table);
			builder.append("\n\t}");
		}
		builder.append("\n}");
		return builder.toString();
	}
}