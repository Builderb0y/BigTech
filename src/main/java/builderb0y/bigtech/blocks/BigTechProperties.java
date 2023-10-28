package builderb0y.bigtech.blocks;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;

import builderb0y.bigtech.blocks.belts.DirectorBeltBlock.DirectorBeltMode;

public class BigTechProperties {

	public static final EnumProperty<DirectorBeltMode>
		DIRECTOR_BELT_MODE = EnumProperty.of("director_belt_mode", DirectorBeltMode.class);
	public static final BooleanProperty
		LEFT  = BooleanProperty.of("left"),
		RIGHT = BooleanProperty.of("right");
}