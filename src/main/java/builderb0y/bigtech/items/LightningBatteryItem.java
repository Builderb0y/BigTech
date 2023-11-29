package builderb0y.bigtech.items;

import java.util.stream.Stream;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import builderb0y.bigtech.api.LightningStorageItem;
import builderb0y.bigtech.damageTypes.BigTechDamageTypes;
import builderb0y.bigtech.lightning.LightningPulse;

public class LightningBatteryItem extends Item implements InventoryVariants, LightningStorageItem {

	public LightningBatteryItem(Settings settings) {
		super(settings);
	}

	@Override
	public int getCharge(ItemStack stack) {
		return stack.maxDamage - stack.damage;
	}

	@Override
	public int getMaxCharge(ItemStack stack) {
		return stack.maxDamage;
	}

	@Override
	public void setCharge(ItemStack stack, int charge) {
		stack.setDamage(this.maxDamage - charge);
	}

	@Override
	public int getDefaultSpreadEvents(ItemStack stack) {
		return 8;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		return this.defaultUseOnBlock(context);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		int charge = this.getCharge(stack);
		if (charge > 0) {
			LightningPulse.shockEntity(
				entity,
				charge / 200.0F,
				new DamageSource(
					entity
					.world
					.registryManager
					.get(RegistryKeys.DAMAGE_TYPE)
					.entryOf(BigTechDamageTypes.SHOCKING)
				)
			);
			return ActionResult.SUCCESS;
		}
		else {
			return ActionResult.PASS;
		}
	}

	@Override
	public boolean isItemBarVisible(ItemStack stack) {
		return this.getCharge(stack) > 0;
	}

	@Override
	public int getItemBarColor(ItemStack stack) {
		double brightness = ((double)(this.getCharge(stack))) / ((double)(this.getMaxCharge(stack)));
		int red   = (int)(brightness * 255.0D);
		int green = (int)(brightness * 239.0D);
		int blue  = (int)(brightness * 223.0D);
		return 0xFF000000 | (red << 16) | (green << 8) | blue;
	}

	@Override
	public int getItemBarStep(ItemStack stack) {
		return super.getItemBarStep(stack);
	}

	@Override
	public Stream<ItemStack> getInventoryStacks() {
		ItemStack empty = this.defaultStack;
		empty.setDamage(empty.maxDamage);
		ItemStack charged = this.defaultStack;
		return Stream.of(empty, charged);
	}
}