package net.azazelzero.derp.core.net;


import net.azazelzero.derp.core.derp.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Packet implements Serializable {
    public Packet.PacketType type;
    public String str;
    public String derpPacket;
    public Slottable[] syncSlottable;
    public ArrayList<DERPData[]> Derps;
    public static Map<String, Map<String, net.azazelzero.derp.core.derp.DERP>> derpSync;
    public String serverPlayer;
    public DERPData.DATPostion Pos;
    public int SlottableIndex;
    public DAT SkillToSlot;
    public int evolution;
    public Packet(String player,DERPData.DATPostion pos, String derpName, String derpLayer,int evolution){
        type=PacketType.UnlockingSkill;
        serverPlayer=player;
        Pos=pos;
        derpPacket=derpName;
        str=derpLayer;
        this.evolution=evolution;
    }
    public Packet(String str){
        type= PacketType.Message;
        this.str=str;
    }
    public Packet(String playername, boolean f){
        type=PacketType.Request;
        serverPlayer=playername;
    }
    public Packet(Slottable[] slots, ArrayList<DERPData[]> derps){
        type=PacketType.Sync;
        syncSlottable=slots;
        Derps=derps;
    }
    public Packet(ArrayList<DERPData[]> derps, String playerName){
        type=PacketType.SelectingDerp;
        serverPlayer=playerName;
        Derps=derps;
    }
    public Packet(Map<String, Map<String, DERP>> derps, boolean selecting){
        derpSync=derps;
        type=PacketType.SelectingDerp;
        if(!selecting)type=PacketType.Sync;

    }
    public Packet(DAT dat, String layer, String DerpID, String playerName ,int Index){
        type=PacketType.SkillSlotted;
        SkillToSlot=dat;
        serverPlayer=playerName;
        str=layer;
        derpPacket=DerpID;
        SlottableIndex=Index;
    }
    public Packet(DAT dat, String layer, String DerpID, String playerName){
        type=PacketType.SlotTriggered;
        SkillToSlot=dat;
        serverPlayer=playerName;
        str=layer;
        derpPacket=DerpID;
    }

    public enum PacketType{
        Sync,
        Request,
        Message,
        Derp,
        SelectingDerp,
        UnlockingSkill,
        SkillSlotted,
        SlotTriggered
    }
    public byte[] SerializeToByteArray() {
        try {
            return SerializeUtil.serialize(this);
        } catch (IOException e) {
            System.out.println("pre ooped");
            System.out.println(e);

            return new byte[256];
        }
    }

    public static Packet FromByteArray(byte[] bytes){
        try {
            return SerializeUtil.deserialize(bytes);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return new Packet("oopsies");
        }

    }
}
