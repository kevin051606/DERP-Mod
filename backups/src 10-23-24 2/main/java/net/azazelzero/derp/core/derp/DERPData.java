package net.azazelzero.derp.core.derp;

import java.util.ArrayList;

public class DERPData {

    ArrayList<ArrayList<ArrayList<Boolean>>> SkillUnlocked;
    ArrayList<DERPData.DATPostion> Slots;

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
