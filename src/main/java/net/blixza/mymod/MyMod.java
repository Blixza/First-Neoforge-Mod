package net.blixza.mymod;

import net.blixza.mymod.block.ModBlocks;
import net.blixza.mymod.block.entity.ModBlockEntities;
import net.blixza.mymod.block.entity.renderer.PedestalBlockEntityRenderer;
import net.blixza.mymod.component.ModDataComponents;
import net.blixza.mymod.effect.ModEffects;
import net.blixza.mymod.enchantment.ModEnchantmentEffects;
import net.blixza.mymod.entity.ModEntities;
import net.blixza.mymod.entity.client.ChairRenderer;
import net.blixza.mymod.entity.client.GeckoRenderer;
import net.blixza.mymod.entity.client.NeritantanRenderer;
import net.blixza.mymod.entity.client.TomahawkProjectileRenderer;
import net.blixza.mymod.item.ModCreativeModeTabs;
import net.blixza.mymod.item.ModItems;
import net.blixza.mymod.loot.ModLootModifiers;
import net.blixza.mymod.particle.BismuthParticles;
import net.blixza.mymod.particle.ModParticles;
import net.blixza.mymod.potion.ModPotions;
import net.blixza.mymod.recipe.ModRecipes;
import net.blixza.mymod.screen.ModMenuTypes;
import net.blixza.mymod.screen.custom.GrowthChamberScreen;
import net.blixza.mymod.screen.custom.PedestalScreen;
import net.blixza.mymod.sound.ModSounds;
import net.blixza.mymod.util.ModItemProperties;
import net.blixza.mymod.villager.ModVillagers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MyMod.MOD_ID)
public class MyMod {
    public static final String MOD_ID = "mymod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MyMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);
        ModEnchantmentEffects.register(modEventBus);
        ModEntities.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModParticles.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MyMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    static class ClientModEvents {
        @SubscribeEvent
        static void onClientSetup(FMLClientSetupEvent event) {
            ModItemProperties.addCustomItemProperties();

            EntityRenderers.register(ModEntities.GECKO.get(), GeckoRenderer::new);
            EntityRenderers.register(ModEntities.NERITANTAN.get(), NeritantanRenderer::new);
            EntityRenderers.register(ModEntities.TOMAHAWK.get(), TomahawkProjectileRenderer::new);
            EntityRenderers.register(ModEntities.CHAIR_ENTITY.get(), ChairRenderer::new);
        }

        @SubscribeEvent
        public static void registerParticleFactoreis(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.BISMUTH_PARTICLES.get(), BismuthParticles.Provider::new);
        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.PEDESTAL_BE.get(), PedestalBlockEntityRenderer::new);
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.PEDESTAL_MENU.get(), PedestalScreen::new);
            event.register(ModMenuTypes.GROWTH_CHAMBER_MENU.get(), GrowthChamberScreen::new);
        }
    }
}
