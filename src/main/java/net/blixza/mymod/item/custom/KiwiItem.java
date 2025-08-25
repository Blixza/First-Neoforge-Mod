package net.blixza.mymod.item.custom;

import net.blixza.mymod.particle.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class KiwiItem extends Item {
    public KiwiItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos blockPos = context.getClickedPos();

        if (player.isShiftKeyDown()) {
            level.addParticle(ParticleTypes.EXPLOSION,
                    blockPos.getX(), blockPos.getY() + 2.0, blockPos.getZ(),
                    0.0, 0.0, 0.0);
        } else {
            level.addParticle(ModParticles.BISMUTH_PARTICLES.get(),
                    blockPos.getX(), blockPos.getY() + 2.0, blockPos.getZ(),
                    0.0, 0.0, 0.0);
        }


        return InteractionResult.SUCCESS;
    }
}
