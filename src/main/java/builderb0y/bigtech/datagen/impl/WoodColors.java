package builderb0y.bigtech.datagen.impl;

import builderb0y.bigtech.datagen.impl.TextureGenerator.ChannelProcessor;
import builderb0y.bigtech.registrableCollections.WoodRegistrableCollection;

public class WoodColors {

	public static float red(int rgb) {
		return (rgb >>> 16) / 255.0F;
	}

	public static float green(int rgb) {
		return ((rgb >>> 8) & 255) / 255.0F;
	}

	public static float blue(int rgb) {
		return (rgb & 255) / 255.0F;
	}

	public static int getBrightestColor(WoodRegistrableCollection.Type type) {
		return switch (type) {
			case OAK      -> 0xC29D62;
			case SPRUCE   -> 0x886539;
			case BIRCH    -> 0xD7CB8D;
			case JUNGLE   -> 0xBF8E6B;
			case ACACIA   -> 0xC26D3F;
			case DARK_OAK -> 0x53381A;
			case MANGROVE -> 0x8B4D3A;
			case CHERRY   -> 0xE7CAC5;
			case PALE_OAK -> 0xFFFBF8;
			case CRIMSON  -> 0x863E5A;
			case WARPED   -> 0x3A8E8C;
		};
	}

	public static int getDarkestColor(WoodRegistrableCollection.Type type) {
		return switch (type) {
			case OAK      -> 0x67502C;
			case SPRUCE   -> 0x553A1F;
			case BIRCH    -> 0x9E8B61;
			case JUNGLE   -> 0x68462F;
			case ACACIA   -> 0x884728;
			case DARK_OAK -> 0x291A0C;
			case MANGROVE -> 0x5D1C1E;
			case CHERRY   -> 0xCD8580;
			case PALE_OAK -> 0xC0B2B2;
			case CRIMSON  -> 0x3F1E2D;
			case WARPED   -> 0x113835;
		};
	}

	public static ChannelProcessor gradient(WoodRegistrableCollection.Type type) {
		int light = getBrightestColor(type);
		int dark  = getDarkestColor(type);
		return ChannelProcessor.lerp(
			red(dark), green(dark), blue(dark), 1.0F,
			red(light), green(light), blue(light), 1.0F
		);
	}
}