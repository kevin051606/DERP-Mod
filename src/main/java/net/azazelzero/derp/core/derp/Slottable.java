package net.azazelzero.derp.core.derp;

import com.google.gson.JsonObject;
import net.azazelzero.derp.DerpEventHandler;
import net.azazelzero.derp.core.derp.requirements.DatRequirement;
import net.azazelzero.derp.core.derp.skillactions.SkillAction;
import net.azazelzero.derp.core.derp.skillactions.SkillActionEntry;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.azazelzero.derp.core.net.Message;
import net.azazelzero.derp.core.net.Packet;
import net.azazelzero.derp.core.net.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.luaj.vm2.ast.Str;

import java.io.Serializable;

public class Slottable implements Serializable {
    public final String PlayerName;
    public final String EventName;
    public int Index;
    public final SkillReference SkillPosition;
    public final SkillAction[] Actions;
    public final int CoolDown;
    public Slottable(String playerName, String eventName, SkillReference skillPosition, SkillAction[] actions, int coolDown){
        PlayerName=playerName;
        EventName = eventName;
        SkillPosition = skillPosition;
        Actions=actions;
        CoolDown = coolDown;
    }
    public void call(JsonObject eventInfo){
        if (!DerpEventHandler.CooldownIs0(EventName, SkillPosition.SkillID, PlayerName,CoolDown)) return;
        ServerPlayer player= ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(PlayerName);
        DAT dat= ModForgeEvents.derpsLoaded.get(SkillPosition.LayerID).get(SkillPosition.DerpID).DATs[SkillPosition.Panel][SkillPosition.X][SkillPosition.Y];
        if (player==null) return;
        if (dat==null) return;
        PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()->player), new Message(new Packet(dat, SkillPosition.LayerID, SkillPosition.DerpID,PlayerName)));
        for (SkillAction action : Actions) {
            JsonObject actionNewParameters=action.getParameters();
            actionNewParameters.add("eventInformation",eventInfo);
            action.setParameters(actionNewParameters);
            action.action(PlayerName);
        }
    }

}
