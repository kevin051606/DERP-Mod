package net.azazelzero.derp.core.derp;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.*;

public class DERP implements Serializable {

    public DAT[][][] DATs;
    public String name;
    public String displayName="";
    public String Description="";
    public String Layer;
    public String Texture="textures/block/dirt.png";
    public Float[] Color= {1.0f,1.0f,1.0f,1.0f};
    public Float[] FontColor = new Float[]{1.0F, 1.0F, 1.0F, 1.0F};
    public Float[] SkillUnlockedColor = new Float[]{1.0F, 1.0F, 1.0F, 1.0F};
    public Float[] SkillLockedColor = new Float[]{0.5F, 0.5F, .5F, .5F};
    public List<String> issue = null;
    public DERP(){
        DATs=new DAT[8][5][6];
    }
    public JsonElement grab(String field,JsonObject jsonFile){
        JsonElement returnElement=jsonFile.get(field);
        if (returnElement.isJsonNull()){issue.add(field); return JsonNull.INSTANCE;}
        return returnElement;
    }

    public DERP(DERP clone){
        this.DATs = clone.DATs;
    }

}
