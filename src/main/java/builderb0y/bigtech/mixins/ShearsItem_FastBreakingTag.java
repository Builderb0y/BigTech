package builderb0y.bigtech.mixins;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.block.Block;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.item.ShearsItem;
import net.minecraft.registry.entry.RegistryEntryList;

import builderb0y.bigtech.blocks.BigTechBlockTags;

@Mixin(ShearsItem.class)
public class ShearsItem_FastBreakingTag {

	@ModifyArg(method = "createToolComponent", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/ToolComponent;<init>(Ljava/util/List;FI)V"), index = 0)
	private static List<ToolComponent.Rule> bigtech_modifyRules(List<ToolComponent.Rule> list) {
		for (ToolComponent.Rule rule : list) {
			if (rule.blocks() instanceof RegistryEntryList.Named<Block> named && named.getTag() == BigTechBlockTags.SHEARS_MINEABLE) {
				return list;
			}
		}
		list = new ArrayList<>(list);
		list.add(ToolComponent.Rule.of(BigTechBlockTags.SHEARS_MINEABLE, 5.0F));
		return list;
	}
}