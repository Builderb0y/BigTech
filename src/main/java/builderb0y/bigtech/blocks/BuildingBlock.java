package builderb0y.bigtech.blocks;

import java.util.Locale;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import builderb0y.bigtech.blockEntities.BigTechBlockEntityTypes;
import builderb0y.bigtech.blockEntities.BuildingBlockEntity;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.dataComponents.BigTechDataComponents;
import builderb0y.bigtech.util.Symmetry;
import builderb0y.bigtech.util.WorldHelper;

public class BuildingBlock extends BlockWithEntity {

	public static final MapCodec<BuildingBlock> CODEC = BigTechAutoCodec.callerMapCodec();

	@Override
	public MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	public BuildingBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.onPlaced(world, pos, state, placer, stack);
		BuildingBlockEntity buildingBlockEntity = WorldHelper.getBlockEntity(world, pos, BuildingBlockEntity.class);
		if (buildingBlockEntity != null) {
			if (placer instanceof PlayerEntity player) {
				buildingBlockEntity.owner = player.getGameProfile().getId();
			}
			BlockPos parentPos = stack.get(BigTechDataComponents.BUILDING_BLOCK_LINK);
			if (parentPos != null) {
				buildingBlockEntity.parent = parentPos;
				if (world.getBlockEntity(parentPos) instanceof BuildingBlockEntity parent) {
					parent.children.add(pos.toImmutable());
				}
			}
		}
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BuildingBlockEntity(BigTechBlockEntityTypes.BUILDING_BLOCK, pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return world.isClient() ? null : validateTicker(type, BigTechBlockEntityTypes.BUILDING_BLOCK, BuildingBlockEntity.TICKER);
	}

	@Override
	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BigTechProperties.BUILDING_BLOCK_MODE);
	}

	public static enum BuildingBlockMode implements StringIdentifiable {
		STACK(Symmetry.IDENTITY),
		FLIP_X(Symmetry.IDENTITY, Symmetry.FLIP_0),
		FLIP_Z(Symmetry.IDENTITY, Symmetry.FLIP_90),
		FLIP_XZ(Symmetry.IDENTITY, Symmetry.FLIP_0, Symmetry.FLIP_90, Symmetry.ROTATE_180),
		ROTATE_2X180(Symmetry.IDENTITY, Symmetry.ROTATE_180),
		ROTATE_4X90(Symmetry.IDENTITY, Symmetry.ROTATE_90, Symmetry.ROTATE_180, Symmetry.ROTATE_270),
		ROTATE_8XFLIP(Symmetry.values());

		public static final BuildingBlockMode[] VALUES = values();

		public final String lowerCaseName = this.name().toLowerCase(Locale.ROOT);
		public final Symmetry[] symmetries;

		BuildingBlockMode(Symmetry... symmetries) {
			this.symmetries = symmetries;
		}

		@Override
		public String asString() {
			return this.lowerCaseName;
		}
	}
}