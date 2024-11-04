package net.azazelzero.derp;

import net.azazelzero.derp.core.derp.Slottable;

import java.util.*;

public class DerpEventHandler {
    // Event type Map<EventName, List<>>
    public static Map<String,ArrayList<Slottable>> Slottables =new HashMap<>();
    public static ArrayList<CoolDown> Cooldown =new ArrayList<>();
    // Map< Playername, ThePlayerSlots>
    public static Map<String, Slottable[]> PlayerSlottables=new HashMap<>();

//    public iterate
    public static boolean CooldownIs0(String EventName, String SkillId, String PlayerId, int setCoolDown){
        boolean nothingMatches = true;
        for (CoolDown coolDown : Cooldown) {
            if (Objects.equals(coolDown.EventName, EventName) && Objects.equals(coolDown.SlottableID, SkillId) && Objects.equals(coolDown.PlayerID, PlayerId)){
                if (nothingMatches) nothingMatches=coolDown.Cooldown<=0;
                if (nothingMatches) coolDown.Cooldown=setCoolDown;
            }
        }
        return nothingMatches;
    }

    public static class CoolDown{
        public final String EventName;
        public final String SlottableID;
        public final String PlayerID;
        public int Cooldown;
        public CoolDown(String eventName, String slottableID, String playerID, int cooldown){

            EventName = eventName;
            SlottableID = slottableID;
            PlayerID = playerID;
            Cooldown = cooldown;
        }
        public void decrement(){
            Cooldown--;
            if (Cooldown<0) Cooldown=0;
        }

    }
}
