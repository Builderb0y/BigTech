package builderb0y.bigtech.datagen.impl.material.circuits;

import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class CircuitOutputTooltipDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.lang.put("tooltip.bigtech.circuit.output",             "Output: %d");
		context.lang.put("tooltip.bigtech.circuit.output.front",       "Front output: %d");
		context.lang.put("tooltip.bigtech.circuit.output.right",       "Right output: %d");
		context.lang.put("tooltip.bigtech.circuit.output.back",        "Back output: %d");
		context.lang.put("tooltip.bigtech.circuit.output.left",        "Left output: %d");
		context.lang.put("tooltip.bigtech.circuit.output.horizontal",  "Horizontal output: %d");
		context.lang.put("tooltip.bigtech.circuit.output.vertical",    "Vertical output: %d");
	}
}