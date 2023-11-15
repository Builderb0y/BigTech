package builderb0y.bigtech.datagen.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;

import builderb0y.bigtech.datagen.base.BasicBlockDataGenerator;
import builderb0y.bigtech.datagen.base.DataGenContext;
import builderb0y.bigtech.datagen.formats.ShapedRecipeBuilder;
import builderb0y.bigtech.util.Enums;

public class PrismDataGenerator extends BasicBlockDataGenerator {

	public static final double
		RADIUS = 6.75D / 16.0D,
		RADIUS_SQUARED = RADIUS * RADIUS;

	public PrismDataGenerator(BlockItem blockItem) {
		super(blockItem);
	}

	@Override
	public void writeBlockModels(DataGenContext context) {
		//before 1.15, alpha testing would allow me to do the sphere in layers.
		//but now alpha testing no longer exists for translucents.
		//so, have a greedy mesh instead.
		Set<VoxelQuad> rawMesh = new HashSet<>(1024);
		for (int voxelZ = 0; voxelZ < 16; voxelZ++) {
			double centerZ = (voxelZ + 0.5D) * 0.0625D - 0.5D;
			for (int voxelX = 0; voxelX < 16; voxelX++) {
				double centerX = (voxelX + 0.5D) * 0.0625D - 0.5D;
				for (int voxelY = 0; voxelY < 16; voxelY++) {
					double centerY = (voxelY + 0.5D) * 0.0625D - 0.5D;
					if (isOnSurface(centerX, centerY, centerZ)) {
						for (Direction direction : Enums.DIRECTIONS) {
							if (!isInside(
								centerX + direction.offsetX * 0.0625D,
								centerY + direction.offsetY * 0.0625D,
								centerZ + direction.offsetZ * 0.0625D
							)) {
								rawMesh.add(new VoxelQuad(voxelX, voxelY, voxelZ, direction));
							}
						}
					}
				}
			}
		}
		if (rawMesh.isEmpty) {
			throw new IllegalStateException("rawMesh is empty");
		}
		Map<VoxelQuad, VoxelSize> greedyMesh = new HashMap<>(512);
		while (!rawMesh.isEmpty) {
			//pick any quad.
			VoxelQuad quad = rawMesh.iterator().next();
			//move as far in the negative direction as possible.
			while (true) {
				VoxelQuad adjacent = new VoxelQuad(quad.x - 1, quad.y, quad.z, quad.facing);
				if (rawMesh.contains(adjacent)) quad = adjacent;
				else break;
			}
			while (true) {
				VoxelQuad adjacent = new VoxelQuad(quad.x, quad.y - 1, quad.z, quad.facing);
				if (rawMesh.contains(adjacent)) quad = adjacent;
				else break;
			}
			while (true) {
				VoxelQuad adjacent = new VoxelQuad(quad.x, quad.y, quad.z - 1, quad.facing);
				if (rawMesh.contains(adjacent)) quad = adjacent;
				else break;
			}
			//find the max size we can for this greedy mesh.
			int sizeX = 1, sizeY = 1, sizeZ = 1;
			while (true) {
				if (!rawMesh.contains(new VoxelQuad(quad.x + sizeX, quad.y, quad.z, quad.facing))) {
					break;
				}
				sizeX++;
			}
			outer:
			while (true) {
				for (int offsetX = 0; offsetX < sizeX; offsetX++) {
					if (!rawMesh.contains(new VoxelQuad(quad.x + offsetX, quad.y + sizeY, quad.z, quad.facing))) {
						break outer;
					}
				}
				sizeY++;
			}
			outer:
			while (true) {
				for (int offsetY = 0; offsetY < sizeY; offsetY++) {
					for (int offsetX = 0; offsetX < sizeX; offsetX++) {
						if (!rawMesh.contains(new VoxelQuad(quad.x + offsetX, quad.y + offsetY, quad.z + sizeZ, quad.facing))) {
							break outer;
						}
					}
				}
				sizeZ++;
			}
			//remove the relevant quads from the raw mesh.
			for (int offsetZ = 0; offsetZ < sizeZ; offsetZ++) {
				for (int offsetY = 0; offsetY < sizeY; offsetY++) {
					for (int offsetX = 0; offsetX < sizeX; offsetX++) {
						VoxelQuad toRemove = new VoxelQuad(quad.x + offsetX, quad.y + offsetY, quad.z + offsetZ, quad.facing);
						if (!rawMesh.remove(toRemove)) {
							throw new IllegalStateException("${toRemove} not in ${rawMesh}");
						}
					}
				}
			}
			//add a new bigger quad to the greedy mesh.
			if (greedyMesh.put(quad, new VoxelSize(sizeX, sizeY, sizeZ)) != null) {
				throw new IllegalStateException("${quad} already in ${greedyMesh}");
			}
		}
		if (greedyMesh.isEmpty) {
			throw new IllegalStateException("greedyMesh is empty");
		}
		//now generate the actual model.
		StringBuilder model = new StringBuilder(1024).append(
			"""
			{
				"parent": "minecraft:block/block",
				"textures": {
					"texture": "bigtech:block/prism_base",
					"particle": "bigtech:block/prism_base"
				},
				"elements": ["""
		);
		for (Map.Entry<VoxelQuad, VoxelSize> entry : greedyMesh.entrySet()) {
			model
			.append("\n\t\t{")
			.append("\n\t\t\t\"from\": [ ");
			appendThreeNumbers(model, entry.key.x, entry.key.y, entry.key.z);
			model
			.append(" ],")
			.append("\n\t\t\t\"to\":   [ ");
			appendThreeNumbers(model, entry.key.x + entry.value.x, entry.key.y + entry.value.y, entry.key.z + entry.value.z);
			model
			.append(" ],")
			.append("\n\t\t\t\"faces\": {")
			.append("\n\t\t\t\t\"")
			.append(entry.key.facing.getName())
			.append("\": { \"texture\": \"#texture\" }")
			.append("\n\t\t\t}")
			.append("\n\t\t},");
		}
		model.setLength(model.length() - 1); //remove trailing comma.
		model
		.append("\n\t]")
		.append("\n}");
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_base")),
			model.toString()
		);
		context.writeToFile(
			context.blockModelPath(context.suffixPath(this.id, "_lens")),
			//language=json
			"""
			{
				"jmxl": true,
				"textures": {
					"edge":   "bigtech:block/prism_lens_edge",
					"center": "bigtech:block/prism_lens_center"
				},
				"elements": [
					{
						"from": [ 7, 10, 0 ],
						"to":   [ 9, 11, 1 ],
						"faces": {
							"up":    { "uv": [ 7, 5, 9, 6 ], "texture": "#edge", "rotation": 180 },
							"down":  { "uv": [ 7, 5, 9, 6 ], "texture": "#edge", "rotation": 180 },
							"north": { "uv": [ 7, 5, 9, 6 ], "texture": "#edge"                  },
							"east":  { "uv": [ 7, 5, 8, 6 ], "texture": "#edge"                  },
							"south": { "uv": [ 7, 5, 9, 6 ], "texture": "#edge", "rotation": 180 },
							"west":  { "uv": [ 8, 5, 9, 6 ], "texture": "#edge"                  }
						}
					},
					{
						"from": [ 7, 5, 0 ],
						"to":   [ 9, 6, 1 ],
						"faces": {
							"up":    { "uv": [ 7, 10, 9, 11 ], "texture": "#edge", "rotation": 180 },
							"down":  { "uv": [ 7, 10, 9, 11 ], "texture": "#edge", "rotation": 180 },
							"north": { "uv": [ 7, 10, 9, 11 ], "texture": "#edge"                  },
							"east":  { "uv": [ 7, 10, 8, 11 ], "texture": "#edge"                  },
							"south": { "uv": [ 7, 10, 9, 11 ], "texture": "#edge", "rotation": 180 },
							"west":  { "uv": [ 8, 10, 9, 11 ], "texture": "#edge"                  }
						}
					},
					{
						"from": [ 6,  9, 0 ],
						"to":   [ 7, 10, 1 ],
						"faces": {
							"up":    { "uv": [ 9, 6, 10, 7 ], "texture": "#edge" },
							"down":  { "uv": [ 9, 6, 10, 7 ], "texture": "#edge" },
							"north": { "uv": [ 9, 6, 10, 7 ], "texture": "#edge" },
							"east":  { "uv": [ 9, 6, 10, 7 ], "texture": "#edge" },
							"south": { "uv": [ 9, 6, 10, 7 ], "texture": "#edge" },
							"west":  { "uv": [ 9, 6, 10, 7 ], "texture": "#edge" }
						}
					},
					{
						"from":  [ 9,  9, 0 ],
						"to":   [ 10, 10, 1 ],
						"faces": {
							"up":    { "uv": [ 6, 6, 7, 7 ], "texture": "#edge" },
							"down":  { "uv": [ 6, 6, 7, 7 ], "texture": "#edge" },
							"north": { "uv": [ 6, 6, 7, 7 ], "texture": "#edge" },
							"east":  { "uv": [ 6, 6, 7, 7 ], "texture": "#edge" },
							"south": { "uv": [ 6, 6, 7, 7 ], "texture": "#edge" },
							"west":  { "uv": [ 6, 6, 7, 7 ], "texture": "#edge" }
						}
					},
					{
						"from": [ 6, 6, 0 ],
						"to":   [ 7, 7, 1 ],
						"faces": {
							"up":    { "uv": [ 9, 9, 10, 10 ], "texture": "#edge" },
							"down":  { "uv": [ 9, 9, 10, 10 ], "texture": "#edge" },
							"north": { "uv": [ 9, 9, 10, 10 ], "texture": "#edge" },
							"east":  { "uv": [ 9, 9, 10, 10 ], "texture": "#edge" },
							"south": { "uv": [ 9, 9, 10, 10 ], "texture": "#edge" },
							"west":  { "uv": [ 9, 9, 10, 10 ], "texture": "#edge" }
						}
					},
					{
						"from": [  9, 6, 0 ],
						"to":   [ 10, 7, 1 ],
						"faces": {
							"up":    { "uv": [ 6, 9, 7, 10 ], "texture": "#edge" },
							"down":  { "uv": [ 6, 9, 7, 10 ], "texture": "#edge" },
							"north": { "uv": [ 6, 9, 7, 10 ], "texture": "#edge" },
							"east":  { "uv": [ 6, 9, 7, 10 ], "texture": "#edge" },
							"south": { "uv": [ 6, 9, 7, 10 ], "texture": "#edge" },
							"west":  { "uv": [ 6, 9, 7, 10 ], "texture": "#edge" }
						}
					},
					{
						"from": [ 5, 7, 0 ],
						"to":   [ 6, 9, 1 ],
						"faces": {
							"up":    { "uv": [ 10, 7, 11, 8 ], "texture": "#edge" },
							"down":  { "uv": [ 10, 8, 11, 9 ], "texture": "#edge" },
							"north": { "uv": [ 10, 7, 11, 9 ], "texture": "#edge" },
							"east":  { "uv": [ 10, 7, 11, 9 ], "texture": "#edge" },
							"south": { "uv": [ 10, 7, 11, 9 ], "texture": "#edge" },
							"west":  { "uv": [ 10, 7, 11, 9 ], "texture": "#edge" }
						}
					},
					{
						"from": [ 10, 7, 0 ],
						"to":   [ 11, 9, 1 ],
						"faces": {
							"up":    { "uv": [ 5, 7, 6, 8 ], "texture": "#edge" },
							"down":  { "uv": [ 5, 8, 6, 9 ], "texture": "#edge" },
							"north": { "uv": [ 5, 7, 6, 9 ], "texture": "#edge" },
							"east":  { "uv": [ 5, 7, 6, 9 ], "texture": "#edge" },
							"south": { "uv": [ 5, 7, 6, 9 ], "texture": "#edge" },
							"west":  { "uv": [ 5, 7, 6, 9 ], "texture": "#edge" }
						}
					},
					{
						"from": [  6,  6, 0.5 ],
						"to":   [ 10, 10, 0.5 ],
						"jmxl_layer": "TRANSLUCENT",
						"faces": {
							"north": { "uv": [ 6, 6, 10, 10 ], "texture": "#center" },
							"south": { "uv": [ 6, 6, 10, 10 ], "texture": "#center" }
						}
					}
				]
			}"""
		);
	}

	public static void appendThreeNumbers(StringBuilder builder, int x, int y, int z) {
		appendTwoDigits(builder, x);
		builder.append(", ");
		appendTwoDigits(builder, y);
		builder.append(", ");
		appendTwoDigits(builder, z);
	}

	public static void appendTwoDigits(StringBuilder builder, int number) {
		if (number < 10) builder.append(' ');
		builder.append(number);
	}

	public static record VoxelQuad(int x, int y, int z, Direction facing) {}

	public static record VoxelSize(int x, int y, int z) {}

	public static boolean isOnSurface(double x, double y, double z) {
		return isInside(x, y, z) && (
			!isInside(x + 0.0625D, y, z) ||
			!isInside(x - 0.0625D, y, z) ||
			!isInside(x, y + 0.0625D, z) ||
			!isInside(x, y - 0.0625D, z) ||
			!isInside(x, y, z + 0.0625D) ||
			!isInside(x, y, z - 0.0625D)
		);
	}

	public static boolean isInside(double x, double y, double z) {
		return x * x + y * y + z * z < RADIUS_SQUARED;
	}

	@Override
	public void setupMiningToolTags(DataGenContext context) {

	}

	@Override
	public void setupMiningLevelTags(DataGenContext context) {

	}

	@Override
	public void setupOtherBlockTags(DataGenContext context) {

	}

	@Override
	public void setupOtherItemTags(DataGenContext context) {

	}

	@Override
	public void writeRecipes(DataGenContext context) {
		context.writeToFile(
			context.recipePath(this.id),
			new ShapedRecipeBuilder()
			.pattern("pbp", "bbb", "pbp")
			.where('p', Items.GLASS_PANE)
			.where('b', Items.GLASS)
			.result(this.id)
			.toString()
		);
	}
}