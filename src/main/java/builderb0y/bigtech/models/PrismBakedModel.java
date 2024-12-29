package builderb0y.bigtech.models;

import java.awt.image.renderable.RenderContext;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.blockEntities.PrismBlockEntity;
import builderb0y.bigtech.util.WorldHelper;

public class PrismBakedModel implements BakedModel {

	public static final EnumMap<BeamDirection, Matrix4f> ROTATIONS = new EnumMap<>(BeamDirection.class);
	static {
		initMatrices();
	}

	public final BakedModel baseModel, lensModel;
	public final int itemLenses;

	public PrismBakedModel(BakedModel baseModel, BakedModel lensModel, int itemLenses) {
		this.baseModel  = baseModel;
		this.lensModel  = lensModel;
		this.itemLenses = itemLenses;
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

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitBlockQuads(QuadEmitter emitter, BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, Predicate<@Nullable Direction> cullTest) {
		this.baseModel.emitBlockQuads(emitter, world, state, pos, randomSupplier, cullTest);
		PrismBlockEntity prism = WorldHelper.getBlockEntity(world, pos, PrismBlockEntity.class);
		if (prism != null && prism.hasAnyLenses()) {
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
					if (prism.hasLens(direction)) {
						matrix.set(ROTATIONS.get(direction));
						this.lensModel.emitBlockQuads(emitter, world, state, pos, randomSupplier, cullTest);
					}
				}
			}
			finally {
				emitter.popTransform();
			}
		}
	}

	@Override
	public void emitItemQuads(QuadEmitter emitter, Supplier<Random> randomSupplier) {
		this.baseModel.emitItemQuads(emitter, randomSupplier);
		int lenses = this.itemLenses;
		if (lenses != 0) {
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
						matrix.set(ROTATIONS.get(direction));
						this.lensModel.emitItemQuads(emitter, randomSupplier);
					}
				}
			}
			finally {
				emitter.popTransform();
			}
		}
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
		return Collections.emptyList();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean hasDepth() {
		return true;
	}

	@Override
	public boolean isSideLit() {
		return true;
	}

	@Override
	public Sprite getParticleSprite() {
		return this.baseModel.getParticleSprite();
	}

	@Override
	public ModelTransformation getTransformation() {
		return this.baseModel.getTransformation();
	}
}