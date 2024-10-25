package net.azazelzero.derp.core.net;

import net.azazelzero.derp.client.event.ClientForgeEvents;
import net.azazelzero.derp.client.gui.SelectDerp;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class MapMessage {
    public final net.azazelzero.derp.core.net.Packet msg;

    public MapMessage(net.azazelzero.derp.core.net.Packet msg) {
        this.msg = msg;
    }


    public MapMessage(FriendlyByteBuf buffer) {
        Map<String, net.azazelzero.derp.core.derp.DERP[]> pain = buffer.readMap(FriendlyByteBuf::readUtf, net.azazelzero.derp.core.net.SerializeUtil::deserializeAdd);
        System.out.println("asdad:" + pain.size());
        Map<String, Map<String, net.azazelzero.derp.core.derp.DERP>> conversionTherapy = new HashMap();
        pain.forEach((key, value) -> {
            conversionTherapy.put(key, new HashMap());

            for(int d = 0; d < value.length; ++d) {
                net.azazelzero.derp.core.derp.DERP derp = value[d];
                System.out.println("zzz" + d);
                ((Map)conversionTherapy.get(key)).put(derp.name, derp);
            }

        });
        this.msg = new net.azazelzero.derp.core.net.Packet(conversionTherapy, false);
    }

    public void encode(FriendlyByteBuf buffer) {
        System.out.println("encoding");
        Map<String, DERP[]> painInThheAss = new HashMap<>();
        ModForgeEvents.derpsLoaded.forEach((key1, value1) -> {
            System.out.println("bruh:" + value1.entrySet().size());
            painInThheAss.put(key1, new net.azazelzero.derp.core.derp.DERP[value1.entrySet().size()]);
            AtomicInteger counter = new AtomicInteger();
            value1.forEach((key, value) -> {
                (painInThheAss.get(key1))[counter.get()] = value;
                PrintStream var10000 = System.out;
                String var10001 = ((DERP[])painInThheAss.get(key1))[counter.get()].name;
                var10000.println("sss:" + var10001);
                counter.getAndIncrement();
            });
        });
        painInThheAss.entrySet().forEach((a) -> {
            System.out.println("dawg" + (String)a.getKey());
        });
        buffer.writeMap(painInThheAss, FriendlyByteBuf::writeUtf, SerializeUtil::serializeAdd);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        new AtomicBoolean(false);
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            System.out.println("name of before" + this.msg.type.name());
            if (this.msg.type == net.azazelzero.derp.core.net.Packet.PacketType.SelectingDerp) {
                ModForgeEvents.derpsLoaded.clear();
                System.out.println(this.msg.derpSync.isEmpty());
                ModForgeEvents.derpsLoaded.putAll(this.msg.derpSync);
                ModForgeEvents.layers.clear();
                ModForgeEvents.derpsLoaded.forEach((key, value) -> {
                    ModForgeEvents.layers.add(key);
                    System.out.println("dude:" + key);
                });
                ModForgeEvents.derpsLoaded.forEach((d, l) -> {
                    System.out.println("layer of derps " + d);
                    l.forEach((k, v) -> {
                        System.out.println("name of derps " + v.name);
                        System.out.println("name of class " + v.getClass().getName());
                    });
                });
                System.out.println("name of derps has been ran");
            }

            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> {
                return handlePacket(this.msg, ctx);
            });
        });
        return true;
    }

    public static DistExecutor.SafeRunnable handlePacket(Packet msg, Supplier<NetworkEvent.Context> ctx) {
        return new DistExecutor.SafeRunnable() {
            public void run() {
                if(ClientForgeEvents.ClientPlayerData==null) ClientForgeEvents.ClientPlayerData=new ArrayList<>();
                ClientForgeEvents.ClientPlayerData.clear();
                List<Integer> pain = new ArrayList();
                pain.add(0);
                pain.add(0);


                Minecraft.getInstance().setScreen(new SelectDerp(pain));
            }
        };
    }
}
