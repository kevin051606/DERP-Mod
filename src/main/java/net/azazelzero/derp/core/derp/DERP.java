package net.azazelzero.derp.core.derp;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.azazelzero.derp.DerpEventHandler;
import net.azazelzero.derp.core.data.DerpPlayerData;
import net.azazelzero.derp.core.data.DerpPlayerDataProvider;
import net.azazelzero.derp.core.derp.requirements.DatRequirement;
import net.azazelzero.derp.core.derp.skillactions.SkillAction;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.minecraft.server.level.ServerPlayer;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DERP implements Serializable {

    public DAT[][][] DATs;
    public String name;
    public String displayName="w";
    public String Description="w";
    public String Layer="Default";
    public String Icon="textures/item/apple.png";
    public String Texture="textures/block/dirt.png";
    public Float[] Color= {1.0f,1.0f,1.0f,1.0f};
    public Float[] FontColor = new Float[]{1.0F, 1.0F, 1.0F, 1.0F};
    public Float[] SkillUnlockedColor = new Float[]{1.0F, 1.0F, 1.0F, 1.0F};
    public Float[] SkillLockedColor = new Float[]{0.5F, 0.5F, .5F, .5F};
    public List<String> issue = null;
    public DERP(){
        DATs=new DAT[8][5][6];
    }
    public DatRequirement[][] Panels=new DatRequirement[8][];
    public JsonElement grab(String field,JsonObject jsonFile){
        JsonElement returnElement=jsonFile.get(field);
        if (returnElement.isJsonNull()){issue.add(field); return JsonNull.INSTANCE;}
        return returnElement;
    }

    public DERP(DERP clone){
        this.DATs = clone.DATs;
    }
    public void foreachDat(Consumer<DAT> datFunction){
        for (DAT[][] dat : DATs) {
            for (DAT[] dats : dat) {
                for (DAT dat1 : dats) {
                    datFunction.accept(dat1);
                }
            }
        }
    }

    public static void remove(ServerPlayer serverPlayer){
        serverPlayer.getCapability(DerpPlayerDataProvider.DERP_DATA).ifPresent(b -> {
            b.derps.forEach((v)->{
                for (DERPData derpData : v) {
                    if (derpData==null) continue;
                    if (!ModForgeEvents.derpsLoaded.containsKey(derpData.layerId)) continue;
                    if (!ModForgeEvents.derpsLoaded.get(derpData.layerId).containsKey(derpData.derpId)) continue;
                    for (DAT[][] dat : ModForgeEvents.derpsLoaded.get(derpData.layerId).get(derpData.derpId).DATs) {
                        for (DAT[] dats : dat) {
                            for (DAT dat1 : dats) {
                                if (dat1==null) continue;
                                for (SkillAction action : dat1.Actions) {
                                    if (action==null) continue;
                                    action.onRemove(serverPlayer.getName().getString());
                                }
                            }
                        }
                    }
                }
            });
            b = new DerpPlayerData();
        });
        for (Slottable slottable : DerpEventHandler.PlayerSlottables.get(serverPlayer.getName().getString())) {
            if (slottable==null) continue;
            DerpEventHandler.Slottables.get(slottable.EventName).removeIf(s -> Objects.equals(s.PlayerName, slottable.PlayerName) && Objects.equals(s.SkillPosition.SkillID, slottable.SkillPosition.SkillID));
        }
        DerpEventHandler.Cooldown.removeIf(c -> Objects.equals(c.PlayerID, serverPlayer.getName().getString()));

    }
}
