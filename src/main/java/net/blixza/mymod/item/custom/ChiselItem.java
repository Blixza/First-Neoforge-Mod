package net.blixza.mymod.item.custom;

import net.blixza.mymod.component.ModDataComponents;
import net.blixza.mymod.sound.ModSounds;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Map;

public class ChiselItem extends Item {
    private static final Map<Block, Block> CHISEL_MAP =
            Map.of(
                    Blocks.LAVA_CAULDRON, Blocks.WATER_CAULDRON,
                    Blocks.OBSIDIAN, Blocks.GOLD_BLOCK,
                    Blocks.PURPUR_BLOCK, Blocks.SPONGE,
                    Blocks.WATER_CAULDRON, Blocks.LAVA_CAULDRON,
                    Blocks.GOLD_BLOCK, Blocks.OBSIDIAN,
                    Blocks.SPONGE, Blocks.PURPUR_BLOCK
            );

    public ChiselItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();

        if (CHISEL_MAP.containsKey(clickedBlock)) {
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(context.getClickedPos(), CHISEL_MAP.get(clickedBlock).defaultBlockState());

                context.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), context.getPlayer(),
                        item -> context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));

                level.playSound(null, context.getClickedPos(), ModSounds.CHISEL_USE.get(), SoundSource.BLOCKS);

                ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION,
                        context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 1.0, context.getClickedPos().getZ(),
                        5, 0, 0, 0, 1);

                context.getItemInHand().set(ModDataComponents.COORDINATES, context.getClickedPos());
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.mymod.chisel.shift_down"));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.mymod.chisel"));
        }

        if (stack.get(ModDataComponents.COORDINATES) != null) {
            tooltipComponents.add(Component.translatable("tooltip.mymod.chisel.last_changed_block" + stack.get(ModDataComponents.COORDINATES)));
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
