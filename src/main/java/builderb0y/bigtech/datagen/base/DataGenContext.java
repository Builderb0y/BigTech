package builderb0y.bigtech.datagen.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import net.minecraft.util.Identifier;

public class DataGenContext {

	public final List<DataGenerator> generators;

	public DataGenContext() {
		if (!DataGen.isEnabled()) {
			throw new IllegalStateException("DataGen not enabled.");
		}
		this.generators = new ArrayList<>(256);
	}

	public void collectGenerators(Class<?> registryClass, Class<?> baseType) {
		for (Field field : registryClass.getDeclaredFields()) {
			if ((field.getModifiers() & (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)) == (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL) && baseType.isAssignableFrom(field.getType())) {
				UseDataGen annotation = field.getDeclaredAnnotation(UseDataGen.class);
				if (annotation == null) {
					annotation = field.getType().getDeclaredAnnotation(UseDataGen.class);
					if (annotation == null) {
						this.error("Missing data generator for ${field.getType()} ${registryClass.getSimpleName()}.${field.getName()}");
						continue;
					}
				}
				Class<?> dataGenClass = annotation.value();
				Constructor<?>[] constructors = dataGenClass.getDeclaredConstructors();
				if (constructors.length != 1) {
					this.error("${dataGenClass} has more than one constructor! For field ${field.getType()} ${registryClass.getSimpleName()}.${field.getName()}");
					continue;
				}
				DataGenerator generator;
				try {
					generator = (DataGenerator)(constructors[0].newInstance(field.get(null)));
				}
				catch (Exception exception) {
					this.error(exception);
					continue;
				}
				this.generators.add(generator);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <D extends DataGenerator> Stream<D> getGenerators(Class<D> clazz) {
		return (Stream<D>)(this.generators.stream().filter(clazz::isInstance));
	}

	public Stream<BlockDataGenerator> blocks() {
		return this.getGenerators(BlockDataGenerator.class);
	}

	public Stream<ItemDataGenerator> items() {
		return this.getGenerators(ItemDataGenerator.class);
	}

	public Stream<BlockItemDataGenerator> blockItems() {
		return this.getGenerators(BlockItemDataGenerator.class);
	}

	public String     blockstatePath(Identifier identifier) { return "assets/${identifier.getNamespace()}/blockstates/${       identifier.getPath()}.json"; }
	public String     blockModelPath(Identifier identifier) { return "assets/${identifier.getNamespace()}/models/block/${      identifier.getPath()}.json"; }
	public String      itemModelPath(Identifier identifier) { return "assets/${identifier.getNamespace()}/models/item/${       identifier.getPath()}.json"; }
	public String blockLootTablePath(Identifier identifier) { return "data/${  identifier.getNamespace()}/loot_tables/blocks/${identifier.getPath()}.json"; }
	public String         recipePath(Identifier identifier) { return "data/${  identifier.getNamespace()}/recipes/${           identifier.getPath()}.json"; }
	public String       blockTagPath(Identifier identifier) { return "data/${  identifier.getNamespace()}/tags/blocks/${       identifier.getPath()}.json"; }
	public String        itemTagPath(Identifier identifier) { return "data/${  identifier.getNamespace()}/tags/items/${        identifier.getPath()}.json"; }

	public void writeToFile(String path, String text) {
		for (File root : DataGen.ROOT_DIRECTORIES) {
			File file = new File(root, path.replace('/', File.separatorChar));
			try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
				writer.write(text);
			}
			catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}

	public Identifier prefixPath(String prefix, Identifier identifier) {
		if (prefix.isEmpty()) return identifier;
		return new Identifier(identifier.getNamespace(), prefix + identifier.getPath());
	}

	public Identifier suffixPath(Identifier identifier, String suffix) {
		if (suffix.isEmpty()) return identifier;
		return new Identifier(identifier.getNamespace(), identifier.getPath() + suffix);
	}

	public Identifier prefixSuffixPath(String prefix, Identifier identifier, String suffix) {
		if (prefix.isEmpty() && suffix.isEmpty()) return identifier;
		return new Identifier(identifier.getNamespace(), prefix + identifier.getPath() + suffix);
	}

	public String replace(String text, Map<String, String> replacements) {
		int length = text.length();
		StringBuilder builder = new StringBuilder(length + 64);
		int varEnd = 0;
		while (true) {
			int varStart = text.indexOf('%', varEnd);
			if (varStart >= 0) {
				builder.append(text, varEnd, varStart);
				varEnd = varStart;
				for (char c; ++varEnd < length && (c = text.charAt(varEnd)) >= 'A' && c <= 'Z';);
				String var = text.substring(varStart + 1, varEnd);
				String replacement = replacements.get(var);
				if (replacement != null) {
					builder.append(replacement);
				}
				else {
					this.error(new IllegalArgumentException("No replacement found for $" + var));
					return text;
				}
			}
			else {
				builder.append(text, varEnd, length);
				break;
			}
		}
		//replace() is usually called before writeToFile(),
		//and since writeToFile() trims its text,
		//this can sometimes result in creating a string,
		//and then immediately truncating it,
		//which is another array copy operation.
		//so, trim early here to avoid that.
		while (!builder.isEmpty() && Character.isWhitespace(builder.charAt(builder.length() - 1))) {
			builder.setLength(builder.length() - 1);
		}
		return builder.toString();
	}
	public String underscoresToCapitals(String baseName) {
		StringBuilder formattedName = new StringBuilder(baseName.length());
		boolean uppercase = true;
		for (int i = 0, length = baseName.length(); i < length; i++) {
			char c = baseName.charAt(i);
			if (c == '_') {
				formattedName.append(' ');
				uppercase = true;
			}
			else if (uppercase) {
				formattedName.append(Character.toUpperCase(c));
				uppercase = false;
			}
			else {
				formattedName.append(Character.toLowerCase(c));
			}
		}
		return formattedName.toString();
	}

	public void copy(File from, String to) {
		for (File root : DataGen.ROOT_DIRECTORIES) {
			this.copy(from, new File(root, to.replace('/', File.separatorChar)));
		}
	}

	public void copy(File from, File to) {
		try {
			if (from.isFile()) {
				FileUtils.copyFile(from, to);
			}
			else if (from.isDirectory()) {
				FileUtils.copyDirectory(from, to);
			}
			else {
				this.error(new IOException("Not a file or directory: ${from.absolutePath}"));
			}
		}
		catch (Exception exception) {
			this.error(exception);
		}
	}

	public void delete(String path) {
		for (File root : DataGen.ROOT_DIRECTORIES) {
			this.delete(new File(root, path.replace('/', File.separatorChar)));
		}
	}

	public void empty(String path) {
		for (File root : DataGen.ROOT_DIRECTORIES) {
			this.delete(new File(root, path.replace('/', File.separatorChar)));
		}
	}

	public void delete(File file) {
		this.empty(file);
		if (!file.delete() && file.exists()) {
			throw new RuntimeException("Failed to delete file: " + file.getAbsolutePath());
		}
	}

	public void empty(File file) {
		File[] children = file.listFiles();
		if (children != null) {
			for (File child : children) {
				this.delete(child);
			}
		}
	}

	public void error(Throwable throwable) {
		DataGen.LOGGER.error("", throwable);
	}

	public void error(String message) {
		DataGen.LOGGER.error(message);
	}
}