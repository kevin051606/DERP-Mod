package net.azazelzero.derp.core.event;

import net.azazelzero.derp.DerpEventHandler;
import net.azazelzero.derp.Main;

//import net.azazelzero.erc.client.net.ClientPacketHandler;
import net.azazelzero.derp.core.net.PacketHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    public net.azazelzero.derp.DerpEventHandler derpEventHandler = new DerpEventHandler();

    private ModEvents(){}

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event){
        System.out.println("is this being fucking touched");
        event.enqueueWork(PacketHandler::init);
//        event.enqueueWork(ClientPacketHandler::init);
    }



}
