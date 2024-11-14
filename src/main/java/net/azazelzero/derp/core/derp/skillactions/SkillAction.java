package net.azazelzero.derp.core.derp.skillactions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.azazelzero.derp.client.gui.utils.Field;

import java.io.Serializable;
import java.util.List;

public abstract  class SkillAction implements Serializable {
    public String StringParameters;

    public boolean CallOnSync =false;
    public void setParameters(JsonObject fieldsAndKeys){this.StringParameters=fieldsAndKeys.toString();}
    public JsonObject getParameters(){
        Gson gson = new Gson();
        return gson.fromJson(StringParameters, JsonObject.class);
    };
    public abstract List<Field> getFields();
    public abstract void action(String playerName);
    public abstract void onRemove(String playerName);
}
