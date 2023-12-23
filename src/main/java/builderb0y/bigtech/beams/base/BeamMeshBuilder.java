package builderb0y.bigtech.beams.base;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;

import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.beams.storage.chunk.ChunkBeamStorageHolder;
import builderb0y.bigtech.beams.storage.section.BasicSectionBeamStorage;
import builderb0y.bigtech.beams.storage.section.CommonSectionBeamStorage;
import builderb0y.bigtech.compatibility.SodiumCompatibility;
import builderb0y.bigtech.util.Directions;

@Environment(EnvType.CLIENT)
public class BeamMeshBuilder {

	public static final float r = 0.0625F - 1.0F / 1024.0F;
	public static final Mesh EMPTY_MESH = RendererAccess.INSTANCE.renderer.meshBuilder().build();
	public static final EnumMap<BeamDirection, Extrusion[]> EXTRUSIONS = new EnumMap<>(BeamDirection.class);
	static {
		Point[] corners = computeCorners();
		Line[] edges = computeEdges(corners);
		//hard-code the directions which just so happened to be inside out with this algorithm.
		final int reversed = 0b100_000_010___111_100_000___101_111_110;
		for (BeamDirection direction : BeamDirection.VALUES) {
			Line[] starts, ends;
			switch (direction.type) {
				default -> { continue; }
				case CENTER -> { continue; }
				case FACE -> {
					starts = Arrays.stream(edges).filter((Line edge) -> edge.start.dot(direction) > 0.0F && edge.end.dot(direction) > 0.0F).toArray(Line[]::new);
					ends = Arrays.stream(edges).filter((Line edge) -> edge.start.dot(direction) < 0.0F && edge.end.dot(direction) < 0.0F).toArray(Line[]::new);
				}
				case EDGE -> {
					starts = Arrays.stream(edges).filter((Line edge) -> edge.start.dot(direction) >= 0.0F && edge.end.dot(direction) >= 0.0F && !(edge.start.dot(direction) > 0.0F && edge.end.dot(direction) > 0.0F)).toArray(Line[]::new);
					ends = Arrays.stream(edges).filter((Line edge) -> edge.start.dot(direction) <= 0.0F && edge.end.dot(direction) <= 0.0F && !(edge.start.dot(direction) < 0.0F && edge.end.dot(direction) < 0.0F)).toArray(Line[]::new);
				}
				case CORNER -> {
					starts = Arrays.stream(edges).filter((Line edge) -> Math.abs(edge.start.dot(direction)) < r * 3.0F && Math.abs(edge.end.dot(direction)) < r * 3.0F).toArray(Line[]::new);
					ends = Arrays.stream(edges).filter((Line edge) -> Math.abs(edge.start.dot(direction)) < r * 3.0F && Math.abs(edge.end.dot(direction)) < r * 3.0F).toArray(Line[]::new);
				}
			}
			assert starts.length == ends.length;
			alignFirst(direction, starts, ends);
			if ((reversed & direction.flag()) != 0) {
				starts[0] = starts[0].flip();
				ends  [0] = ends  [0].flip();
			}
			sortEdges(starts);
			sortEdges(ends);
			EXTRUSIONS.put(direction, IntStream.range(0, starts.length).mapToObj((int index) -> new Extrusion(starts[index], ends[index])).toArray(Extrusion[]::new));
		}
	}
	public static final byte[] FACE_FLAGS = new byte[BeamDirection.VALUES.length];
	static {
		for (BeamDirection direction : BeamDirection.VALUES) {
			int faceFlags = 0;
			if (direction.x > 0) faceFlags |= Directions.POSITIVE_X.flag(); else
			if (direction.x < 0) faceFlags |= Directions.NEGATIVE_X.flag();
			if (direction.y > 0) faceFlags |= Directions.POSITIVE_Y.flag(); else
			if (direction.y < 0) faceFlags |= Directions.NEGATIVE_Y.flag();
			if (direction.z > 0) faceFlags |= Directions.POSITIVE_Z.flag(); else
			if (direction.z < 0) faceFlags |= Directions.NEGATIVE_Z.flag();
			FACE_FLAGS[direction.ordinal()] = (byte)(faceFlags);
		}
	}

	public static Mesh build(CommonSectionBeamStorage storage) {
		if (!storage.isEmpty) {
			MeshBuilder builder = RendererAccess.INSTANCE.renderer.meshBuilder();
			QuadEmitter emitter = builder.emitter;
			EnumMap<BeamDirection, AdjacentColorAccumulator> directionToColor = new EnumMap<>(BeamDirection.class);
			for (BeamDirection direction : BeamDirection.VALUES) {
				directionToColor.put(direction, new AdjacentColorAccumulator());
			}
			ColorAccumulator averageColor = new ColorAccumulator();
			Sprite sprite = MinecraftClient.instance.bakedModelManager.getAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).getSprite(BigTechMod.modID("block/beam"));
			SodiumCompatibility.markSpriteActive(sprite);
			ObjectIterator<Short2ObjectMap.Entry<LinkedList<BeamSegment>>> iterator = storage.short2ObjectEntrySet().fastIterator();
			while (iterator.hasNext()) {
				Short2ObjectMap.Entry<LinkedList<BeamSegment>> blockEntry = iterator.next();
				short packedPos = blockEntry.shortKey;
				int x = BasicSectionBeamStorage.unpackX(packedPos);
				int y = BasicSectionBeamStorage.unpackY(packedPos);
				int z = BasicSectionBeamStorage.unpackZ(packedPos);
				for (BeamSegment segment : blockEntry.value) {
					AdjacentColorAccumulator accumulator = directionToColor.get(segment.direction);
					accumulator.accept(segment.color);
					averageColor.accept(segment.color);
					if (segment.direction != BeamDirection.CENTER) {
						int adjacentX = x + segment.direction.x;
						int adjacentY = y + segment.direction.y;
						int adjacentZ = z + segment.direction.z;
						LinkedList<BeamSegment> adjacentSegments = getAdjacentSegments(storage, adjacentX, adjacentY, adjacentZ);
						if (adjacentSegments != null) {
							for (BeamSegment adjacentSegment : adjacentSegments) {
								if (adjacentSegment.direction == segment.direction.opposite) {
									accumulator.accept(adjacentSegment.color);
									accumulator.containsAdjacents = true;
								}
							}
						}
					}
				}
				float x1 = x + 0.5F;
				float y1 = y + 0.5F;
				float z1 = z + 0.5F;
				int faceFlags = 0b111_111;
				for (BeamDirection direction : BeamDirection.VALUES) {
					if (direction == BeamDirection.CENTER) continue;
					AdjacentColorAccumulator accumulator = directionToColor.get(direction);
					Vector3f color = accumulator.getColor();
					if (color != null) {
						faceFlags &= ~FACE_FLAGS[direction.ordinal()];
						float red   = color.x;
						float green = color.y;
						float blue  = color.z;
						boolean containsAdjacents = accumulator.containsAdjacents;
						accumulator.reset();
						if (containsAdjacents && direction.ordinal() < BeamDirection.CENTER.ordinal()) {
							continue;
						}
						float x2 = x1 + direction.x;
						float y2 = y1 + direction.y;
						float z2 = z1 + direction.z;
						Extrusion[] extrusions = EXTRUSIONS.get(direction);
						for (Extrusion extrusion : extrusions) {
							Point p0 = extrusion.start.start;
							Point p1 = extrusion.start.end;
							Point p2 = extrusion.end.end;
							Point p3 = extrusion.end.start;
							quad(
								emitter,
								x1 + p0.x, x1 + p1.x, x2 + p2.x, x2 + p3.x,
								y1 + p0.y, y1 + p1.y, y2 + p2.y, y2 + p3.y,
								z1 + p0.z, z1 + p1.z, z2 + p2.z, z2 + p3.z,
								0.0F, 1.0F, containsAdjacents ? 0.125F : 0.0F, containsAdjacents ? 0.25F : 0.125F,
								red, green, blue, 1.0F,
								sprite
							);
						}
					}
					if ((faceFlags & FACE_FLAGS[direction.ordinal()]) != 0) {
						LinkedList<BeamSegment> adjacentSegments = getAdjacentSegments(storage, x + direction.x, y + direction.y, z + direction.z);
						if (adjacentSegments != null) {
							for (BeamSegment segment : adjacentSegments) {
								if (segment.direction == direction.opposite) {
									faceFlags &= ~FACE_FLAGS[direction.ordinal()];
								}
							}
						}
					}
				}
				Vector3f color = averageColor.getColor();
				if (color != null) {
					float red   = color.x;
					float green = color.y;
					float blue  = color.z;
					averageColor.reset();
					if ((faceFlags & Directions.POSITIVE_Y.flag()) != 0) quad(
						emitter,
						x1 - r, x1 - r, x1 + r, x1 + r,
						y1 + r, y1 + r, y1 + r, y1 + r,
						z1 - r, z1 + r, z1 + r, z1 - r,
						0.0F, 0.125F, 0.875F, 1.0F,
						red, green, blue, 1.0F,
						sprite
					);
					if ((faceFlags & Directions.NEGATIVE_Y.flag()) != 0) quad(
						emitter,
						x1 - r, x1 - r, x1 + r, x1 + r,
						y1 - r, y1 - r, y1 - r, y1 - r,
						z1 + r, z1 - r, z1 - r, z1 + r,
						0.0F, 0.125F, 0.875F, 1.0F,
						red, green, blue, 1.0F,
						sprite
					);
					if ((faceFlags & Directions.POSITIVE_X.flag()) != 0) quad(
						emitter,
						x1 + r, x1 + r, x1 + r, x1 + r,
						y1 + r, y1 - r, y1 - r, y1 + r,
						z1 + r, z1 + r, z1 - r, z1 - r,
						0.0F, 0.125F, 0.875F, 1.0F,
						red, green, blue, 1.0F,
						sprite
					);
					if ((faceFlags & Directions.NEGATIVE_X.flag()) != 0) quad(
						emitter,
						x1 - r, x1 - r, x1 - r, x1 - r,
						y1 + r, y1 - r, y1 - r, y1 + r,
						z1 - r, z1 - r, z1 + r, z1 + r,
						0.0F, 0.125F, 0.875F, 1.0F,
						red, green, blue, 1.0F,
						sprite
					);
					if ((faceFlags & Directions.POSITIVE_Z.flag()) != 0) quad(
						emitter,
						x1 - r, x1 - r, x1 + r, x1 + r,
						y1 + r, y1 - r, y1 - r, y1 + r,
						z1 + r, z1 + r, z1 + r, z1 + r,
						0.0F, 0.125F, 0.875F, 1.0F,
						red, green, blue, 1.0F,
						sprite
					);
					if ((faceFlags & Directions.NEGATIVE_Z.flag()) != 0) quad(
						emitter,
						x1 + r, x1 + r, x1 - r, x1 - r,
						y1 + r, y1 - r, y1 - r, y1 + r,
						z1 - r, z1 - r, z1 - r, z1 - r,
						0.0F, 0.125F, 0.875F, 1.0F,
						red, green, blue, 1.0F,
						sprite
					);
				}
			}
			return builder.build();
		}
		else {
			return EMPTY_MESH;
		}
	}

	public static @Nullable LinkedList<BeamSegment> getAdjacentSegments(CommonSectionBeamStorage storage, int adjacentX, int adjacentY, int adjacentZ) {
		CommonSectionBeamStorage adjacentStorage = getAdjacentStorage(storage, adjacentX, adjacentY, adjacentZ);
		return adjacentStorage != null ? adjacentStorage.checkSegments(adjacentX, adjacentY, adjacentZ) : null;
	}

	public static @Nullable CommonSectionBeamStorage getAdjacentStorage(CommonSectionBeamStorage storage, int adjacentX, int adjacentY, int adjacentZ) {
		if (adjacentX >> 4 == 0 && adjacentZ >> 4 == 0) {
			if (adjacentY >> 4 == 0) {
				return storage;
			}
			else {
				return ChunkBeamStorageHolder.KEY.get(storage.chunk).require().get(storage.sectionCoordY + (adjacentY >> 4));
			}
		}
		else {
			Chunk chunk = storage.chunk.world.getChunk(storage.chunk.pos.x + (adjacentX >> 4), storage.chunk.pos.z + (adjacentZ >> 4), ChunkStatus.FULL, false);
			if (chunk != null) {
				return ChunkBeamStorageHolder.KEY.get(chunk).require().get(storage.sectionCoordY + (adjacentY >> 4));
			}
			else {
				return null;
			}
		}
	}

	public static void quad(
		QuadEmitter quadEmitter,
		float x0, float x1, float x2, float x3,
		float y0, float y1, float y2, float y3,
		float z0, float z1, float z2, float z3,
		float u0, float u1, float v0, float v1,
		float r,  float g,  float b,  float a,
		Sprite sprite
	) {
		float lerpedU0 = MathHelper.lerp(u0, sprite.minU, sprite.maxU);
		float lerpedU1 = MathHelper.lerp(u1, sprite.minU, sprite.maxU);
		float lerpedV0 = MathHelper.lerp(v0, sprite.minV, sprite.maxV);
		float lerpedV1 = MathHelper.lerp(v1, sprite.minV, sprite.maxV);
		int color = (
			Math.max(Math.min((int)(a * 256.0F), 255), 0) << 24 |
			Math.max(Math.min((int)(r * 256.0F), 255), 0) << 16 |
			Math.max(Math.min((int)(g * 256.0F), 255), 0) <<  8 |
			Math.max(Math.min((int)(b * 256.0F), 255), 0)
		);
		quadEmitter
		.pos(0, x0, y0, z0)
		.pos(1, x1, y1, z1)
		.pos(2, x2, y2, z2)
		.pos(3, x3, y3, z3)
		.uv(0, lerpedU0, lerpedV0)
		.uv(1, lerpedU0, lerpedV1)
		.uv(2, lerpedU1, lerpedV1)
		.uv(3, lerpedU1, lerpedV0)
		.color(0, color)
		.color(1, color)
		.color(2, color)
		.color(3, color)
		.lightmap(0, 0xF000F0)
		.lightmap(1, 0xF000F0)
		.lightmap(2, 0xF000F0)
		.lightmap(3, 0xF000F0)
		.emit();
	}

	public static class AdjacentColorAccumulator extends ColorAccumulator {

		public boolean containsAdjacents;

		@Override
		public void reset() {
			super.reset();
			this.containsAdjacents = false;
		}
	}

	public static record Point(float x, float y, float z) {

		public float dot(BeamDirection direction) {
			return this.x * direction.x + this.y * direction.y + this.z * direction.z;
		}
	}
	public static record Line(Point start, Point end) {

		public Line flip() {
			return new Line(this.end, this.start);
		}
	}

	public static record Extrusion(Line start, Line end) {}

	public static Point[] computeCorners() {
		Point[] corners = new Point[8];
		for (int index = 0; index < 8; index++) {
			corners[index] = new Point(
				(index & 4) != 0 ? +r : -r,
				(index & 2) != 0 ? +r : -r,
				(index & 1) != 0 ? +r : -r
			);
		}
		return corners;
	}

	public static Line[] computeEdges(Point[] corners) {
		Line[] edges = new Line[12];
		int index = 0;
		for (int index1 = 0; index1 < 8; index1++) {
			for (int index2 = index1; ++index2 < 8; ) {
				//one bit for every coordinate which is different.
				int similarity = index1 ^ index2;
				//test if exactly one bit is set.
				if (similarity != 0 && (similarity & (similarity - 1)) == 0) {
					edges[index++] = new Line(corners[index1], corners[index2]);
				}
			}
		}
		assert index == 12;
		return edges;
	}

	public static void alignFirst(BeamDirection direction, Line[] starts, Line[] ends) {
		if (direction.type == BeamDirection.Type.FACE) {
			//there will be no perfect co-linear match,
			//so choose the closest one.
			Line target = starts[0];
			Line best = null;
			int bestIndex = -1;
			float bestDistance = Float.POSITIVE_INFINITY;
			for (int index = 0, length = ends.length; index < length; index++) {
				Line test = ends[index];
				float distance1 = distance(test.start, target.start) + distance(test.end, target.end);
				float distance2 = distance(test.end, target.start) + distance(test.start, target.end);
				if (distance1 < bestDistance) {
					best = test;
					bestIndex = index;
					bestDistance = distance1;
				}
				if (distance2 < bestDistance) {
					best = test.flip();
					bestIndex = index;
					bestDistance = distance2;
				}
			}
			ends[bestIndex] = ends[0];
			ends[0] = best;
		}
		else {
			//there will be at least one perfect co-linear line.
			for (int index1 = 0, length = starts.length; index1 < length; index1++) {
				Line start = starts[index1];
				for (int index2 = 0; index2 < length; index2++) {
					Line end = ends[index2];
					if (start.start.equals(end.start) && start.end.equals(end.end)) {
						starts[index1] = starts[0];
						starts[0] = start;
						ends[index2] = ends[0];
						ends[0] = end;
						return;
					}
					else if (start.end.equals(end.start) && start.start.equals(end.end)) {
						starts[index1] = starts[0];
						starts[0] = start;
						ends[index2] = ends[0];
						ends[0] = end.flip();
						return;
					}
				}
			}
			throw new AssertionError("Unable to find perfect match.");
		}
	}

	public static float distance(Point p1, Point p2) {
		return length(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
	}

	public static float length(float x, float y, float z) {
		return MathHelper.sqrt(x * x + y * y + z * z);
	}

	public static void sortEdges(Line[] selectedEdges) {
		outer:
		for (int index1 = 1, length = selectedEdges.length; index1 < length; index1++) {
			Line first = selectedEdges[index1 - 1];
			for (int index2 = index1; index2 < length; index2++) {
				Line test = selectedEdges[index2];
				if (test.start.equals(first.end)) {
					selectedEdges[index2] = selectedEdges[index1];
					selectedEdges[index1] = test;
					continue outer;
				}
				else if (test.end.equals(first.end)) {
					selectedEdges[index2] = selectedEdges[index1];
					selectedEdges[index1] = test.flip();
					continue outer;
				}
			}
			throw new AssertionError("Could not find connected line");
		}
		assert selectedEdges[selectedEdges.length - 1].end.equals(selectedEdges[0].start);
	}
}