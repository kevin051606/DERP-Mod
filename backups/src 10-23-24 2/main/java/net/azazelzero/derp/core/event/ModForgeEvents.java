package net.azazelzero.derp.core.event;

import net.azazelzero.derp.Main;
import net.azazelzero.derp.core.commands.DerpSelectCommand;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.reader.DatapackDerpsLoader;
import net.azazelzero.derp.core.data.DerpPlayerData;
import net.azazelzero.derp.core.data.DerpPlayerDataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = net.azazelzero.derp.Main.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModForgeEvents {

    public final static Map<String, Map<String, DERP>> derpsLoaded = new HashMap<>();
    public final static List<String> layers = new ArrayList<>();

//    public final static Map<String, SkillTreeObject> scriptsLoaded = new HashMap<>();

    private ModForgeEvents(){}

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event){
        System.out.println("info +: ");
//        event.addListener(new DatapackScriptsLoader(event));
        event.addListener(new DatapackDerpsLoader(event));
    }
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Player)
        {
            if(!event.getObject().getCapability(DerpPlayerDataProvider.DERP_DATA).isPresent()) {
                event.addCapability(new ResourceLocation(Main.MODID, "properties"), new DerpPlayerDataProvider());
            }

        }
    }
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(DerpPlayerDataProvider.DERP_DATA).ifPresent(oldStore -> {
                event.getOriginal().getCapability(DerpPlayerDataProvider.DERP_DATA).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(DerpPlayerData.class);
    }


//    @SubscribeEvent
//    public static  void worldTickEvent(TickEvent.WorldTickEvent event){
//
//    }

//    public static Supplier<IForgeRegistry<RequirementRegistryEntry>> RequirementRegistry;
//    @SubscribeEvent
//    public static void newRegistryEvent(NewRegistryEvent event){
//        RegistryBuilder<RequirementRegistryEntry> unfinishedRegistry = new RegistryBuilder<RequirementRegistryEntry>();
////        unfinishedRegistry.add(new DatRequirement());
//         RequirementRegistry = event.create(unfinishedRegistry);
//
////        private static final DeferredRegister<RequirementRegistryEntry> ffff = DeferredRegister.create(ForgeRegistries.BLOCKS);
////        RequirementRegistry.
//    }
    @SubscribeEvent
    public static void registerCommandsEvent(RegisterCommandsEvent event){
        new DerpSelectCommand(event.getDispatcher());
    }

}
