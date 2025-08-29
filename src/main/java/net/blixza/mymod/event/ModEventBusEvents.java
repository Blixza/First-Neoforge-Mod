package net.blixza.mymod.event;

import net.blixza.mymod.MyMod;
import net.blixza.mymod.entity.ModEntities;
import net.blixza.mymod.entity.client.GeckoModel;
import net.blixza.mymod.entity.client.NeritantanModel;
import net.blixza.mymod.entity.client.TomahawkProjectileModel;
import net.blixza.mymod.entity.custom.GeckoEntity;
import net.blixza.mymod.entity.custom.NeritantanEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = MyMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GeckoModel.LAYER_LOCATION, GeckoModel::createBodyLayer);
        event.registerLayerDefinition(NeritantanModel.LAYER_LOCATION, NeritantanModel::createBodyLayer);
        event.registerLayerDefinition(TomahawkProjectileModel.LAYER_LOCATION, TomahawkProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GECKO.get(), GeckoEntity.createAttributes().build());
        event.put(ModEntities.NERITANTAN.get(), NeritantanEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.GECKO.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
