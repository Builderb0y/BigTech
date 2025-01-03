package builderb0y.bigtech.datagen.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import it.unimi.dsi.fastutil.HashCommon;

import net.minecraft.util.math.MathHelper;

import builderb0y.autocodec.util.AutoCodecUtil;
import builderb0y.bigtech.models.IntRng;
import builderb0y.bigtech.util.BigTechMath;

public class TextureGenerator implements Cloneable {

	public static final float[]
		BOX_BLUR_1      = {  1.0F /  3.0F,  1.0F /  3.0F                             },
		BOX_BLUR_2      = {  1.0F /  5.0F,  1.0F /  5.0F, 1.0F /  5.0F               },
		BOX_BLUR_3      = {  1.0F /  7.0F,  1.0F /  7.0F, 1.0F /  7.0F, 1.0F /  7.0F },
		GAUSSIAN_BLUR_1 = {  2.0F /  4.0F,  1.0F /  4.0F                             },
		GAUSSIAN_BLUR_2 = {  6.0F / 16.0F,  4.0F / 16.0F, 1.0F / 16.0F               },
		GAUSSIAN_BLUR_3 = { 20.0F / 64.0F, 15.0F / 64.0F, 6.0F / 64.0F, 1.0F / 64.0F };

	public static final int
		RED_OFFSET    = 0,
		GREEN_OFFSET  = 1,
		BLUE_OFFSET   = 2,
		ALPHA_OFFSET  = 3,
		RED_CHANNEL   = 1 <<   RED_OFFSET,
		GREEN_CHANNEL = 1 << GREEN_OFFSET,
		BLUE_CHANNEL  = 1 <<  BLUE_OFFSET,
		ALPHA_CHANNEL = 1 << ALPHA_OFFSET,
		RGB_CHANNELS  = RED_CHANNEL  | GREEN_CHANNEL | BLUE_CHANNEL,
		RGBA_CHANNELS = RGB_CHANNELS | ALPHA_CHANNEL ;

	public float[] pixels = new float[16 * 16 * 4];
	public Mask mask = new FilledRectangleMask(0, 0, 16, 16);
	public byte channels = RGBA_CHANNELS;

	public TextureGenerator view() {
		try {
			return (TextureGenerator)(this.clone());
		}
		catch (CloneNotSupportedException exception) {
			throw AutoCodecUtil.rethrow(exception);
		}
	}

	public TextureGenerator copy() {
		TextureGenerator clone = this.view();
		clone.pixels = clone.pixels.clone();
		return clone;
	}

	public TextureGenerator mask(Mask mask) {
		this.mask = mask;
		return this;
	}

	public TextureGenerator region(int minX, int minY, int maxX, int maxY) {
		return this.mask(new FilledRectangleMask(minX, minY, maxX, maxY));
	}

	public TextureGenerator hollowRegion(int minX, int minY, int maxX, int maxY, int thickness) {
		return this.mask(new HollowRectangleMask(minX, minY, maxX, maxY, thickness));
	}

	public TextureGenerator channels(int mask) {
		this.channels = (byte)(mask);
		return this;
	}

	public TextureGenerator select(int minX, int minY, int maxX, int maxY, int mask) {
		return this.region(minX, minY, maxX, maxY).channels(mask);
	}

	public BufferedImage toBufferedImage() {
		BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		int[] pixel = new int[1];
		for (int y = 0; y < 16; y++) {
			for (int x = 0; x < 16; x++) {
				int baseIndex = baseIndex(x, y);
				float
					redF   = this.pixels[baseIndex |   RED_OFFSET],
					greenF = this.pixels[baseIndex | GREEN_OFFSET],
					blueF  = this.pixels[baseIndex |  BLUE_OFFSET],
					alphaF = this.pixels[baseIndex | ALPHA_OFFSET];
				int
					redI   = Math.max(Math.min((int)(redF   * 256.0F), 255), 0),
					greenI = Math.max(Math.min((int)(greenF * 256.0F), 255), 0),
					blueI  = Math.max(Math.min((int)(blueF  * 256.0F), 255), 0),
					alphaI = Math.max(Math.min((int)(alphaF * 256.0F), 255), 0);
				pixel[0] = (alphaI << 24) | (redI << 16) | (greenI << 8) | blueI;
				image.getRaster().setDataElements(x, y, pixel);
			}
		}
		return image;
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			ImageIO.write(this.toBufferedImage(), "png", stream);
		}
		catch (IOException exception) {
			throw new AssertionError("ByteArrayOutputStream threw an IOException?", exception);
		}
		return stream.toByteArray();
	}

	public boolean   redOn() { return (this.channels &   RED_CHANNEL) != 0; }
	public boolean greenOn() { return (this.channels & GREEN_CHANNEL) != 0; }
	public boolean  blueOn() { return (this.channels &  BLUE_CHANNEL) != 0; }
	public boolean alphaOn() { return (this.channels & ALPHA_CHANNEL) != 0; }

	public static int  baseIndex(int x, int y) { return ((y << 4) | x) << 2; }
	public static int   redIndex(int x, int y) { return baseIndex(x, y) |   RED_OFFSET; }
	public static int greenIndex(int x, int y) { return baseIndex(x, y) | GREEN_OFFSET; }
	public static int  blueIndex(int x, int y) { return baseIndex(x, y) |  BLUE_OFFSET; }
	public static int alphaIndex(int x, int y) { return baseIndex(x, y) | ALPHA_OFFSET; }

	public float getRed  (int x, int y) { return this.pixels[  redIndex(x, y)]; }
	public float getGreen(int x, int y) { return this.pixels[greenIndex(x, y)]; }
	public float getBlue (int x, int y) { return this.pixels[ blueIndex(x, y)]; }
	public float getAlpha(int x, int y) { return this.pixels[alphaIndex(x, y)]; }

	public void setRed  (int x, int y, float value) { this.pixels[  redIndex(x, y)] = value; }
	public void setGreen(int x, int y, float value) { this.pixels[greenIndex(x, y)] = value; }
	public void setBlue (int x, int y, float value) { this.pixels[ blueIndex(x, y)] = value; }
	public void setAlpha(int x, int y, float value) { this.pixels[alphaIndex(x, y)] = value; }

	public void setRGBA(int x, int y, float red, float green, float blue, float alpha) {
		int baseIndex = baseIndex(x, y);
		if (this.  redOn()) this.pixels[baseIndex |   RED_OFFSET] = red;
		if (this.greenOn()) this.pixels[baseIndex | GREEN_OFFSET] = green;
		if (this. blueOn()) this.pixels[baseIndex |  BLUE_OFFSET] = blue;
		if (this.alphaOn()) this.pixels[baseIndex | ALPHA_OFFSET] = alpha;
	}

	public void setGrayscale(int x, int y, float brightness, float alpha) {
		this.setRGBA(x, y, brightness, brightness, brightness, alpha);
	}

	public TextureGenerator pullFrom(TextureGenerator that) {
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				int baseIndex = baseIndex(x, y);
				if (this.  redOn()) this.pixels[baseIndex |   RED_OFFSET] = that.pixels[baseIndex |   RED_OFFSET];
				if (this.greenOn()) this.pixels[baseIndex | GREEN_OFFSET] = that.pixels[baseIndex | GREEN_OFFSET];
				if (this. blueOn()) this.pixels[baseIndex |  BLUE_OFFSET] = that.pixels[baseIndex |  BLUE_OFFSET];
				if (this.alphaOn()) this.pixels[baseIndex | ALPHA_OFFSET] = that.pixels[baseIndex | ALPHA_OFFSET];
			}
		}
		return this;
	}

	public TextureGenerator pushTo(TextureGenerator that) {
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				int baseIndex = baseIndex(x, y);
				if (this.  redOn()) that.pixels[baseIndex |   RED_OFFSET] = this.pixels[baseIndex |   RED_OFFSET];
				if (this.greenOn()) that.pixels[baseIndex | GREEN_OFFSET] = this.pixels[baseIndex | GREEN_OFFSET];
				if (this. blueOn()) that.pixels[baseIndex |  BLUE_OFFSET] = this.pixels[baseIndex |  BLUE_OFFSET];
				if (this.alphaOn()) that.pixels[baseIndex | ALPHA_OFFSET] = this.pixels[baseIndex | ALPHA_OFFSET];
			}
		}
		return this;
	}

	public TextureGenerator fillColor(float red, float green, float blue, float alpha) {
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				this.setRGBA(x, y, red, green, blue, alpha);
			}
		}
		return this;
	}

	public TextureGenerator fillRgbaNoise(int seed) {
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				int baseIndex = baseIndex(x, y);
				int adjustedSeed = hash(seed, x, y);
				if (this.  redOn()) this.pixels[baseIndex |   RED_OFFSET] = rng2F(hash(adjustedSeed,   RED_OFFSET));
				if (this.greenOn()) this.pixels[baseIndex | GREEN_OFFSET] = rng2F(hash(adjustedSeed, GREEN_OFFSET));
				if (this. blueOn()) this.pixels[baseIndex |  BLUE_OFFSET] = rng2F(hash(adjustedSeed,  BLUE_OFFSET));
				if (this.alphaOn()) this.pixels[baseIndex | ALPHA_OFFSET] = rng2F(hash(adjustedSeed, ALPHA_OFFSET));
			}
		}
		return this;
	}

	public TextureGenerator fillGrayscaleNoise(int seed) {
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				int baseIndex = baseIndex(x, y);
				float value = rng2F(hash(seed, x, y));
				if (this.  redOn()) this.pixels[baseIndex |   RED_OFFSET] = value;
				if (this.greenOn()) this.pixels[baseIndex | GREEN_OFFSET] = value;
				if (this. blueOn()) this.pixels[baseIndex |  BLUE_OFFSET] = value;
				if (this.alphaOn()) this.pixels[baseIndex | ALPHA_OFFSET] = value;
			}
		}
		return this;
	}

	public TextureGenerator processPixels(PixelProcessor processor) {
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				processor.process(x, y, this);
			}
		}
		return this;
	}

	public TextureGenerator processChannels(ChannelProcessor processor) {
		return this.processPixels(PixelProcessor.channels(processor));
	}

	public TextureGenerator square() {
		return this.processChannels(ChannelProcessor.SQUARE);
	}

	public TextureGenerator sqrt() {
		return this.processChannels(ChannelProcessor.SQRT);
	}

	public TextureGenerator add(float value) {
		return this.processChannels(ChannelProcessor.add(value));
	}

	public TextureGenerator mul(float value) {
		return this.processChannels(ChannelProcessor.mul(value));
	}

	public TextureGenerator add(float red, float green, float blue, float alpha) {
		return this.processChannels(ChannelProcessor.add(red, green, blue, alpha));
	}

	public TextureGenerator mul(float red, float green, float blue, float alpha) {
		return this.processChannels(ChannelProcessor.mul(red, green, blue, alpha));
	}

	public TextureGenerator add(TextureGenerator other) {
		return this.processChannels(ChannelProcessor.add(other));
	}

	public TextureGenerator mul(TextureGenerator other) {
		return this.processChannels(ChannelProcessor.mul(other));
	}

	public static float select(int channel, float red, float green, float blue, float alpha) {
		return switch (channel) {
			case RED_OFFSET   -> red;
			case GREEN_OFFSET -> green;
			case BLUE_OFFSET  -> blue;
			case ALPHA_OFFSET -> alpha;
			default -> throw new IllegalArgumentException("Invalid channel: " + channel);
		};
	}

	public TextureGenerator swizzle(int redOffset, int greenOffset, int blueOffset, int alphaOffset) {
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				int baseIndex = baseIndex(x, y);
				float red   = this.pixels[baseIndex |   RED_OFFSET];
				float green = this.pixels[baseIndex | GREEN_OFFSET];
				float blue  = this.pixels[baseIndex |  BLUE_OFFSET];
				float alpha = this.pixels[baseIndex | ALPHA_OFFSET];
				if (this.  redOn()) this.pixels[baseIndex |   RED_OFFSET] = select(  redOffset, red, green, blue, alpha);
				if (this.greenOn()) this.pixels[baseIndex | GREEN_OFFSET] = select(greenOffset, red, green, blue, alpha);
				if (this. blueOn()) this.pixels[baseIndex |  BLUE_OFFSET] = select( blueOffset, red, green, blue, alpha);
				if (this.alphaOn()) this.pixels[baseIndex | ALPHA_OFFSET] = select(alphaOffset, red, green, blue, alpha);
			}
		}
		return this;
	}

	public float min() {
		float min = Float.POSITIVE_INFINITY;
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				int baseIndex = baseIndex(x, y);
				if (this.  redOn()) min = Math.min(min, this.pixels[baseIndex |   RED_OFFSET]);
				if (this.greenOn()) min = Math.min(min, this.pixels[baseIndex | GREEN_OFFSET]);
				if (this. blueOn()) min = Math.min(min, this.pixels[baseIndex |  BLUE_OFFSET]);
				if (this.alphaOn()) min = Math.min(min, this.pixels[baseIndex | ALPHA_OFFSET]);
			}
		}
		return min;
	}

	public float max() {
		float max = Float.NEGATIVE_INFINITY;
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				int baseIndex = baseIndex(x, y);
				if (this.  redOn()) max = Math.max(max, this.pixels[baseIndex |   RED_OFFSET]);
				if (this.greenOn()) max = Math.max(max, this.pixels[baseIndex | GREEN_OFFSET]);
				if (this. blueOn()) max = Math.max(max, this.pixels[baseIndex |  BLUE_OFFSET]);
				if (this.alphaOn()) max = Math.max(max, this.pixels[baseIndex | ALPHA_OFFSET]);
			}
		}
		return max;
	}

	public TextureGenerator normalizeChannels() {
		float
			minRed   = Float.POSITIVE_INFINITY,
			maxRed   = Float.NEGATIVE_INFINITY,
			minGreen = Float.POSITIVE_INFINITY,
			maxGreen = Float.NEGATIVE_INFINITY,
			minBlue  = Float.POSITIVE_INFINITY,
			maxBlue  = Float.NEGATIVE_INFINITY,
			minAlpha = Float.POSITIVE_INFINITY,
			maxAlpha = Float.NEGATIVE_INFINITY;
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				int baseIndex = baseIndex(x, y);

				float value = this.pixels[baseIndex | RED_OFFSET];
				minRed = Math.min(minRed, value);
				maxRed = Math.max(maxRed, value);

				value = this.pixels[baseIndex | GREEN_OFFSET];
				minGreen = Math.min(minGreen, value);
				maxGreen = Math.max(maxGreen, value);

				value = this.pixels[baseIndex | BLUE_OFFSET];
				minBlue = Math.min(minBlue, value);
				maxBlue = Math.max(maxBlue, value);

				value = this.pixels[baseIndex | ALPHA_OFFSET];
				minAlpha = Math.min(minAlpha, value);
				maxAlpha = Math.max(maxAlpha, value);
			}
		}

		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				int baseIndex = baseIndex(x, y);
				if (this.  redOn() && minRed   != maxRed  ) this.pixels[baseIndex |   RED_OFFSET] = (this.pixels[baseIndex |   RED_OFFSET] - minRed  ) / (maxRed   - minRed  );
				if (this.greenOn() && minGreen != maxGreen) this.pixels[baseIndex | GREEN_OFFSET] = (this.pixels[baseIndex | GREEN_OFFSET] - minGreen) / (maxGreen - minGreen);
				if (this. blueOn() && minBlue  != maxBlue ) this.pixels[baseIndex |  BLUE_OFFSET] = (this.pixels[baseIndex |  BLUE_OFFSET] - minBlue ) / (maxBlue  - minBlue );
				if (this.alphaOn() && minAlpha != maxAlpha) this.pixels[baseIndex | ALPHA_OFFSET] = (this.pixels[baseIndex | ALPHA_OFFSET] - minAlpha) / (maxAlpha - minAlpha);
			}
		}
		return this;
	}

	public TextureGenerator normalize() {
		float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				int baseIndex = baseIndex(x, y);
				float value;

				if (this.redOn()) {
					value = this.pixels[baseIndex | RED_OFFSET];
					min = Math.min(min, value);
					max = Math.max(max, value);
				}

				if (this.greenOn()) {
					value = this.pixels[baseIndex | GREEN_OFFSET];
					min = Math.min(min, value);
					max = Math.max(max, value);
				}

				if (this.blueOn()) {
					value = this.pixels[baseIndex | BLUE_OFFSET];
					min = Math.min(min, value);
					max = Math.max(max, value);
				}

				if (this.alphaOn()) {
					value = this.pixels[baseIndex | ALPHA_OFFSET];
					min = Math.min(min, value);
					max = Math.max(max, value);
				}
			}
		}

		if (min != max) {
			for (int y = this.mask.minY; y < this.mask.maxY; y++) {
				for (int x = this.mask.minX; x < this.mask.maxX; x++) {
					if (!this.mask.contains(x, y)) continue;
					int baseIndex = baseIndex(x, y);
					if (this.  redOn()) this.pixels[baseIndex |   RED_OFFSET] = (this.pixels[baseIndex |   RED_OFFSET] - min) / (max - min);
					if (this.greenOn()) this.pixels[baseIndex | GREEN_OFFSET] = (this.pixels[baseIndex | GREEN_OFFSET] - min) / (max - min);
					if (this. blueOn()) this.pixels[baseIndex |  BLUE_OFFSET] = (this.pixels[baseIndex |  BLUE_OFFSET] - min) / (max - min);
					if (this.alphaOn()) this.pixels[baseIndex | ALPHA_OFFSET] = (this.pixels[baseIndex | ALPHA_OFFSET] - min) / (max - min);
				}
			}
		}

		return this;
	}

	@FunctionalInterface
	public static interface ChannelProcessor {

		public static ChannelProcessor
			SQUARE = (int x, int y, int channel, float oldValue) -> oldValue * oldValue,
			SQRT   = (int x, int y, int channel, float oldValue) -> (float)(Math.sqrt(oldValue));

		public abstract float processChannel(int x, int y, int channel, float oldValue);

		public static ChannelProcessor add(float value) {
			return (int x, int y, int channel, float oldValue) -> oldValue + value;
		}

		public static ChannelProcessor mul(float value) {
			return (int x, int y, int channel, float oldValue) -> oldValue * value;
		}

		public static ChannelProcessor add(float red, float green, float blue, float alpha) {
			return (int x, int y, int channel, float oldValue) -> oldValue + select(channel, red, green, blue, alpha);
		}

		public static ChannelProcessor mul(float red, float green, float blue, float alpha) {
			return (int x, int y, int channel, float oldValue) -> oldValue * select(channel, red, green, blue, alpha);
		}

		public static ChannelProcessor add(TextureGenerator texture) {
			return (int x, int y, int channel, float oldValue) -> oldValue + texture.pixels[baseIndex(x, y) | channel];
		}

		public static ChannelProcessor mul(TextureGenerator texture) {
			return (int x, int y, int channel, float oldValue) -> oldValue * texture.pixels[baseIndex(x, y) | channel];
		}

		public static ChannelProcessor lerp(int argb0, int argb1) {
			return lerp(
				WoodColors.red(argb0),
				WoodColors.green(argb0),
				WoodColors.blue(argb0),
				WoodColors.alpha(argb0),
				WoodColors.red(argb1),
				WoodColors.green(argb1),
				WoodColors.blue(argb1),
				WoodColors.alpha(argb1)
			);
		}

		public static ChannelProcessor lerp(
			float r0, float g0, float b0, float a0,
			float r1, float g1, float b1, float a1
		) {
			return (int x, int y, int channel, float oldValue) -> {
				return MathHelper.lerp(
					oldValue,
					select(channel, r0, g0, b0, a0),
					select(channel, r1, g1, b1, a1)
				);
			};
		}
	}

	@FunctionalInterface
	public static interface PixelProcessor {

		public abstract void process(int x, int y, TextureGenerator texture);

		public static PixelProcessor channels(ChannelProcessor channelProcessor) {
			return (int x, int y, TextureGenerator texture) -> {
				int baseIndex = baseIndex(x, y);
				if (texture.  redOn()) texture.pixels[baseIndex |   RED_OFFSET] = channelProcessor.processChannel(x, y,   RED_OFFSET, texture.pixels[baseIndex |   RED_OFFSET]);
				if (texture.greenOn()) texture.pixels[baseIndex | GREEN_OFFSET] = channelProcessor.processChannel(x, y, GREEN_OFFSET, texture.pixels[baseIndex | GREEN_OFFSET]);
				if (texture. blueOn()) texture.pixels[baseIndex |  BLUE_OFFSET] = channelProcessor.processChannel(x, y,  BLUE_OFFSET, texture.pixels[baseIndex |  BLUE_OFFSET]);
				if (texture.alphaOn()) texture.pixels[baseIndex | ALPHA_OFFSET] = channelProcessor.processChannel(x, y, ALPHA_OFFSET, texture.pixels[baseIndex | ALPHA_OFFSET]);
			};
		}
	}

	public TextureGenerator blurHorizontal(float[] weights, OOBHandler oobHandler) {
		TextureGenerator old = this.copy();
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				for (int channel = 0; channel < 4; channel++) {
					if ((this.channels & (1 << channel)) != 0) {
						float sum = old.pixels[baseIndex(x, y) | channel] * weights[0];
						for (int offset = 1; offset < weights.length; offset++) {
							sum += (this.mask.contains(x + offset, y) ? old.pixels[baseIndex(x + offset, y) | channel] : oobHandler.getFallback(x + offset, y, channel, old)) * weights[offset];
							sum += (this.mask.contains(x - offset, y) ? old.pixels[baseIndex(x - offset, y) | channel] : oobHandler.getFallback(x - offset, y, channel, old)) * weights[offset];
						}
						this.pixels[baseIndex(x, y) | channel] = sum;
					}
				}
			}
		}
		return this;
	}

	public TextureGenerator blurVertical(float[] weights, OOBHandler oobHandler) {
		TextureGenerator old = this.copy();
		for (int y = this.mask.minY; y < this.mask.maxY; y++) {
			for (int x = this.mask.minX; x < this.mask.maxX; x++) {
				if (!this.mask.contains(x, y)) continue;
				for (int channel = 0; channel < 4; channel++) {
					if ((this.channels & (1 << channel)) != 0) {
						float sum = old.pixels[baseIndex(x, y) | channel] * weights[0];
						for (int offset = 1; offset < weights.length; offset++) {
							sum += (this.mask.contains(x, y + offset) ? old.pixels[baseIndex(x, y + offset) | channel] : oobHandler.getFallback(x, y + offset, channel, old)) * weights[offset];
							sum += (this.mask.contains(x, y - offset) ? old.pixels[baseIndex(x, y - offset) | channel] : oobHandler.getFallback(x, y - offset, channel, old)) * weights[offset];
						}
						this.pixels[baseIndex(x, y) | channel] = sum;
					}
				}
			}
		}
		return this;
	}

	public TextureGenerator blur(float[] horizontalWeights, float[] verticalWeights, OOBHandler oobHandler) {
		return this.blurHorizontal(horizontalWeights, oobHandler).blurVertical(verticalWeights, oobHandler);
	}

	public TextureGenerator blurConcentric(float[] weights, OOBHandler oobHandler) {
		TextureGenerator old = this.copy();
		for (int radius = 0; radius < 8; radius++) {
			int pixelsInRadius = (radius << 3) | 4;
			int pixelsOnSide = (radius << 1) | 1;
			for (int angle = 0; angle < pixelsInRadius; angle++) {
				int x = decodeConcentricX(radius, angle, pixelsOnSide);
				int y = decodeConcentricY(radius, angle, pixelsOnSide);
				if (!this.mask.contains(x, y)) continue;
				int baseIndex = baseIndex(x, y);
				for (int channel = 0; channel < 4; channel++) {
					if ((this.channels & (1 << channel)) == 0) continue;
					float sum = 0.0F;
					for (int offset = 1 - weights.length; offset < weights.length; offset++) {
						int angle2 = BigTechMath.modulus_BP(angle + offset, pixelsInRadius);
						x = decodeConcentricX(radius, angle2, pixelsOnSide);
						y = decodeConcentricY(radius, angle2, pixelsOnSide);
						sum += (this.mask.contains(x, y) ? old.pixels[baseIndex(x, y) | channel] : oobHandler.getFallback(x, y, channel, old)) * weights[Math.abs(offset)];
					}
					this.pixels[baseIndex | channel] = sum;
				}
			}
		}
		return this;
	}

	public static int decodeConcentricX(int radius, int angle, int pixelsOnSide) {
		if (angle < pixelsOnSide) return 8 + radius;
		else if ((angle -= pixelsOnSide) < pixelsOnSide) return 8 + radius - angle;
		else if ((angle -= pixelsOnSide) < pixelsOnSide) return 7 - radius;
		else return 7 - radius + (angle - pixelsOnSide);
	}

	public static int decodeConcentricY(int radius, int angle, int pixelsOnSide) {
		if (angle < pixelsOnSide) return 7 - radius + angle;
		else if ((angle -= pixelsOnSide) < pixelsOnSide) return 8 + radius;
		else if ((angle -= pixelsOnSide) < pixelsOnSide) return 8 + radius - angle;
		else return 7 - radius;
	}

	@FunctionalInterface
	public static interface OOBHandler {

		public static final OOBHandler
			CLAMP = (int x, int y, int channel, TextureGenerator texture) -> {
				return texture.pixels[
					baseIndex(
						MathHelper.clamp(x, texture.mask.minX, texture.mask.maxX - 1),
						MathHelper.clamp(y, texture.mask.minY, texture.mask.maxY - 1)
					)
					| channel
				];
			},
			LOOP = (int x, int y, int channel, TextureGenerator texture) -> {
				return texture.pixels[
					baseIndex(
						Math.floorMod(x - texture.mask.minX, texture.mask.maxX - texture.mask.minX) + texture.mask.minX,
						Math.floorMod(y - texture.mask.minY, texture.mask.maxY - texture.mask.minY) + texture.mask.minY
					)
					| channel
				];
			},
			THROW = (int x, int y, int channel, TextureGenerator texture) -> {
				throw new IndexOutOfBoundsException(x + ", " + y);
			};

		public abstract float getFallback(int x, int y, int channel, TextureGenerator texture);

		public static OOBHandler ignore(OOBHandler fallback) {
			return (int x, int y, int channel, TextureGenerator texture) -> {
				if (x >= 0 && x < 16 && y >= 0 && y < 16) {
					return texture.pixels[baseIndex(x, y) | channel];
				}
				else {
					return fallback.getFallback(x, y, channel, texture);
				}
			};
		}

		public static OOBHandler rgbaNoise(int seed) {
			return (int x, int y, int channel, TextureGenerator texture) -> {
				return rng2F(hash(seed, x, y, channel));
			};
		}

		public static OOBHandler grayscaleNoise(int seed) {
			return (int x, int y, int channel, TextureGenerator texture) -> {
				return rng2F(hash(seed, x, y));
			};
		}
	}

	public static float rng2F(int seed) {
		return Integer.toUnsignedLong(Integer.reverse(seed)) / 0x1.0P32F;
	}

	public static int hash(int seed, int x) {
		seed = HashCommon.murmurHash3(seed + x * IntRng.INT_PHI);
		return seed;
	}

	public static int hash(int seed, int x, int y) {
		seed = HashCommon.murmurHash3(seed + x * IntRng.INT_PHI);
		seed = HashCommon.murmurHash3(seed + y * IntRng.INT_PHI);
		return seed;
	}

	public static int hash(int seed, int x, int y, int z) {
		seed = HashCommon.murmurHash3(seed + x * IntRng.INT_PHI);
		seed = HashCommon.murmurHash3(seed + y * IntRng.INT_PHI);
		seed = HashCommon.murmurHash3(seed + z * IntRng.INT_PHI);
		return seed;
	}

	public static abstract class Mask {

		public int minX, minY, maxX, maxY;

		public Mask(int minX, int minY, int maxX, int maxY) {
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
		}

		public abstract boolean contains(int x, int y);

		public static Mask union(Mask... masks) {
			int minX = 16, minY = 16, maxX = 0, maxY = 0;
			for (Mask mask : masks) {
				minX = Math.min(minX, mask.minX);
				minY = Math.min(minY, mask.minY);
				maxX = Math.max(maxX, mask.maxX);
				maxY = Math.max(maxY, mask.maxY);
			}
			return new UnionMask(minX, minY, maxX, maxY, masks);
		}

		public static Mask intersection(Mask... masks) {
			int minX = 0, minY = 0, maxX = 16, maxY = 16;
			for (Mask mask : masks) {
				minX = Math.max(minX, mask.minX);
				minY = Math.max(minY, mask.minY);
				maxX = Math.min(maxX, mask.maxX);
				maxY = Math.min(maxY, mask.maxY);
			}
			return new IntersectionMask(minX, minY, maxX, maxY, masks);
		}
	}

	@FunctionalInterface
	public static interface MaskPredicate {

		public abstract boolean test(Mask mask, int x, int y);
	}

	public static class CustomMask extends Mask {

		public MaskPredicate predicate;

		public CustomMask(int minX, int minY, int maxX, int maxY, MaskPredicate predicate) {
			super(minX, minY, maxX, maxY);
			this.predicate = predicate;
		}

		@Override
		public boolean contains(int x, int y) {
			return this.predicate.test(this, x, y);
		}
	}

	public static class FilledRectangleMask extends Mask {

		public FilledRectangleMask(int minX, int minY, int maxX, int maxY) {
			super(minX, minY, maxX, maxY);
		}

		@Override
		public boolean contains(int x, int y) {
			return x >= this.minX && x < this.maxX && y >= this.minY && y < this.maxY;
		}
	}

	public static class HollowRectangleMask extends Mask {

		public int thickness;

		public HollowRectangleMask(int minX, int minY, int maxX, int maxY, int thickness) {
			super(minX, minY, maxX, maxY);
			this.thickness = thickness;
		}

		@Override
		public boolean contains(int x, int y) {
			return (
				x >= this.minX && x < this.minX + this.thickness ||
				y >= this.minY && y < this.minY + this.thickness ||
				x >= this.maxX - this.thickness && x < this.maxX ||
				y >= this.maxY - this.thickness && y < this.maxY
			);
		}
	}

	public static class UnionMask extends Mask {

		public Mask[] masks;

		public UnionMask(int minX, int minY, int maxX, int maxY, Mask[] masks) {
			super(minX, minY, maxX, maxY);
			this.masks = masks;
		}

		@Override
		public boolean contains(int x, int y) {
			for (Mask mask : this.masks) {
				if (mask.contains(x, y)) return true;
			}
			return false;
		}
	}

	public static class IntersectionMask extends Mask {

		public Mask[] masks;

		public IntersectionMask(int minX, int minY, int maxX, int maxY, Mask[] masks) {
			super(minX, minY, maxX, maxY);
			this.masks = masks;
		}

		@Override
		public boolean contains(int x, int y) {
			for (Mask mask : this.masks) {
				if (!mask.contains(x, y)) return false;
			}
			return true;
		}
	}
}