package builderb0y.bigtech.datagen.impl;

import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Util;

import builderb0y.bigtech.datagen.base.DataGenContext;

public class MinerDataGenerator extends InventoryDataGenerator {

	public MinerDataGenerator(ScreenHandlerType<?> screenHandlerType) {
		super(screenHandlerType);
	}

	@Override
	public String getLangKey(DataGenContext context) {
		return Util.createTranslationKey("entity", this.getId());
	}

	@Override
	public void run(DataGenContext context) {
		super.run(context);
		this.genDynamicLightsConfig(context);
	}

	public void genDynamicLightsConfig(DataGenContext context) {
		context.writeToFile(
			"assets/bigtech/dynamiclights/entity/miner.json",
			//language=json
			"""
			{
				"match": {
					"type": "bigtech:miner"
				},
				"luminance": {
					"type": "bigtech:miner"
				}
			}"""
		);
	}
}