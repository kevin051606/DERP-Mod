package net.azazelzero.derp.core.derp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DERPData implements Serializable {

    public List<String> SkillsUnlocked = new ArrayList<>();
    public DERPData.DATPostion[] Slots=null;
    public String derpId;
    public String layerId;
    public DERPData(){}


    public static class DATPostion{

        public final int Panel;
        public final int X;
        public final int Y;

        public DATPostion(int panel, int x, int y) {
            Panel = panel;
            X = x;
            Y = y;
        }
    }

}
