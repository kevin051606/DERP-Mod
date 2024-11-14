package net.azazelzero.derp.client.net;

import net.azazelzero.derp.DerpEventHandler;
import net.azazelzero.derp.core.data.DerpPlayerData;
import net.azazelzero.derp.core.data.DerpPlayerDataProvider;
import net.azazelzero.derp.core.derp.*;
import net.azazelzero.derp.core.derp.requirements.DatRequirementRegistry;
import net.azazelzero.derp.core.derp.skillactions.SkillAction;
import net.azazelzero.derp.core.derp.skillactions.SkillActions;
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

import java.util.ArrayList;
import java.util.Arrays;
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
                case SkillSlotted:
                    DAT Dat=msg.SkillToSlot;
                    if (!Dat.Slottable) break;
                    if (Objects.equals(Dat.Icon, "nulled")){
                        System.out.println("yuo");
                        System.out.println(DerpEventHandler.PlayerSlottables.get(msg.serverPlayer)[msg.SlottableIndex].EventName);
                        System.out.println(DerpEventHandler.Slottables.get(Dat.SlottableEvent).size());
                        DerpEventHandler.PlayerSlottables.get(msg.serverPlayer)[msg.SlottableIndex]=null;
                        DerpEventHandler.Slottables.get(Dat.SlottableEvent).removeIf(value -> Objects.equals(value.SkillPosition.SkillID, Dat.Id));
                        System.out.println(DerpEventHandler.Slottables.get(Dat.SlottableEvent).size());
                        break;
                    }
                    if (!DatRequirementRegistry.verify(Dat.Requirements,msg.serverPlayer)) break;
                    System.out.println(Dat.Slottable);
                    Slottable slot=new Slottable(
                            msg.serverPlayer,
                            msg.SkillToSlot.SlottableEvent,
                            new SkillReference(Dat.Panel,Dat.X,Dat.Y, msg.str, msg.derpPacket,Dat.Id),
                            Dat.Actions,
                            Dat.Cooldown
                            );

                    if (!DerpEventHandler.PlayerSlottables.containsKey(msg.serverPlayer)) DerpEventHandler.PlayerSlottables.put(msg.serverPlayer, new Slottable[5]);
                    boolean duplicates=Arrays.stream(DerpEventHandler.PlayerSlottables.get(msg.serverPlayer)).anyMatch(b -> {
                        if (b==null) return false;
                        return (Objects.equals(b.SkillPosition.SkillID, slot.SkillPosition.SkillID)) &&
                                (Objects.equals(b.SkillPosition.DerpID, slot.SkillPosition.SkillID));
                    });
                    if (duplicates) break;
                    DerpEventHandler.PlayerSlottables.get(msg.serverPlayer)[msg.SlottableIndex]=slot;
                    if (!DerpEventHandler.Slottables.containsKey(slot.EventName)) DerpEventHandler.Slottables.put(slot.EventName, new ArrayList<>());
                    DerpEventHandler.Slottables.get(slot.EventName).add(slot);
                    System.out.println("Cooldown: "+slot.CoolDown);
                    if (slot.CoolDown>0)DerpEventHandler.Cooldown.add(new DerpEventHandler.CoolDown(slot.EventName,slot.SkillPosition.SkillID,slot.PlayerName,slot.CoolDown));
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
                    for (int i = 0; i < msg.Derps.size(); i++) {
                        DERPData[] e=msg.Derps.get(i);
                        System.out.println("derp ID: "+e[0].derpId);
                        for (DAT[][] dat : ModForgeEvents.derpsLoaded.get(e[0].layerId).get(e[0].derpId).DATs) {
                            for (DAT[] dats : dat) {
                                for (DAT dat1 : dats) {
                                    if (dat1==null)continue;
                                    if (dat1.Requirements.length>0)continue;
                                    int finalI = i;
                                    playerS.getCapability(DerpPlayerDataProvider.DERP_DATA).ifPresent(derpPlayerData -> unlocker(derpPlayerData, finalI,dat1.Id));
                                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()->playerS), new Message(new Packet(msg.serverPlayer,new DERPData.DATPostion(dat1.Panel,dat1.X,dat1.Y),e[0].derpId,e[0].layerId,0)));
                                    for (SkillAction action : dat1.Actions) {
                                        action.action(msg.serverPlayer);
                                    }
                                }

                            }
                        }
                    }
                    break;
            }

        });
        return false;
    }
    private void unlocker(DerpPlayerData dpd, Integer i,String id){
        dpd.derps.get(i)[0].SkillsUnlocked.add(id);
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

            Slotttable(player, derpPlayerData );
        });
    }

    private void Slotttable(ServerPlayer player, DerpPlayerData derpPlayerData) {
        if (!DerpEventHandler.PlayerSlottables.containsKey(player.getName().getString())) DerpEventHandler.PlayerSlottables.put(player.getName().getString(), new Slottable[5]);
        if (DerpEventHandler.PlayerSlottables.get(player.getName().getString()).length>=5) return;
        for (Slottable slottable : DerpEventHandler.PlayerSlottables.get(player.getName().getString())) {
            DerpEventHandler.Slottables.get(slottable.EventName).removeIf((b)-> Objects.equals(b.PlayerName, player.getName().getString()));
        }
        DerpEventHandler.PlayerSlottables.replace(player.getName().getString(), new Slottable[5]);
        for (int i = 0; i < 5; i++) {
            Slottable slot=derpPlayerData.slots[i];
            if (!DerpEventHandler.Slottables.containsKey(slot.EventName)) DerpEventHandler.Slottables.put(slot.EventName,new ArrayList<>());
            DerpEventHandler.Slottables.get(slot.EventName).add(slot);
            DerpEventHandler.PlayerSlottables.get(player.getName().getString())[i]=slot;

        }
        DerpEventHandler.Cooldown.removeIf(val-> Objects.equals(val.PlayerID, player.getName().getString()));
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
                if(Objects.equals(e[msg.evolution].derpId, msg.derpPacket)) {
                    if (e[msg.evolution].SkillsUnlocked.contains(ModForgeEvents.derpsLoaded.get(msg.str).get(msg.derpPacket).DATs[msg.Pos.Panel][msg.Pos.X][msg.Pos.Y].Id)) return;
                    e[msg.evolution].SkillsUnlocked.add(ModForgeEvents.derpsLoaded.get(msg.str).get(msg.derpPacket).DATs[msg.Pos.Panel][msg.Pos.X][msg.Pos.Y].Id);
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(msg.serverPlayer)), new Message(msg));
                    if (ModForgeEvents.derpsLoaded.get(msg.str).get(msg.derpPacket).DATs[msg.Pos.Panel][msg.Pos.X][msg.Pos.Y].Slottable) return;
                    for (SkillAction action : ModForgeEvents.derpsLoaded.get(msg.str).get(msg.derpPacket).DATs[msg.Pos.Panel][msg.Pos.X][msg.Pos.Y].Actions) {
                        action.action(msg.serverPlayer);
                    }
                }
                });
        });

    }
}
