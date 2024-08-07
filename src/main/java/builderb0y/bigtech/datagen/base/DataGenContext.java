package builderb0y.bigtech.datagen.base;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.apache.commons.io.FileUtils;

import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.tag.TagManagerLoader;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.registrableCollections.RegistrableCollection;
import builderb0y.bigtech.registrableCollections.RegistrableCollection.RegistrableVariant;

public class DataGenContext {

	public final List<DataGenerator> generators;
	public final Set<Class<? extends DataGenerator>> dependencies;
	public final Map<TagKey<?>, TagBuilder> tags;
	public final Map<String, String> lang;
	public boolean errored;

	public DataGenContext() {
		if (!DataGen.isEnabled()) {
			throw new IllegalStateException("DataGen not enabled.");
		}
		this.generators = new ArrayList<>(256);
		this.dependencies = new HashSet<>(16);
		this.tags = new IdentityHashMap<>(64);
		this.lang = new TreeMap<>();
	}

	public void collectGenerators(Class<?> registryClass, Class<?> baseType) {
		for (Field field : registryClass.getDeclaredFields()) {
			if ((field.modifiers & (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)) == (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)) {
				if (baseType.isAssignableFrom(field.type)) {
					UseDataGen annotation = field.getDeclaredAnnotation(UseDataGen.class);
					if (annotation == null) {
						this.error("Missing data generator for ${field.type} ${registryClass.getSimpleName()}.${field.name}");
						continue;
					}
					Class<?> dataGenClass = annotation.value();
					if (dataGenClass == void.class) continue;
					Constructor<?>[] constructors = dataGenClass.getDeclaredConstructors();
					if (constructors.length != 1) {
						this.error("${dataGenClass} does not have exactly one constructor! For field ${field.type} ${registryClass.getSimpleName()}.${field.name}");
						continue;
					}
					if (constructors[0].parameterCount != 1) {
						this.error("${dataGenClass}'s constructor does not take exactly 1 argument! For field ${field.type} ${registryClass.getSimpleName()}.${field.name}");
						continue;
					}
					DataGenerator generator;
					try {
						generator = constructors[0].newInstance(field.get(null)).as();
					}
					catch (Exception exception) {
						this.error(exception);
						continue;
					}
					this.addWithDependencies(dataGenClass.asSubclass(DataGenerator.class), generator);
				}
				else if (RegistrableCollection.class.isAssignableFrom(field.type)) {
					UseDataGen annotation = field.getDeclaredAnnotation(UseDataGen.class);
					if (annotation == null) {
						this.error("Missing data generator for ${field.type} ${registryClass.getSimpleName()}.${field.name}");
						continue;
					}
					Class<?> dataGenClass = annotation.value();
					if (dataGenClass == void.class) continue;
					Constructor<?>[] constructors = dataGenClass.getDeclaredConstructors();
					if (constructors.length != 1) {
						this.error("${dataGenClass} does not have exactly one constructor! For field ${field.type} ${registryClass.getSimpleName()}.${field.name}");
						continue;
					}
					if (constructors[0].parameterCount != 2) {
						this.error("${dataGenClass}'s constructor does not take exactly 2 arguments! For field ${field.type} ${registryClass.getSimpleName()}.${field.name}");
						continue;
					}
					try {
						for (RegistrableVariant<?> variant : ((RegistrableCollection<?>)(field.get(null))).getRegistrableVariants()) {
							this.addWithDependencies(
								dataGenClass.asSubclass(DataGenerator.class),
								constructors[0].newInstance(variant.object, variant.variant).as()
							);
						}
					}
					catch (Exception exception) {
						this.error(exception);
					}
				}
			}
		}
	}

	public void addWithDependencies(Class<? extends DataGenerator> dataGenClass, DataGenerator generator) {
		this.generators.add(generator);
		Dependencies dependencies = dataGenClass.getAnnotation(Dependencies.class);
		if (dependencies != null) {
			for (Class<? extends DataGenerator> dependencyClass : dependencies.value()) {
				if (!this.dependencies.add(dependencyClass)) continue;
				Constructor<?>[] constructors = dependencyClass.getDeclaredConstructors();
				if (constructors.length != 1) {
					this.error("${dependencyClass} does not have exactly one constructor! From @Dependencies applied to ${dataGenClass}");
					continue;
				}
				if (constructors[0].parameterCount != 0) {
					this.error("${dependencyClass}'s constructor does not take exactly 0 arguments! From @Dependencies applied to ${dataGenClass}");
					continue;
				}
				DataGenerator dependency;
				try {
					dependency = constructors[0].newInstance((Object[])(null)).as();
				}
				catch (Exception exception) {
					this.error(exception);
					continue;
				}
				this.addWithDependencies(dependencyClass, dependency);
			}
		}
	}

	public TagBuilder getTags(TagKey<?> key) {
		return this.tags.computeIfAbsent(key, k -> new TagBuilder());
	}

	public String genericPath(String type, Identifier identifier, String subPath) {
		StringBuilder builder = new StringBuilder(type).append('/').append(identifier.namespace);
		if (subPath.charAt(0) != '/') builder.append('/');
		builder.append(subPath);
		if (subPath.charAt(subPath.length() - 1) != '/') builder.append('/');
		return builder.append(identifier.path).append(".json").toString();
	}

	public String genericAssetsPath(Identifier identifier, String subPath) {
		return this.genericPath("assets", identifier, subPath);
	}

	public String genericDataPath(Identifier identifier, String subPath) {
		return this.genericPath("data", identifier, subPath);
	}

	public String     blockstatePath(Identifier identifier) { return this.genericAssetsPath(identifier, "blockstates"       ); }
	public String     blockModelPath(Identifier identifier) { return this.genericAssetsPath(identifier, "models/block"      ); }
	public String      itemModelPath(Identifier identifier) { return this.genericAssetsPath(identifier, "models/item"       ); }
	public String       particlePath(Identifier identifier) { return this.genericAssetsPath(identifier, "particles"         ); }
	public String blockLootTablePath(Identifier identifier) { return this.genericDataPath  (identifier, "loot_tables/blocks"); }
	public String         recipePath(Identifier identifier) { return this.genericDataPath  (identifier, "recipes"           ); }
	public String       blockTagPath(Identifier identifier) { return this.genericDataPath  (identifier, "tags/blocks"       ); }
	public String        itemTagPath(Identifier identifier) { return this.genericDataPath  (identifier, "tags/items"        ); }
	public String            tagPath(TagKey<?>  key       ) { return this.genericDataPath  (key.id,     TagManagerLoader.getPath(key.registry)); }

	public void writeToFile(String path, String text) {
		byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
		for (File root : DataGen.ROOT_DIRECTORIES) {
			File file = new File(root, path.replace('/', File.separatorChar));
			if (file.exists()) {
				this.error(new IllegalStateException("Duplicate file: " + path + " -> " + file.getAbsolutePath()));
			}
			file.getParentFile().mkdirs();
			try (FileOutputStream writer = new FileOutputStream(file)) {
				writer.write(bytes);
			}
			catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}

	public Identifier prefixPath(String prefix, Identifier identifier) {
		if (prefix.isEmpty) return identifier;
		return new Identifier(identifier.namespace, prefix + identifier.path);
	}

	public Identifier suffixPath(Identifier identifier, String suffix) {
		if (suffix.isEmpty) return identifier;
		return new Identifier(identifier.namespace, identifier.path + suffix);
	}

	public Identifier prefixSuffixPath(String prefix, Identifier identifier, String suffix) {
		if (prefix.isEmpty && suffix.isEmpty) return identifier;
		return new Identifier(identifier.namespace, prefix + identifier.path + suffix);
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
		while (!builder.isEmpty && Character.isWhitespace(builder.charAt(builder.length() - 1))) {
			builder.length = builder.length() - 1;
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
		this.errored = true;
		DataGen.LOGGER.error("", throwable);
	}

	public void error(String message) {
		this.errored = true;
		DataGen.LOGGER.error(message);
	}
}