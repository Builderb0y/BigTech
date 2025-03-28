package builderb0y.bigtech.models;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Predicate;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.MeshBakedGeometry;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderState.LayerRenderState;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.model.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.blockEntities.PrismBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.util.Directions;

@Environment(EnvType.CLIENT)
public class PrismRenderer implements BlockStateModel, ItemModel {

	public static final RenderMaterial TRANSLUCENT = Renderer.get().materialFinder().blendMode(BlendMode.TRANSLUCENT).find();
	public static final EnumMap<BeamDirection, Matrix4f> ROTATIONS = new EnumMap<>(BeamDirection.class);
	static {
		initMatrices();
	}

	public static void initMatrices() {
		final float edgeAngle = (float)(Math.atan2(1.0D, 1.0D));
		final float cornerAngle = (float)(Math.atan2(1.0D, Math.sqrt(2.0D)));
		for (BeamDirection direction : BeamDirection.VALUES) {
			if (direction.x == 0 && direction.z == 0) continue;
			Matrix4f matrix = new Matrix4f().translation(0.5F, 0.5F, 0.5F);
			matrix.rotateY(-(float)(Math.atan2(direction.x, -direction.z)));
			if (direction.y != 0) {
				matrix.rotateX(direction.y * (direction.type == BeamDirection.Type.CORNER ? cornerAngle : edgeAngle));
			}
			matrix.translate(-0.5F, -0.5F, -0.5F);
			ROTATIONS.put(direction, matrix);
		}
		ROTATIONS.put(
			BeamDirection.UP,
			new Matrix4f()
				.translation(0.5F, 0.5F, 0.5F)
				.rotateX((float)(Math.PI * 0.5D))
				.translate(-0.5F, -0.5F, -0.5F)
		);
		ROTATIONS.put(
			BeamDirection.DOWN,
			new Matrix4f()
				.translation(0.5F, 0.5F, 0.5F)
				.rotateX((float)(Math.PI * -0.5D))
				.translate(-0.5F, -0.5F, -0.5F)
		);
	}

	public final BakedSimpleModel baseModel, lensModel;
	public final BakedGeometry baseGeometry, lensGeometry;

	public PrismRenderer(
		BakedSimpleModel baseModel,
		BakedSimpleModel lensModel,
		BakedGeometry baseGeometry,
		BakedGeometry lensGeometry
	) {
		this.baseModel = baseModel;
		this.lensModel = lensModel;
		this.baseGeometry = baseGeometry;
		this.lensGeometry = lensGeometry;
	}

	@Override
	public void addParts(Random random, List<BlockModelPart> parts) {

	}

	@Override
	public Sprite particleSprite() {
		SpriteIdentifier id = this.baseModel.getTextures().get("particle");
		return id != null ? id.getSprite() : null;
	}

	@Override
	public void emitQuads(
		QuadEmitter emitter,
		BlockRenderView world,
		BlockPos pos,
		BlockState state,
		Random random,
		Predicate<@Nullable Direction> cullTest
	) {
		pipe(emitter, this.baseGeometry);
		if (world.getBlockEntity(pos) instanceof PrismBlockEntity prism && prism.hasAnyLenses()) {
			pipeRotations(emitter, this.lensGeometry, prism.lenses);
		}
	}

	@Override
	public @Nullable Object createGeometryKey(BlockRenderView world, BlockPos pos, BlockState state, Random random) {
		return world.getBlockEntity(pos) instanceof PrismBlockEntity prism && prism.hasAnyLenses() ? new Key(prism.lenses) : Key.ZERO;
	}

	public static record Key(int lenses) {

		public static final Key ZERO = new Key(0);
	}

	@Override
	public void update(ItemRenderState state, ItemStack stack, ItemModelManager resolver, ItemDisplayContext displayContext, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed) {
		LayerRenderState layer = state.newLayer();
		layer.setRenderLayer(RenderLayer.getTranslucent());
		layer.setTransform(this.baseModel.getTransformations().getTransformation(displayContext));
		if (stack.hasGlint()) {
			layer.setGlint(ItemRenderState.Glint.STANDARD);
		}
		QuadEmitter emitter = layer.emitter();
		pipe(emitter, this.baseGeometry);
		NbtComponent data = stack.get(DataComponentTypes.BLOCK_ENTITY_DATA);
		int lenses = data != null ? data.getNbt().getInt("lenses", 0) & PrismBlockEntity.FLAG_MASK : 0;
		if (lenses != 0) {
			pipeRotations(emitter, this.lensGeometry, lenses);
		}
	}


	public static void pipeRotations(QuadEmitter emitter, BakedGeometry geometry, int lenses) {
		Matrix4f matrix = new Matrix4f();
		Vector4f position = new Vector4f();
		emitter.pushTransform((MutableQuadView quad) -> {
			for (int index = 0; index < 4; index++) {
				position.x = quad.x(index);
				position.y = quad.y(index);
				position.z = quad.z(index);
				position.w = 1.0F;
				matrix.transform(position);
				quad.pos(index, position.x, position.y, position.z);
			}
			return true;
		});
		try {
			for (BeamDirection direction : BeamDirection.VALUES) {
				if ((lenses & direction.flag()) != 0) {
					matrix.set(PrismRenderer.ROTATIONS.get(direction));
					PrismRenderer.pipe(emitter, geometry);
				}
			}
		}
		finally {
			emitter.popTransform();
		}
	}

	public static void pipe(QuadEmitter emitter, BakedGeometry geometry) {
		if (geometry instanceof MeshBakedGeometry meshGeometry) {
			meshGeometry.getMesh().outputTo(emitter);
		}
		else {
			for (Direction side : Directions.ALL_INCLUDING_NULL) {
				for (BakedQuad quad : geometry.getQuads(side)) {
					emitter.fromVanilla(quad, TRANSLUCENT, side);
					emitter.emit();
				}
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static class UnbakedItemModel implements ItemModel.Unbaked {

		public static final MapCodec<UnbakedItemModel> CODEC = BigTechAutoCodec.callerMapCodec();

		public final Identifier prism, lens;

		public UnbakedItemModel(Identifier prism, Identifier lens) {
			this.prism = prism;
			this.lens = lens;
		}

		@Override
		public ItemModel bake(BakeContext context) {
			BakedSimpleModel prismModel = context.blockModelBaker().getModel(this.prism);
			BakedSimpleModel lensModel = context.blockModelBaker().getModel(this.lens);
			BakedGeometry prismGeometry = prismModel.bakeGeometry(prismModel.getTextures(), context.blockModelBaker(), ModelRotation.X0_Y0);
			BakedGeometry lensGeometry = lensModel.bakeGeometry(lensModel.getTextures(), context.blockModelBaker(), ModelRotation.X0_Y0);
			return new PrismRenderer(prismModel, lensModel, prismGeometry, lensGeometry);
		}

		@Override
		public MapCodec<? extends ItemModel.Unbaked> getCodec() {
			return CODEC;
		}

		@Override
		public void resolve(Resolver resolver) {
			resolver.markDependency(this.prism);
			resolver.markDependency(this.lens);
		}
	}

	@Environment(EnvType.CLIENT)
	public static class UnbakedBlockModel implements UnbakedGrouped {

		@Override
		public void resolve(Resolver resolver) {
			resolver.markDependency(BigTechMod.modID("block/prism_base"));
			resolver.markDependency(BigTechMod.modID("block/prism_lens"));
		}

		@Override
		public BlockStateModel bake(BlockState state, Baker baker) {
			BakedSimpleModel
				baseModel = baker.getModel(BigTechMod.modID("block/prism_base")),
				lensModel = baker.getModel(BigTechMod.modID("block/prism_lens"));
			BakedGeometry
				baseGeometry = baseModel.bakeGeometry(baseModel.getTextures(), baker, ModelRotation.X0_Y0),
				lensGeometry = lensModel.bakeGeometry(lensModel.getTextures(), baker, ModelRotation.X0_Y0);
			return new PrismRenderer(baseModel, lensModel, baseGeometry, lensGeometry);
		}

		@Override
		public Object getEqualityGroup(BlockState state) {
			return this;
		}
	}
}