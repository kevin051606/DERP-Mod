package net.azazelzero.derp.client.net;

import com.google.gson.JsonObject;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.azazelzero.derp.core.net.MapMessage;
import net.azazelzero.derp.core.net.Message;
import net.azazelzero.derp.core.net.Packet;
import net.azazelzero.derp.core.net.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ClientMessage {
    public final Packet msg;

    public ClientMessage(Packet msg) {
        this.msg = msg;
    }

    public ClientMessage(FriendlyByteBuf buffer) {
        this.msg = Packet.FromByteArray(buffer.readByteArray());
        System.out.println(msg.type+" sdsd");
        System.out.println(msg.str+" sdsd");

    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeByteArray(msg.SerializeToByteArray());
        System.out.println(msg.type+"  jkjk");
        byte[] a = msg.SerializeToByteArray();
        Packet c = (Packet) Packet.FromByteArray(a);
        System.out.println(c.type);

    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx){
        final var success = new AtomicBoolean(false);
        ctx.get().enqueueWork(()->{
            Object ClientPacketHandlerClass;
//                DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> this.handlePacket(msg, ctx));
//                Minecraft.getInstance().player.displayClientMessage(new TextComponent(msg), false);
                    System.out.println(msg.serverPlayer);
                    HashMap<String, net.azazelzero.derp.core.derp.DERP> nullDerpMap = new HashMap<String, net.azazelzero.derp.core.derp.DERP>();
                    net.azazelzero.derp.core.derp.DERP nullDerp=new DERP();
                    nullDerp.Layer="null";
                    nullDerpMap.put("null", nullDerp);
                    if (ModForgeEvents.derpsLoaded.isEmpty()) ModForgeEvents.derpsLoaded.put("Default",nullDerpMap);
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(msg.serverPlayer)), new MapMessage(new Packet(ModForgeEvents.derpsLoaded, true)));

        });
        return false;
    }

    public void handlePacket(String msg, Supplier<NetworkEvent.Context> ctx) {
//        if(Minecraft.getInstance().player.)

        Minecraft.getInstance().player.displayClientMessage(new TextComponent(msg), false);
        System.out.println("yup its reading alr");
    }
}
