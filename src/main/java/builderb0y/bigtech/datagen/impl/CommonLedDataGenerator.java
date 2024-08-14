package builderb0y.bigtech.datagen.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.SplittableRandom;
import java.util.random.RandomGenerator;
import javax.imageio.ImageIO;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import net.minecraft.item.Items;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.math.MathHelper;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.DecoItems;
import builderb0y.bigtech.registrableCollections.LedRegistrableCollection.LedColor;

public class CommonLedDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		this.writeTexture(context);
		this.writeBaseRecipe(context);
	}

	public void writeTexture(DataGenContext context) {
		BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		int[] pixel = new int[1];
		Vector3f scratch = new Vector3f();
		for (LedColor color : LedColor.VALUES) {
			for (int on = 0; on <= 1; on++) {
				int startX = color.startX(on);
				int startY = color.startY(on);
				for (int offsetY = 0; offsetY < 6; offsetY++) {
					for (int offsetX = 0; offsetX < 4; offsetX++) {
						double distance = Math.sqrt(MathHelper.square(offsetX - 1.25) + MathHelper.square(offsetY - 1.25D)) / 4.75D;
						if (!(distance >= 0.0D && distance <= 1.0D)) throw new AssertionError("invalid distance");
						color.centerColor.lerp(color.edgeColor, (float)(distance), scratch);
						if (on == 0) scratch.mul(0.5F);
						pixel[0] = pack(scratch);
						image.getRaster().setDataElements(startX + offsetX, startY + offsetY, pixel);
					}
				}
			}
		}
		RandomGenerator random = new SplittableRandom("LED texture".hashCode());
		for (int[] params : new int[][] { { 0, 24, 8 }, { 8, 30, 2 } }) {
			for (int y = 0; y < params[2]; y++) {
				for (int x = 0; x < 8; x++) {
					if (y >= 2 && y < 6 && x >= 2 && x < 6) continue;
					float value = (
						+ random.nextFloat()
						+ random.nextFloat()
						+ random.nextFloat()
						+ random.nextFloat()
					)
					* 0.03125F + 0.6875F;
					if (y == 0 || y == 7 || x == 0 || x == 7) {
						if (x + y > 7) value *= 0.75F;
						else if (x + y < 7) value += 0.125F;
					}
					pixel[0] = pack(scratch.set(value));
					image.getRaster().setDataElements(x + params[0], y + params[1], pixel);
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
		context.writeToFile(context.blockTexturePath(BigTechMod.modID("led")), stream.toByteArray());
	}

	public static int pack(Vector3fc color) {
		return (
			0xFF000000
			| (Math.min((int)(color.x() * 256.0F), 255) << 16)
			| (Math.min((int)(color.y() * 256.0F), 255) <<  8)
			| (Math.min((int)(color.z() * 256.0F), 255)      )
		);
	}

	public void writeBaseRecipe(DataGenContext context) {
		context.writeToFile(
			context.recipePath(BigTechMod.modID("base_led")),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.group("bigtech:leds")
			.pattern("g", "r", "s")
			.where('g', ConventionalItemTags.GLOWSTONE_DUSTS)
			.where('r', ConventionalItemTags.REDSTONE_DUSTS)
			.where('s', Items.SMOOTH_STONE_SLAB)
			.result(DecoItems.LEDS.white)
			.count(4)
			.toString()
		);
	}
}