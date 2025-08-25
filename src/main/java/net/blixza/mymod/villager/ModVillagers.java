package net.blixza.mymod.villager;

import com.google.common.collect.ImmutableSet;
import net.blixza.mymod.MyMod;
import net.blixza.mymod.block.ModBlocks;
import net.blixza.mymod.sound.ModSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(BuiltInRegistries.POINT_OF_INTEREST_TYPE, MyMod.MOD_ID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(BuiltInRegistries.VILLAGER_PROFESSION, MyMod.MOD_ID);

    public static final Holder<PoiType> NANACHI_POI = POI_TYPES.register("nanachi_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.CHAIR.get().getStateDefinition().getPossibleStates()), 1, 1));

    public static final Holder<VillagerProfession> NANACHIGER = VILLAGER_PROFESSIONS.register("nanachiger",
            () -> new VillagerProfession("nanachiger", holder -> holder.value() == NANACHI_POI.value(),
                    poiTypeHolder -> poiTypeHolder.value() == NANACHI_POI.value(), ImmutableSet.of(), ImmutableSet.of(),
                    ModSounds.NANACHI_SOUND.get()));

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
