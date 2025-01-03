package builderb0y.bigtech.datagen.impl.frames;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.datagen.impl.TextureGenerator;
import builderb0y.bigtech.datagen.impl.TextureGenerator.CustomMask;
import builderb0y.bigtech.datagen.impl.TextureGenerator.Mask;
import builderb0y.bigtech.datagen.impl.TextureGenerator.OOBHandler;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.registrableCollections.WoodRegistrableCollection;

import static builderb0y.bigtech.datagen.impl.TextureGenerator.*;
import static builderb0y.bigtech.datagen.impl.WoodColors.*;

public class WoodenFrameDataGenerator extends FrameDataGenerator {

	public WoodRegistrableCollection.Type type;
	public Item planks;

	public WoodenFrameDataGenerator(BlockItem blockItem, WoodRegistrableCollection.Type type) {
		super(blockItem);
		this.type = type;
		this.planks = BigTechBlocks.VANILLA_PLANKS.get(type).asItem();
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeFrameRecipe(
			context,
			new TagOrItem(this.planks),
			new TagOrItem(ConventionalItemTags.WOODEN_RODS)
		);
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		super.setupOtherBlockTags(context);
		context.getTags(BigTechBlockTags.WOODEN_FRAMES).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.WOODEN_FRAMES).addElement(this.getId());
	}

	@Override
	public void genTextures(DataGenContext context) {
		context.writeToFile(
			context.blockTexturePath(context.suffixPath(this.getId(), "_outer")),
			new TextureGenerator()
			.hollowRegion(0, 0, 16, 16, 3)
			.channels(ALPHA_CHANNEL)
			.fillColor(0.0F, 0.0F, 0.0F, 1.0F)
			.channels(RED_CHANNEL)
			.fillGrayscaleNoise(0)
			.blurConcentric(GAUSSIAN_BLUR_1, OOBHandler.THROW)
			.mul(0.5F)
			.hollowRegion(1, 1, 15, 15, 1)
			.add(0.5F)
			.hollowRegion(0, 0, 16, 16, 3)
			.channels(RGB_CHANNELS)
			.swizzle(RED_OFFSET, RED_OFFSET, RED_OFFSET, ALPHA_OFFSET)
			.processChannels(gradient(this.type))
			.toByteArray()
		);
		context.writeToFile(
			context.blockTexturePath(context.suffixPath(this.getId(), "_inner")),
			new TextureGenerator()
			.mask(new CustomMask(0, 0, 16, 16, (Mask mask, int x, int y) -> {
				return (x < 3 || x >= 13) ^ (y < 3 || y >= 13);
			}))
			.processPixels((int x, int y, TextureGenerator texture) -> {
				int dx, dy;
				if (x < 3 || x >= 13) { dx =  0; dy = -1; }
				else                  { dx = -1; dy =  0; }
				float[] weights = GAUSSIAN_BLUR_1;
				float sum = rng2F(hash(0, x, y)) * weights[0];
				for (int offset = 1; offset < weights.length; offset++) {
					sum += rng2F(hash(0, x + (dx & offset), y + (dy & offset))) * weights[offset];
					sum += rng2F(hash(0, x - (dx & offset), y - (dy & offset))) * weights[offset];
				}
				sum = sum * 0.5F + 0.5F;
				if (((0b1011000000001101 >>> x) & 1) != 0 || ((0b1011000000001101 >>> y) & 1) != 0) {
					sum -= 0.5F;
				}
				texture.setGrayscale(x, y, sum, 1.0F);
			})
			.processChannels(gradient(this.type))
			.toByteArray()
		);
	}
}