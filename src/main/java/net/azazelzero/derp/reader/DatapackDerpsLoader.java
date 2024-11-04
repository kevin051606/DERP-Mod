package net.azazelzero.derp.reader;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.azazelzero.derp.core.derp.DAT;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.core.derp.requirements.DatRequirement;
import net.azazelzero.derp.core.derp.requirements.DatRequirementRegistry;
import net.azazelzero.derp.core.derp.requirements.RequirementRegistryEntry;
import net.azazelzero.derp.core.derp.skillactions.SkillAction;
import net.azazelzero.derp.core.derp.skillactions.SkillActionEntry;
import net.azazelzero.derp.core.derp.skillactions.SkillActionRegistry;
import net.azazelzero.derp.core.event.ModForgeEvents;

import net.azazelzero.derp.core.scripting.Global;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import com.google.gson.Gson;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;


import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DatapackDerpsLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();
    private static AddReloadListenerEvent eventGiven;

    public DatapackDerpsLoader(AddReloadListenerEvent event) {
        super(GSON, "derps");
        this.eventGiven = event;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> json, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Logger logger = LogManager.getLogger("derp");
        logger.info("Loading Datapack");

        net.azazelzero.derp.core.event.ModForgeEvents.derpsLoaded.clear();
        json.forEach((key, value) -> {
            System.out.println("ran");
            JsonObject jsonFile = value.getAsJsonObject();
            if (!jsonFile.get("Type").isJsonNull()) {
                switch (jsonFile.get("Type").getAsString()) {
                    case "Derp":
                        net.azazelzero.derp.core.derp.DERP derp = new net.azazelzero.derp.core.derp.DERP();
                        if(jsonFile.get("Layer")!=null) derp.Layer=jsonFile.get("Layer").getAsString();
                        derp.name = derp.grab("Id", jsonFile).getAsString();
                        derp.displayName = derp.grab("DisplayName", jsonFile).getAsString();
                        derp.Texture = derp.grab("UiBackground", jsonFile).getAsString();
                        derp.Description = derp.grab("Description", jsonFile).getAsString();
                        if (jsonFile.get("FontColor").isJsonArray()) {
                            derp.FontColor = new Float[4];
                            AtomicInteger skillIndex = new AtomicInteger();
                            derp.grab("FontColor", jsonFile).getAsJsonArray().forEach((v) -> {
                                derp.FontColor[skillIndex.get()] = v.getAsFloat() * 256.0F;
                                skillIndex.getAndIncrement();
                            });
                        }

                        if (jsonFile.get("UiColor").isJsonArray()) {
                            derp.Color = new Float[4];
                            AtomicInteger skillIndex = new AtomicInteger();
                            derp.grab("UiColor", jsonFile).getAsJsonArray().forEach((v) -> {
                                derp.Color[skillIndex.get()] = v.getAsFloat();
                                skillIndex.getAndIncrement();
                            });
                        }
                        if (jsonFile.get("SkillUnlockedColor").isJsonArray()) {
                            derp.SkillUnlockedColor = new Float[4];
                            AtomicInteger skillIndex = new AtomicInteger();
                            derp.grab("SkillUnlockedColor", jsonFile).getAsJsonArray().forEach((v) -> {
                                derp.SkillUnlockedColor[skillIndex.get()] = v.getAsFloat() ;
                                skillIndex.getAndIncrement();
                            });
                        }
                        if (jsonFile.get("SkillLockedColor").isJsonArray()) {
                            derp.SkillLockedColor = new Float[4];
                            AtomicInteger skillIndex = new AtomicInteger();
                            derp.grab("SkillLockedColor", jsonFile).getAsJsonArray().forEach((v) -> {
                                derp.SkillLockedColor[skillIndex.get()] = v.getAsFloat();
                                skillIndex.getAndIncrement();
                            });
                        }

                        AtomicInteger skillIndex = new AtomicInteger();
                        derp.grab("Skills", jsonFile).getAsJsonArray().forEach((Skill) -> {
                            DAT SKILL = new DAT();
                            JsonObject skillValue = Skill.getAsJsonObject();
                            if (skillValue.get("Description") != null) SKILL.Description = skillValue.get("Description").getAsString();
                            if (skillValue.get("Id") != null) SKILL.Id = skillValue.get("Id").getAsString();
                            if (skillValue.get("Icon") != null) SKILL.Icon = skillValue.get("Icon").getAsString();
                            if (skillValue.get("Slottable") != null) SKILL.Slottable = skillValue.get("Slottable").getAsBoolean();
                            if (SKILL.Slottable&&skillValue.get("SlottableEvent") != null) SKILL.SlottableEvent = skillValue.get("SlottableEvent").getAsString();
                            if (skillValue.get("Cooldown") != null) SKILL.Cooldown = skillValue.get("Cooldown").getAsInt();
                            if (skillValue.get("X") != null) SKILL.X = skillValue.get("X").getAsInt();
                            if (skillValue.get("Y") != null) SKILL.Y = skillValue.get("Y").getAsInt();
                            if (skillValue.get("Panel") != null) SKILL.Panel = skillValue.get("Panel").getAsInt();
                            if (SKILL.X < 0 | SKILL.Y < 0 | SKILL.Panel < 0) {
                                derp.issue.add("Issue with skill location at" + String.valueOf(skillIndex));
                            }

                            AtomicInteger skillActionIndex = new AtomicInteger();
                            boolean ifSkillArray = skillValue.get("SkillActions") != null;
                            if (ifSkillArray) {
                                SKILL.Actions = new SkillAction[skillValue.get("SkillActions").getAsJsonArray().size()];
                            }

                            if (ifSkillArray) {
                                skillValue.get("SkillActions").getAsJsonArray().forEach((SkillAction) -> {
                                    String name = SkillAction.getAsJsonObject().get("SkillType").getAsString();
                                    if (Objects.equals(name, "")) {
                                        name = "no:type";
                                    }

                                    if (!name.contains(":")) {
                                        name = "no:type";
                                    }

                                    int colon = name.indexOf(":");
                                    RegistryObject<net.azazelzero.derp.core.derp.skillactions.SkillActionEntry> val2 = RegistryObject.create(new ResourceLocation(name.substring(0, colon), name.substring(colon + 1)), SkillActionRegistry.location, "derp");
                                    if (val2.isPresent()) {
                                        SKILL.Actions[skillActionIndex.get()] = val2.get().skillAction();
                                        SKILL.Actions[skillActionIndex.get()].setParameters(SkillAction.getAsJsonObject());

                                    } else {
                                        List var10000 = derp.issue;
                                        int var10001 = skillActionIndex.get();
                                        String var8 = String.valueOf("" + var10001 + " at Skill index" + String.valueOf(skillIndex.get()));
                                        var10000.add("SkillAction at index" + var8);
                                    }

                                    skillActionIndex.getAndIncrement();
                                });
                            }

                            AtomicInteger RequirementIndex = new AtomicInteger();
                            boolean isArray = skillValue.get("Requirements") != null;
                            if (isArray) {
                                SKILL.Requirements = new DatRequirement[skillValue.get("Requirements").getAsJsonArray().size()];
                            }

                            if (isArray) {
                                skillValue.get("Requirements").getAsJsonArray().forEach((requirements) -> {
                                    String name = requirements.getAsJsonObject().get("RequirementType").getAsString();
                                    if (Objects.equals(name, "")) {
                                        name = "no:type";
                                    }

                                    if (!name.contains(":")) {
                                        name = "no:type";
                                    }

                                    int colon = name.indexOf(":");
                                    RegistryObject<RequirementRegistryEntry> val2 = RegistryObject.create(new ResourceLocation(name.substring(0, colon), name.substring(colon + 1)), DatRequirementRegistry.location, "derp");
                                    if (val2.isPresent()) {
                                        SKILL.Requirements[RequirementIndex.get()] = val2.get().DatFunction();
                                        SKILL.Requirements[RequirementIndex.get()].setParameters(requirements.getAsJsonObject());

                                    } else {
                                        List var10000 = derp.issue;
                                        int var10001 = skillActionIndex.get();
                                        String var9 = String.valueOf("" + var10001 + " at Skill index" + String.valueOf(skillIndex.get()));
                                        var10000.add("Skill Requirement at index" + var9);
                                    }

                                    RequirementIndex.getAndIncrement();
                                });
                            }
                            if(derp.issue==null)derp.DATs[SKILL.Panel][SKILL.X][SKILL.Y] = SKILL;
                            skillIndex.getAndIncrement();
                        });
                        if (derp.Layer == null) {
                            derp.Layer = "Default";
                        }

                        if (derp.issue != null) {
                            derp.issue.forEach((Issue) -> {
                                System.out.println();
                            });
                        }

                        if (derp.issue == null && ModForgeEvents.derpsLoaded.get(derp.Layer) == null) {
                            ModForgeEvents.derpsLoaded.put(derp.Layer, new HashMap());
                        }

                        if (derp.issue == null) {
                            (ModForgeEvents.derpsLoaded.get(derp.Layer)).put(derp.name, derp);
                            PrintStream var10000 = System.out;
                            DERP var10001 = (DERP) ((Map) ModForgeEvents.derpsLoaded.get(derp.Layer)).get(derp.name);
                            var10000.println("getting" + var10001.name);
                        }
                    case "layer":
                    default:
                }
            }
        });

        json.forEach((key, value) -> {
//                if (value.getAsJsonObject().get("name").getAsString() != "") {
//                    ModForgeEvents.derpsLoaded.put(value.getAsJsonObject().get("name").getAsString(), new DERP());
//                }
            Globals globals = JsePlatform.standardGlobals();
            globals.set("Global", Global.getTable());
            try {
                LuaValue chunk = globals.load(value.getAsJsonObject().get("code").getAsString());
                chunk.call();

            } catch (Exception e) {
                logger.warn(e.getMessage());

            }
        });
    };

//            }


//        ModForgeEvents.derpsLoaded.forEach((k,v)->{System.out.println(k);});
//        DERP example = new DERP();
//        if(!(ServerLifecycleHooks==null)) PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName("Dev")),new Message(new Packet("pain")));



    @Override
    public String getName() {
        return "DatapackLoader";
    }

}
