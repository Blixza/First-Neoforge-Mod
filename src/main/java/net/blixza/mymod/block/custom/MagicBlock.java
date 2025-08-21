package net.blixza.mymod.block.custom;

import net.blixza.mymod.MyMod;
import net.blixza.mymod.item.ModItems;
import net.blixza.mymod.sound.ModSounds;
import net.blixza.mymod.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class MagicBlock extends Block {
    public MagicBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return InteractionResult.SUCCESS;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!level.isClientSide()) {
            ServerLevel serverLevel = ((ServerLevel) level);
            if (entity instanceof ItemEntity itemEntity) {
                Item item = itemEntity.getItem().getItem();
                if (isValidItem(itemEntity.getItem())) {
                    itemEntity.setItem(new ItemStack(ModItems.RED_EYE.get(), itemEntity.getItem().getCount()));
                }

                if (isValidItem(itemEntity.getItem())) {
                    itemEntity.setItem(new ItemStack(Items.WATER_BUCKET, itemEntity.getItem().getCount()));
                }

                if (item == Items.SWEET_BERRIES) {
                    itemEntity.kill();
                    EntityType.FOX.spawn(
                            serverLevel,
                            null,
                            null,
                            pos.above(),
                            MobSpawnType.MOB_SUMMONED,
                            false,
                            false
                    );
                }
            }
            super.stepOn(level, pos, state, entity);
        }
    }

    private boolean isValidItem(ItemStack item) {
        return item.is(ModTags.Items.TRANSFORMABLE_ITEMS);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.mymod.magic_block.tooltip"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
