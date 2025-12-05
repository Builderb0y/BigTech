package builderb0y.bigtech.extensions.java.lang.String;

import java.util.stream.Collectors;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

@Extension
public class StringExtensions {

	//why does the default String.indent() method not take a char to indent with?
	public static String indent(@This String self, char c, int amount) {
		return self.lines().map(String.valueOf(c).repeat(amount)::concat).collect(Collectors.joining("\n"));
	}
}