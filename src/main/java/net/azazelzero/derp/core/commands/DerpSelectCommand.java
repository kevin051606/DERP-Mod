package net.azazelzero.derp.core.commands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.azazelzero.derp.core.data.DerpPlayerData;
import net.azazelzero.derp.core.data.DerpPlayerDataProvider;
import net.azazelzero.derp.core.derp.DAT;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.core.derp.DERPData;
import net.azazelzero.derp.core.derp.skillactions.SkillAction;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.azazelzero.derp.core.net.MapMessage;
import net.azazelzero.derp.core.net.Message;
import net.azazelzero.derp.core.net.Packet;
import net.azazelzero.derp.core.net.PacketHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;

public class DerpSelectCommand {
    public DerpSelectCommand(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("derp").requires((as)->{return  as.hasPermission(2);}).then(Commands.literal("editor").then(Commands.argument("target", EntityArgument.players()).executes((command)->{
            sendEditorPacket(command);
            return 0;
        })))
        .then(Commands.literal("select").then(Commands.argument("target", EntityArgument.players()).executes((command) -> {
            sendGuiPacket(command);
            return 0;
        }))));
    }
    public void sendGuiPacket(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        for (ServerPlayer serverPlayer : EntityArgument.getPlayers(command, "target")){
            System.out.println(serverPlayer.getName().getString());
            HashMap<String, net.azazelzero.derp.core.derp.DERP> nullDerpMap = new HashMap<String, net.azazelzero.derp.core.derp.DERP>();
            net.azazelzero.derp.core.derp.DERP nullDerp=new DERP();
            nullDerp.Layer="null";
            nullDerpMap.put("null", nullDerp);
            if (ModForgeEvents.derpsLoaded.size()<1) ModForgeEvents.derpsLoaded.put(nullDerp.Layer,nullDerpMap);
            DERP.remove(serverPlayer);
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),new MapMessage(new Packet(ModForgeEvents.derpsLoaded,true)));
        }
    }
    public void sendEditorPacket(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        for (ServerPlayer serverPlayer : EntityArgument.getPlayers(command, "target")){
            System.out.println(serverPlayer.getName().getString());
            HashMap<String, net.azazelzero.derp.core.derp.DERP> nullDerpMap = new HashMap<String, net.azazelzero.derp.core.derp.DERP>();
            net.azazelzero.derp.core.derp.DERP nullDerp=new DERP();
            nullDerp.Layer="null";
            nullDerpMap.put("null", nullDerp);
            if (ModForgeEvents.derpsLoaded.size()<1) ModForgeEvents.derpsLoaded.put("Default",nullDerpMap);
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),new MapMessage(new Packet(ModForgeEvents.derpsLoaded,false)));
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),new Message(new Packet(Packet.PacketType.OpenEditor)));
        }
    }
    private void test(ServerPlayer s){

    }
}
