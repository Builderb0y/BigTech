package builderb0y.bigtech.recipes;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.collection.Weight;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.collection.Weighting;
import net.minecraft.world.World;

import builderb0y.autocodec.annotations.*;
import builderb0y.autocodec.coders.AutoCoder;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.items.FunctionalItems;

public class TransmuteRecipe implements Recipe<TransmuteRecipeInventory> {

	public static final AutoCoder<ItemStack> ITEM_STACK_CODER = BigTechAutoCodec.AUTO_CODEC.wrapDFUCodec(RecipeCodecs.CRAFTING_RESULT, false);
	public static final Codec<TransmuteRecipe> CODEC = BigTechAutoCodec.AUTO_CODEC.createDFUCodec(TransmuteRecipe.class);

	public final Ingredient input;
	public final @SingletonArray @VerifySizeRange(min = 1) List<Output> output;
	public final transient int outputWeight;
	public final @VerifyIntRange(min = 0) int energy;

	public TransmuteRecipe(Ingredient input, List<Output> output, int energy) {
		this.input  = input;
		this.output = output;
		this.energy = energy;
		this.outputWeight = Weighting.getWeightSum(output);
	}

	@Override
	public boolean matches(TransmuteRecipeInventory inventory, World world) {
		return this.input.test(inventory.getStack());
	}

	@Override
	public ItemStack craft(TransmuteRecipeInventory inventory, DynamicRegistryManager registryManager) {
		double scaledEnergy = ((double)(inventory.slotEnergy)) / ((double)(this.energy));
		if (inventory.random.nextDouble() >= 1.0D / (scaledEnergy * scaledEnergy + 1.0D)) {
			return Weighting.getAt(this.output, inventory.random.nextInt(this.outputWeight)).orElseThrow().toStack();
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResult(DynamicRegistryManager registryManager) {
		return this.output.get(0).toStack();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BigTechRecipeSerializers.TRANSMUTE;
	}

	@Override
	public RecipeType<?> getType() {
		return BigTechRecipeTypes.TRANSMUTE;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return DefaultedList.ofSize(1, this.input);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(FunctionalItems.TRANSMUTER);
	}

	@Override
	public boolean isEmpty() {
		return this.input.isEmpty;
	}

	public static class Output implements Weighted {

		public final Item item;
		public final @VerifyNullable NbtCompound nbt;
		public final @DefaultInt(1) @VerifyIntRange(min = 1) int weight;
		public final transient Weight weightObject;

		public Output(Item item, @VerifyNullable NbtCompound nbt, int weight) {
			this.item = item;
			this.weight = weight;
			this.nbt = nbt;
			this.weightObject = Weight.of(weight);
		}

		@Override
		public Weight getWeight() {
			return this.weightObject;
		}

		public ItemStack toStack() {
			ItemStack stack = new ItemStack(this.item, 1);
			stack.nbt = this.nbt;
			return stack;
		}
	}

	public static class Serializer implements RecipeSerializer<TransmuteRecipe> {

		@Override
		public Codec<TransmuteRecipe> codec() {
			return CODEC;
		}

		@Override
		public TransmuteRecipe read(PacketByteBuf buffer) {
			Ingredient input = Ingredient.fromPacket(buffer);
			int outputCount = buffer.readVarInt();
			List<Output> output = new ArrayList<>(outputCount);
			for (int index = 0; index < outputCount; index++) {
				Item item = Registries.ITEM.get(buffer.readInt());
				NbtCompound nbt = buffer.readNbt();
				int weight = buffer.readVarInt();
				output.add(new Output(item, nbt, weight));
			}
			int energy = buffer.readVarInt();
			return new TransmuteRecipe(input, output, energy);
		}

		@Override
		public void write(PacketByteBuf buffer, TransmuteRecipe recipe) {
			recipe.input.write(buffer);
			int size = recipe.output.size();
			buffer.writeVarInt(size);
			for (int index = 0; index < size; index++) {
				Output output = recipe.output.get(index);
				buffer.writeInt(Registries.ITEM.getRawId(output.item));
				buffer.writeNbt(output.nbt);
				buffer.writeVarInt(output.weight);
			}
			buffer.writeVarInt(recipe.energy);
		}
	}
}