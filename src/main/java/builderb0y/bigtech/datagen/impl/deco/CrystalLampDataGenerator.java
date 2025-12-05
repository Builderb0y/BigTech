package builderb0y.bigtech.datagen.impl.deco;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import builderb0y.bigtech.blocks.BigTechBlockTags;
import builderb0y.bigtech.datagen.base.*;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.impl.deco.CrystalLampDataGenerator.CommonCrystalLampDataGenerator;
import builderb0y.bigtech.items.BigTechItemTags;
import builderb0y.bigtech.items.MaterialItems;
import builderb0y.bigtech.registrableCollections.CrystalRegistrableCollection.CrystalColor;

@Dependencies(CommonCrystalLampDataGenerator.class)
public class CrystalLampDataGenerator extends BasicBlockDataGenerator {

	public final CrystalColor color;

	public CrystalLampDataGenerator(BlockItem blockItem, CrystalColor color) {
		super(blockItem);
		this.color = color;
	}

	@Override
	public void run(DataGenContext context) {
		super.run(context);
		this.writeTextures(context);
	}

	public void writeTextures(DataGenContext context) {
		final int
			resolutionX = 16,
			resolutionY = 16,
			frames = 40;
		enum TexturePart {

			INNER_GLOW {

				@Override
				public int computeColor(double uvX, double uvY, double time, int on, CrystalColor color) {
					double brightness = animate(Math.max(Math.abs(uvX), Math.abs(uvY)) * 0.125D, time);
					if (on == 0) brightness *= 0.5D;
					return multiplyColor(brightness, color);
				}
			},

			GRAY {

				@Override
				public int computeColor(double uvX, double uvY, double time, int on, CrystalColor color) {
					int brightness = 79;
					if (of(uvX - 1.0D, uvY) != this || of(uvX - 1.0D, uvY - 1.0D) != this || of(uvX, uvY - 1.0D) != this) brightness += 0x10;
					if (of(uvX + 1.0D, uvY) != this || of(uvX + 1.0D, uvY + 1.0D) != this || of(uvX, uvY + 1.0D) != this) brightness -= 0x10;
					return 0xFF000000 | (brightness << 16) | (brightness << 8) | brightness;
				}
			},

			OUTER_GLOW {

				@Override
				public int computeColor(double uvX, double uvY, double time, int on, CrystalColor color) {
					double brightness = animate(MathHelper.getLerpProgress(Math.min(Math.abs(uvX), Math.abs(uvY)), 8.0D, 4.0D), time);
					if (on == 0) brightness *= 0.5D;
					return multiplyColor(brightness, color);
				}
			};

			public static TexturePart of(double uvX, double uvY) {
				double min = Math.min(Math.abs(uvX), Math.abs(uvY));
				if (min < 1.0D) return INNER_GLOW;
				if (min < 4.0D) return GRAY;
				return OUTER_GLOW;
			}

			public abstract int computeColor(double uvX, double uvY, double time, int on, CrystalColor color);

			public static double animate(double x, double time) {
				double pulse = Math.sin((x - time) * Math.PI) * 0.5D + 0.5D;
				pulse *= pulse;
				pulse *= pulse;
				return MathHelper.lerp(pulse, 1.0D - x, 1.0D) * 0.5D + 0.5D;
			}

			public static int multiplyColor(double brightness, CrystalColor color) {
				int red   = (int)(brightness * color.red   + 0.5D);
				int green = (int)(brightness * color.green + 0.5D);
				int blue  = (int)(brightness * color.blue  + 0.5D);
				return 0xFF000000 | (red << 16) | (green << 8) | blue;
			}
		}
		int[] pixel = new int[1];
		for (int on = 0; on <= 1; on++) {
			BufferedImage image = new BufferedImage(resolutionX, resolutionY * frames, BufferedImage.TYPE_INT_ARGB);
			for (int frame = 0; frame < frames; frame++) {
				int startY = frame * resolutionY;
				double time = ((double)(frame)) / ((double)(frames)) * 2.0D + 1.0D;
				for (int relativeY = 0; relativeY < resolutionY; relativeY++) {
					for (int relativeX = 0; relativeX < resolutionX; relativeX++) {
						double uvX = relativeX + (0.5D - 8.0D);
						double uvY = relativeY + (0.5D - 8.0D);
						pixel[0] = TexturePart.of(uvX, uvY).computeColor(uvX, uvY, time, on, this.color);
						image.getRaster().setDataElements(relativeX, relativeY + startY, pixel);
					}
				}
			}
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			try {
				ImageIO.write(image, "png", stream);
			}
			catch (IOException exception) {
				throw new AssertionError("ByteArrayOutputStream threw an IOException?", exception);
			}
			String basePath = context.blockTexturePath(context.suffixPath(this.getId(), on != 0 ? "_on" : "_off"));
			context.writeToFile(
				basePath,
				stream.toByteArray()
			);
			context.writeToFile(
				basePath + ".mcmeta",
				"""
				{
					"animation": {}
				}"""
			);
		}
	}

	@Override
	public BlockStateJsonVariant createVariant(DataGenContext context, BlockState state) {
		return new BlockStateJsonVariant(state, context.prefixSuffixPath("block/", this.getId(), state.get(Properties.LIT) ? "_on" : "_off").toString(), null, null);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_on")),
			new Models.block.cube_all()
			.all(context.suffixPath(this.getId(), "_on"))
			.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.getId(), "_off")),
			new Models.block.cube_all()
			.all(context.suffixPath(this.getId(), "_off"))
			.toString()
		);
	}

	@Override
	public Identifier getItemModelParent(DataGenContext context) {
		return context.suffixPath(this.getId(), "_on");
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {

	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {

	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BigTechBlockTags.CRYSTAL_LAMPS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(BigTechItemTags.CRYSTAL_LAMPS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.BUILDING)
			.group("bigtech:crystal_lamps")
			.pattern("ngn", "gcg", "ngn")
			.where('n', ConventionalItemTags.IRON_NUGGETS)
			.where('g', BigTechItemTags.COLORLESS_GLASS_MATERIALS)
			.where('c', MaterialItems.CRYSTAL_CLUSTERS.get(this.color))
			.result(this.getId())
			.toString()
		);
	}

	public static class CommonCrystalLampDataGenerator implements DataGenerator {

		@Override
		public void run(DataGenContext context) {
			context.getTags(MiningToolTags.PICKAXE).add(BigTechBlockTags.CRYSTAL_LAMPS);
			context.getTags(BigTechItemTags.COLORLESS_GLASS_MATERIALS).addAll(ConventionalItemTags.GLASS_BLOCKS_COLORLESS, ConventionalItemTags.GLASS_PANES_COLORLESS);
		}
	}
}