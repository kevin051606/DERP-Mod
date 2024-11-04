package net.azazelzero.derp.core.event;

import net.azazelzero.derp.DerpEventHandler;
import net.azazelzero.derp.Main;

//import net.azazelzero.erc.client.net.ClientPacketHandler;
import net.azazelzero.derp.core.data.DerpPlayerData;
import net.azazelzero.derp.core.data.DerpPlayerDataProvider;
import net.azazelzero.derp.core.net.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    public net.azazelzero.derp.DerpEventHandler derpEventHandler = new DerpEventHandler();

    private ModEvents(){}
//    public static void setup(){
//        IEventBus bus = MinecraftForge.EVENT_BUS;
////        bus.addListener(Capabilities::onRegisterCapabilities);
//        bus.addGenericListener(Entity.class,Capabilities::onAttachCapabilitiesPlayer);
//        bus.addListener(Capabilities::clonedEvent);
//    }
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event){
        System.out.println("is this being fucking touched");
        event.enqueueWork(PacketHandler::init);
//        event.enqueueWork(ClientPacketHandler::init);
    }





}
