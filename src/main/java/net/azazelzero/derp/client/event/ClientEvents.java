package net.azazelzero.derp.client.event;

import net.azazelzero.derp.Main;
import net.azazelzero.derp.client.gui.SlottableCooldown;
import net.azazelzero.derp.client.keybinds.KeyInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MODID,bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public class ClientEvents {


    private ClientEvents(){}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        KeyInit.init();
        OverlayRegistry.registerOverlayTop("Slottable Cooldown", new SlottableCooldown());
    }

//    public static void registerGuiOverlays(OverlayRegistry )
}
