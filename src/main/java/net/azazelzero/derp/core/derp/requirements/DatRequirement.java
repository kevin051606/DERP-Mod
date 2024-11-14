package net.azazelzero.derp.core.derp.requirements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.azazelzero.derp.client.gui.utils.Field;

import java.io.Serializable;
import java.util.List;

public abstract class DatRequirement implements Serializable {
    public String StringParameters;

    public void setParameters(JsonObject fieldsAndKeys){
        this.StringParameters=fieldsAndKeys.toString();
    }
    public JsonObject getParameters(){
        Gson gson = new Gson();
        return gson.fromJson(StringParameters, JsonObject.class);
    };
    public abstract void init();
    public abstract List<Field> getFields();
    public abstract String[] DescriptionString(String playerName);
    public abstract boolean check(String playerName);
}
