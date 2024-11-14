package net.azazelzero.derp.core.event;

import com.google.gson.JsonObject;
import net.azazelzero.derp.DerpEventHandler;
import net.azazelzero.derp.core.commands.DerpSelectCommand;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.core.derp.skillactions.SkillAction;
import net.azazelzero.derp.reader.DatapackDerpsLoader;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = net.azazelzero.derp.Main.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModForgeEvents {

    public final static Map<String, Map<String, DERP>> derpsLoaded = new HashMap<>();
    public final static List<DERP> derpsLoadedList = new ArrayList<>();
    public final static List<String> layers = new ArrayList<>();

//    public final static Map<String, SkillTreeObject> scriptsLoaded = new HashMap<>();

    private ModForgeEvents(){}

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event){
        System.out.println("info +: ");
//        event.addListener(new DatapackScriptsLoader(event));\
        ModForgeEvents.derpsLoaded.clear();
        event.addListener(new DatapackDerpsLoader(event));
    }

    public static AtomicInteger Tick=new AtomicInteger();
    @SubscribeEvent
    public static  void worldTickEvent(TickEvent.ServerTickEvent event){
        if (Tick.get()>18){
            DerpEventHandler.Cooldown.forEach(DerpEventHandler.CoolDown::decrement);
            DerpEventHandler.Cooldown.forEach(val->System.out.println("ticking: "+val.Cooldown));
            Tick.set(0);
        }
        Tick.getAndIncrement();
    }

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

    @SubscribeEvent
    public static void livingHurtEvent(LivingHurtEvent event){
        if (!DerpEventHandler.Slottables.containsKey("LivingHurt")) DerpEventHandler.Slottables.put("LivingHurt",new ArrayList<>());
        JsonObject eventInfo= new JsonObject();
        eventInfo.addProperty("EventType","LivingHurt");
        eventInfo.addProperty("AmountOfDamageDealt",event.getAmount());
        if(event.getSource().getEntity()!=null) eventInfo.addProperty("SourceUUID",event.getSource().getEntity().getUUID().toString());

//        if(event.getSource().getEntity()!=null) eventInfo.addProperty("SourceUUID",event.getSource().);
        DerpEventHandler.Slottables.get("LivingHurt").forEach((slottable)->{
            slottable.call(eventInfo);
        });
    }



}
