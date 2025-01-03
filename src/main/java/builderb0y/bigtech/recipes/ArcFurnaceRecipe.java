package builderb0y.bigtech.recipes;

import java.util.List;

import com.mojang.serialization.MapCodec;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.world.World;

import builderb0y.autocodec.annotations.DefaultObject;
import builderb0y.autocodec.annotations.DefaultObject.DefaultObjectMode;
import builderb0y.autocodec.annotations.VerifyIntRange;
import builderb0y.autocodec.annotations.VerifyNotEmpty;
import builderb0y.bigtech.blockEntities.CrucibleProgress;
import builderb0y.bigtech.codecs.BigTechAutoCodec;

public class ArcFurnaceRecipe implements Recipe<ArcFurnaceRecipeInput> {

	public static final MapCodec<ArcFurnaceRecipe> CODEC = BigTechAutoCodec.callerMapCodec();
	public static final PacketCodec<RegistryByteBuf, ArcFurnaceRecipe> PACKET_CODEC = PacketCodec.tuple(
		Ingredient  .PACKET_CODEC.collect(PacketCodecs.toList()), ArcFurnaceRecipe::getInputs,
		ItemStack   .PACKET_CODEC,                                ArcFurnaceRecipe::getSlowCoolResult,
		ItemStack   .OPTIONAL_PACKET_CODEC,                       ArcFurnaceRecipe::getFastCoolResult,
		PacketCodecs.VAR_INT,                                     ArcFurnaceRecipe::getEnergy,
		PacketCodecs.VAR_INT,                                     ArcFurnaceRecipe::getCoolingRate,
		ArcFurnaceRecipe::new
	);
	public static final ItemStack EMPTY_STACK = ItemStack.EMPTY; //copy to avoid obfuscation issues.

	public final @VerifyNotEmpty                                                                                   List<Ingredient>    inputs;           public List<Ingredient> getInputs        () { return this.inputs; }
	public final                                                                                                   ItemStack           slow_cool_result; public ItemStack        getSlowCoolResult() { return this.slow_cool_result; }
	public final @DefaultObject(name = "EMPTY_STACK", in = ArcFurnaceRecipe.class, mode = DefaultObjectMode.FIELD) ItemStack           fast_cool_result; public ItemStack        getFastCoolResult() { return this.fast_cool_result; }
	public final @VerifyIntRange(min = 0, minInclusive = false)                                                    int                 energy;           public int              getEnergy        () { return this.energy; }
	public final @VerifyIntRange(min = 0, minInclusive = false)                                                    int                 cooling_rate;     public int              getCoolingRate   () { return this.cooling_rate; }
	public                                                                                                         IngredientPlacement placement;

	public ArcFurnaceRecipe(
		List<Ingredient> inputs,
		ItemStack        slow_cool_result,
		ItemStack        fast_cool_result,
		int              energy,
		int              cooling_rate
	) {
		this.inputs           = inputs;
		this.slow_cool_result = slow_cool_result;
		this.fast_cool_result = fast_cool_result;
		this.energy           = energy;
		this.cooling_rate     = cooling_rate;
	}

	@Override
	public ItemStack craft(ArcFurnaceRecipeInput input, WrapperLookup registries) {
		return (input.fastCool() && !this.fast_cool_result.isEmpty() ? this.fast_cool_result : this.slow_cool_result).copy();
	}

	@Override
	public boolean matches(ArcFurnaceRecipeInput input, World world) {
		return this.inputs.size() == input.size() && this.recursiveMatch(input.stacks().toArray(new ItemStack[input.stacks().size()]), 0);
	}

	public boolean recursiveMatch(ItemStack[] stacks, int ingredientIndex) {
		if (ingredientIndex >= this.inputs.size()) return true;
		Ingredient ingredient = this.inputs.get(ingredientIndex);
		for (int slot = 0; slot < stacks.length; slot++) {
			ItemStack taken = stacks[slot];
			if (taken != null && ingredient.test(taken)) {
				stacks[slot] = null;
				if (this.recursiveMatch(stacks, ingredientIndex + 1)) return true;
				stacks[slot] = taken;
			}
		}
		return false;
	}

	@Override
	public RecipeSerializer<? extends Recipe<ArcFurnaceRecipeInput>> getSerializer() {
		return BigTechRecipeSerializers.ARC_FURNACE;
	}

	@Override
	public RecipeType<? extends Recipe<ArcFurnaceRecipeInput>> getType() {
		return BigTechRecipeTypes.ARC_FURNACE;
	}

	@Override
	public IngredientPlacement getIngredientPlacement() {
		if (this.placement == null) {
			this.placement = IngredientPlacement.forShapeless(this.inputs);
		}
		return this.placement;
	}

	@Override
	public RecipeBookCategory getRecipeBookCategory() {
		return BigTechRecipeBookCategories.ARC_FURNACE;
	}

	@Override
	public boolean isIgnoredInRecipeBook() {
		return true;
	}

	public static class Serializer implements RecipeSerializer<ArcFurnaceRecipe> {

		@Override
		public MapCodec<ArcFurnaceRecipe> codec() {
			return CODEC;
		}

		@Override
		@Deprecated
		public PacketCodec<RegistryByteBuf, ArcFurnaceRecipe> packetCodec() {
			return PACKET_CODEC;
		}
	}
}