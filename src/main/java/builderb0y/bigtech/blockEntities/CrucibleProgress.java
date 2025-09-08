package builderb0y.bigtech.blockEntities;

import com.mojang.serialization.Codec;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

import builderb0y.autocodec.coders.AutoCoder;
import builderb0y.autocodec.decoders.DecodeException;
import builderb0y.bigtech.BigTechMod;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.codecs.ItemStackCoder.VerifyEmptyable;
import builderb0y.bigtech.recipes.ArcFurnaceRecipe;

public class CrucibleProgress {

	public static final AutoCoder<CrucibleProgress> CODER = BigTechAutoCodec.callerCoder();
	public static final Codec<CrucibleProgress> CODEC = BigTechAutoCodec.AUTO_CODEC.createDFUCodec(CODER);

	public @VerifyEmptyable ItemStack slowCoolResult;
	public @VerifyEmptyable ItemStack fastCoolResult;
	public int heat;
	public int maxHeat;
	public int coolingRate;
	public boolean cooling;

	public static CrucibleProgress fromRecipe(ArcFurnaceRecipe recipe) {
		CrucibleProgress progress = new CrucibleProgress();
		progress.slowCoolResult = recipe.slow_cool_result;
		progress.fastCoolResult = recipe.fast_cool_result;
		progress.maxHeat        = recipe.energy;
		progress.coolingRate    = recipe.cooling_rate;
		return progress;
	}

	public NbtCompound toNbt(WrapperLookup registries) {
		return BigTechAutoCodec.AUTO_CODEC.encode(CODER, this, registries.getOps(NbtOps.INSTANCE)).as();
	}

	public static CrucibleProgress fromNbt(NbtCompound compound, WrapperLookup registries) {
		try {
			return BigTechAutoCodec.AUTO_CODEC.decode(CODER, compound, registries.getOps(NbtOps.INSTANCE));
		}
		catch (DecodeException error) {
			BigTechMod.LOGGER.error("Failed to read crucible progress from NBT.", error);
			return new CrucibleProgress();
		}
	}
}