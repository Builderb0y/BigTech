package builderb0y.bigtech.mixins;

import java.util.Optional;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import builderb0y.bigtech.api.ProficiencyManager;

@Mixin(Item.class)
public class Item_RandomizeComponents {

	@Unique
	private static final Set<RegistryEntry<EntityAttribute>> BIGTECH_RANDOMIZEABLE_ATTRIBUTES = Set.of(
		EntityAttributes.ATTACK_DAMAGE,
		EntityAttributes.ATTACK_SPEED,
		EntityAttributes.ATTACK_KNOCKBACK,
		EntityAttributes.ARMOR,
		EntityAttributes.ARMOR_TOUGHNESS,
		EntityAttributes.KNOCKBACK_RESISTANCE,
		EntityAttributes.EXPLOSION_KNOCKBACK_RESISTANCE
	);

	@Inject(method = "onCraftByPlayer", at = @At("HEAD"))
	private void bigtech_randomizeComponents(ItemStack stack, PlayerEntity player, CallbackInfo callback) {
		if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
		double base = ProficiencyManager.getProficiency(serverPlayer, stack.getItem(), true) + 1.0D;
		AttributeModifiersComponent attributes = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
		if (attributes != null) {
			stack.set(
				DataComponentTypes.ATTRIBUTE_MODIFIERS,
				new AttributeModifiersComponent(
					attributes
					.modifiers()
					.stream()
					.map((AttributeModifiersComponent.Entry entry) -> {
						if (BIGTECH_RANDOMIZEABLE_ATTRIBUTES.contains(entry.attribute()) && entry.modifier().value() > 0.0D) {
							return new AttributeModifiersComponent.Entry(
								entry.attribute(),
								new EntityAttributeModifier(
									entry.modifier().id(),
									entry.modifier().value() * serverPlayer.getRandom().nextTriangular(base, 1.0D),
									entry.modifier().operation()
								),
								entry.slot(),
								entry.display()
							);
						}
						else {
							return entry;
						}
					})
					.toList()
				)
			);
		}
		ToolComponent tool = stack.get(DataComponentTypes.TOOL);
		if (tool != null) {
			double speedMultiplier = serverPlayer.getRandom().nextTriangular(base, 1.0D);
			stack.set(
				DataComponentTypes.TOOL,
				new ToolComponent(
					tool
					.rules()
					.stream()
					.map((ToolComponent.Rule rule) -> {
						//minecraft uses Float.MAX_VALUE for insta-breaking,
						//which is prone to overflowing to infinity.
						//but infinity crashes in other parts of code,
						//so we can't let it overflow here.
						if (rule.speed().isPresent() && rule.speed().get().floatValue() < Float.MAX_VALUE * 0.5F) {
							return new ToolComponent.Rule(
								rule.blocks(),
								Optional.of((float)(rule.speed().get() * speedMultiplier)),
								rule.correctForDrops()
							);
						}
						else {
							return rule;
						}
					})
					.toList(),
					(float)(tool.defaultMiningSpeed() * speedMultiplier),
					tool.damagePerBlock(),
					tool.canDestroyBlocksInCreative()
				)
			);
		}
		Integer maxDamage = stack.get(DataComponentTypes.MAX_DAMAGE);
		if (maxDamage != null) {
			stack.set(
				DataComponentTypes.MAX_DAMAGE,
				(int)(maxDamage.doubleValue() * serverPlayer.getRandom().nextTriangular(base, 1.0D))
			);
		}
		ProficiencyManager.onCrafted(serverPlayer, stack.getItem());
	}
}