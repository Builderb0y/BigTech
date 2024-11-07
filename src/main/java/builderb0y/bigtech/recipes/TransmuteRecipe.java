package builderb0y.bigtech.recipes;

import java.util.List;

import com.mojang.serialization.MapCodec;

import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.*;
import net.minecraft.world.World;

import builderb0y.autocodec.annotations.*;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.networking.PacketCodecs2;

public class TransmuteRecipe implements Recipe<TransmuteRecipeInventory> {

	public static final MapCodec<TransmuteRecipe> CODEC = BigTechAutoCodec.callerMapCodec();
	public static final PacketCodec<RegistryByteBuf, TransmuteRecipe> PACKET_CODEC = PacketCodec.tuple(
		Ingredient.PACKET_CODEC,                 (TransmuteRecipe recipe) -> recipe.input,
		PacketCodecs2.list(Output.PACKET_CODEC), (TransmuteRecipe recipe) -> recipe.output,
		PacketCodecs.VAR_INT,                    (TransmuteRecipe recipe) -> recipe.energy,
		TransmuteRecipe::new
	);

	public final Ingredient input;
	public final @SingletonArray @VerifySizeRange(min = 1) List<Output> output;
	public final transient int outputWeight;
	public final @VerifyIntRange(min = 0) int energy;

	public IngredientPlacement ingredientPlacement;

	public TransmuteRecipe(Ingredient input, List<Output> output, int energy) {
		this.input  = input;
		this.output = output;
		this.energy = energy;
		this.outputWeight = Weighting.getWeightSum(output);
	}

	@Override
	public boolean matches(TransmuteRecipeInventory inventory, World world) {
		return this.input.test(inventory.stack);
	}

	@Override
	public ItemStack craft(TransmuteRecipeInventory inventory, WrapperLookup lookup) {
		double scaledEnergy = ((double)(inventory.slotEnergy)) / ((double)(this.energy));
		if (inventory.random.nextDouble() >= 1.0D / (scaledEnergy * scaledEnergy + 1.0D)) {
			return Weighting.getAt(this.output, inventory.random.nextInt(this.outputWeight)).orElseThrow().toStack();
		}
		return ItemStack.EMPTY;
	}

	@Override
	public IngredientPlacement getIngredientPlacement() {
		return this.ingredientPlacement == null ? this.ingredientPlacement = IngredientPlacement.forSingleSlot(this.input) : this.ingredientPlacement;
	}

	@Override
	public RecipeBookCategory getRecipeBookCategory() {
		return BigTechRecipeBookCategories.TRANSMUTING;
	}

	@Override
	public RecipeSerializer<? extends Recipe<TransmuteRecipeInventory>> getSerializer() {
		return BigTechRecipeSerializers.TRANSMUTE;
	}

	@Override
	public RecipeType<? extends Recipe<TransmuteRecipeInventory>> getType() {
		return BigTechRecipeTypes.TRANSMUTE;
	}

	public static class Output implements Weighted {

		public static final PacketCodec<RegistryByteBuf, Output> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.registryEntry(RegistryKeys.ITEM),          (Output output) -> output.item,
			PacketCodecs2.nullable(ComponentChanges.PACKET_CODEC),  (Output output) -> output.components,
			PacketCodecs2.nullableDefault(PacketCodecs.VAR_INT, 1), (Output output) -> output.weight,
			Output::new
		);

		public final RegistryEntry<Item> item;
		public final @VerifyNullable ComponentChanges components;
		public final @DefaultInt(1) @VerifyIntRange(min = 1) int weight;
		public final transient Weight weightObject;

		public Output(RegistryEntry<Item> item, @VerifyNullable ComponentChanges components, int weight) {
			this.item = item;
			this.weight = weight;
			this.components = components;
			this.weightObject = Weight.of(weight);
		}

		@Override
		public Weight getWeight() {
			return this.weightObject;
		}

		public ItemStack toStack() {
			if (this.components != null) {
				return new ItemStack(this.item, 1, this.components);
			}
			else {
				return new ItemStack(this.item, 1);
			}
		}
	}

	public static class Serializer implements RecipeSerializer<TransmuteRecipe> {

		@Override
		public MapCodec<TransmuteRecipe> codec() {
			return CODEC;
		}

		@Override
		public PacketCodec<RegistryByteBuf, TransmuteRecipe> packetCodec() {
			return PACKET_CODEC;
		}
	}
}