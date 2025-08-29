package net.blixza.mymod.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.blixza.mymod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public record FloralPathEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<FloralPathEnchantmentEffect> CODEC = MapCodec.unit(FloralPathEnchantmentEffect::new);

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        Player player = ((Player) entity);
        BlockPos blockPos = player.getOnPos().above();
        if (player.getInventory().getArmor(0).getItem() != Items.AIR) {
            ArmorItem boots = ((ArmorItem) player.getInventory().getArmor(0).getItem());
            if (boots == ModItems.BISMUTH_BOOTS.get()) {
                if (level.isLoaded(blockPos)) {
                    if (level.getBlockState(blockPos) == Blocks.AIR.defaultBlockState() && level.getBlockState(blockPos.below()) == Blocks.GRASS_BLOCK.defaultBlockState()) {
                        level.setBlockAndUpdate(blockPos, Blocks.POPPY.defaultBlockState());
                    }
                }
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
