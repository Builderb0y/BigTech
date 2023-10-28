package builderb0y.bigtech.util;

public interface RegistrableCollection<T> {

	public abstract RegistrableVariant<T>[] getRegistrableVariants();

	public static record RegistrableVariant<T>(T object, Object variant) {}
}