package net.azazelzero.derp.core.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.azazelzero.derp.Main;
import net.azazelzero.derp.client.event.ClientForgeEvents;
import net.azazelzero.derp.client.gui.SelectDerp;
import net.azazelzero.derp.client.gui.utils.Field;
import net.azazelzero.derp.core.derp.DERP;
import net.azazelzero.derp.core.derp.requirements.DatRequirement;
import net.azazelzero.derp.core.derp.skillactions.SkillAction;
import net.azazelzero.derp.core.derp.skillactions.SkillActionEntry;
import net.azazelzero.derp.core.event.ModForgeEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class MapMessageUpload {
    public final Packet msg;

    public MapMessageUpload(Packet msg) {
        this.msg = msg;
    }


    public MapMessageUpload(FriendlyByteBuf buffer) {
        Map<String, DERP[]> pain = buffer.readMap(FriendlyByteBuf::readUtf, SerializeUtil::deserializeAdd);
        System.out.println("asdad:" + pain.size());
        Map<String, Map<String, DERP>> conversionTherapy = new HashMap();
        pain.forEach((key, value) -> {
            conversionTherapy.put(key, new HashMap());

            for(int d = 0; d < value.length; ++d) {
                DERP derp = value[d];
                System.out.println("zzz" + d);
                ((Map)conversionTherapy.get(key)).put(derp.name, derp);
            }

        });
        this.msg = new Packet(conversionTherapy, false);
        this.msg.type=buffer.readEnum(Packet.PacketType.class);
        this.msg.serverPlayer=buffer.readUtf();

    }

    public void encode(FriendlyByteBuf buffer) {
        System.out.println("encoding");
        SelectDerpBufferWrtie(buffer);
    }
    public void SelectDerpBufferWrtie(FriendlyByteBuf buffer){
        Map<String, DERP[]> painInThheAss = new HashMap<>();
        ModForgeEvents.derpsLoaded.forEach((key1, value1) -> {
            System.out.println("bruh:" + value1.entrySet().size());
            painInThheAss.put(key1, new DERP[value1.entrySet().size()]);
            AtomicInteger counter = new AtomicInteger();
            value1.forEach((key, value) -> {
                (painInThheAss.get(key1))[counter.get()] = value;
                PrintStream var10000 = System.out;
                String var10001 = ((DERP[])painInThheAss.get(key1))[counter.get()].name;
                var10000.println("sss:" + var10001);
                counter.getAndIncrement();
            });
        });
        painInThheAss.entrySet().forEach((a) -> {
            System.out.println("dawg" + (String)a.getKey());
        });
        buffer.writeMap(painInThheAss, FriendlyByteBuf::writeUtf, SerializeUtil::serializeAdd);
        buffer.writeEnum(msg.type);
        buffer.writeUtf(msg.serverPlayer);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        new AtomicBoolean(false);
        ((NetworkEvent.Context)ctx.get()).enqueueWork(() -> {
            ServerPlayer serverPlayer= ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(msg.serverPlayer);
            if (serverPlayer==null||!serverPlayer.hasPermissions(3)) return;
            System.out.println("name of before" + this.msg.type.name());
                ModForgeEvents.derpsLoaded.clear();
                System.out.println(this.msg.derpSync.isEmpty());
                ModForgeEvents.derpsLoaded.putAll(this.msg.derpSync);
                ModForgeEvents.layers.clear();
                ModForgeEvents.derpsLoaded.forEach((key, value) -> {
                    ModForgeEvents.layers.add(key);
                    System.out.println("dude:" + key);
                });
                ModForgeEvents.derpsLoaded.forEach((d, l) -> {
                    System.out.println("layer of derps " + d);
                    l.forEach((k, v) -> {
                        System.out.println("name of derps " + v.name);
                        System.out.println("name of class " + v.getClass().getName());
                    });
                });
                System.out.println("name of derps has been ran");
            MinecraftServer mcServerInstance = ServerLifecycleHooks.getCurrentServer();
            String folderName = mcServerInstance.getServerDirectory().getAbsolutePath();
            String worldName=mcServerInstance.getWorldData().getLevelName();
            if (mcServerInstance.isSingleplayer()){
                DatapackDirectory=folderName.substring(0,folderName.length()-1)+"saves\\"+worldName+"\\datapacks\\default\\data\\default\\derps\\";
            } else {
                DatapackDirectory=folderName.substring(0,folderName.length()-1)+worldName+"\\datapacks\\default\\data\\default\\derps\\";

            }
            final File file = new File(DatapackDirectory+"\\pack.mcmeta");
            final File parent_directory = file.getParentFile();
            if (null != parent_directory)
            {
                parent_directory.mkdirs();
                //add pack.mcmeta
                try {
                    String sDatapackDirectory;
                    if (mcServerInstance.isSingleplayer()){
                        sDatapackDirectory=folderName.substring(0,folderName.length()-1)+"saves\\"+worldName+"\\datapacks\\default\\";
                    } else {
                        sDatapackDirectory=folderName.substring(0,folderName.length()-1)+worldName+"\\datapacks\\default\\";

                    }
                    final File sfile = new File(sDatapackDirectory+"\\pack.mcmeta");
                    FileWriter fw =new FileWriter(sfile);
                    fw.write(
                            ""+
                                    "{\n" +
                                    "  \"pack\": {\n" +
                                    "    \"pack_format\": 12,\n" +
                                    "    \"description\": \"\"\n" +
                                    "  }\n" +
                                    "}\n"
                    );
                    fw.close();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            for (DERP derp : ModForgeEvents.derpsLoadedList) {
                JsonObject derpAsJson=new JsonObject();
                derpAsJson.addProperty("Type","Derp");
                derpAsJson.addProperty("Id",derp.name);
                derpAsJson.addProperty("DisplayName",derp.displayName);
                derpAsJson.addProperty("Description",derp.Description);
                derpAsJson.addProperty("Layer",derp.Layer);
                derpAsJson.addProperty("Icon",derp.Icon);
                derpAsJson.addProperty("UiBackground",derp.Texture);
                derpAsJson.add("UiColor",arrayToJsonArray(derp.Color));
                derpAsJson.add("FontColor",arrayToJsonArrayFont(derp.FontColor));
                derpAsJson.add("SkillUnlockedColor",arrayToJsonArray(derp.SkillUnlockedColor));
                derpAsJson.add("SkillLockedColor",arrayToJsonArray(derp.SkillLockedColor));
                // panels
                JsonArray panelArray= new JsonArray();
                for (DatRequirement[] panel : derp.Panels) {
                    JsonObject jsonPanel=new JsonObject();
                    JsonArray requirements = new JsonArray();
                    if (panel==null) continue;
                    for (DatRequirement datRequirement : panel) {
                        requirements.add(datRequirement.getParameters());
                    }
                    if(!requirements.isEmpty())jsonPanel.add("Requirements",requirements);
                    panelArray.add(jsonPanel);
                }
                derpAsJson.add("Panels",panelArray);
                JsonArray Skills= new JsonArray();
                derp.foreachDat(dat->{
                    if (dat==null) return;
                    JsonObject Skill= new JsonObject();
                    Skill.addProperty("Panel",dat.Panel);
                    Skill.addProperty("X",dat.X);
                    Skill.addProperty("Y",dat.Y);
                    Skill.addProperty("Id",dat.Id);
                    Skill.addProperty("Icon",dat.Icon);
                    Skill.addProperty("Slottable",dat.Slottable);
                    if (dat.Slottable){
                        Skill.addProperty("Cooldown",dat.Cooldown);
                        Skill.addProperty("SlottableEvent",dat.SlottableEvent);
                    }
                    //actions
                    JsonArray actions=new JsonArray();
                    for (SkillAction action : dat.Actions) {
                        actions.add(action.getParameters());
                    }
                    JsonArray requirements=new JsonArray();
                    for (DatRequirement requirement : dat.Requirements) {
                        requirements.add(requirement.getParameters());
                    }
                    Skill.add("SkillActions",actions);
                    Skill.add("Requirements",requirements);
                    Skills.add(Skill);
                });
                derpAsJson.add("Skills",Skills);
                writeFile(derp.name,derpAsJson.toString());
            }
//                writeFile(name, text);
        });
        return true;
    }
    public String DatapackDirectory;
    public void writeFile(String name, String text){
        final File file = new File(DatapackDirectory+"\\"+name+".json");
        try {
            BufferedWriter fw= new BufferedWriter(new FileWriter(file));
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            Main.LOGGER.error(name+" couldnt be added because of error:\n"+e.getMessage());
        }
    }
    public JsonArray arrayToJsonArray(Float[] array){
        JsonArray returnArray = new JsonArray();
        for (Float v : array) {
            returnArray.add(v);
        }
        return returnArray;
    }
    public JsonArray arrayToJsonArrayFont(Float[] array){
        JsonArray returnArray = new JsonArray();
        for (Float v : array) {
            returnArray.add(v/256);
        }
        return returnArray;
    }

}
