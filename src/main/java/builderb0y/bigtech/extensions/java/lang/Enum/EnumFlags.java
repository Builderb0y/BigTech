package builderb0y.bigtech.extensions.java.lang.Enum;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

@Extension
public class EnumFlags {

	public static int flag(@This Enum<?> enum_) {
		return 1 << enum_.ordinal();
	}
}