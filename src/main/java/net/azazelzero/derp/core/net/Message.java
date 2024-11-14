package net.azazelzero.derp.core.net;

import com.mojang.blaze3d.vertex.PoseStack;
import net.azazelzero.derp.client.event.ClientForgeEvents;
import net.azazelzero.derp.client.gui.SkillTreeEditor;
import net.azazelzero.derp.client.gui.utils.PopUpMenuManager;
import net.azazelzero.derp.core.derp.*;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;


import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class Message {
    public final net.azazelzero.derp.core.net.Packet msg;

    public Message(net.azazelzero.derp.core.net.Packet msg) {
        this.msg = msg;
    }

    public Message(FriendlyByteBuf buffer) {
        this.msg = net.azazelzero.derp.core.net.Packet.FromByteArray(buffer.readByteArray());
//        System.out.println(msg.type);
//        System.out.println(msg.str);

    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByteArray(msg.SerializeToByteArray());
        System.out.println(msg.type+"wowwwwi");
        byte[] a = msg.SerializeToByteArray();
        net.azazelzero.derp.core.net.Packet c = (net.azazelzero.derp.core.net.Packet) net.azazelzero.derp.core.net.Packet.FromByteArray(a);
        System.out.println(c.type+"wwwwwad02920890");

    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {

            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> {
                return handlePacket(this.msg, ctx);
            });

        });
        return true;
    }

    public static DistExecutor.SafeRunnable handlePacket(Packet msg, Supplier<NetworkEvent.Context> ctx) {
        return new DistExecutor.SafeRunnable() {
            public void run() {
                switch (msg.type){
                    case Message -> Minecraft.getInstance().player.chat(msg.str);
                    case Sync ->handleSync(msg);
                    case UnlockingSkill -> unlockSkill(msg);
                    case SlotTriggered -> slotTriggered(msg);
                    case OpenEditor -> editorScreen();
                }
            }
        };
    }

    private static void editorScreen() {
        List<Integer> pain = new ArrayList<>();
        PoseStack painB = new PoseStack();
        pain.add(0); //derp index for all derps
        pain.add(0); //evolution tab
        pain.add(0); //panel index
        pain.add(0); //lock scroll 0 is false 1 true
        pain.add(0); //scrolling number
        pain.add(-1); //hovering element x / can be used for drag
        pain.add(-1); //hovering element y / can be used for drag
        pain.add(1); //allow hovering menu
        pain.add(1); //texture phase
        pain.add(0); // derp scroll bar
        pain.add(0); // pop up menu toggle
        pain.add(0); // moue button being held
        Minecraft.getInstance().setScreen(new SkillTreeEditor(pain,painB, new PopUpMenuManager()));
    }

    private static void slotTriggered(Packet msg) {
        System.out.println("here"+ClientForgeEvents.Slots.size());
        ClientForgeEvents.Slots.forEach((slots,coolDowns)->{
            System.out.println(slots.SkillPosition.SkillID+" vs "+msg.SkillToSlot.Id);
            if (Objects.equals(slots.SkillPosition.SkillID, msg.SkillToSlot.Id) && Objects.equals(slots.SkillPosition.DerpID, msg.derpPacket)) coolDowns.set(slots.CoolDown);
        });
    }


    public static void unlockSkill(Packet msg) {
        AtomicInteger counter=new AtomicInteger();
        AtomicInteger scounter=new AtomicInteger();

        ClientForgeEvents.ClientPlayerData.forEach((v)->{


            if(Objects.equals(v[msg.evolution].name, msg.derpPacket)){
                v[msg.evolution].DATs[msg.Pos.Panel][msg.Pos.X][msg.Pos.Y].unlocked=true;
                scounter.set(counter.get());
            }
            counter.getAndIncrement();
        });
    }

    public static void handleSync(Packet msg) {
        if (ClientForgeEvents.ClientPlayerData == null) ClientForgeEvents.ClientPlayerData = new ArrayList<>();
        ClientForgeEvents.ClientPlayerData.clear();
        for (DERPData[] derp : msg.Derps) {
            DERP[] derpSetToBeAdded = new DERP[4];
            for (int i = 0; i < derp.length; i++) {
                DERPData derpData=derp[i];
                if(derpData==null) continue;
                DERP derpToBeAdded = ModForgeEvents.derpsLoaded.get(derpData.layerId).get(derpData.derpId);
                if(null!=derpData.Slots){
                    for (int i1 = 0; i1 < 5; i1++) {
                        SkillReference pos = derpData.Slots[i1].SkillPosition;
                        ClientForgeEvents.SlottedSkills[i1]=(ModForgeEvents.derpsLoaded.get(pos.LayerID).get(pos.DerpID).DATs[pos.Panel][pos.X][pos.Y]);
                    }
                }
                if (derpData.SkillsUnlocked ==null) continue;
                matchingSkillToData(derpToBeAdded, derpData);
                derpSetToBeAdded[i]=derpToBeAdded;
            }
            ClientForgeEvents.ClientPlayerData.add(derpSetToBeAdded);
        }
    }
    public static void matchingSkillToData(DERP derpToBeAdded, DERPData data){
        Map<String, DAT> datMap=new HashMap<>();
        for (DAT[][] Panel : derpToBeAdded.DATs) {
            if(Panel==null) continue;
            for (DAT[] X : Panel) {
                if (X == null) continue;
                for (DAT dat : X) {
                    if (dat==null) continue;
                    datMap.put(dat.Id, dat);
                }
            }
        }
        for (String s : data.SkillsUnlocked) {
            if (datMap.containsKey(s)) datMap.get(s).unlocked=true;
        }
    }
}
