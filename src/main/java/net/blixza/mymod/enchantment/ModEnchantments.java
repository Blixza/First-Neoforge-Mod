package net.blixza.mymod.enchantment;

import net.blixza.mymod.MyMod;
import net.blixza.mymod.enchantment.custom.FloralPathEnchantmentEffect;
import net.blixza.mymod.enchantment.custom.LightningStrikerEnchantmentEffect;
import net.blixza.mymod.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.ReplaceDisk;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> LIGHTNING_STRIKER = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MyMod.MOD_ID, "lightning_striker"));

    public static final ResourceKey<Enchantment> FLORAL_PATH = ResourceKey.create(Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MyMod.MOD_ID, "floral_path"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup(Registries.ITEM);
        HolderGetter<Enchantment> holdergetter1 = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> holdergetter2 = context.lookup(Registries.ITEM);

        register(context, LIGHTNING_STRIKER, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                5,
                2,
                Enchantment.dynamicCost(5, 7),
                Enchantment.dynamicCost(25, 7),
                2,
                EquipmentSlotGroup.MAINHAND))
                .exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                .withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM, new LightningStrikerEnchantmentEffect()));

        register(context, FLORAL_PATH, Enchantment.enchantment(
                Enchantment.definition(
                        holdergetter2.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                        2,
                        3,
                        Enchantment.dynamicCost(10, 10),
                        Enchantment.dynamicCost(25, 10),
                        2,
                        EquipmentSlotGroup.FEET
                ))
                .exclusiveWith(holdergetter1.getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE))
                .withEffect(EnchantmentEffectComponents.LOCATION_CHANGED,
                        new FloralPathEnchantmentEffect(
                            new LevelBasedValue.Clamped(LevelBasedValue.perLevel(1.0F, 1.0F), 0.0F, 5.0F),
                            LevelBasedValue.constant(1.0F),
                            new Vec3i(0, 0, 0),
                            Optional.of(
                                    BlockPredicate.allOf(
                                            BlockPredicate.matchesBlocks(new Vec3i(0, -1, 0), Blocks.GRASS_BLOCK),
                                            BlockPredicate.matchesBlocks(Blocks.AIR)
                                    )
                            ),
                            new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder().add(Blocks.POPPY.defaultBlockState()).add(Blocks.ALLIUM.defaultBlockState()).build()),
                            Optional.of(GameEvent.BLOCK_PLACE)
                ))
        );
    }
    private static Block randomFlower() {
        Random random = new Random();
        Set<Holder<Block>> blocksInTag = BuiltInRegistries.BLOCK.getOrCreateTag(BlockTags.FLOWERS).stream().collect(Collectors.toSet());

        int i = 0;
        for (Holder<Block> block : blocksInTag) {
            if (random.nextInt(blocksInTag.size()) == i) {
                return block.value();
            }
            i++;
        }

        return Blocks.POPPY;
    }


    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key,
                                 Enchantment.Builder builer) {
        registry.register(key, builer.build(key.location()));
    }
}
