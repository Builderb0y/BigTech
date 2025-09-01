package builderb0y.bigtech.datagen.impl.functional.belts;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class BeltTemplateModelDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_belt")),
			//language=json
			"""
			{
				"parent": "block/thin_block",
				"textures": {
					"particle": "#belt",
					"bottom": "bigtech:block/belt_bottom"
				},
				"elements": [
					{
						"from": [  0, 0,  0 ],
						"to":   [ 16, 1, 16 ],
						"faces": {
							"up":    { "uv": [  0,  0, 16, 16 ], "texture": "#belt"                                         },
							"down":  { "uv": [  0,  0, 16, 16 ], "texture": "#bottom", "cullface": "down"                   },
							"north": { "uv": [  0,  0, 16,  1 ], "texture": "#belt",   "cullface": "north", "rotation": 180 },
							"south": { "uv": [  0, 15, 16, 16 ], "texture": "#belt",   "cullface": "south"                  },
							"east":  { "uv": [ 15,  0, 16, 16 ], "texture": "#belt",   "cullface": "east",  "rotation": 90  },
							"west":  { "uv": [  0,  0,  1, 16 ], "texture": "#belt",   "cullface": "west",  "rotation": 270 }
						}
					}
				]
			}
			"""
		);
	}
}