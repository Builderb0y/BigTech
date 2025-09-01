package builderb0y.bigtech.datagen.impl.functional;

import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.item.BlockItem;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.RetexturedModelBuilder;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.items.BigTechItemTags;

public class SteelDoorDataGenerator extends BasicBlockDataGenerator {

	public SteelDoorDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {
		context.getTags(MiningToolTags.PICKAXE).addElement(this.getId());
	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {
		context.getTags(MiningLevelTags.IRON).addElement(this.getId());
	}

	@Override
	public void writeBlockstateJson(DataGenContext context) {
		context.writeToFile(
			context.blockstatePath(this.getId()),
			//language=json
			"""
			{
				"variants": {
					"facing=east,half=lower,hinge=left,open=false":   { "model": "bigtech:block/steel_door_bottom_left",       "y": 0   },
					"facing=east,half=lower,hinge=left,open=true":    { "model": "bigtech:block/steel_door_bottom_left_open",  "y": 90  },
					"facing=east,half=lower,hinge=right,open=false":  { "model": "bigtech:block/steel_door_bottom_right",      "y": 0   },
					"facing=east,half=lower,hinge=right,open=true":   { "model": "bigtech:block/steel_door_bottom_right_open", "y": 270 },
					"facing=east,half=upper,hinge=left,open=false":   { "model": "bigtech:block/steel_door_top_left",          "y": 0   },
					"facing=east,half=upper,hinge=left,open=true":    { "model": "bigtech:block/steel_door_top_left_open",     "y": 90  },
					"facing=east,half=upper,hinge=right,open=false":  { "model": "bigtech:block/steel_door_top_right",         "y": 0   },
					"facing=east,half=upper,hinge=right,open=true":   { "model": "bigtech:block/steel_door_top_right_open",    "y": 270 },
					"facing=north,half=lower,hinge=left,open=false":  { "model": "bigtech:block/steel_door_bottom_left",       "y": 270 },
					"facing=north,half=lower,hinge=left,open=true":   { "model": "bigtech:block/steel_door_bottom_left_open",  "y": 0   },
					"facing=north,half=lower,hinge=right,open=false": { "model": "bigtech:block/steel_door_bottom_right",      "y": 270 },
					"facing=north,half=lower,hinge=right,open=true":  { "model": "bigtech:block/steel_door_bottom_right_open", "y": 180 },
					"facing=north,half=upper,hinge=left,open=false":  { "model": "bigtech:block/steel_door_top_left",          "y": 270 },
					"facing=north,half=upper,hinge=left,open=true":   { "model": "bigtech:block/steel_door_top_left_open",     "y": 0   },
					"facing=north,half=upper,hinge=right,open=false": { "model": "bigtech:block/steel_door_top_right",         "y": 270 },
					"facing=north,half=upper,hinge=right,open=true":  { "model": "bigtech:block/steel_door_top_right_open",    "y": 180 },
					"facing=south,half=lower,hinge=left,open=false":  { "model": "bigtech:block/steel_door_bottom_left",       "y": 90  },
					"facing=south,half=lower,hinge=left,open=true":   { "model": "bigtech:block/steel_door_bottom_left_open",  "y": 180 },
					"facing=south,half=lower,hinge=right,open=false": { "model": "bigtech:block/steel_door_bottom_right",      "y": 90  },
					"facing=south,half=lower,hinge=right,open=true":  { "model": "bigtech:block/steel_door_bottom_right_open", "y": 0   },
					"facing=south,half=upper,hinge=left,open=false":  { "model": "bigtech:block/steel_door_top_left",          "y": 90  },
					"facing=south,half=upper,hinge=left,open=true":   { "model": "bigtech:block/steel_door_top_left_open",     "y": 180 },
					"facing=south,half=upper,hinge=right,open=false": { "model": "bigtech:block/steel_door_top_right",         "y": 90  },
					"facing=south,half=upper,hinge=right,open=true":  { "model": "bigtech:block/steel_door_top_right_open",    "y": 0   },
					"facing=west,half=lower,hinge=left,open=false":   { "model": "bigtech:block/steel_door_bottom_left",       "y": 180 },
					"facing=west,half=lower,hinge=left,open=true":    { "model": "bigtech:block/steel_door_bottom_left_open",  "y": 270 },
					"facing=west,half=lower,hinge=right,open=false":  { "model": "bigtech:block/steel_door_bottom_right",      "y": 180 },
					"facing=west,half=lower,hinge=right,open=true":   { "model": "bigtech:block/steel_door_bottom_right_open", "y": 90  },
					"facing=west,half=upper,hinge=left,open=false":   { "model": "bigtech:block/steel_door_top_left",          "y": 180 },
					"facing=west,half=upper,hinge=left,open=true":    { "model": "bigtech:block/steel_door_top_left_open",     "y": 270 },
					"facing=west,half=upper,hinge=right,open=false":  { "model": "bigtech:block/steel_door_top_right",         "y": 180 },
					"facing=west,half=upper,hinge=right,open=true":   { "model": "bigtech:block/steel_door_top_right_open",    "y": 90  }
				}
			}"""
		);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		for (BlockHalf half : BlockHalf.values()) {
			for (DoorHinge hinge : DoorHinge.values()) {
				for (boolean open : new boolean[] { false, true }) {
					String suffix = '_' + half.asString() + '_' + hinge.asString() + (open ? "_open" : "");
					context.writeToFile(
						context.blockModelPath(context.suffixPath(this.getId(), suffix)),
						new RetexturedModelBuilder()
						.blockParent(context.suffixPath(Identifier.ofVanilla("door"), suffix))
						.blockTexture("bottom", context.suffixPath(this.getId(), "_bottom"))
						.blockTexture("top", context.suffixPath(this.getId(), "_top"))
						.toString()
					);
				}
			}
		}
	}

	@Override
	public void writeItemModels(DataGenContext context) {
		context.writeToFile(
			context.itemModelPath(this.getId()),
			new RetexturedModelBuilder()
			.parent("minecraft:item/generated")
			.itemTexture("layer0", this.getId())
			.toString()
		);
	}

	@Override
	public void writeItemDefinitions(DataGenContext context) {
		this.writeDefaultItemDefinition(context, context.prefixPath("item/", this.getId()));
	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {
		context.getTags(BlockTags.DOORS).addElement(this.getId());
	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {
		context.getTags(ItemTags.DOORS).addElement(this.getId());
	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.getId()),
			new ShapedRecipeBuilder()
			.category(CraftingRecipeCategory.REDSTONE)
			.pattern("ss", "ss", "ss")
			.where('s', BigTechItemTags.STEEL_INGOTS)
			.result(this.getId())
			.count(3)
			.toString()
		);
	}
}