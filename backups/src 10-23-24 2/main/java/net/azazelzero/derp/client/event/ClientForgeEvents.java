package net.azazelzero.derp.client.event;

import net.azazelzero.derp.Main;
import net.azazelzero.derp.client.gui.SkillTree;
import net.azazelzero.derp.client.keybinds.KeyInit;
import net.azazelzero.derp.client.net.ClientMessage;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.core.data.DerpPlayerDataProvider;
import net.azazelzero.derp.core.net.Packet;
import net.azazelzero.derp.core.net.PacketHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
//import org.luaj.vm2.Globals;
//import org.luaj.vm2.LuaValue;
//import org.luaj.vm2.lib.jse.JsePlatform;

@Mod.EventBusSubscriber(modid = Main.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {
    public static List<DERP[]> ClientPlayerData;
    private ClientForgeEvents(){}

//    @SubscribeEvent
//    public static void clientTick(TickEvent.ClientTickEvent event){
//        if(KeyInit.ercKeyOpenUI.isDown()&&Minecraft.getInstance().screen==null) {
//            System.out.println("Info: ");
//            Minecraft.getInstance().setScreen(new SkillTree());
//        }
//    }

    @SubscribeEvent
    public static void keyInputEvent(InputEvent.KeyInputEvent event){
        if(KeyInit.ercKeyOpenUI.isDown()&&Minecraft.getInstance().screen==null) {
            System.out.println("Info: ");
            List<Integer> pain = new ArrayList();
            pain.add(0); //derp index for all derps
            pain.add(0); //evolution tab
            pain.add(0); //panel index
            pain.add(0); //lock scroll 0 is false 1 true
            pain.add(0); //scrolling number
            pain.add(0); //hovering element x
            Minecraft.getInstance().setScreen(new SkillTree(pain));
        }

    }

    @SubscribeEvent
    public static void loggedInEvent(ClientPlayerNetworkEvent.LoggedInEvent event){
        event.getPlayer().chat(event.getPlayer().getName().getString());
        event.getPlayer().getCapability(DerpPlayerDataProvider.DERP_DATA).ifPresent(derp -> {

        });
           PacketHandler.INSTANCE.sendToServer(new ClientMessage(new Packet(event.getPlayer().getName().getString(),true)));

    }



}
