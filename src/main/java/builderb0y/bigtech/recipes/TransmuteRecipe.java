package builderb0y.bigtech.recipes;

import com.mojang.serialization.Codec;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import builderb0y.autocodec.annotations.MemberUsage;
import builderb0y.autocodec.annotations.UseCoder;
import builderb0y.autocodec.annotations.VerifyIntRange;
import builderb0y.autocodec.coders.AutoCoder;
import builderb0y.bigtech.codecs.BigTechAutoCodec;
import builderb0y.bigtech.items.BigTechItems;

public class TransmuteRecipe implements Recipe<TransmuteRecipeInventory> {

	public static final AutoCoder<ItemStack> ITEM_STACK_CODER = BigTechAutoCodec.AUTO_CODEC.wrapDFUCodec(RecipeCodecs.CRAFTING_RESULT, false);
	public static final Codec<TransmuteRecipe> CODEC = BigTechAutoCodec.AUTO_CODEC.createDFUCodec(TransmuteRecipe.class);

	public final Ingredient input;
	public final @UseCoder(name = "ITEM_STACK_CODER", in = TransmuteRecipe.class, usage = MemberUsage.FIELD_CONTAINS_HANDLER) ItemStack output;
	public final @VerifyIntRange(min = 0) int energy;

	public TransmuteRecipe(Ingredient input, ItemStack output, int energy) {
		this.input  = input;
		this.output = output.copyWithCount(1);
		this.energy = energy;
	}

	@Override
	public boolean matches(TransmuteRecipeInventory inventory, World world) {
		return this.input.test(inventory.getStack());
	}

	@Override
	public ItemStack craft(TransmuteRecipeInventory inventory, DynamicRegistryManager registryManager) {
		double scaledEnergy = ((double)(inventory.slotEnergy)) / ((double)(this.energy));
		return inventory.random.nextDouble() >= 1.0D / (scaledEnergy * scaledEnergy + 1.0D) ? this.output.copy() : ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResult(DynamicRegistryManager registryManager) {
		return this.output;
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
		return new ItemStack(BigTechItems.TRANSMUTER);
	}

	@Override
	public boolean isEmpty() {
		return this.input.isEmpty;
	}

	public static class Serializer implements RecipeSerializer<TransmuteRecipe> {

		@Override
		public Codec<TransmuteRecipe> codec() {
			return CODEC;
		}

		@Override
		public TransmuteRecipe read(PacketByteBuf buffer) {
			return new TransmuteRecipe(Ingredient.fromPacket(buffer), buffer.readItemStack(), buffer.readVarInt());
		}

		@Override
		public void write(PacketByteBuf buffer, TransmuteRecipe recipe) {
			recipe.input.write(buffer);
			buffer.writeItemStack(recipe.output);
			buffer.writeVarInt(recipe.energy);
		}
	}
}