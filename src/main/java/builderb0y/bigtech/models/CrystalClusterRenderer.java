package builderb0y.bigtech.models;

import java.util.List;
import java.util.function.Predicate;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderState.LayerRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.Directions;

@Environment(EnvType.CLIENT)
public class CrystalClusterRenderer implements BlockStateModel, ItemModel {

	public final Sprite sprite;
	public final ModelTransformation transformation;
	public final int spriteHash;

	public CrystalClusterRenderer(Sprite sprite, ModelTransformation transformation) {
		this.sprite = sprite;
		this.transformation = transformation;
		this.spriteHash = sprite.getContents().getId().hashCode();
	}

	@Override
	public void addParts(Random random, List<BlockModelPart> parts) {

	}

	@Override
	public Sprite particleSprite() {
		return this.sprite;
	}

	@Override
	public void emitQuads(
		QuadEmitter emitter,
		BlockRenderView blockView,
		BlockPos pos,
		BlockState state,
		Random random,
		Predicate<@Nullable Direction> cullTest
	) {
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();
		for (Direction direction : Directions.ALL) {
			BlockState adjacentState = blockView.getBlockState(mutablePos.set(pos, direction));
			if (!adjacentState.isOpaqueFullCube()) {
				this.emitQuads(this.getSeedForPosition(pos), emitter);
				return;
			}
		}
	}

	@Override
	public void update(ItemRenderState state, ItemStack stack, ItemModelManager resolver, ItemDisplayContext displayContext, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed) {
		state.addModelKey(this);
		LayerRenderState layer = state.newLayer();
		layer.setRenderLayer(TexturedRenderLayers.getItemEntityTranslucentCull());
		layer.setTransform(this.transformation.getTransformation(displayContext));
		if (stack.hasGlint()) {
			layer.setGlint(ItemRenderState.Glint.STANDARD);
		}
		this.emitQuads(this.spriteHash, layer.emitter());
	}

	public Vec3d getParticlePosition(int seed, BlockPos pos, Random random) {
		int cuboidCount = IntRng.nextRangedIntInclusive(seed, 3, 6);
		int chosenIndex = random.nextInt(cuboidCount);
		seed += chosenIndex * (6 * IntRng.INT_PHI);
		int
			sizeX = IntRng.nextRangedIntInclusive(seed += IntRng.INT_PHI, 4, 8),
			sizeY = IntRng.nextRangedIntInclusive(seed += IntRng.INT_PHI, 4, 8),
			sizeZ = IntRng.nextRangedIntInclusive(seed += IntRng.INT_PHI, 4, 8),
			posX = IntRng.nextBoundedIntInclusive(seed += IntRng.INT_PHI, 16 - sizeX),
			posY = IntRng.nextBoundedIntInclusive(seed += IntRng.INT_PHI, 16 - sizeY),
			posZ = IntRng.nextBoundedIntInclusive(seed += IntRng.INT_PHI, 16 - sizeZ);
		double
			minX = posX * 0.0625D,
			minY = posY * 0.0625D,
			minZ = posZ * 0.0625D,
			maxX = (posX + sizeX) * 0.0625D,
			maxY = (posY + sizeY) * 0.0625D,
			maxZ = (posZ + sizeZ) * 0.0625D;
		return new Vec3d(
			pos.getX() + (random.nextBoolean() ? maxX : minX),
			pos.getY() + (random.nextBoolean() ? maxY : minY),
			pos.getZ() + (random.nextBoolean() ? maxZ : minZ)
		);
	}

	public void emitQuads(int seed, QuadEmitter quadEmitter) {
		int cuboidCount = IntRng.nextRangedIntInclusive(seed, 3, 6);
		for (int cuboidIndex = 0; cuboidIndex < cuboidCount; cuboidIndex++) {
			int
				sizeX = IntRng.nextRangedIntInclusive (seed += IntRng.INT_PHI, 4, 8),
				sizeY = IntRng.nextRangedIntInclusive (seed += IntRng.INT_PHI, 4, 8),
				sizeZ = IntRng.nextRangedIntInclusive (seed += IntRng.INT_PHI, 4, 8),
				posX  = IntRng.nextBoundedIntInclusive(seed += IntRng.INT_PHI, 16 - sizeX),
				posY  = IntRng.nextBoundedIntInclusive(seed += IntRng.INT_PHI, 16 - sizeY),
				posZ  = IntRng.nextBoundedIntInclusive(seed += IntRng.INT_PHI, 16 - sizeZ);
			float
				minX = posX * 0.0625F,
				minY = posY * 0.0625F,
				minZ = posZ * 0.0625F,
				maxX = (posX + sizeX) * 0.0625F,
				maxY = (posY + sizeY) * 0.0625F,
				maxZ = (posZ + sizeZ) * 0.0625F;
			quad(quadEmitter, minX, minX, maxX, maxX, maxY, maxY, maxY, maxY, minZ, maxZ, maxZ, minZ, minX, maxX, minZ, maxZ, this.sprite, Direction.UP   );
			quad(quadEmitter, minX, minX, maxX, maxX, minY, minY, minY, minY, maxZ, minZ, minZ, maxZ, minX, maxX, maxZ, minZ, this.sprite, Direction.DOWN );
			quad(quadEmitter, maxX, maxX, minX, minX, maxY, minY, minY, maxY, minZ, minZ, minZ, minZ, maxX, minX, maxY, minY, this.sprite, Direction.NORTH);
			quad(quadEmitter, minX, minX, maxX, maxX, maxY, minY, minY, maxY, maxZ, maxZ, maxZ, maxZ, minX, maxX, maxY, minY, this.sprite, Direction.SOUTH);
			quad(quadEmitter, maxX, maxX, maxX, maxX, maxY, minY, minY, maxY, maxZ, maxZ, minZ, minZ, maxZ, minZ, maxY, minY, this.sprite, Direction.EAST );
			quad(quadEmitter, minX, minX, minX, minX, maxY, minY, minY, maxY, minZ, minZ, maxZ, maxZ, minZ, maxZ, maxY, minY, this.sprite, Direction.WEST );
		}
	}

	public static void quad(
		QuadEmitter quadEmitter,
		float x0, float x1, float x2, float x3,
		float y0, float y1, float y2, float y3,
		float z0, float z1, float z2, float z3,
		float u0, float u1, float v0, float v1,
		Sprite sprite,
		Direction direction
	) {
		float
			lerpedU0 = MathHelper.lerp(u0, sprite.getMinU(), sprite.getMaxU()),
			lerpedU1 = MathHelper.lerp(u1, sprite.getMinU(), sprite.getMaxU()),
			lerpedV0 = MathHelper.lerp(v0, sprite.getMinV(), sprite.getMaxV()),
			lerpedV1 = MathHelper.lerp(v1, sprite.getMinV(), sprite.getMaxV()),
			normalX = direction.getOffsetX(),
			normalY = direction.getOffsetY(),
			normalZ = direction.getOffsetZ();
		quadEmitter
		.pos(0, x0, y0, z0)
		.pos(1, x1, y1, z1)
		.pos(2, x2, y2, z2)
		.pos(3, x3, y3, z3)
		.uv(0, lerpedU0, lerpedV0)
		.uv(1, lerpedU0, lerpedV1)
		.uv(2, lerpedU1, lerpedV1)
		.uv(3, lerpedU1, lerpedV0)
		.normal(0, normalX, normalY, normalZ)
		.normal(1, normalX, normalY, normalZ)
		.normal(2, normalX, normalY, normalZ)
		.normal(3, normalX, normalY, normalZ)
		.nominalFace(direction)
		.color(0, -1)
		.color(1, -1)
		.color(2, -1)
		.color(3, -1)
		.emit();
	}

	public int getSeedForPosition(BlockPos pos) {
		return IntRng.permute(this.spriteHash, pos.getX(), pos.getY(), pos.getZ());
	}

	@Environment(EnvType.CLIENT)
	public static class UnbakedItemModel implements ItemModel.Unbaked, SimpleModel {

		public static final MapCodec<UnbakedItemModel> CODEC = BigTechAutoCodec.callerMapCodec();

		public final Identifier texture;

		public UnbakedItemModel(Identifier texture) {
			this.texture = texture;
		}

		@Override
		public ItemModel bake(BakeContext context) {
			return new CrystalClusterRenderer(
				context.blockModelBaker().getSpriteGetter().get(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, this.texture), this),
				context.blockModelBaker().getModel(Identifier.ofVanilla("block/block")).getTransformations()
			);
		}

		@Override
		public MapCodec<? extends ItemModel.Unbaked> getCodec() {
			return CODEC;
		}

		@Override
		public void resolve(Resolver resolver) {
			resolver.markDependency(Identifier.ofVanilla("block/block"));
		}

		@Override
		public String name() {
			return this.texture.toString();
		}
	}

	@Environment(EnvType.CLIENT)
	public static class UnbakedBlockModel implements UnbakedGrouped {

		public final Identifier texture;

		public UnbakedBlockModel(Identifier texture) {
			this.texture = texture;
		}

		@Override
		public void resolve(Resolver resolver) {
			resolver.markDependency(Identifier.ofVanilla("block/block"));
		}

		@Override
		public BlockStateModel bake(BlockState state, Baker baker) {
			BakedSimpleModel block = baker.getModel(Identifier.ofVanilla("block/block"));
			Sprite sprite = baker.getSpriteGetter().get(new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, this.texture), block);
			ModelTransformation transformations = block.getTransformations();
			return new CrystalClusterRenderer(sprite, transformations);
		}

		@Override
		public Object getEqualityGroup(BlockState state) {
			return this;
		}
	}
}