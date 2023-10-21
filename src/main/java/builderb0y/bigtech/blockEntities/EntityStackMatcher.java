package builderb0y.bigtech.blockEntities;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;

/**
used by sorter belts to determine which direction to send entities based on the belt's inventory.
the primary method in this interface, {@link #test(Entity, ItemStack)},
returns and int describing how good a match the current entity is for the current ItemStack.
for example, player entities match with player head items.
if the head item has a SkullOwner NBT tag (and that tag encodes a valid {@link GameProfile}),
then the player will be a better match for the item if, and only if, the player's profile
matches the item's profile. if there is a mismatch, then the player is no longer matched
by the item. if the item does NOT have a SkullOwner Nbt tag, or the tag does not encode
a valid GameProfile, then the player still matches the item, but it's a worse match.
what does this mean for {@link #test(Entity, ItemStack)}? it means that test() returns
a bigger number if the game profiles match, a smaller number if there is no profile on the item,
and 0 if the profiles do not match or the item is not a player head.

EntityStackMatcher's are registered via {@link SorterBeltBlockEntity#registerMatcher(EntityType, EntityStackMatcher)}.
*/
@FunctionalInterface
public interface EntityStackMatcher<E extends Entity> {

	public abstract int test(E entity, ItemStack stack);

	public static <E extends Entity> EntityStackMatcher<E> forItem(Item item) {
		return (E entity, ItemStack stack) -> stack.getItem() == item ? Integer.MAX_VALUE : 0;
	}

	public static <E extends Entity> EntityStackMatcher<E> forItemConvertible(ItemConvertible item) {
		return forItem(item.asItem());
	}
}