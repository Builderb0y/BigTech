package builderb0y.bigtech.datagen.impl.destroyers;

import java.util.Arrays;
import java.util.Map;

import net.minecraft.item.BlockItem;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.TableFormats.BlockStateJsonVariant;
import builderb0y.bigtech.datagen.tables.Table;

public abstract class AbstractDestroyerDataGenerator extends BasicBlockDataGenerator {

	public AbstractDestroyerDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupLang(DataGenContext context) {
		super.setupLang(context);
		context.lang.put(Util.createTranslationKey("container", this.getId()), context.underscoresToCapitals(this.getId().getPath()));
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			new Table<>(BlockStateJsonVariant.FORMAT)
			.addRows(
				Arrays
				.stream(BlockStateJsonVariant.HORIZONTAL_FACING_ORDER)
				.map((Direction direction) -> new BlockStateJsonVariant(
					"facing=${direction.asString()}",
					context.prefixPath("block/", this.getId()).toString(),
					null,
					BlockStateJsonVariant.yFromNorth(direction)
				))
				::iterator
			)
			.toString()
		);
	}

	public boolean hasBack() {
		return true;
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		context.writeToFile(
			context.blockModelPath(this.getId()),
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
						"south":    "%BACK",
						"west":     "bigtech:block/destroyer_side",
						"particle": "%FRONT"
					}
				}""",
				Map.of(
					"FRONT", context.prefixSuffixPath("block/", this.getId(), "_front").toString(),
					"BACK", "bigtech:block/destroyer_${this.hasBack() ? \"back\" : \"side\"}"
				)
			)
		);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
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