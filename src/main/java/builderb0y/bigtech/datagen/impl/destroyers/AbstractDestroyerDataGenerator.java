package builderb0y.bigtech.datagen.impl.destroyers;

import java.util.Arrays;
import java.util.Map;

import net.minecraft.item.BlockItem;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

public abstract class AbstractDestroyerDataGenerator extends BasicBlockDataGenerator {

	public AbstractDestroyerDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.id),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.HORIZONTAL_FACING_ORDER)
				.map(direction -> new BlockStateJsonVariant(
					"facing=${direction.getName()}",
					context.prefixPath("block/", this.id).toString(),
					null,
					BlockStateJsonVariant.yFromNorth(direction)
				))
				::iterator
			)
			.toString()
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.id),
			context.replace(
				//use furnace rotation to face the player.
				//language=json
				"""
				{
					"parent": "block/cube",
					"display": {
						"firstperson_righthand": {
							"rotation":    [ 0,   135,   0   ],
							"translation": [ 0,     0,   0   ],
							"scale":       [ 0.4,   0.4, 0.4 ]
						}
					},
					"textures": {
						"up":       "bigtech:block/destroyer_top",
						"down":     "bigtech:block/destroyer_top",
						"north":    "%FRONT",
						"east":     "bigtech:block/destroyer_side",
						"south":    "bigtech:block/destroyer_back",
						"west":     "bigtech:block/destroyer_side",
						"particle": "%FRONT"
					}
				}""",
				Map.of("FRONT", context.prefixSuffixPath("block/", this.id, "_front").toString())
			)
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.id);
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		//wood is fine.
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}
}