package net.azazelzero.derp.core.derp;

import java.io.Serializable;

public class SkillReference implements Serializable {
    public final int Panel;
    public final int X;
    public final int Y;
    public final String LayerID;
    public final String DerpID;
    public final String SkillID;
    public SkillReference(int panel, int x, int y, String layerID, String derpID, String skillID){
        Panel = panel;
        X = x;
        Y = y;
        LayerID = layerID;
        DerpID = derpID;
        SkillID = skillID;
    }
    public  SkillReference(){
        SkillID = "nulled";
        Panel = -1;
        X = -1;
        Y = -1;
        LayerID = "null";
        DerpID = "null";
    }
}