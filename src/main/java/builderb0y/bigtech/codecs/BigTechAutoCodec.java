package builderb0y.bigtech.codecs;

import java.lang.StackWalker.Option;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import builderb0y.autocodec.AutoCodec;
import builderb0y.autocodec.coders.*;
import builderb0y.autocodec.common.EnumName;
import builderb0y.autocodec.logging.DisabledTaskLogger;
import builderb0y.autocodec.logging.TaskLogger;
import builderb0y.autocodec.reflection.PseudoField;
import builderb0y.autocodec.reflection.ReflectionManager;
import builderb0y.autocodec.reflection.memberViews.FieldLikeMemberView;
import builderb0y.autocodec.reflection.memberViews.PseudoFieldView;
import builderb0y.autocodec.reflection.reification.ReifiedType;
import builderb0y.autocodec.util.AutoCodecUtil;

public class BigTechAutoCodec extends AutoCodec {

	public static final StackWalker STACK_WALKER = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);

	public static final AutoCodec
		AUTO_CODEC = new BigTechAutoCodec(),
		SILENT_CODEC = new BigTechAutoCodec() {

			@Override
			@OverrideOnly
			public @NotNull TaskLogger createEncodeLogger(@NotNull ReentrantLock lock) {
				return new DisabledTaskLogger();
			}

			@Override
			@OverrideOnly
			public @NotNull TaskLogger createDecodeLogger(@NotNull ReentrantLock lock) {
				return new DisabledTaskLogger();
			}
		};

	@Override
	@OverrideOnly
	public @NotNull CoderFactoryList createCoders() {
		return new CoderFactoryList(this) {

			@Override
			public void setup() {
				super.setup();
				this.addFactoryAfter(DefaultEmptyCoder.Factory.class, new ItemStackCoder.Factory());
				this.getFactory(EnumCoder.Factory.class).nameGetter = EnumName.LOWER_SNAKE_CASE;
			}

			@Override
			public @NotNull AutoCoder.CoderFactory createLookupFactory() {
				return new LookupCoderFactory() {

					@Override
					public void setup() {
						super.setup();
						this.addRaw(Identifier.class, PrimitiveCoders.stringBased("IdentifierCodec", Identifier::of, Identifier::toString));
						this.addRaw(AbstractBlock.Settings.class, autoCodec.wrapDFUCodec(AbstractBlock.Settings.CODEC));
						this.addRaw(Block.class, autoCodec.wrapDFUCodec(Registries.BLOCK.getCodec()));
						this.addRaw(Item.class, autoCodec.wrapDFUCodec(Registries.ITEM.getCodec()));
						this.addRaw(Ingredient.class, autoCodec.wrapDFUCodec(Ingredient.CODEC));
						this.addRaw(NbtCompound.class, NbtAutoCoder.INSTANCE);
						this.addGeneric(ReifiedType.parameterize(TagKey.class, ReifiedType.from(Block.class)), autoCodec.wrapDFUCodec(TagKey.codec(RegistryKeys.BLOCK)));
						this.addGeneric(ReifiedType.parameterize(TagKey.class, ReifiedType.from(Item.class)), autoCodec.wrapDFUCodec(TagKey.codec(RegistryKeys.ITEM)));
						this.addGeneric(ReifiedType.parameterize(RegistryEntry.class, ReifiedType.from(Block.class)), autoCodec.wrapDFUCodec(RegistryFixedCodec.of(RegistryKeys.BLOCK)));
						this.addGeneric(ReifiedType.parameterize(RegistryEntry.class, ReifiedType.from(Item.class)), autoCodec.wrapDFUCodec(RegistryFixedCodec.of(RegistryKeys.ITEM)));
						this.addRaw(ComponentChanges.class, autoCodec.wrapDFUCodec(ComponentChanges.CODEC));
						this.addRaw(BlockSetType.class, autoCodec.wrapDFUCodec(BlockSetType.CODEC));
						this.addGeneric(
							ReifiedType.parameterize(
								RegistryEntryList.class,
								ReifiedType.from(Block.class)
							),
							autoCodec.wrapDFUCodec(
								RegistryEntryListCodec.create(
									RegistryKeys.BLOCK,
									RegistryElementCodec.of(
										RegistryKeys.BLOCK,
										Block.CODEC.codec(),
										false
									),
									false
								)
							)
						);
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