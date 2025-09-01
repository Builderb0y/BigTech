package builderb0y.bigtech.datagen.impl.material;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class CommonFlatItemDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(BigTechMod.modID("photovoltaic_cell")),
			//language=json
			"""
			{
				"parent": "minecraft:block/thin_block",
				"elements": [
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 1, 16 ],
						"faces": {
							"up":    { "uv": [  0,  0, 16, 16 ], "texture": "#top"                   },
							"down":  { "uv": [  0,  0, 16, 16 ], "texture": "#back", "rotation": 270 },
							"north": { "uv": [  0,  0, 16,  1 ], "texture": "#back", "rotation": 180 },
							"south": { "uv": [  0, 15, 16, 16 ], "texture": "#back"                  },
							"east":  { "uv": [ 15,  0, 16, 16 ], "texture": "#back", "rotation":  90 },
							"west":  { "uv": [  0,  0,  1, 16 ], "texture": "#back", "rotation": 270 }
						}
					}
				]
			}"""
		);
	}
}