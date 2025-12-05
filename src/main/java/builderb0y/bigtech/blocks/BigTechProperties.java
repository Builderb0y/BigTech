package builderb0y.bigtech.blocks;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;

import builderb0y.bigtech.blocks.BuildingBlock.BuildingBlockMode;
import builderb0y.bigtech.blocks.belts.DirectorBeltBlock.DirectorBeltMode;

public class BigTechProperties {

	public static final EnumProperty<DirectorBeltMode>
		DIRECTOR_BELT_MODE = EnumProperty.of("director_belt_mode", DirectorBeltMode.class);
	public static final BooleanProperty
		LEFT  = BooleanProperty.of("left"),
		RIGHT = BooleanProperty.of("right");
	public static final IntProperty
		ROTATION_0_7 = IntProperty.of("rotation", 0, 7);
	public static final BooleanProperty
		ACTIVE = BooleanProperty.of("active");
	public static final EnumProperty<BuildingBlockMode>
		BUILDING_BLOCK_MODE = EnumProperty.of("mode", BuildingBlockMode.class);
}