package builderb0y.bigtech.mixins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import builderb0y.bigtech.asm.BeaconBlockEntityASM;

public class BigTechMixinPlugin implements IMixinConfigPlugin {

	public static final Logger
		MIXIN_LOGGER = LoggerFactory.getLogger("Big Tech/Mixins"),
		ASM_LOGGER   = LoggerFactory.getLogger("Big Tech/ASM");

	public Map<String, Boolean> defaults, settings;

	@Override
	public void onLoad(String mixinPackage) {
		this.defaults = this.initDefaults(mixinPackage);
		this.settings = this.convertProperties(this.loadProperties());
		this.checkChanged();
	}

	public Map<String, Boolean> initDefaults(String mixinPackage) {
		Map<String, Boolean> defaults = new HashMap<>(32);
		defaults.put(mixinPackage + ".BeaconBlockEntity_UseColorProviders",                              Boolean.TRUE);
		defaults.put(mixinPackage + ".ChainRestrictedNeighborUpdater_UpdateDiagonalForCatwalkPlatforms", Boolean.TRUE);
		defaults.put(mixinPackage + ".ChestBlock_CreateMiner",                                           Boolean.TRUE);
		defaults.put(mixinPackage + ".ChunkBlockLightProvider_AllowBeamsToEmitLight",                    Boolean.TRUE);
		defaults.put(mixinPackage + ".Entity_CollideWithBeams",                                          Boolean.TRUE);
		defaults.put(mixinPackage + ".ExperienceOrbEntity_MakeMagnetiteAttractable",                              Boolean.TRUE);
		defaults.put(mixinPackage + ".ItemEntity_DontMergeDuringTransport",                              Boolean.TRUE);
		defaults.put(mixinPackage + ".ItemEntity_MakeMagnetiteAttractable",                                       Boolean.TRUE);
		defaults.put(mixinPackage + ".LadderBlock_MakePlaceableOnFrames",                                Boolean.TRUE);
		defaults.put(mixinPackage + ".LightningEntity_TweakLogic",                                       Boolean.TRUE);
		defaults.put(mixinPackage + ".LightningRod_EmitLightningPulse",                                  Boolean.TRUE);
		defaults.put(mixinPackage + ".LivingEntity_AttractStuffWhenWearingMagnetiteArmor",               Boolean.TRUE);
		defaults.put(mixinPackage + ".LivingEntity_MakePlayersNotClimbWhileFlying",                      Boolean.TRUE);
		defaults.put(mixinPackage + ".PistonHandler_AdvancedStickyBlocks",                               Boolean.TRUE);
		defaults.put(mixinPackage + ".ProjectileEntity_MakeMagnetiteAttractable",                                 Boolean.TRUE);
		defaults.put(mixinPackage + ".RedstoneWireBlock_ConnectToCorrectSideOfEncasedRedstoneBlock",     Boolean.TRUE);
		defaults.put(mixinPackage + ".ServerWorld_SilentBlocks",                                         Boolean.TRUE);
		defaults.put(mixinPackage + ".ShearsItem_FastBreakingTag",                                       Boolean.TRUE);
		defaults.put(mixinPackage + ".WorldChunk_NotifyBeamsOfBlockChange",                              Boolean.TRUE);
		return defaults;
	}

	//copy-pasted from my other mod, big globe.
	public Properties loadProperties() {
		Path bigGlobeConfigFolder = FabricLoader.getInstance().getConfigDir().resolve("bigtech");
		Path path = bigGlobeConfigFolder.resolve("mixins.properties");
		Path tmp  = bigGlobeConfigFolder.resolve("mixins.tmp");
		Properties properties = new Properties();
		if (Files.exists(path)) try {
			//file exists, so try loading it.
			try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
				properties.load(reader);
			}
			//ensure that the loaded properties file
			//contains ONLY //keys that are in our defaults.
			//we don't want users to be able to toggle
			//options that we don't intentionally expose.
			int oldSize = properties.size();
			properties.keySet().retainAll(this.defaults.keySet());
			int newSize = properties.size();
			boolean changed = newSize != oldSize;

			//add any missing options.
			if (newSize != this.defaults.size()) {
				for (Map.Entry<String, Boolean> entry : this.defaults.entrySet()) {
					properties.putIfAbsent(entry.getKey(), entry.getValue().toString());
				}
				changed = true;
			}

			//if the properties changed as a result of retaining
			//or adding missing options, save it again.
			if (changed) {
				this.saveProperties(properties, path, tmp);
			}
		}
		catch (IOException exception) {
			MIXIN_LOGGER.error("", exception);

			//if we were successful in loading some entries,
			//but not others, then we won't've done retaining,
			//and therefore these entries should not be trusted.
			if (!properties.isEmpty()) properties.clear();

			//if any error occurred while loading the file, use defaults.
			for (Map.Entry<String, Boolean> entry : this.defaults.entrySet()) {
				properties.setProperty(entry.getKey(), entry.getValue().toString());
			}

			//don't save the properties file, because we don't want
			//to overwrite user options when they are malformed.
		}
		else {
			//if the file does not exist, use defaults.
			for (Map.Entry<String, Boolean> entry : this.defaults.entrySet()) {
				properties.setProperty(entry.getKey(), entry.getValue().toString());
			}

			//and also save the defaults.
			this.saveProperties(properties, path, tmp);
		}
		return properties;
	}

	public void saveProperties(Properties properties, Path path, Path tmp) {
		try {
			Files.createDirectories(path.getParent());
			try (BufferedWriter writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
				properties.store(writer, null);
			}
			Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public Map<String, Boolean> convertProperties(Properties properties) {
		Map<String, Boolean> map = new HashMap<>(properties.size());
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			if ("true".equalsIgnoreCase(entry.getValue().toString())) {
				map.put(entry.getKey().toString(), Boolean.TRUE);
			}
			else if ("false".equalsIgnoreCase(entry.getValue().toString())) {
				map.put(entry.getKey().toString(), Boolean.FALSE);
			}
			else {
				MIXIN_LOGGER.warn(".minecraft/config/bigtech/mixins.properties has an invalid key " + entry.getKey() + " = " + entry.getValue() + "; expected true or false.");
			}
		}
		return map;
	}

	public void checkChanged() {
		for (Map.Entry<String, Boolean> entry : this.defaults.entrySet()) {
			Boolean enabled = this.settings.get(entry.getKey());
			if (entry.getValue() != enabled) {
				MIXIN_LOGGER.info(entry.getKey() + " has been changed from its default value: " + entry.getValue() + " -> " + enabled);
			}
		}
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	public boolean isEnabledInConfig(String mixinClassName) {
		Boolean enabled = this.settings.get(mixinClassName);
		return enabled != null ? enabled.booleanValue() : true;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return switch (mixinClassName) {
			case "builderb0y.bigtech.mixins.BigTechConfig_ImplementConfigData" -> {
				yield FabricLoader.getInstance().isModLoaded("cloth-config");
			}
			default -> {
				yield this.isEnabledInConfig(mixinClassName);
			}
		};
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

	@Override
	public @Nullable List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		switch (mixinClassName) {
			//now replaced with mixin extras.
			//case "builderb0y.bigtech.mixins.PistonHandler_AddNecessaryMethods" -> PistonHandlerASM.transform(targetClass);
			case "builderb0y.bigtech.mixins.BeaconBlockEntity_UseColorProviders" -> {
				if (this.isEnabledInConfig(mixinClassName)) {
					BeaconBlockEntityASM.transform(targetClass);
				}
			}
			default -> {}
		}
	}
}