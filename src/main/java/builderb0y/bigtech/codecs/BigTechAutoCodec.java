package builderb0y.bigtech.codecs;

import org.jetbrains.annotations.NotNull;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import builderb0y.autocodec.AutoCodec;
import builderb0y.autocodec.decoders.AutoDecoder;
import builderb0y.autocodec.decoders.DecoderFactoryList;
import builderb0y.autocodec.decoders.LookupDecoderFactory;
import builderb0y.autocodec.encoders.AutoEncoder;
import builderb0y.autocodec.encoders.EncoderFactoryList;
import builderb0y.autocodec.encoders.LookupEncoderFactory;

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
							this.addRaw(ItemStack.class, autoCodec.wrapDFUCodec(ItemStack.CODEC, false));
							this.addRaw(Ingredient.class, autoCodec.wrapDFUCodec(Ingredient.DISALLOW_EMPTY_CODEC, false));
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
							this.addRaw(ItemStack.class, autoCodec.wrapDFUCodec(ItemStack.CODEC, false));
							this.addRaw(Ingredient.class, autoCodec.wrapDFUCodec(Ingredient.DISALLOW_EMPTY_CODEC, false));
						}
					};
				}
			};
		}
	};
}