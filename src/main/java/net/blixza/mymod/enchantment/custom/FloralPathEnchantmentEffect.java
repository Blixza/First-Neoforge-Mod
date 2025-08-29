package net.blixza.mymod.enchantment.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.blixza.mymod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.item.enchantment.effects.ReplaceDisk;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record FloralPathEnchantmentEffect(
    LevelBasedValue radius,
    LevelBasedValue height,
    Vec3i offset,
    Optional<BlockPredicate> predicate,
    BlockStateProvider blockState,
    Optional<Holder<GameEvent>> triggerGameEvent
) implements EnchantmentEntityEffect {
    public static final MapCodec<ReplaceDisk> CODEC = RecordCodecBuilder.mapCodec(
            group -> group.group(
                            LevelBasedValue.CODEC.fieldOf("radius").forGetter(ReplaceDisk::radius),
                            LevelBasedValue.CODEC.fieldOf("height").forGetter(ReplaceDisk::height),
                            Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(ReplaceDisk::offset),
                            BlockPredicate.CODEC.optionalFieldOf("predicate").forGetter(ReplaceDisk::predicate),
                            BlockStateProvider.CODEC.fieldOf("block_state").forGetter(ReplaceDisk::blockState),
                            GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(ReplaceDisk::triggerGameEvent)
                    )
                    .apply(group, ReplaceDisk::new)
    );

    @Override
    public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 origin) {
        BlockPos blockpos = BlockPos.containing(origin).offset(this.offset);
        RandomSource randomsource = entity.getRandom();
        int i = (int)this.radius.calculate(enchantmentLevel);
        int j = (int)this.height.calculate(enchantmentLevel);

        for (BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-i, 0, -i), blockpos.offset(i, Math.min(j - 1, 0), i))) {
            if (blockpos1.distToCenterSqr(origin.x(), (double)blockpos1.getY() + 0.5, origin.z()) < (double) Mth.square(i)
                    && this.predicate.map(blockPredicate -> blockPredicate.test(level, blockpos1)).orElse(true)
                    && level.setBlockAndUpdate(blockpos1, this.blockState.getState(randomsource, blockpos1))) {
                this.triggerGameEvent.ifPresent(aVoid -> level.gameEvent(entity, (Holder<GameEvent>)aVoid, blockpos1));
            }
        }
    }

    @Override
    public MapCodec<ReplaceDisk> codec() {
        return CODEC;
    }
}
