package builderb0y.bigtech.extensions.net.minecraft.nbt.NbtList;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

@Extension
public class NbtListExtensions {

	public static NbtCompound createCompound(@This NbtList thiz) {
		NbtCompound result = new NbtCompound();
		thiz.add(result);
		return result;
	}
}