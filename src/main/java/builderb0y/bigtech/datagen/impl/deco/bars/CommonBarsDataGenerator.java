package builderb0y.bigtech.datagen.impl.deco.bars;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.DataGenerator;

public class CommonBarsDataGenerator implements DataGenerator {

	@Override
	public void run(DataGenContext context) {
		this.writeBlockModels(context);
	}

	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_bars_center")),
			//language=json
			"""
			{
				"textures": {
					"particle": "#bars"
				},
				"elements": [
					{
						"from": [ 7,  0, 7 ],
						"to":   [ 9, 16, 9 ],
						"faces": {
							"up":    { "uv": [ 7,  0, 9,  2 ], "texture": "#bars", "cullface": "up"   },
							"down":  { "uv": [ 7, 14, 9, 16 ], "texture": "#bars", "cullface": "down" },
							"north": { "uv": [ 9,  0, 7, 16 ], "texture": "#bars" },
							"east":  { "uv": [ 7,  0, 9, 16 ], "texture": "#bars" },
							"south": { "uv": [ 7,  0, 9, 16 ], "texture": "#bars" },
							"west":  { "uv": [ 9,  0, 7, 16 ], "texture": "#bars" }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_bars_north")),
			//language=json
			"""
			{
				"textures": {
					"bars":     "bigtech:block/copper_bars",
					"particle": "bigtech:block/copper_bars"
				},
				"elements": [
					{
						"from": [ 7,  0, 2 ],
						"to":   [ 9, 16, 4 ],
						"faces": {
							"up":    { "uv": [ 12,  0, 14,  2 ], "texture": "#bars" },
							"down":  { "uv": [ 12, 14, 14, 16 ], "texture": "#bars" },
							"north": { "uv": [ 12,  0, 14, 16 ], "texture": "#bars" },
							"east":  { "uv": [ 12,  0, 14, 16 ], "texture": "#bars" },
							"south": { "uv": [ 12,  0, 14, 16 ], "texture": "#bars" },
							"west":  { "uv": [ 14,  0, 12, 16 ], "texture": "#bars" }
						}
					},
					{
						"from": [ 7, 2, 4 ],
						"to":   [ 9, 4, 7 ],
						"faces": {
							"up":    { "uv": [ 12, 12,  9, 14 ], "texture": "#bars", "rotation": 90 },
							"down":  { "uv": [  9, 12, 12, 14 ], "texture": "#bars", "rotation": 90 },
							"east":  { "uv": [  9, 12, 12, 14 ], "texture": "#bars" },
							"west":  { "uv": [ 12, 12,  9, 14 ], "texture": "#bars" }
						}
					},
					{
						"from": [ 7, 7, 0 ],
						"to":   [ 9, 9, 2 ],
						"faces": {
							"up":    { "uv": [ 16, 7, 14, 9 ], "texture": "#bars", "rotation": 90 },
							"down":  { "uv": [ 14, 7, 16, 9 ], "texture": "#bars", "rotation": 90 },
							"north": { "uv": [ 14, 7, 16, 9 ], "texture": "#bars", "cullface": "north" },
							"east":  { "uv": [ 14, 7, 16, 9 ], "texture": "#bars" },
							"west":  { "uv": [ 16, 7, 14, 9 ], "texture": "#bars" }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_bars_east")),
			//language=json
			"""
			{
				"textures": {
					"bars":     "bigtech:block/copper_bars",
					"particle": "bigtech:block/copper_bars"
				},
				"elements": [
					{
						"from": [ 12,  0, 7 ],
						"to":   [ 14, 16, 9 ],
						"faces": {
							"up":    { "uv": [ 12,  0, 14,  2 ], "texture": "#bars" },
							"down":  { "uv": [ 12, 14, 14, 16 ], "texture": "#bars" },
							"north": { "uv": [ 14,  0, 12, 16 ], "texture": "#bars" },
							"east":  { "uv": [ 12,  0, 14, 16 ], "texture": "#bars" },
							"south": { "uv": [ 12,  0, 14, 16 ], "texture": "#bars" },
							"west":  { "uv": [ 12,  0, 14, 16 ], "texture": "#bars" }
						}
					},
					{
						"from": [  9, 2, 7 ],
						"to":   [ 12, 4, 9 ],
						"faces": {
							"up":    { "uv": [ 12, 12,  9, 14 ], "texture": "#bars", "rotation": 180 },
							"down":  { "uv": [  9, 12, 12, 14 ], "texture": "#bars" },
							"north": { "uv": [ 12, 12,  9, 14 ], "texture": "#bars" },
							"south": { "uv": [  9, 12, 12, 14 ], "texture": "#bars" }
						}
					},
					{
						"from": [ 14, 7, 7 ],
						"to":   [ 16, 9, 9 ],
						"faces": {
							"up":    { "uv": [ 16, 7, 14, 9 ], "texture": "#bars", "rotation": 180 },
							"down":  { "uv": [ 14, 7, 16, 9 ], "texture": "#bars" },
							"north": { "uv": [ 16, 7, 14, 9 ], "texture": "#bars" },
							"east":  { "uv": [ 14, 7, 16, 9 ], "texture": "#bars", "cullface": "east" },
							"south": { "uv": [ 14, 7, 16, 9 ], "texture": "#bars" }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_bars_south")),
			//language=json
			"""
			{
				"textures": {
					"bars":     "bigtech:block/copper_bars",
					"particle": "bigtech:block/copper_bars"
				},
				"elements": [
					{
						"from": [ 7,  0, 12 ],
						"to":   [ 9, 16, 14 ],
						"faces": {
							"up":    { "uv": [ 2,  0, 4,  2 ], "texture": "#bars" },
							"down":  { "uv": [ 2, 14, 4, 16 ], "texture": "#bars" },
							"north": { "uv": [ 2,  0, 4, 16 ], "texture": "#bars" },
							"east":  { "uv": [ 2,  0, 4, 16 ], "texture": "#bars" },
							"south": { "uv": [ 2,  0, 4, 16 ], "texture": "#bars" },
							"west":  { "uv": [ 4,  0, 2, 16 ], "texture": "#bars" }
						}
					},
					{
						"from": [ 7, 12,  9 ],
						"to":   [ 9, 14, 12 ],
						"faces": {
							"up":    { "uv": [ 7, 2, 4, 4 ], "texture": "#bars", "rotation": 90 },
							"down":  { "uv": [ 4, 2, 7, 4 ], "texture": "#bars", "rotation": 90 },
							"east":  { "uv": [ 4, 2, 7, 4 ], "texture": "#bars" },
							"west":  { "uv": [ 7, 2, 4, 4 ], "texture": "#bars" }
						}
					},
					{
						"from": [ 7, 7, 14 ],
						"to":   [ 9, 9, 16 ],
						"faces": {
							"up":    { "uv": [ 2, 7, 0, 9 ], "texture": "#bars", "rotation": 90 },
							"down":  { "uv": [ 0, 7, 2, 9 ], "texture": "#bars", "rotation": 90 },
							"east":  { "uv": [ 0, 7, 2, 9 ], "texture": "#bars" },
							"south": { "uv": [ 0, 7, 2, 9 ], "texture": "#bars", "cullface": "south" },
							"west":  { "uv": [ 2, 7, 0, 9 ], "texture": "#bars" }
						}
					}
				]
			}"""
		);
		context.writeToFile(
			context.blockModelPath(BigTechMod.modID("template_bars_west")),
			//language=json
			"""
			{
				"textures": {
					"bars":     "bigtech:block/copper_bars",
					"particle": "bigtech:block/copper_bars"
				},
				"elements": [
					{
						"from": [ 2,  0, 7 ],
						"to":   [ 4, 16, 9 ],
						"faces": {
							"up":    { "uv": [ 2,  0, 4,  2 ], "texture": "#bars" },
							"down":  { "uv": [ 2, 14, 4, 16 ], "texture": "#bars" },
							"north": { "uv": [ 4,  0, 2, 16 ], "texture": "#bars" },
							"east":  { "uv": [ 2,  0, 4, 16 ], "texture": "#bars" },
							"south": { "uv": [ 2,  0, 4, 16 ], "texture": "#bars" },
							"west":  { "uv": [ 2,  0, 4, 16 ], "texture": "#bars" }
						}
					},
					{
						"from": [ 4, 12, 7 ],
						"to":   [ 7, 14, 9 ],
						"faces": {
							"up":    { "uv": [ 7, 2, 4, 4 ], "texture": "#bars", "rotation": 180 },
							"down":  { "uv": [ 4, 2, 7, 4 ], "texture": "#bars" },
							"north": { "uv": [ 7, 2, 4, 4 ], "texture": "#bars" },
							"south": { "uv": [ 4, 2, 7, 4 ], "texture": "#bars" }
						}
					},
					{
						"from": [ 0, 7, 7 ],
						"to":   [ 2, 9, 9 ],
						"faces": {
							"up":    { "uv": [ 2, 7, 0, 9 ], "texture": "#bars", "rotation": 180 },
							"down":  { "uv": [ 0, 7, 2, 9 ], "texture": "#bars" },
							"north": { "uv": [ 2, 7, 0, 9 ], "texture": "#bars" },
							"south": { "uv": [ 0, 7, 2, 9 ], "texture": "#bars" },
							"west":  { "uv": [ 0, 7, 2, 9 ], "texture": "#bars", "cullface": "west" }
						}
					}
				]
			}"""
		);
	}
}