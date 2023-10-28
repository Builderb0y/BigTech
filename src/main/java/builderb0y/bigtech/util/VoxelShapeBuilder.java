package builderb0y.bigtech.util;

import net.minecraft.util.BlockRotation;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public interface VoxelShapeBuilder {

	public static VoxelShapeBuilder create() {
		return new Base();
	}

	public abstract void addCuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ);

	public abstract VoxelShape buildAndReset();

	public default VoxelShapeBuilder translate(double x, double y, double z) {
		return x == 0.0D && y == 0.0D && z == 0.0D ? this : new Translate(this, x, y, z);
	}

	public default VoxelShapeBuilder rotateAround(double x, double y, double z, BlockRotation rotation) {
		return this.translate(x, y, z).rotate(rotation).translate(-x, -y, -z);
	}

	public default VoxelShapeBuilder rotate(BlockRotation rotation) {
		return switch (rotation) {
			case NONE                -> this;
			case CLOCKWISE_90        -> new Rotate90 (this);
			case CLOCKWISE_180       -> new Rotate180(this);
			case COUNTERCLOCKWISE_90 -> new Rotate270(this);
		};
	}

	public static class Base implements VoxelShapeBuilder {

		public VoxelShape shape = VoxelShapes.empty();

		@Override
		public void addCuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
			this.shape = VoxelShapes.combine(
				this.shape,
				VoxelShapes.cuboid(minX, minY, minZ, maxX, maxY, maxZ),
				BooleanBiFunction.OR
			);
		}

		@Override
		public VoxelShape buildAndReset() {
			VoxelShape result = this.shape.simplify();
			this.shape = VoxelShapes.empty();
			return result;
		}
	}

	public static interface Delegated extends VoxelShapeBuilder {

		public abstract VoxelShapeBuilder delegate();

		@Override
		public default VoxelShape buildAndReset() {
			return this.delegate().buildAndReset();
		}
	}

	public static record Translate(VoxelShapeBuilder delegate, double x, double y, double z) implements Delegated {

		@Override
		public void addCuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
			this.delegate.addCuboid(this.x + minX, this.y + minY, this.z + minZ, this.x + maxX, this.y + maxY, this.z + maxZ);
		}

		@Override
		public VoxelShapeBuilder translate(double x, double y, double z) {
			return this.delegate.translate(this.x + x, this.y + y, this.z + z);
		}
	}

	public static record Rotate90(VoxelShapeBuilder delegate) implements Delegated {

		@Override
		public void addCuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
			this.delegate.addCuboid(-maxZ, minY, minX, -minZ, maxY, maxX);
		}

		@Override
		public VoxelShapeBuilder rotate(BlockRotation rotation) {
			return this.delegate.rotate(BlockRotation.CLOCKWISE_90.rotate(rotation));
		}
	}

	public static record Rotate180(VoxelShapeBuilder delegate) implements Delegated {

		@Override
		public void addCuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
			this.delegate.addCuboid(-maxX, minY, -maxZ, -minX, maxY, -minZ);
		}

		@Override
		public VoxelShapeBuilder rotate(BlockRotation rotation) {
			return this.delegate.rotate(BlockRotation.CLOCKWISE_180.rotate(rotation));
		}
	}

	public static record Rotate270(VoxelShapeBuilder delegate) implements Delegated {

		@Override
		public void addCuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
			this.delegate.addCuboid(minZ, minY, -maxX, maxZ, maxY, -minX);
		}

		@Override
		public VoxelShapeBuilder rotate(BlockRotation rotation) {
			return this.delegate.rotate(BlockRotation.COUNTERCLOCKWISE_90.rotate(rotation));
		}
	}
}