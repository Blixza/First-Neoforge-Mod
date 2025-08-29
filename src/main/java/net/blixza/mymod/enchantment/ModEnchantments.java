package net.blixza.mymod.enchantment;

import net.blixza.mymod.MyMod;
import net.blixza.mymod.enchantment.custom.FloralPathEnchantmentEffect;
import net.blixza.mymod.enchantment.custom.LightningStrikerEnchantmentEffect;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.DamageImmunity;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;

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
                        1,
                        Enchantment.dynamicCost(10, 10),
                        Enchantment.dynamicCost(25, 10),
                        2,
                        EquipmentSlotGroup.FEET
                ))
                .exclusiveWith(holdergetter1.getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE))
                .withEffect(EnchantmentEffectComponents.LOCATION_CHANGED, new FloralPathEnchantmentEffect())
        );
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key,
                                 Enchantment.Builder builer) {
        registry.register(key, builer.build(key.location()));
    }
}
