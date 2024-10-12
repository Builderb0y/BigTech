package builderb0y.bigtech.codecs;

import java.lang.StackWalker.Option;
import java.lang.reflect.Method;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.registry.tag.TagKey;

import builderb0y.autocodec.AutoCodec;
import builderb0y.autocodec.coders.AutoCoder;
import builderb0y.autocodec.coders.CoderFactoryList;
import builderb0y.autocodec.coders.LookupCoderFactory;
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
		@OverrideOnly
		public @NotNull CoderFactoryList createCoders() {
			return new CoderFactoryList(this) {

				@Override
				public @NotNull AutoCoder.CoderFactory createLookupFactory() {
					return new LookupCoderFactory() {

						@Override
						public void setup() {
							super.setup();
							this.addRaw(AbstractBlock.Settings.class, autoCodec.wrapDFUCodec(AbstractBlock.Settings.CODEC));
							this.addRaw(Block.class, autoCodec.wrapDFUCodec(Registries.BLOCK.getCodec()));
							this.addRaw(Item.class, autoCodec.wrapDFUCodec(Registries.ITEM.getCodec()));
							this.addRaw(ItemStack.class, autoCodec.wrapDFUCodec(ItemStack.CODEC));
							this.addRaw(Ingredient.class, autoCodec.wrapDFUCodec(Ingredient.DISALLOW_EMPTY_CODEC));
							this.addRaw(NbtCompound.class, NbtAutoCoder.INSTANCE);
							this.addGeneric(ReifiedType.parameterize(TagKey.class, ReifiedType.from(Block.class)), autoCodec.wrapDFUCodec(TagKey.codec(RegistryKeys.BLOCK)));
							this.addGeneric(ReifiedType.parameterize(TagKey.class, ReifiedType.from(Item.class)), autoCodec.wrapDFUCodec(TagKey.codec(RegistryKeys.ITEM)));
							this.addGeneric(ReifiedType.parameterize(RegistryEntry.class, ReifiedType.from(Block.class)), autoCodec.wrapDFUCodec(RegistryFixedCodec.of(RegistryKeys.BLOCK)));
							this.addGeneric(ReifiedType.parameterize(RegistryEntry.class, ReifiedType.from(Item.class)), autoCodec.wrapDFUCodec(RegistryFixedCodec.of(RegistryKeys.ITEM)));
							this.addRaw(ComponentChanges.class, autoCodec.wrapDFUCodec(ComponentChanges.CODEC));
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