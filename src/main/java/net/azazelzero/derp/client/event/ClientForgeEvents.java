package net.azazelzero.derp.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.azazelzero.derp.DerpEventHandler;
import net.azazelzero.derp.Main;
import net.azazelzero.derp.client.gui.SkillTree;
import net.azazelzero.derp.client.keybinds.KeyInit;
import net.azazelzero.derp.client.net.ClientMessage;
import net.azazelzero.derp.core.derp.DAT;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.core.data.DerpPlayerDataProvider;
import net.azazelzero.derp.core.derp.Slottable;
import net.azazelzero.derp.core.net.Packet;
import net.azazelzero.derp.core.net.PacketHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.Minecraft;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
//import org.luaj.vm2.Globals;
//import org.luaj.vm2.LuaValue;
//import org.luaj.vm2.lib.jse.JsePlatform;

@Mod.EventBusSubscriber(modid = Main.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {
    public static List<DERP[]> ClientPlayerData=new ArrayList<>();
    public static DAT[] SlottedSkills= new DAT[5];
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
            pain.add(-1); //hovering element x / can be used for drag
            pain.add(-1); //hovering element y / can be used for drag
            pain.add(1); //allow hovering menu
            pain.add(1); //texture phase
            Minecraft.getInstance().setScreen(new SkillTree(pain,new PoseStack()));
        }

    }

    @SubscribeEvent
    public static void loggedInEvent(ClientPlayerNetworkEvent.LoggedInEvent event){
        event.getPlayer().chat(event.getPlayer().getName().getString());
        event.getPlayer().getCapability(DerpPlayerDataProvider.DERP_DATA).ifPresent(derp -> {

        });
        ClientPlayerData.clear();
        SlottedSkills=new DAT[5];
           PacketHandler.INSTANCE.sendToServer(new ClientMessage(new Packet(event.getPlayer().getName().getString(),true)));

    }
    public static Map<Slottable,AtomicInteger> Slots= new HashMap<>();
    public static AtomicInteger Tick=new AtomicInteger();

    @SubscribeEvent
    public static void tickEvent(TickEvent.ClientTickEvent event){
        if (Tick.get()>18){
            Slots.forEach((s,i)->decrement(i));
            Tick.set(0);
        }
        Tick.getAndIncrement();
    }
    public static void AddSlot(Slottable slot){
        AtomicBoolean addSlot=new AtomicBoolean(true);
        Slots.forEach((slottable, coolDown)-> {
            if (addSlot.get())
                addSlot.set(!(
                    Objects.equals(slottable.SkillPosition.SkillID, slot.SkillPosition.SkillID) &&
                    Objects.equals(slottable.SkillPosition.DerpID, slot.SkillPosition.DerpID)
                ));
            if (!addSlot.get()) Slots.get(slottable).set(slottable.CoolDown);
        });
        System.out.println(addSlot.get());
        if (addSlot.get()) Slots.put(slot,new AtomicInteger(slot.CoolDown));

    }
    public static void decrement(AtomicInteger integer){
        integer.getAndDecrement();
        if (integer.get()<0)integer.set(0);
    }

}
