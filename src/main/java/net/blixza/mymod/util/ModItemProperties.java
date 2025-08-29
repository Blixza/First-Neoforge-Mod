package net.blixza.mymod.util;

import net.blixza.mymod.MyMod;
import net.blixza.mymod.component.ModDataComponents;
import net.blixza.mymod.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ModItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(ModItems.CHISEL.get(), ResourceLocation.fromNamespaceAndPath(MyMod.MOD_ID, "used"),
                (stack, level, entity, seed) -> stack.get(ModDataComponents.COORDINATES) != null ? 1f : 0f);

        makeCustomBow(ModItems.NANACHI_BOW.get());
    }

    private static void makeCustomBow(Item item) {
        ItemProperties.register(item, ResourceLocation.withDefaultNamespace("pull"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getUseItem() != itemStack ? 0.0F : (float)(itemStack.getUseDuration(livingEntity) - livingEntity.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(
                item,
                ResourceLocation.withDefaultNamespace("pulling"),
                (itemStack, clientLevel, livingEntity, p_174633_) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );
    }
}
