package builderb0y.bigtech.datagen.impl.deco.catwalkPlatforms;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.blocks.BigTechBlocks;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.TagOrItem;
import builderb0y.bigtech.datagen.impl.TextureGenerator;
import builderb0y.bigtech.datagen.impl.TextureGenerator.CustomMask;
import builderb0y.bigtech.datagen.impl.TextureGenerator.Mask;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.registrableCollections.WoodRegistrableCollection;

import static builderb0y.bigtech.datagen.impl.TextureGenerator.*;
import static builderb0y.bigtech.datagen.impl.WoodColors.*;

public class WoodCatwalkPlatformDataGenerator extends CatwalkPlatformDataGenerator {

	public final WoodRegistrableCollection.Type type;
	public final Item planks;

	public WoodCatwalkPlatformDataGenerator(BlockItem blockItem, WoodRegistrableCollection.Type type) {
		super(blockItem);
		this.type = type;
		this.planks = BigTechBlocks.VANILLA_PLANKS.get(type).asItem();
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.AXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//no-op.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.WOODEN_CATWALK_PLATFORMS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.WOODEN_CATWALK_PLATFORMS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		this.writeCatwalkPlatformRecipe(
			context,
			new TagOrItem(this.planks),
			new TagOrItem(Items.STICK)
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		this.writeCatwalkPlatformBlockModels(
			context,
			Identifier.ofVanilla("${this.type.prefix}planks"),
			BigTechMod.modID("${this.type.prefix}catwalk_platform_rail")
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		this.writeCatwalkPlatformItemModels(
			context,
			Identifier.ofVanilla("${this.type.prefix}planks"),
			BigTechMod.modID("${this.type.prefix}catwalk_platform_rail")
		);
	}

	@Override
	public void genTextures(DataGenContext context) {
		context.writeToFile(
			context.blockTexturePath(context.suffixPath(this.getId(), "_rail")),
			new TextureGenerator()
			.mask(new CustomMask(0, 6, 16, 16, (Mask mask, int x, int y) -> {
				return ((x + 1) & 2) != 0;
			}))
			.processPixels((int x, int y, TextureGenerator texture) -> {
				float[] weights = GAUSSIAN_BLUR_1;
				float sum = rng2F(hash(0, x, y)) * weights[0];
				for (int offset = 1; offset < weights.length; offset++) {
					sum += rng2F(hash(0, x, y + offset)) * weights[offset];
					sum += rng2F(hash(0, x, y - offset)) * weights[offset];
				}
				sum = sum * 0.5F + 0.5F;
				if ((x & 3) == 2) sum -= 0.5F;
				texture.setGrayscale(x, y, sum, 1.0F);
			})
			.region(0, 4, 16, 6)
			.channels(ALPHA_CHANNEL)
			.fillColor(0.0F, 0.0F, 0.0F, 1.0F)
			.channels(RED_CHANNEL)
			.fillGrayscaleNoise(0)
			.blurHorizontal(GAUSSIAN_BLUR_1, OOBHandler.LOOP)
			.mul(0.5F)
			.add(0.5F)
			.region(0, 5, 16, 6)
			.add(-0.5F)
			.region(0, 4, 16, 6)
			.channels(RGB_CHANNELS)
			.swizzle(RED_OFFSET, RED_OFFSET, RED_OFFSET, ALPHA_OFFSET)
			.region(0, 4, 16, 16)
			.processChannels(gradient(this.type))
			.toByteArray()
		);
	}
}