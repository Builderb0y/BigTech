package builderb0y.bigtech.mixins;

import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import builderb0y.bigtech.asm.BeaconBlockEntityASM;
import builderb0y.bigtech.asm.PistonHandlerASM;

public class BigTechMixinPlugin implements IMixinConfigPlugin {

	public static final Logger
		MIXIN_LOGGER = LoggerFactory.getLogger("Big Tech/Mixins"),
		ASM_LOGGER   = LoggerFactory.getLogger("Big Tech/ASM");

	@Override
	public void onLoad(String mixinPackage) {}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
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
			case "builderb0y.bigtech.mixins.PistonHandler_AddNecessaryMethods" -> PistonHandlerASM.transform(targetClass);
			case "builderb0y.bigtech.mixins.BeaconBlockEntity_UseColorProviders" -> BeaconBlockEntityASM.transform(targetClass);
			default -> {}
		}
	}
}