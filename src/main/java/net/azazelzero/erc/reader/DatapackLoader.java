package net.azazelzero.erc.reader;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.ScriptableObject;
import net.azazelzero.erc.Main;
import net.azazelzero.erc.core.scripting.Storage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.common.Mod;
import com.google.gson.Gson;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class DatapackLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();
    private static AddReloadListenerEvent eventGiven;

    public DatapackLoader(AddReloadListenerEvent event) {
        super(GSON, "derps");
        this.eventGiven = event;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> json, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        System.out.println("<Insert racist Comment>");
        json.forEach((key, value) -> {
            System.out.println(value.toString());
            System.out.println(value.getAsJsonObject().get("name").getAsString());
            Context cx = Context.enter();
            try {

                Scriptable scope = cx.initStandardObjects();
                System.out.println("<Insert racist Commenat>");
                ScriptableObject.defineClass(scope, Storage.class);

                Object result = cx.evaluateString(scope,value.getAsJsonObject().get("code").getAsString(),"",1,null);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } finally {
                // Exit from the context.

                Context.exit();
            }
        });

    }

    @Override
    public String getName() {
        return "idk";
    }
}
