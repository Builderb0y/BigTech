package builderb0y.bigtech.models;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
	public final ModelTransformation transformation;

	public PrismBakedModel(BakedModel baseModel, BakedModel lensModel, ModelTransformation transformation) {
		this.baseModel      = baseModel;
		this.lensModel      = lensModel;
		this.transformation = transformation;
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
	public void emitBlockQuads(BlockRenderView world, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		this.baseModel.emitBlockQuads(world, state, pos, randomSupplier, context);
		PrismBlockEntity prism = WorldHelper.getBlockEntity(world, pos, PrismBlockEntity.class);
		if (prism != null && prism.hasAnyLenses()) {
			Matrix4f matrix = new Matrix4f();
			Vector4f position = new Vector4f();
			context.pushTransform(quad -> {
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
						this.lensModel.emitBlockQuads(world, state, pos, randomSupplier, context);
					}
				}
			}
			finally {
				context.popTransform();
			}
		}
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
		this.baseModel.emitItemQuads(stack, randomSupplier, context);
		NbtComponent blockEntityTag = stack.get(DataComponentTypes.BLOCK_ENTITY_DATA);
		if (blockEntityTag != null) {
			int lenses = blockEntityTag.getNbt().getInt("lenses") & PrismBlockEntity.FLAG_MASK;
			if (lenses != 0) {
				Matrix4f matrix = new Matrix4f();
				Vector4f position = new Vector4f();
				context.pushTransform((MutableQuadView quad) -> {
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
							this.lensModel.emitItemQuads(stack, randomSupplier, context);
						}
					}
				}
				finally {
					context.popTransform();
				}
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
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getParticleSprite() {
		return this.baseModel.getParticleSprite();
	}

	@Override
	public ModelTransformation getTransformation() {
		return this.transformation;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return ModelOverrideList.EMPTY;
	}
}