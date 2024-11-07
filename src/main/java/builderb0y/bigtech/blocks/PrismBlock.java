package builderb0y.bigtech.blocks;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;
import org.joml.Intersectiond;
import org.joml.Vector2d;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import builderb0y.bigtech.api.BeamInteractor;
import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.SpreadingBeamSegment;
import builderb0y.bigtech.blockEntities.PrismBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.items.FunctionalItems;
import builderb0y.bigtech.util.WorldHelper;

public class PrismBlock extends Block implements BeamInteractor, BlockEntityProvider, Waterloggable {

	public static final double
		RADIUS = 0.4375D,
		RADIUS_SQUARED = RADIUS * RADIUS;
	public static final VoxelShape SHAPE = VoxelShapes.cuboid(0.0625D, 0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);

	public static final MapCodec<PrismBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapCodec getCodec() {
		return CODEC;
	}

	public PrismBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this
			.getDefaultState()
			.with(Properties.WATERLOGGED, Boolean.FALSE)
		);
	}

	@Override
	public ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (stack.isEmpty() || stack.isOf(FunctionalItems.LENS)) {
			//workaround for bug in vanilla code:
			//returning FAIL from the main hand doesn't prevent the offhand from being used.
			if (hand == Hand.OFF_HAND) {
				ItemStack mainHand = player.getStackInHand(Hand.MAIN_HAND);
				if (mainHand.isEmpty() || mainHand.isOf(FunctionalItems.LENS)) {
					return ActionResult.FAIL;
				}
			}
			PrismBlockEntity prism = WorldHelper.getBlockEntity(world, pos, PrismBlockEntity.class);
			if (prism != null) {
				Vec3d eyePos = player.getEyePos();
				Vec3d lookDir = player.getRotationVector();
				Vector2d intersectionDistances = new Vector2d();
				//step 1: ray trace the sphere.
				//this turns out to be trivial because joml has a method to do that for me.
				//thanks joml!
				if (
					Intersectiond.intersectRaySphere(
						eyePos.x,
						eyePos.y,
						eyePos.z,
						lookDir.x,
						lookDir.y,
						lookDir.z,
						pos.getX() + 0.5D,
						pos.getY() + 0.5D,
						pos.getZ() + 0.5D,
						RADIUS_SQUARED,
						intersectionDistances
					)
				) {
					//step 2: use the intersection data to determine
					//which lens the player wants to interact with.
					double rayDistance = (
						MathHelper.squaredMagnitude(
							eyePos.x - (pos.getX() + 0.5D),
							eyePos.y - (pos.getY() + 0.5D),
							eyePos.z - (pos.getZ() + 0.5D)
						)
						< RADIUS_SQUARED
						? intersectionDistances.y
						: intersectionDistances.x
					);
					double intersectionX = eyePos.x + lookDir.x * rayDistance - (pos.getX() + 0.5D);
					double intersectionY = eyePos.y + lookDir.y * rayDistance - (pos.getY() + 0.5D);
					double intersectionZ = eyePos.z + lookDir.z * rayDistance - (pos.getZ() + 0.5D);
					double scalar = 1.0D / Math.max(Math.max(Math.abs(intersectionX), Math.abs(intersectionY)), Math.abs(intersectionZ));
					intersectionX *= scalar;
					intersectionY *= scalar;
					intersectionZ *= scalar;
					BeamDirection direction = BeamDirection.getUnchecked(
						MathHelper.floor(intersectionX + 0.5D),
						MathHelper.floor(intersectionY + 0.5D),
						MathHelper.floor(intersectionZ + 0.5D)
					);
					//step 3: interact with the lens.
					//in other words, add a lens or remove it,
					//if we added a lens, decrement the stack in the player's hand,
					//except in creative.
					//if we removed a lens, add a new lens to the player's inventory,
					//except if the player is both in creative and already has a lens.
					//and also don't modify the prism or the lens on the client.
					//only do that on the server.
					if (stack.isEmpty()) {
						if (world.isClient) {
							if (prism.hasLens(direction)) {
								return ActionResult.SUCCESS;
							}
							else {
								return ActionResult.FAIL;
							}
						}
						else {
							if (prism.removeLens(direction)) {
								ItemStack toInsert = FunctionalItems.LENS.getDefaultStack();
								if (player.getAbilities().creativeMode) {
									if (!player.getInventory().contains(toInsert)) {
										player.setStackInHand(hand, toInsert);
									}
								}
								else {
									player.getInventory().offerOrDrop(toInsert);
								}
								return ActionResult.SUCCESS;
							}
							else {
								return ActionResult.FAIL;
							}
						}
					}
					else {
						if (world.isClient) {
							if (prism.hasLens(direction)) {
								return ActionResult.FAIL;
							}
							else {
								return ActionResult.SUCCESS;
							}
						}
						else {
							if (prism.addLens(direction)) {
								if (!player.getAbilities().creativeMode) {
									stack.decrement(1);
								}
								return ActionResult.SUCCESS;
							}
							else {
								return ActionResult.FAIL;
							}
						}
					}
				}
				else {
					//ray does not intersect with prism. no action is performed in this case.
					return ActionResult.FAIL;
				}
			}
			else {
				//BlockEntity missing. can't perform any actions in this case.
				return ActionResult.FAIL;
			}
		}
		else {
			//holding an item that we don't care about.
			//in other words, not an empty hand or a lens.
			return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
		}
	}

	@Override
	public boolean spreadOut(ServerWorld world, BlockPos pos, BlockState state, SpreadingBeamSegment inputSegment) {
		PrismBlockEntity prism = WorldHelper.getBlockEntity(world, pos, PrismBlockEntity.class);
		if (prism != null && prism.hasAnyLenses()) {
			double baseDistance = inputSegment.distanceRemaining() / prism.countLenses();
			for (BeamDirection direction : BeamDirection.VALUES) {
				if (prism.hasLens(direction)) {
					inputSegment.beam().addSegment(world, inputSegment.extend(baseDistance - direction.type.magnitude, direction));
				}
			}
		}
		else {
			inputSegment.beam().addSegment(world, inputSegment.terminate());
		}
		return true;
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		PrismBlockEntity prism = WorldHelper.getBlockEntity(world, pos, PrismBlockEntity.class);
		if (prism != null && prism.hasAnyLenses()) {
			ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(FunctionalItems.LENS, prism.countLenses()));
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PrismBlockEntity(pos, state);
	}

	@Override
	public BlockState getStateForNeighborUpdate(
		BlockState state,
		WorldView world,
		ScheduledTickView tickView,
		BlockPos pos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		Random random
	) {
		if (state.get(Properties.WATERLOGGED)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.WATERLOGGED, context.getWorld().getFluidState(context.getBlockPos()).isEqualAndStill(Fluids.WATER));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).getDefaultState();
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.WATERLOGGED);
	}
}