package net.azazelzero.derp.reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.azazelzero.derp.Main;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.azazelzero.derp.core.scripting.Global;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatapackScriptsLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();
    private static AddReloadListenerEvent eventGiven;

    public DatapackScriptsLoader(AddReloadListenerEvent event) {
        super(GSON, "scripts");
        this.eventGiven = event;
    }

    private <T> List<T> parseMyArray(JsonArray array, T T){
        List<T> list = new ArrayList<>();
        array.forEach((val) -> {
            if(val.getClass()==T) list.add((T) val);
        });
        return list;
    }
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> json, ResourceManager resourceManager, ProfilerFiller profilerFiller) {

        Logger logger = LogManager.getLogger(Main.MODID);
        logger.info("Loading Datapack");

        Globals globals = JsePlatform.standardGlobals();
        globals.set("Global", Global.getTable());

        ModForgeEvents.derpsLoaded.clear();
        json.forEach((key, value) -> {

        });

    }

    @Override
    public String getName() {
        return "DatapackLoader";
    }




}
