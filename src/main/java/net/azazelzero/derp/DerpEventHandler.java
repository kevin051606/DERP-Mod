package net.azazelzero.derp;

import net.azazelzero.derp.core.derp.Slottable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DerpEventHandler {
    public List chatEvents = new ArrayList<DerpEvent>();
    // Event type Map<EventName, List<>>
    public static Map<String,List<Slottable>> Slottables=new HashMap<>();
    // Map< Playername, ThePlayerSlots>
    public static Map<String, Slottable[]> PlayerSlottables=new HashMap<>();

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
