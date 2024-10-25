package net.azazelzero.derp.core.net;

import net.azazelzero.derp.client.event.ClientForgeEvents;
import net.azazelzero.derp.core.derp.DAT;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.core.derp.DERPData;
import net.azazelzero.derp.core.derp.SkillBoolean;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;


import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
                }
            }
        };
    }
    public static void handleSync(Packet msg) {
        if (ClientForgeEvents.ClientPlayerData == null) ClientForgeEvents.ClientPlayerData = new ArrayList<>();
        for (DERPData[] derp : msg.Derps) {
            DERP[] derpSetToBeAdded = new DERP[3];
            for (int i = 0; i < derp.length; i++) {
                DERPData derpData=derp[i];
                if(derpData==null) continue;
                DERP derpToBeAdded = ModForgeEvents.derpsLoaded.get(derpData.layerId).get(derpData.derpId);
                if(null!=derpData.Slots);
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
