package net.azazelzero.derp.core.derp;

public class DerpEventObject {
    public final String DERPName;
    public final int DerpPanel;
    public final int DerpX;
    public final int DerpY;

    public final String Name;
    public final Object ConditionOrDat;

    public DerpEventObject(String name, int derpPanel, int derpX, int derpY, String Name, Object ConditionOrDat){
        this.DERPName=name;
        DerpPanel = derpPanel;
        DerpX = derpX;
        DerpY = derpY;
        this.Name=Name;
        this.ConditionOrDat=ConditionOrDat;
    }

    public void exmaple(){

    }
}
