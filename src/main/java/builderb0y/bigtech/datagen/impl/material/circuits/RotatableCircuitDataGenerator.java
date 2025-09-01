package builderb0y.bigtech.datagen.impl.material.circuits;

import java.util.Map;

import net.minecraft.item.Item;

import builderb0y.bigtech.datagen.base.BasicItemDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.base.Dependencies;
import builderb0y.bigtech.util.BigTechMath;

@Dependencies(CircuitOutputTooltipDataGenerator.class)
public abstract class RotatableCircuitDataGenerator extends BasicItemDataGenerator {

	public RotatableCircuitDataGenerator(Item item) {
		super(item);
	}

	@Override
	public void writeItemDefinitions(DataGenContext context) {
		context.writeToFile(
			context.itemDefinitionPath(this.getId()),
			context.replace(
				//language=json
				"""
				{
					"model": {
						"type": "minecraft:select",
						"property": "bigtech:circuit_rotation",
						"cases": [
							{
								"when": "default",
								"model": {
									"type": "minecraft:model",
									"model": "%PATH_0",
									"tints": [
										{
											"type": "minecraft:constant",
											"value": -1
										},
										{
											"type": "bigtech:circuit",
											"index": 1
										}
									]
								}
							},
							{
								"when": "rotate_right",
								"model": {
									"type": "minecraft:model",
									"model": "%PATH_90",
									"tints": [
										{
											"type": "minecraft:constant",
											"value": -1
										},
										{
											"type": "bigtech:circuit",
											"index": 1
										}
									]
								}
							},
							{
								"when": "rotate_180",
								"model": {
									"type": "minecraft:model",
									"model": "%PATH_180",
									"tints": [
										{
											"type": "minecraft:constant",
											"value": -1
										},
										{
											"type": "bigtech:circuit",
											"index": 1
										}
									]
								}
							},
							{
								"when": "rotate_left",
								"model": {
									"type": "minecraft:model",
									"model": "%PATH_270",
									"tints": [
										{
											"type": "minecraft:constant",
											"value": -1
										},
										{
											"type": "bigtech:circuit",
											"index": 1
										}
									]
								}
							}
						],
						"fallback": {
							"type": "minecraft:model",
							"model": "%PATH_0",
							"tints": [
								{
									"type": "minecraft:constant",
									"value": -1
								},
								{
									"type": "bigtech:circuit",
									"index": 1
								}
							]
						}
					}
				}""",
				Map.of("PATH", context.prefixPath("item/", this.getId()).toString())
			)
		);
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		for (int rotation = 0; rotation < 360; rotation += 90) {
			context.writeToFile(
				context.itemModelPath(context.suffixPath(this.getId(), "_" + rotation)),
				context.replace(
					//language=json
					"""
					{
						"parent": "minecraft:item/generated",
						"textures": {
							"layer0": "%ID_base",
							"layer1": "%ID_tint"
						},
						"display": {
							"gui": {
								"rotation":    [ 0, 0, %ROT ],
								"translation": [ 0, 0, 0 ],
								"scale":       [ 1, 1, 1 ]
							}
						}
					}""",
					Map.of(
						"ID", context.prefixPath("item/", this.getId()).toString(),
						"ROT", Integer.toString(BigTechMath.modulus_BP(-rotation, 360))
					)
				)
			);
		}
	}
}