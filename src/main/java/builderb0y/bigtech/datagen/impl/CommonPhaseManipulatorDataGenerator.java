package builderb0y.bigtech.datagen.impl;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class CommonPhaseManipulatorDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("phase_manipulator")),
			//language=json
			"""
			{
				"parent": "block/block",
				"ambientocclusion": false,
				"textures": {
					"particle": "#texture"
				},
				"elements": [
					{
						"from": [ 7, 7, 7 ],
						"to":   [ 9, 9, 9 ],
						"shade": false,
						"faces": {
							"up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"east":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"west":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" }
						}
					},
					{
						"from": [  6,  6,  6 ],
						"to":   [ 10, 10, 10 ],
						"shade": false,
						"faces": {
							"up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"east":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"west":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" }
						}
					},
					{
						"from": [  5,  5,  5 ],
						"to":   [ 11, 11, 11 ],
						"shade": false,
						"faces": {
							"up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"east":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"west":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" }
						}
					},
					{
						"from": [  4,  4,  4 ],
						"to":   [ 12, 12, 12 ],
						"shade": false,
						"faces": {
							"up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"east":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"west":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" }
						}
					},
					{
						"from": [  3,  3,  3 ],
						"to":   [ 13, 13, 13 ],
						"shade": false,
						"faces": {
							"up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"east":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"west":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" }
						}
					},
					{
						"from": [  2,  2,  2 ],
						"to":   [ 14, 14, 14 ],
						"shade": false,
						"faces": {
							"up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"east":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"west":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" }
						}
					},
					{
						"from": [  1,  1,  1 ],
						"to":   [ 15, 15, 15 ],
						"shade": false,
						"faces": {
							"up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"east":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"west":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" }
						}
					},
					{
						"from": [  0,  0,  0 ],
						"to":   [ 16, 16, 16 ],
						"shade": false,
						"faces": {
							"up":    { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"down":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"north": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"south": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"east":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
							"west":  { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" }
						}
					}
				]
			}"""
		);
	}
}