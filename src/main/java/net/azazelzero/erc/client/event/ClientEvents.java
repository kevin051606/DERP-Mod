package net.azazelzero.erc.client.event;

import net.azazelzero.erc.Main;
import net.azazelzero.erc.client.keybinds.KeyInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID,bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {


    private ClientEvents(){}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        KeyInit.init();
    }
}
