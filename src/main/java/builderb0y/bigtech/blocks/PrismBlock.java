package builderb0y.bigtech.blocks;

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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import builderb0y.bigtech.api.BeamInteractor;
import builderb0y.bigtech.beams.base.BeamDirection;
import builderb0y.bigtech.beams.base.BeamSegment;
import builderb0y.bigtech.blockEntities.PrismBlockEntity;
import builderb0y.bigtech.items.BigTechItems;
import builderb0y.bigtech.util.WorldHelper;

public class PrismBlock extends Block implements BeamInteractor, BlockEntityProvider, Waterloggable {

	public static final double
		RADIUS = 0.4375D,
		RADIUS_SQUARED = RADIUS * RADIUS;
	public static final VoxelShape SHAPE = VoxelShapes.cuboid(0.0625D, 0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);

	public PrismBlock(Settings settings) {
		super(settings);
		this.defaultState = this.defaultState.with(Properties.WATERLOGGED, Boolean.FALSE);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		if (stack.isEmpty || stack.isOf(BigTechItems.LENS)) {
			//workaround for bug in vanilla code:
			//returning FAIL from the main hand doesn't prevent the offhand from being used.
			if (hand == Hand.OFF_HAND) {
				ItemStack mainHand = player.getStackInHand(Hand.MAIN_HAND);
				if (mainHand.isEmpty || mainHand.isOf(BigTechItems.LENS)) {
					return ActionResult.FAIL;
				}
			}
			PrismBlockEntity prism = WorldHelper.getBlockEntity(world, pos, PrismBlockEntity.class);
			if (prism != null) {
				Vec3d eyePos = player.eyePos;
				Vec3d lookDir = player.rotationVector;
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
						pos.x + 0.5D,
						pos.y + 0.5D,
						pos.z + 0.5D,
						RADIUS_SQUARED,
						intersectionDistances
					)
				) {
					//step 2: use the intersection data to determine
					//which lens the player wants to interact with.
					double rayDistance = (
						MathHelper.squaredMagnitude(
							eyePos.x - (pos.x + 0.5D),
							eyePos.y - (pos.y + 0.5D),
							eyePos.z - (pos.z + 0.5D)
						)
						< RADIUS_SQUARED
						? intersectionDistances.y
						: intersectionDistances.x
					);
					double intersectionX = eyePos.x + lookDir.x * rayDistance - (pos.x + 0.5D);
					double intersectionY = eyePos.y + lookDir.y * rayDistance - (pos.y + 0.5D);
					double intersectionZ = eyePos.z + lookDir.z * rayDistance - (pos.z + 0.5D);
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
					if (stack.isEmpty) {
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
								ItemStack toInsert = BigTechItems.LENS.defaultStack;
								if (player.abilities.creativeMode) {
									if (!player.inventory.contains(toInsert)) {
										player.setStackInHand(hand, toInsert);
									}
								}
								else {
									player.inventory.offerOrDrop(toInsert);
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
								if (!player.abilities.creativeMode) {
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
			return ActionResult.PASS;
		}
	}

	@Override
	public boolean spreadOut(BlockPos pos, BlockState state, BeamSegment inputSegment) {
		PrismBlockEntity prism = WorldHelper.getBlockEntity(inputSegment.beam.world, pos, PrismBlockEntity.class);
		if (prism != null && prism.hasAnyLenses()) {
			inputSegment = inputSegment.withDistance(inputSegment.distanceRemaining / prism.countLenses());
			for (BeamDirection direction : BeamDirection.VALUES) {
				if (prism.hasLens(direction)) {
					inputSegment.beam.addSegment(pos, inputSegment.withDirection(direction).extend());
				}
			}
		}
		else {
			inputSegment.beam.addSegment(pos, inputSegment.terminate());
		}
		return true;
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		PrismBlockEntity prism = WorldHelper.getBlockEntity(world, pos, PrismBlockEntity.class);
		if (prism != null && prism.hasAnyLenses()) {
			ItemScatterer.spawn(world, pos.x, pos.y, pos.z, new ItemStack(BigTechItems.LENS, prism.countLenses()));
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PrismBlockEntity(pos, state);
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(Properties.WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(Properties.WATERLOGGED, context.world.getFluidState(context.blockPos).isEqualAndStill(Fluids.WATER));
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state) {
		return (state.get(Properties.WATERLOGGED) ? Fluids.WATER : Fluids.EMPTY).defaultState;
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.WATERLOGGED);
	}
}