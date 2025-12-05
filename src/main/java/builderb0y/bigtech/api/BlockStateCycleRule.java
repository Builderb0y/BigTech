package builderb0y.bigtech.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;

import builderb0y.bigtech.blocks.BigTechProperties;

import static net.minecraft.block.enums.Orientation.*;
import static net.minecraft.util.math.Direction.*;

public interface BlockStateCycleRule {

	public static final List<BlockStateCycleRule> RULES = new ArrayList<>();
	public static final Object INITIALIZER = new Object() {{
		builder(BigTechProperties.ROTATION_0_7).values(7, 6, 5, 4, 3, 2, 1, 0).lock(Properties.FACING).buildAndRegister();
		builder(Properties.HORIZONTAL_FACING).values(NORTH, EAST, SOUTH, WEST).lock(Properties.BLOCK_HALF).buildAndRegister();
		builder(Properties.FACING).values(NORTH, EAST, SOUTH, WEST).buildAndRegister();
		builder(Properties.FACING).values(UP, DOWN).buildAndRegister();
		builder(Properties.HOPPER_FACING).values(NORTH, SOUTH, EAST, WEST).buildAndRegister();
		builder(Properties.ROTATION).buildAndRegister();
		builder(Properties.AXIS).buildAndRegister();
		builder(Properties.HORIZONTAL_AXIS).buildAndRegister();
		builder(Properties.ORIENTATION).values(NORTH_UP, EAST_UP, SOUTH_UP, WEST_UP).buildAndRegister();
		builder(Properties.ORIENTATION).values(UP_NORTH, UP_EAST, UP_SOUTH, UP_WEST).buildAndRegister();
		builder(Properties.ORIENTATION).values(DOWN_NORTH, DOWN_EAST, DOWN_SOUTH, DOWN_WEST).buildAndRegister();
		builder(Properties.INVERTED).testPlacementState((BlockState state) -> state.isOf(Blocks.DAYLIGHT_DETECTOR)).buildAndRegister();
		builder(Properties.SLAB_TYPE).values(SlabType.BOTTOM, SlabType.TOP).testBlock(SlabBlock.class::isInstance).buildAndRegister();
		builder(BigTechProperties.BUILDING_BLOCK_MODE).buildAndRegister();
	}};

	public static void register(BlockStateCycleRule rule) {
		RULES.add(rule);
	}

	public static <C extends Comparable<C>> Builder<C> builder(Property<C> property) {
		return new Builder<>(property);
	}

	public abstract boolean test(Context context);

	public abstract BlockStateComponent cycleState(Context context, boolean forward);

	public abstract BlockStateComponent removeRelevantProperties(Context context);

	public static class Context {

		public BlockState placementState;
		public BlockStateComponent forcedProperties;
		public ServerPlayerEntity player;

		public Context(BlockState placementState, BlockStateComponent forcedProperties, ServerPlayerEntity player) {
			this.forcedProperties = forcedProperties;
			this.placementState = placementState;
			this.player = player;
		}

		public BlockState getStateWithForcedProperties() {
			return this.forcedProperties.applyToState(this.placementState);
		}
	}

	public static class Builder<C extends Comparable<C>> {

		public static final Property<?>[] EMPTY_PROPERTY_ARRAY = {};

		public Predicate<Context> predicate;
		public Property<C> cycleProperty;
		public List<C> cycleValues;
		public Property<?>[] lockProperties;

		public Builder(Property<C> cycleProperty) {
			this.predicate = (Context context) -> context.placementState.contains(cycleProperty);
			this.cycleProperty = cycleProperty;
			this.cycleValues = cycleProperty.getValues();
			this.lockProperties = EMPTY_PROPERTY_ARRAY;
		}

		public Builder<C> testContext(Predicate<Context> predicate) {
			this.predicate = predicate;
			return this;
		}

		public Builder<C> testPlacementState(Predicate<BlockState> predicate) {
			this.predicate = (Context context) -> predicate.test(context.placementState);
			return this;
		}

		public Builder<C> testForcedState(Predicate<BlockState> predicate) {
			this.predicate = (Context context) -> predicate.test(context.getStateWithForcedProperties());
			return this;
		}

		public Builder<C> testBlock(Predicate<Block> predicate) {
			this.predicate = (Context context) -> predicate.test(context.placementState.getBlock());
			return this;
		}

		public Builder<C> values(C... validValues) {
			this.cycleValues = Arrays.asList(validValues);
			return this;
		}

		public Builder<C> lock(Property<?>... lockProperties) {
			this.lockProperties = lockProperties;
			return this;
		}

		public BlockStateCycleRule build() {
			return new BasicBlockStateCycleRule(this);
		}

		public void buildAndRegister() {
			register(this.build());
		}
	}

	public static class BasicBlockStateCycleRule implements BlockStateCycleRule {

		public final Predicate<Context> predicate;
		public final Property<?> cycleProperty;
		public final List<?> validValues;
		public final Property<?>[] lockProperties;

		public BasicBlockStateCycleRule(Builder<?> builder) {
			this.predicate = builder.predicate;
			this.cycleProperty = builder.cycleProperty;
			this.validValues = builder.cycleValues;
			this.lockProperties = builder.lockProperties;
		}

		@Override
		public boolean test(Context context) {
			return this.predicate.test(context) && this.validValues.contains(context.getStateWithForcedProperties().get(this.cycleProperty));
		}

		@Override
		public BlockStateComponent cycleState(Context context, boolean forward) {
			Comparable<?> value = context.forcedProperties.getValue(this.cycleProperty);
			if (value == null) {
				if (context.placementState.contains(this.cycleProperty)) {
					value = context.placementState.get(this.cycleProperty);
				}
				else {
					return context.forcedProperties;
				}
			}
			int oldIndex = this.validValues.indexOf(value);
			if (oldIndex >= 0) {
				int newIndex;
				if (forward) {
					newIndex = oldIndex + 1;
					if (newIndex >= this.validValues.size()) newIndex = 0;
				}
				else {
					newIndex = oldIndex - 1;
					if (newIndex < 0) newIndex = this.validValues.size() - 1;
				}
				HashMap<String, String> newProperties = new HashMap<>(context.forcedProperties.properties());
				newProperties.put(this.cycleProperty.getName(), this.cycleProperty.name(this.validValues.get(newIndex).as()));
				for (Property<?> lockProperty : this.lockProperties) {
					if (context.placementState.contains(lockProperty)) {
						newProperties.putIfAbsent(lockProperty.getName(), lockProperty.name(context.placementState.get(lockProperty).as()));
					}
				}
				return new BlockStateComponent(newProperties);
			}
			return context.forcedProperties;
		}

		@Override
		public BlockStateComponent removeRelevantProperties(Context context) {
			HashMap<String, String> newProperties = new HashMap<>(context.forcedProperties.properties());
			String toRemove = newProperties.get(this.cycleProperty.getName());
			for (Object value : this.validValues) {
				if (this.cycleProperty.name(value.as()).equals(toRemove)) {
					newProperties.remove(this.cycleProperty.getName());
					break;
				}
			}
			for (Property<?> property : this.lockProperties) {
				newProperties.remove(property.getName());
			}
			return newProperties.isEmpty() ? BlockStateComponent.DEFAULT : new BlockStateComponent(newProperties);
		}
	}
}