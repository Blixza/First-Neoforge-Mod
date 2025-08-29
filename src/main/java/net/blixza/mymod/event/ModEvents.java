package net.blixza.mymod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.blixza.mymod.MyMod;
import net.blixza.mymod.item.ModItems;
import net.blixza.mymod.item.custom.HammerItem;
import net.blixza.mymod.potion.ModPotions;
import net.blixza.mymod.villager.ModVillagers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = MyMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();

    // Done with the help of https://github.com/CoFH/CoFHCore/blob/1.19.x/src/main/java/cofh/core/event/AreaEffectEvents.java
    // Don't be a jerk License
    @SubscribeEvent
    public static void onHammerUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getMainHandItem();

        if(mainHandItem.getItem() instanceof HammerItem hammer && player instanceof ServerPlayer serverPlayer) {
            BlockPos initialBlockPos = event.getPos();
            if(HARVESTED_BLOCKS.contains(initialBlockPos)) {
                return;
            }

            for(BlockPos pos : HammerItem.getBlocksToBeDestroyed(1, initialBlockPos, serverPlayer)) {
                if(pos.equals(initialBlockPos) || !hammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) {
                    continue;
                }

                HARVESTED_BLOCKS.add(pos);
                serverPlayer.gameMode.destroyBlock(pos);
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }

    @SubscribeEvent
    public static void livingDamage(LivingDamageEvent.Pre event) {
        LivingEntity target = event.getEntity();
        if (target instanceof Fox fox && event.getSource().getDirectEntity() instanceof Player player) {
            player.kill();
            fox.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 5));
        }

        if (!(target instanceof Fox) && event.getSource().getDirectEntity() instanceof Player player) {
            if (player.getMainHandItem().getItem() == ModItems.RED_EYE.get()) {
                target.kill();
            }
        }
    }

    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.SLIMEY_POTION);
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == ModVillagers.NANACHIGER.value()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add((trader, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3),
                    new ItemStack(ModItems.CHERY_TIGGO_MUSIC_DISC.get(), 1), 16, 3, 0.05f
            ));

            trades.get(1).add((trader, random) -> new MerchantOffer(
                    new ItemCost(ModItems.RED_EYE.get(), 3),
                    new ItemStack(Items.FOX_SPAWN_EGG, 1), 1, 3, 0.05f
            ));
        }
    }

    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        genericTrades.add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 64),
                new ItemStack(ModItems.I_DONT_CARE_MUSIC_DISC.get(), 1), 1, 10, 0.2f
        ));

        rareTrades.add((trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 48),
                new ItemStack(Items.END_CRYSTAL, 1), 1, 10, 0.2f
        ));
    }

    @SubscribeEvent
    public static void entityTeleport(EntityTeleportEvent.EnderPearl event) {
        Entity entity = event.getEntity();

        entity.kill();
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        Level level = player.level();
        BlockPos blockPos = player.getOnPos().above();
        if (player.getInventory().getArmor(0).getItem() != Items.AIR) {
            ArmorItem boots = ((ArmorItem) player.getInventory().getArmor(0).getItem());
            if (!level.isClientSide()) {
                if (boots == ModItems.BISMUTH_BOOTS.get()) {
                    if (level.isLoaded(blockPos)) {
                        if (level.getBlockState(blockPos) == Blocks.AIR.defaultBlockState() && level.getBlockState(blockPos.below()) == Blocks.GRASS_BLOCK.defaultBlockState()) {
                            level.setBlockAndUpdate(blockPos, Blocks.POPPY.defaultBlockState());
                        }
                    }
                }
            }
        }
    }
}
