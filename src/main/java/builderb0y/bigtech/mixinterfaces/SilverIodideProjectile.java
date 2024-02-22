package builderb0y.bigtech.mixinterfaces;

public interface SilverIodideProjectile {

	public abstract Type bigtech_getProjectileType();

	public abstract void bigtech_setProjectileType(Type type);

	public static enum Type {
		NONE,
		LESS_RAINY,
		MORE_RAINY;

		public static final Type[] VALUES = values();
	}
}