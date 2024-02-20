package builderb0y.bigtech.codecs;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;

import builderb0y.autocodec.AutoCodec;
import builderb0y.autocodec.decoders.AutoDecoder;
import builderb0y.autocodec.decoders.DecoderFactoryList;
import builderb0y.autocodec.decoders.LookupDecoderFactory;
import builderb0y.autocodec.encoders.AutoEncoder;
import builderb0y.autocodec.encoders.EncoderFactoryList;
import builderb0y.autocodec.encoders.LookupEncoderFactory;
import builderb0y.autocodec.reflection.ReflectionManager;

public class BigTechAutoCodec {

	@SuppressWarnings("OverrideOnly") //super calls.
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
							this.addRaw(Block.class, autoCodec.wrapDFUCodec(Registries.BLOCK.codec, false));
							this.addRaw(Item.class, autoCodec.wrapDFUCodec(Registries.ITEM.codec, false));
							this.addRaw(ItemStack.class, autoCodec.wrapDFUCodec(ItemStack.CODEC, false));
							this.addRaw(Ingredient.class, autoCodec.wrapDFUCodec(Ingredient.DISALLOW_EMPTY_CODEC, false));
							this.addRaw(NbtCompound.class, NbtAutoCoder.INSTANCE);
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
							this.addRaw(Block.class, autoCodec.wrapDFUCodec(Registries.BLOCK.codec, false));
							this.addRaw(Item.class, autoCodec.wrapDFUCodec(Registries.ITEM.codec, false));
							this.addRaw(ItemStack.class, autoCodec.wrapDFUCodec(ItemStack.CODEC, false));
							this.addRaw(Ingredient.class, autoCodec.wrapDFUCodec(Ingredient.DISALLOW_EMPTY_CODEC, false));
							this.addRaw(NbtCompound.class, NbtAutoCoder.INSTANCE);
						}
					};
				}
			};
		}

		@Override
		public @NotNull ReflectionManager createReflectionManager() {
			return new ReflectionManager() {

				@Override
				public boolean canView(@NotNull Class<?> clazz) {
					return clazz.getName().startsWith("builderb0y.bigtech.");
				}
			};
		}

		/*
		@Override
		public @NotNull TaskLogger createFactoryLogger(@NotNull ReentrantLock lock) {
			return new IndentedTaskLogger(lock, Printer.SYSTEM, true);
		}
		*/
	};
}