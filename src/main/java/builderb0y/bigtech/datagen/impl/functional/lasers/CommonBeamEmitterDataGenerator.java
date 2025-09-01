package builderb0y.bigtech.datagen.impl.functional.lasers;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class CommonBeamEmitterDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("beam_emitter_base_transforms")),
			"""
			{
				"parent": "block/block",
				"display": {
					"gui": {
						"rotation":    [ 30,   225,     0     ],
						"translation": [  0,     1,     0     ],
						"scale":       [  0.625, 0.625, 0.625 ]
					},
					"ground": {
						"rotation":    [ 0,    0,    0    ],
						"translation": [ 0,    3,    0    ],
						"scale":       [ 0.25, 0.25, 0.25 ]
					},
					"fixed": {
						"rotation":    [ 0,   0,   0   ],
						"translation": [ 0,   0,   0   ],
						"scale":       [ 0.5, 0.5, 0.5 ]
					},
					"thirdperson_righthand": {
						"rotation":    [ 75,    45,     0     ],
						"translation": [  0,     2.5,   0     ],
						"scale":       [  0.375, 0.375, 0.375 ]
					},
					"firstperson_righthand": {
						"rotation":    [ 0,  135,    0    ],
						"translation": [ 0,    3,    0    ],
						"scale":       [ 0.40, 0.40, 0.40 ]
					},
					"firstperson_lefthand": {
						"rotation":    [ 0,  135,    0    ],
						"translation": [ 0,    3,    0    ],
						"scale":       [ 0.40, 0.40, 0.40 ]
					}
				}
			}"""
		);
	}
}