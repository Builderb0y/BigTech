package builderb0y.bigtech.datagen.impl;

import java.util.Map;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.LocalizedDataGenerator;

public abstract class DamageTypeDataGenerator implements LocalizedDataGenerator {

	public final RegistryKey<DamageType> key;
	public final TagKey<DamageType>[] tags;

	@SafeVarargs
	public DamageTypeDataGenerator(RegistryKey<DamageType> key, TagKey<DamageType>... tags) {
		this.key = key;
		this.tags = tags;
	}

	@Override
	public Identifier getId() {
		return this.key.getValue();
	}

	@Override
	public String getLangKey(DataGenContext context) {
		return "death.attack.${this.getId().getNamespace()}.${this.getId().getPath()}";
	}

	@Override
	public void run(DataGenContext context) {
		LocalizedDataGenerator.super.run(context);
		context.writeToFile(
			context.genericDataPath(this.getId(), "damage_type"),
			context.replace(
				//language=json
				"""
				{
					"exhaustion": 0.1,
					"message_id": "%MESSAGE",
					"scaling": "when_caused_by_living_non_player"
				}""",
				Map.of("MESSAGE", "${this.getId().getNamespace()}.${this.getId().getPath()}")
			)
		);
		for (TagKey<DamageType> tag : this.tags) {
			context.getTags(tag).addElement(this.getId());
		}
	}

	public static class Shocking extends DamageTypeDataGenerator {

		public Shocking(RegistryKey<DamageType> key) {
			super(key, DamageTypeTags.BYPASSES_ARMOR);
		}

		@Override
		public String getLangValue(DataGenContext context) {
			return "%1\$s got electrocuted";
		}
	}

	public static class TeslaCoil extends DamageTypeDataGenerator {

		public TeslaCoil(RegistryKey<DamageType> key) {
			super(key, DamageTypeTags.BYPASSES_ARMOR);
		}

		@Override
		public String getLangValue(DataGenContext context) {
			return "%1\$s stood too close to a tesla coil";
		}
	}

	public static class Ignitor extends DamageTypeDataGenerator {

		public Ignitor(RegistryKey<DamageType> key) {
			super(key, DamageTypeTags.IS_FIRE);
		}

		@Override
		public String getLangValue(DataGenContext context) {
			return "%1\$s got ignited";
		}
	}
}