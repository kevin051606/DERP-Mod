package net.azazelzero.derp;

import net.azazelzero.derp.core.derp.Slottable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DerpEventHandler {
    public List chatEvents = new ArrayList<DerpEvent>();
    public Map<String,List<Slottable>> Slottables=new HashMap<>();
    public Map<String, Slottable[]> PlayerSlottables=new HashMap<>();

//    public iterate


    public class DerpEvent{
        public String type;
        public void function(){

        }
        public DerpEvent(String code){}
        //public DerpEvent(DATPosition datPosition){}
        public DerpEvent(){}

    }
}
