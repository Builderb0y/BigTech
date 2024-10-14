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
	public static final Mesh EMPTY_MESH = RendererAccess.INSTANCE.getRenderer().meshBuilder().build();
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
			for (int index = 0, length = ends.length; index < length; index++) {
				ends[index] = ends[index].offset(direction.x, direction.y, direction.z);
			}
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

	//todo: consider using a dedicated class that isn't Mesh,
	//	so that normals and UVs can be encoded more efficiently
	//	(meaning on a per-quad basis, not a per-vertex basis).
	public static <T_Storage extends BasicSectionBeamStorage> Mesh build(T_Storage storage, AdjacentSegmentLoader<T_Storage> loader) {
		if (!storage.isEmpty()) {
			MeshBuilder builder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
			QuadEmitter emitter = builder.getEmitter();
			EnumMap<BeamDirection, AdjacentColorAccumulator> directionToColor = new EnumMap<>(BeamDirection.class);
			for (BeamDirection direction : BeamDirection.VALUES) {
				directionToColor.put(direction, new AdjacentColorAccumulator());
			}
			ColorAccumulator averageColor = new ColorAccumulator();
			Sprite sprite = MinecraftClient.getInstance().getBakedModelManager().getAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).getSprite(BigTechMod.modID("block/beam"));
			SodiumCompatibility.markSpriteActive(sprite);
			ObjectIterator<Short2ObjectMap.Entry<LinkedList<BeamSegment>>> iterator = storage.short2ObjectEntrySet().fastIterator();
			while (iterator.hasNext()) {
				Short2ObjectMap.Entry<LinkedList<BeamSegment>> blockEntry = iterator.next();
				short packedPos = blockEntry.getShortKey();
				int x = BasicSectionBeamStorage.unpackX(packedPos);
				int y = BasicSectionBeamStorage.unpackY(packedPos);
				int z = BasicSectionBeamStorage.unpackZ(packedPos);
				for (BeamSegment segment : blockEntry.getValue()) {
					AdjacentColorAccumulator accumulator = directionToColor.get(segment.direction());
					accumulator.accept(segment.color());
					averageColor.accept(segment.color());
					if (segment.direction() != BeamDirection.CENTER) {
						int adjacentX = x + segment.direction().x;
						int adjacentY = y + segment.direction().y;
						int adjacentZ = z + segment.direction().z;
						LinkedList<BeamSegment> adjacentSegments = loader.getAdjacentSegments(storage, adjacentX, adjacentY, adjacentZ);
						if (adjacentSegments != null) {
							for (BeamSegment adjacentSegment : adjacentSegments) {
								if (adjacentSegment.direction() == segment.direction().getOpposite()) {
									accumulator.accept(adjacentSegment.color());
									accumulator.containsAdjacents = true;
								}
							}
						}
					}
				}
				float centerX = x + 0.5F;
				float centerY = y + 0.5F;
				float centerZ = z + 0.5F;
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
						Extrusion[] extrusions = EXTRUSIONS.get(direction);
						for (Extrusion extrusion : extrusions) {
							Point p0 = extrusion.start.start;
							Point p1 = extrusion.start.end;
							Point p2 = extrusion.end.end;
							Point p3 = extrusion.end.start;
							quad(
								emitter,
								centerX + p0.x, centerX + p1.x, centerX + p2.x, centerX + p3.x,
								centerY + p0.y, centerY + p1.y, centerY + p2.y, centerY + p3.y,
								centerZ + p0.z, centerZ + p1.z, centerZ + p2.z, centerZ + p3.z,
								0.0F, 1.0F, containsAdjacents ? 0.125F : 0.0F, containsAdjacents ? 0.25F : 0.125F,
								red, green, blue, 1.0F,
								extrusion.normal.x, extrusion.normal.y, extrusion.normal.z,
								sprite
							);
						}
					}
					if ((faceFlags & FACE_FLAGS[direction.ordinal()]) != 0) {
						LinkedList<BeamSegment> adjacentSegments = loader.getAdjacentSegments(storage, x + direction.x, y + direction.y, z + direction.z);
						if (adjacentSegments != null) {
							for (BeamSegment segment : adjacentSegments) {
								if (segment.direction() == direction.getOpposite()) {
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
						centerX - r, centerX - r, centerX + r, centerX + r,
						centerY + r, centerY + r, centerY + r, centerY + r,
						centerZ - r, centerZ + r, centerZ + r, centerZ - r,
						0.0F, 0.125F, 0.875F, 1.0F,
						red, green, blue, 1.0F,
						0.0F, 1.0F, 0.0F,
						sprite
					);
					if ((faceFlags & Directions.NEGATIVE_Y.flag()) != 0) quad(
						emitter,
						centerX - r, centerX - r, centerX + r, centerX + r,
						centerY - r, centerY - r, centerY - r, centerY - r,
						centerZ + r, centerZ - r, centerZ - r, centerZ + r,
						0.0F, 0.125F, 0.875F, 1.0F,
						red, green, blue, 1.0F,
						0.0F, -1.0F, 0.0F,
						sprite
					);
					if ((faceFlags & Directions.POSITIVE_X.flag()) != 0) quad(
						emitter,
						centerX + r, centerX + r, centerX + r, centerX + r,
						centerY + r, centerY - r, centerY - r, centerY + r,
						centerZ + r, centerZ + r, centerZ - r, centerZ - r,
						0.0F, 0.125F, 0.875F, 1.0F,
						red, green, blue, 1.0F,
						1.0F, 0.0F, 0.0F,
						sprite
					);
					if ((faceFlags & Directions.NEGATIVE_X.flag()) != 0) quad(
						emitter,
						centerX - r, centerX - r, centerX - r, centerX - r,
						centerY + r, centerY - r, centerY - r, centerY + r,
						centerZ - r, centerZ - r, centerZ + r, centerZ + r,
						0.0F, 0.125F, 0.875F, 1.0F,
						red, green, blue, 1.0F,
						-1.0F, 0.0F, 0.0F,
						sprite
					);
					if ((faceFlags & Directions.POSITIVE_Z.flag()) != 0) quad(
						emitter,
						centerX - r, centerX - r, centerX + r, centerX + r,
						centerY + r, centerY - r, centerY - r, centerY + r,
						centerZ + r, centerZ + r, centerZ + r, centerZ + r,
						0.0F, 0.125F, 0.875F, 1.0F,
						red, green, blue, 1.0F,
						0.0F, 0.0F, 1.0F,
						sprite
					);
					if ((faceFlags & Directions.NEGATIVE_Z.flag()) != 0) quad(
						emitter,
						centerX + r, centerX + r, centerX - r, centerX - r,
						centerY + r, centerY - r, centerY - r, centerY + r,
						centerZ - r, centerZ - r, centerZ - r, centerZ - r,
						0.0F, 0.125F, 0.875F, 1.0F,
						red, green, blue, 1.0F,
						0.0F, 0.0F, -1.0F,
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

	@FunctionalInterface
	public static interface AdjacentSegmentLoader<T_Storage extends BasicSectionBeamStorage> {

		public static final AdjacentSegmentLoader<CommonSectionBeamStorage> PERSISTENT = (CommonSectionBeamStorage storage, int adjacentX, int adjacentY, int adjacentZ) -> {
			if (adjacentX >> 4 == 0 && adjacentZ >> 4 == 0) {
				if (adjacentY >> 4 == 0) {
					return storage;
				}
				else {
					return ChunkBeamStorageHolder.KEY.get(storage.chunk).require().get(storage.sectionY + (adjacentY >> 4));
				}
			}
			else {
				Chunk chunk = storage.chunk.getWorld().getChunk(storage.chunk.getPos().x + (adjacentX >> 4), storage.chunk.getPos().z + (adjacentZ >> 4), ChunkStatus.FULL, false);
				if (chunk != null) {
					return ChunkBeamStorageHolder.KEY.get(chunk).require().get(storage.sectionY + (adjacentY >> 4));
				}
				else {
					return null;
				}
			}
		};

		public static AdjacentSegmentLoader<BasicSectionBeamStorage> pulse(PulseBeam beam) {
			return (BasicSectionBeamStorage storage, int adjacentX, int adjacentY, int adjacentZ) -> {
				if (adjacentX >> 4 == 0 && adjacentY >> 4 == 0 && adjacentZ >> 4 == 0) {
					return storage;
				}
				else {
					return beam.seen.checkSegments(
						storage.sectionX + (adjacentX >> 4),
						storage.sectionY + (adjacentY >> 4),
						storage.sectionZ + (adjacentZ >> 4)
					);
				}
			};
		}

		public default @Nullable LinkedList<BeamSegment> getAdjacentSegments(T_Storage storage, int adjacentX, int adjacentY, int adjacentZ) {
			T_Storage adjacentStorage = this.getAdjacentStorage(storage, adjacentX, adjacentY, adjacentZ);
			return adjacentStorage != null ? adjacentStorage.checkSegments(adjacentX, adjacentY, adjacentZ) : null;
		}

		public abstract @Nullable T_Storage getAdjacentStorage(T_Storage storage, int adjacentX, int adjacentY, int adjacentZ);
	}

	public static void quad(
		QuadEmitter quadEmitter,
		float x0, float x1, float x2, float x3,
		float y0, float y1, float y2, float y3,
		float z0, float z1, float z2, float z3,
		float u0, float u1, float v0, float v1,
		float r,  float g,  float b,  float a,
		float nx, float ny, float nz,
		Sprite sprite
	) {
		float lerpedU0 = MathHelper.lerp(u0, sprite.getMinU(), sprite.getMaxU());
		float lerpedU1 = MathHelper.lerp(u1, sprite.getMinU(), sprite.getMaxU());
		float lerpedV0 = MathHelper.lerp(v0, sprite.getMinV(), sprite.getMaxV());
		float lerpedV1 = MathHelper.lerp(v1, sprite.getMinV(), sprite.getMaxV());
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
		.normal(0, nx, ny, nz)
		.normal(1, nx, ny, nz)
		.normal(2, nx, ny, nz)
		.normal(3, nx, ny, nz)
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

		public Point offset(float x, float y, float z) {
			return new Point(this.x + x, this.y + y, this.z + z);
		}
	}
	public static record Line(Point start, Point end) {

		public Line flip() {
			return new Line(this.end, this.start);
		}

		public Line offset(float x, float y, float z) {
			return new Line(this.start.offset(x, y, z), this.end.offset(x, y, z));
		}
	}

	public static record Extrusion(Line start, Line end, Point normal) {

		public Extrusion(Line start, Line end) {
			this(start, end, computeNormal(start, end));
		}

		public static Point computeNormal(Line start, Line end) {
			float
				x1 = start.end.x - start.start.x,
				y1 = start.end.y - start.start.y,
				z1 = start.end.z - start.start.z,
				x2 = end.start.x - start.start.x,
				y2 = end.start.y - start.start.y,
				z2 = end.start.z - start.start.z,
				//x  y  z
				//x1 y1 z1
				//x2 y2 z2
				crossX = y1 * z2 - z1 * y2,
				crossY = z1 * x2 - x1 * z2,
				crossZ = x1 * y2 - y1 * x2,
				scalar = (float)(1.0D / Math.sqrt(crossX * crossX + crossY * crossY + crossZ * crossZ));
			return new Point(crossX * scalar, crossY * scalar, crossZ * scalar);
		}
	}

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