package builderb0y.bigtech.extensions.java.lang.Object;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

@Extension
public class ConvenientCasting {

	/**
	first, ((Bar)foo).barMethod() is ugly syntax.
	the ideal syntax would be foo.as(Bar).barMethod().
	the closest I can get to that with extensions is foo.as(Bar.class).barMethod()
	the way to do this is: {@code
		public static <T> T as(@This Object object, Class<T> clazz) {
			return clazz.cast(object);
		}
	}
	however, due to generic type erasure, this will actually cast the object twice.
	once inside the extension method (Class::cast), and once at the return site (CHECKCAST).
	so a slightly more efficient option would be to do an unchecked generic cast inside as(),
	since the CHECKCAST will still remain. but if you're doing that, you don't really need
	the Class parameter anymore. so the new syntax looks like foo.<Bar>as().barMethod().
	this is not my ideal syntax, but it's close enough, and it's about as efficient as it can get.
	as a side bonus, this also allows casting to generic types and, in some cases, automatic type inference.
	*/
	@SuppressWarnings("unchecked")
	public static <T> T as(@This Object object) {
		return (T)(object);
	}
}