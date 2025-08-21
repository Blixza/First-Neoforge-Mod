package net.blixza.mymod.trim;

import net.blixza.mymod.MyMod;
import net.blixza.mymod.item.ModItems;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModTrimPatterns {
    public static final ResourceKey<TrimPattern> NANACHI = ResourceKey.create(Registries.TRIM_PATTERN,
            ResourceLocation.fromNamespaceAndPath(MyMod.MOD_ID, "nanachi"));

    public static void bootstrap(BootstrapContext<TrimPattern> context) {
        register(context, ModItems.NANACHI_SMITHING_TEMPLATE, NANACHI);
    }

    private static void register(BootstrapContext<TrimPattern> context, DeferredItem<Item> item,ResourceKey<TrimPattern> key) {
        TrimPattern trimPattern = new TrimPattern(key.location(), item.getDelegate(),
                Component.translatable(Util.makeDescriptionId("trim_pattern", key.location())), false);
        context.register(key, trimPattern);
    }
}
