package net.azazelzero.derp.core.net;

import net.azazelzero.derp.core.event.ModForgeEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.network.NetworkEvent;


import java.util.Objects;
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
//            Object ClientPacketHandlerClass;
//            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> {handlePacket(msg, ctx); return null;});
            System.out.println(msg.type.toString());
            System.out.println("Mesdasaaaa");


            //                    case Packet.PacketType.Request:
            //                        System.out.println(msg.serverPlayer);
            //                        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(msg.serverPlayer)), new Message(new Packet(new DERP[3], true)));
            //                        break;
            if (Objects.requireNonNull(msg.type) == net.azazelzero.derp.core.net.Packet.PacketType.SelectingDerp) {
                net.azazelzero.derp.core.event.ModForgeEvents.derpsLoaded.clear();
                net.azazelzero.derp.core.event.ModForgeEvents.derpsLoaded.putAll(msg.derpSync);
                net.azazelzero.derp.core.event.ModForgeEvents.layers.clear();
                net.azazelzero.derp.core.event.ModForgeEvents.derpsLoaded.entrySet().forEach((k) -> {
                    ModForgeEvents.layers.add(k.getKey());
                });
                System.out.println("here 1");
//                Minecraft.getInstance().setScreen(new SelectDerp());
//                Minecraft.getInstance().setScreen(new SkillTree());

            }


        });
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void handlePacket(net.azazelzero.derp.core.net.Packet msg, Supplier<NetworkEvent.Context> ctx) {
//        if(Minecraft.getInstance().player.)
//        Minecraft.getInstance().player.displayClientMessage(new TextComponent(msg), false);
//        System.out.println("yup its reading alr");

        if (msg.type == Packet.PacketType.Message) {
            Minecraft.getInstance().player.displayClientMessage(new TextComponent(msg.str), false);
        }

//        if (msg.type == Packet.PacketType.SelectingDerp) {
//            System.out.println("pain");
//            Minecraft.getInstance().setScreen(new SelectDerp());
//
//        }

    }
}
