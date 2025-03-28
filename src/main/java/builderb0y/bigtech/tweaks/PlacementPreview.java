package builderb0y.bigtech.tweaks;

import java.util.Set;
import java.util.function.IntUnaryOperator;

import com.google.common.base.Predicates;
import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadView;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.WorldRenderer.BrightnessGetter;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.config.BigTechConfig;
import builderb0y.bigtech.mixins.MinecraftClient_CooldownAccessor;
import builderb0y.bigtech.mixins.MultiPhaseParameters_TextureAccess;
import builderb0y.bigtech.mixins.MultiPhase_PhasesAccess;
import builderb0y.bigtech.mixins.RenderLayerTexture_IdAccess;
import builderb0y.bigtech.util.Directions;

@Environment(EnvType.CLIENT)
public class PlacementPreview {

	/** blocks that throw exceptions when you try to render them. */
	public static final Set<Block> badBlocks = new ReferenceOpenHashSet<>();
	public static final PlacementPreview INSTANCE = new PlacementPreview();

	public int opacityTicks;

	public static void init() {
		WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(INSTANCE::render);
		ClientTickEvents.END_CLIENT_TICK.register(INSTANCE::tick);
	}

	public void tick(MinecraftClient client) {
		if (client.<MinecraftClient_CooldownAccessor>as().bigtech_getItemUseCooldown() == 0) {
			if (this.opacityTicks < 8) this.opacityTicks++;
		}
		else {
			this.opacityTicks = 0;
		}
	}

	public boolean render(WorldRenderContext renderContext, @Nullable HitResult hitResult) {
		if (!BigTechConfig.INSTANCE.get().client.placementPreview) {
			return true;
		}
		if (this.opacityTicks == 0) {
			return true;
		}
		if (!(hitResult instanceof BlockHitResult blockHitResult) || blockHitResult.getType() == HitResult.Type.MISS) {
			this.opacityTicks = 0;
			return true;
		}
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null || player.isSpectator()) {
			return true;
		}
		Hand hand;
		ItemStack stack = player.getStackInHand(hand = Hand.MAIN_HAND);
		if (!(stack.getItem() instanceof BlockItem)) {
			stack = player.getStackInHand(hand = Hand.OFF_HAND);
			if (!(stack.getItem() instanceof BlockItem)) {
				return true;
			}
		}
		BlockItem blockItem = (BlockItem)(stack.getItem());
		if (badBlocks.contains(blockItem.getBlock())) {
			return true;
		}
		World world = player.getWorld();
		ItemPlacementContext placementContext = blockItem.getPlacementContext(new ItemPlacementContext(player, hand, stack, blockHitResult));
		if (placementContext == null || !placementContext.canPlace()) {
			return true;
		}
		BlockPos placementPos = placementContext.getBlockPos();
		BlockState placementState = blockItem.getBlock().getPlacementState(placementContext);
		if (placementState == null) {
			return true;
		}
		BlockStateComponent component = stack.get(DataComponentTypes.BLOCK_STATE);
		if (component != null) {
			placementState = component.applyToState(placementState);
			Direction skip = (
				placementState.contains(Properties.DOUBLE_BLOCK_HALF)
				? switch (placementState.get(Properties.DOUBLE_BLOCK_HALF)) {
					case LOWER -> Direction.UP;
					case UPPER -> Direction.DOWN;
				}
				: null
			);
			BlockPos.Mutable mutablePos = new BlockPos.Mutable();
			for (Direction direction : Directions.ALL) {
				if (direction == skip) continue;
				placementState = placementState.getStateForNeighborUpdate(
					world,
					world,
					placementPos,
					direction,
					mutablePos.set(placementPos, direction),
					world.getBlockState(mutablePos),
					world.random
				);
			}
		}
		final BlockState placementState_ = placementState;
		int light = WorldRenderer.getLightmapCoordinates(BrightnessGetter.DEFAULT, world, placementState, placementPos);
		VertexConsumer buffer = renderContext.consumers().getBuffer(RenderLayer.getTranslucent());
		MatrixStack matrixStack = renderContext.matrixStack();
		IntUnaryOperator color = (int tintIndex) -> (
			MinecraftClient
			.getInstance()
			.getBlockColors()
			.getColor(placementState_, world, placementPos, tintIndex)
		);
		int alpha = (int)(
			Math.min(
				this.opacityTicks
				+
				renderContext
				.tickCounter()
				.getTickProgress(false),
				8.0F
			)
			* (127.0F / 8.0F)
			* (
				(
					(float)(
						Math.sin(
							world.getTime() * (Math.PI / 20.0D)
						)
					)
				)
				* 0.25F + 0.75F
			)
		);
		MutableMesh mesh = Renderer.get().mutableMesh();
		matrixStack.push();
		try {
			matrixStack.translate(
				placementPos.getX() - renderContext.camera().getPos().x,
				placementPos.getY() - renderContext.camera().getPos().y,
				placementPos.getZ() - renderContext.camera().getPos().z
			);
			MatrixStack.Entry matrices = matrixStack.peek();
			BlockStateModel model = MinecraftClient.getInstance().getBlockRenderManager().getModel(placementState);
			long seed = placementState.getRenderingSeed(placementPos);
			model.emitQuads(
				mesh.emitter(),
				world,
				placementPos,
				placementState,
				Random.create(seed),
				Predicates.alwaysFalse()
			);
			skipDouble:
			if (placementState.contains(Properties.DOUBLE_BLOCK_HALF)) {
				DoubleBlockHalf half = placementState.get(Properties.DOUBLE_BLOCK_HALF);
				BlockPos altPos;
				BlockState altState;
				float offsetY;
				switch (half) {
					case LOWER -> {
						altPos = placementPos.up();
						altState = placementState.with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
						offsetY = 1.0F;
					}
					case UPPER -> {
						altPos = placementPos.down();
						altState = placementState.with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
						offsetY = -1.0F;
					}
					default -> {
						break skipDouble;
					}
				}
				BlockStateModel altModel = MinecraftClient.getInstance().getBlockRenderManager().getModel(altState);
				long altSeed = altState.getRenderingSeed(altPos);
				mesh.emitter().pushTransform((MutableQuadView quad) -> {
					quad
					.pos(0, quad.x(0), quad.y(0) + offsetY, quad.z(0))
					.pos(1, quad.x(1), quad.y(1) + offsetY, quad.z(1))
					.pos(2, quad.x(2), quad.y(2) + offsetY, quad.z(2))
					.pos(3, quad.x(3), quad.y(3) + offsetY, quad.z(3));
					return true;
				});
				try {
					altModel.emitQuads(
						mesh.emitter(),
						world,
						altPos,
						altState,
						Random.create(altSeed),
						Predicates.alwaysFalse()
					);
				}
				finally {
					mesh.emitter().popTransform();
				}
			}
			mesh.forEach((QuadView quad) -> {
				int shade = Math.min((int)(world.getBrightness(quad.lightFace(), true) * 256.0F), 255);
				pipe(quad, buffer, 0, light, matrices, color, shade, alpha);
				pipe(quad, buffer, 1, light, matrices, color, shade, alpha);
				pipe(quad, buffer, 2, light, matrices, color, shade, alpha);
				pipe(quad, buffer, 3, light, matrices, color, shade, alpha);
			});
			doneWithBlockEntity:
			if (placementState.hasBlockEntity()) {
				BlockEntity blockEntity = ((BlockEntityProvider)(placementState.getBlock())).createBlockEntity(placementPos, placementState);
				if (blockEntity == null) break doneWithBlockEntity;
				blockEntity.setWorld(world);
				BlockEntityRenderer renderer = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().get(blockEntity);
				if (renderer == null) break doneWithBlockEntity;
				renderer.render(
					blockEntity,
					renderContext.tickCounter().getTickProgress(false),
					matrixStack,
					(RenderLayer layer) -> {
						//this is probably not the best way to do this.
						//if anyone knows a better way, please tell me.
						if (layer.getVertexFormat() == VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL) {
							layer = RenderLayer.getTranslucent();
						}
						else if (
							layer.getVertexFormat() == VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL &&
							layer instanceof MultiPhase_PhasesAccess multiPhase &&
							multiPhase.bigtech_getPhases().as() instanceof MultiPhaseParameters_TextureAccess params &&
							params.bigtech_getTexture() instanceof RenderLayerTexture_IdAccess texture &&
							texture.bigtech_getId().isPresent()
						) {
							layer = RenderLayer.getEntityTranslucent(texture.bigtech_getId().get());
						}
						return new DelegatingVertexConsumer(renderContext.consumers().getBuffer(layer)) {

							@Override
							public VertexConsumer color(int r, int g, int b, int a) {
								return super.color(r, g, b, a * alpha / 255);
							}
						};
					},
					light,
					OverlayTexture.DEFAULT_UV,
					renderContext.camera().getPos()
				);
			}
		}
		catch (Exception exception) {
			badBlocks.add(blockItem.getBlock());
			BigTechMod.LOGGER.error("Failed to render block " + Registries.BLOCK.getId(blockItem.getBlock()) + ':', exception);
		}
		finally {
			matrixStack.pop();
		}
		return false;
	}

	public static void pipe(
		QuadView quad,
		VertexConsumer buffer,
		int index,
		int light,
		MatrixStack.Entry matrices,
		IntUnaryOperator colors,
		int shade,
		int alpha
	) {
		int tint = quad.tintIndex() >= 0 ? colors.applyAsInt(quad.tintIndex()) : -1;
		int color = mulColor(quad.color(index), tint, shade, alpha);
		buffer
		.vertex(matrices, quad.x(index), quad.y(index), quad.z(index))
		.color(color)
		.texture(quad.u(index), quad.v(index))
		.light(light)
		.normal(quad.normalX(index), quad.normalY(index), quad.normalZ(index));
	}

	@SuppressWarnings("SSBasedInspection")
	public static int mulColor(int color1, int color2, int shade, int alpha) {
		return ColorHelper.getArgb(
			ColorHelper.getAlpha(color1) * ColorHelper.getAlpha(color2) * alpha / (255 * 255),
			ColorHelper.getRed(color1) * ColorHelper.getRed(color2) * shade / (255 * 255),
			ColorHelper.getGreen(color1) * ColorHelper.getGreen(color2) * shade / (255 * 255),
			ColorHelper.getBlue(color1) * ColorHelper.getBlue(color2) * shade / (255 * 255)
		);
	}

	public static class DelegatingVertexConsumer implements VertexConsumer {

		public final VertexConsumer delegate;

		public DelegatingVertexConsumer(VertexConsumer delegate) {
			this.delegate = delegate;
		}

		@Override
		public VertexConsumer vertex(float x, float y, float z) {
			this.delegate.vertex(x, y, z);
			return this;
		}

		@Override
		public VertexConsumer color(int red, int green, int blue, int alpha) {
			this.delegate.color(red, green, blue, alpha);
			return this;
		}

		@Override
		public VertexConsumer texture(float u, float v) {
			this.delegate.texture(u, v);
			return this;
		}

		@Override
		public VertexConsumer overlay(int u, int v) {
			this.delegate.overlay(u, v);
			return this;
		}

		@Override
		public VertexConsumer light(int u, int v) {
			this.delegate.light(u, v);
			return this;
		}

		@Override
		public VertexConsumer normal(float x, float y, float z) {
			this.delegate.normal(x, y, z);
			return this;
		}
	}
}