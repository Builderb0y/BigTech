package builderb0y.bigtech.codecs;

import java.lang.StackWalker.Option;
import java.lang.reflect.Method;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryElementCodec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.registry.tag.TagKey;

import builderb0y.autocodec.AutoCodec;
import builderb0y.autocodec.coders.AutoCoder;
import builderb0y.autocodec.decoders.AutoDecoder;
import builderb0y.autocodec.decoders.DecoderFactoryList;
import builderb0y.autocodec.decoders.LookupDecoderFactory;
import builderb0y.autocodec.encoders.AutoEncoder;
import builderb0y.autocodec.encoders.EncoderFactoryList;
import builderb0y.autocodec.encoders.LookupEncoderFactory;
import builderb0y.autocodec.reflection.PseudoField;
import builderb0y.autocodec.reflection.ReflectionManager;
import builderb0y.autocodec.reflection.memberViews.FieldLikeMemberView;
import builderb0y.autocodec.reflection.memberViews.PseudoFieldView;
import builderb0y.autocodec.reflection.reification.ReifiedType;
import builderb0y.autocodec.util.AutoCodecUtil;

public class BigTechAutoCodec {

	public static final StackWalker STACK_WALKER = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);

	public static final AutoCodec AUTO_CODEC = new AutoCodec() {

		@Override
		public @NotNull DecoderFactoryList createDecoders() {
			return new DecoderFactoryList(this) {

				@Override
				public @NotNull AutoDecoder.DecoderFactory createLookupFactory() {
					return new LookupDecoderFactory() {

						@Override
						public void setup() {
							super.setup();
							this.addRaw(AbstractBlock.Settings.class, autoCodec.wrapDFUCodec(AbstractBlock.Settings.CODEC, false, false));
							this.addRaw(Block.class, autoCodec.wrapDFUCodec(Registries.BLOCK.getCodec(), false, false));
							this.addRaw(Item.class, autoCodec.wrapDFUCodec(Registries.ITEM.getCodec(), false, false));
							this.addRaw(ItemStack.class, autoCodec.wrapDFUCodec(ItemStack.CODEC, false, false));
							this.addRaw(Ingredient.class, autoCodec.wrapDFUCodec(Ingredient.DISALLOW_EMPTY_CODEC, false, false));
							this.addRaw(NbtCompound.class, NbtAutoCoder.INSTANCE);
							this.addGeneric(ReifiedType.parameterize(TagKey.class, ReifiedType.from(Block.class)), autoCodec.wrapDFUCodec(TagKey.codec(RegistryKeys.BLOCK), false, false));
							this.addGeneric(ReifiedType.parameterize(TagKey.class, ReifiedType.from(Item.class)), autoCodec.wrapDFUCodec(TagKey.codec(RegistryKeys.ITEM), false, false));
							this.addGeneric(ReifiedType.parameterize(RegistryEntry.class, ReifiedType.from(Block.class)), autoCodec.wrapDFUCodec(RegistryFixedCodec.of(RegistryKeys.BLOCK), false, false));
							this.addGeneric(ReifiedType.parameterize(RegistryEntry.class, ReifiedType.from(Item.class)), autoCodec.wrapDFUCodec(RegistryFixedCodec.of(RegistryKeys.ITEM), false, false));
							this.addRaw(ComponentChanges.class, autoCodec.wrapDFUCodec(ComponentChanges.CODEC, false, false));
						}
					};
				}
			};
		}

		@Override
		public @NotNull EncoderFactoryList createEncoders() {
			return new EncoderFactoryList(this) {

				@Override
				public @NotNull AutoEncoder.EncoderFactory createLookupFactory() {
					return new LookupEncoderFactory() {

						@Override
						public void setup() {
							super.setup();
							this.addRaw(AbstractBlock.Settings.class, autoCodec.wrapDFUCodec(AbstractBlock.Settings.CODEC, false, false));
							this.addRaw(Block.class, autoCodec.wrapDFUCodec(Registries.BLOCK.getCodec(), false, false));
							this.addRaw(Item.class, autoCodec.wrapDFUCodec(Registries.ITEM.getCodec(), false, false));
							this.addRaw(ItemStack.class, autoCodec.wrapDFUCodec(ItemStack.CODEC, false, false));
							this.addRaw(Ingredient.class, autoCodec.wrapDFUCodec(Ingredient.DISALLOW_EMPTY_CODEC, false, false));
							this.addRaw(NbtCompound.class, NbtAutoCoder.INSTANCE);
							this.addGeneric(ReifiedType.parameterize(TagKey.class, ReifiedType.from(Block.class)), autoCodec.wrapDFUCodec(TagKey.codec(RegistryKeys.BLOCK), false, false));
							this.addGeneric(ReifiedType.parameterize(TagKey.class, ReifiedType.from(Item.class)), autoCodec.wrapDFUCodec(TagKey.codec(RegistryKeys.ITEM), false, false));
							this.addGeneric(ReifiedType.parameterize(RegistryEntry.class, ReifiedType.from(Block.class)), autoCodec.wrapDFUCodec(RegistryFixedCodec.of(RegistryKeys.BLOCK), false, false));
							this.addGeneric(ReifiedType.parameterize(RegistryEntry.class, ReifiedType.from(Item.class)), autoCodec.wrapDFUCodec(RegistryFixedCodec.of(RegistryKeys.ITEM), false, false));
							this.addRaw(ComponentChanges.class, autoCodec.wrapDFUCodec(ComponentChanges.CODEC, false, false));
						}
					};
				}
			};
		}

		@Override
		public @NotNull ReflectionManager createReflectionManager() {
			return new ReflectionManager() {

				public static final Method BLOCK_SETTINGS_GETTER;

				static {
					try {
						BLOCK_SETTINGS_GETTER = AbstractBlock.class.getDeclaredMethod(
							FabricLoader
							.getInstance()
							.getMappingResolver()
							.mapMethodName(
								"intermediary",
								"net.minecraft.class_4970",
								"method_54095",
								"()Lnet/minecraft/class_4970\$class_2251;"
							)
						);
					}
					catch (NoSuchMethodException exception) {
						throw AutoCodecUtil.rethrow(exception);
					}
				}

				@Override
				public @NotNull <T_Owner> ClassCache<T_Owner> createClassCache(@NotNull Class<T_Owner> owner) {
					ClassCache<T_Owner> cache = super.createClassCache(owner);
					if (owner == BLOCK_SETTINGS_GETTER.getDeclaringClass()) {
						cache.methods = new Method[] { BLOCK_SETTINGS_GETTER };
					}
					return cache;
				}

				@Override
				public @NotNull <T_Owner> TypeCache<T_Owner> createTypeCache(@NotNull ReifiedType<T_Owner> owner) {
					TypeCache<T_Owner> cache = super.createTypeCache(owner);
					if (owner.getRawClass() == BLOCK_SETTINGS_GETTER.getDeclaringClass()) {
						cache.fields = new FieldLikeMemberView[] {
							new PseudoFieldView<>(owner, new PseudoField(owner.getRawClass(), "settings", BLOCK_SETTINGS_GETTER, null))
						};
					}
					return cache;
				}

				@Override
				public boolean canView(@NotNull Class<?> clazz) {
					return super.canView(clazz) && clazz.getName().startsWith("builderb0y.bigtech.");
				}
			};
		}

		/*
		@Override
		public @NotNull TaskLogger createFactoryLogger(@NotNull ReentrantLock lock) {
			return new IndentedTaskLogger(lock, Printer.SYSTEM, true);
		}
		//*/
	};

	@SuppressWarnings("unchecked")
	public static <T> AutoCoder<T> callerCoder() {
		return AUTO_CODEC.createCoder((Class<T>)(STACK_WALKER.getCallerClass()));
	}

	@SuppressWarnings("unchecked")
	public static <T> Codec<T> callerCodec() {
		return AUTO_CODEC.createDFUCodec((Class<T>)(STACK_WALKER.getCallerClass()));
	}

	@SuppressWarnings("unchecked")
	public static <T> MapCodec<T> callerMapCodec() {
		return AUTO_CODEC.createDFUMapCodec((Class<T>)(STACK_WALKER.getCallerClass()));
	}
}