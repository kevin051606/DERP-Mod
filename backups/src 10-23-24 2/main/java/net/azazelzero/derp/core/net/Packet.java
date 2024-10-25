package net.azazelzero.derp.core.net;


import net.azazelzero.derp.core.derp.DERP;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;


public class Packet implements Serializable {
    public Packet.PacketType type;
    public String str;
    public String derpPacket;
    public static Map<String, Map<String, net.azazelzero.derp.core.derp.DERP>> derpSync;
    public String serverPlayer;
    public Packet(String str){
        type= PacketType.Message;
        this.str=str;
    }
    public Packet(int DerpPanel, int DerpX, int DerpY){
//        type="SkillUnlock";
    }
    public Packet(int DerpPanel, int DerpX, int DerpY, int slot){
//        type="AbilitySlot";
    }
    public Packet(int DerpPanel, int DerpX, int DerpY, String keyPressed){
//        type="KeyEvent";
    }
    public Packet(net.azazelzero.derp.core.derp.DERP[] derp){
        type=PacketType.Derp;
    }
    public Packet(String playername, boolean f){
        type=PacketType.Request;
        serverPlayer=playername;
    }

    //downstream

    public Packet(int DerpPanel, int DerpX, int DerpY, String PlayerName, int bs){
//        type="AbilityActivated";
    }

    public Packet(Map<String, Map<String, DERP>> derps, boolean selecting){
        derpSync=derps;
        type=PacketType.SelectingDerp;
    }

    public enum PacketType{
        Sync,
        Request,
        Message,
        Derp,
        SelectingDerp
    }
    public byte[] SerializeToByteArray() {
        try {
            return SerializeUtil.serialize(this);
        } catch (IOException e) {
            return new byte[256];
        }
    }

    public static Packet FromByteArray(byte[] bytes){
        try {
            return SerializeUtil.deserialize(bytes);
        } catch (IOException | ClassNotFoundException e) {
            return new Packet("oopsies");
        }

    }
}
