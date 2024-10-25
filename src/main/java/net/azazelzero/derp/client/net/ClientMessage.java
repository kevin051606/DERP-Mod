package net.azazelzero.derp.client.net;

import net.azazelzero.derp.core.data.DerpPlayerDataProvider;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.core.derp.SkillBoolean;
import net.azazelzero.derp.core.derp.Slottable;
import net.azazelzero.derp.core.derp.requirements.DatRequirementRegistry;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.azazelzero.derp.core.net.MapMessage;
import net.azazelzero.derp.core.net.Message;
import net.azazelzero.derp.core.net.Packet;
import net.azazelzero.derp.core.net.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Objects;
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
            switch (msg.type) {
                case Request:
                    HashMap<String, net.azazelzero.derp.core.derp.DERP> nullDerpMap =new HashMap<String, net.azazelzero.derp.core.derp.DERP>();
                    net.azazelzero.derp.core.derp.DERP nullDerp = new DERP();
                    nullDerp.Layer = "null";
                    nullDerpMap.put("null", nullDerp);
                    if (ModForgeEvents.derpsLoaded.isEmpty()) ModForgeEvents.derpsLoaded.put("Default", nullDerpMap);

                    ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(msg.serverPlayer);
                    boolean newPlayer = true;

                    loadData(player);
                    break;
                case UnlockingSkill:
                    UnlockSkill(msg);
                    break;
                case SelectingDerp:
                    System.out.println("here to send down");
                    ServerPlayer playerS=ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(msg.serverPlayer);
                    if (playerS==null) return;
                    playerS.getCapability(DerpPlayerDataProvider.DERP_DATA).ifPresent(derpPlayerData -> {
                        System.out.println("set");
                        derpPlayerData.derps=msg.Derps;
                        derpPlayerData.slots=new Slottable[5];
                    });
                    break;
            }

        });
        return false;
    }
    public void loadData(ServerPlayer player){
        player.getCapability(DerpPlayerDataProvider.DERP_DATA).ifPresent(derpPlayerData->{
            if(derpPlayerData.derps==null) {
                System.out.println("new player");
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MapMessage(new Packet(ModForgeEvents.derpsLoaded, true)));
            return;
            }
            System.out.println("not a new player");
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MapMessage(new Packet(ModForgeEvents.derpsLoaded, false)));
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()->player), new Message(new Packet(derpPlayerData.slots,derpPlayerData.derps)));
        });
    }
    public void handlePacket(String msg, Supplier<NetworkEvent.Context> ctx) {
//        if(Minecraft.getInstance().player.)

        Minecraft.getInstance().player.displayClientMessage(new TextComponent(msg), false);
        System.out.println("yup its reading alr");
    }
    public static void UnlockSkill(Packet msg){
        boolean unlockSkill= DatRequirementRegistry.verify(
                ModForgeEvents.derpsLoaded.get(msg.str).get(msg.derpPacket).DATs[msg.Pos.Panel][msg.Pos.X][msg.Pos.Y].Requirements,
                msg.serverPlayer
        );
        ServerPlayer player=ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(msg.serverPlayer);
        if (player==null) return;
        if(unlockSkill) player.getCapability(DerpPlayerDataProvider.DERP_DATA).ifPresent(derpPlayerData -> {
            derpPlayerData.derps.forEach(e->{
                if(Objects.equals(e[msg.evolution].derpId, msg.derpPacket))
                    e[msg.evolution].SkillsUnlocked.add(ModForgeEvents.derpsLoaded.get(msg.str).get(msg.derpPacket).DATs[msg.Pos.Panel][msg.Pos.X][msg.Pos.Y].Id);
            });
        });

    }
}
