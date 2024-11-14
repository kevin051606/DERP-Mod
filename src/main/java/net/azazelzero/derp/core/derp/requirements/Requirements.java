package net.azazelzero.derp.core.derp.requirements;

import com.google.gson.JsonObject;
import net.azazelzero.derp.client.gui.utils.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Requirements{
//    @Override
//    public DatRequirement(String nameHere) {
//Stats
//    }
//
//    public enum RequirementsType{
//        Skill,
//        Statistic,
//        Item
//    }
//    public final RequirementsType requirementType;
//    public String Derp;
//    public final String PlayerName;
//    public String itemID;
//    public DERPData.DATPostion Skill;
//    public String StatisticName;
//    public int StatisticValue=-100;
//    public String Item;
//
//    public DatRequirement(RequirementsType t, String playerName){
//        requirementType=t;
//        PlayerName=playerName;
//    }
//
//    public final boolean check(){
//        AtomicBoolean returnValue = new AtomicBoolean(false);
//
//        ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(PlayerName);
//        if (player == null) return false;
//
//        switch (requirementType){
//            case Statistic:
//                if (StatisticValue == -100) return false;
//                if (StatisticName == null) return false;
//                Registry.CUSTOM_STAT.entrySet().forEach((k)->{
////                        if(k.getValue().getPath().equals(StatisticName)) System.out.println(player.getStats().getValue(Stats.CUSTOM.get(k.getValue())));
//                    if(k.getValue().getPath().equals(StatisticName)) returnValue.set(player.getStats().getValue(Stats.CUSTOM.get(k.getValue())) > StatisticValue);
//                });
//                break;
//            case Skill:
//                player.getCapability(DerpPlayerDataProvider.DERP_DATA).isPresent();
//                break;
//            case Item:
//                net.minecraft.world.item.Item item = Registry.ITEM.get(new ResourceLocation("dirt"));
//                System.out.println(item.getDefaultInstance().getDisplayName().getString());
//
//                int slot= player.getInventory().findSlotMatchingItem(item.getDefaultInstance());
//                System.out.println(slot);
//                if (slot>-1) return player.getInventory().getItem(slot).getCount() > 4;;
//
//
////                    break;
//        }
//
//        return returnValue.get();
//    }
    public static class Statistics extends DatRequirement{
        public Statistics(){}

    @Override
    public void init() {
        Objects.requireNonNull(Minecraft.getInstance().getConnection()).send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
    }
    public List<Field> getFields(){
        ArrayList<Field> l = new ArrayList<Field>();
        l.add(new Field("value",""));
        l.add(new Field("name",""));
        l.add(new Field("displayName",""));
        return l;
    };

    @Override
    public String[] DescriptionString(String playerName) {
        JsonObject Parameters = getParameters();


        String[] s = new String[1];
        if(!Parameters.has("value")) return s;
        if(!Parameters.has("name")) return s;
        if(!Parameters.has("displayName")) return s;
        int StatisticValue = Parameters.get("value").getAsInt();
        String StatisticName = Parameters.get("name").getAsString();
        String DisplayName=Parameters.get("displayName").getAsString();

        if (StatisticValue == -100) return s;
        if (StatisticName == null) return s;

        AtomicReference<String> returnString= new AtomicReference<>("");
        Registry.CUSTOM_STAT.entrySet().forEach((k) -> {
            //                        if(k.getValue().getPath().equals(StatisticName)) System.out.println(player.getStats().getValue(Stats.CUSTOM.get(k.getValue())));
            if (k.getValue().getPath().equals(StatisticName)){
                int statValue = Minecraft.getInstance().player.getStats().getValue(Stats.CUSTOM.get(k.getValue()));
                returnString.set(DisplayName+": "+statValue + " / " + StatisticValue);

            }

        });
        s[0]=returnString.get();
        return s;
    }

    @Override
        public boolean check(String playerName) {
            JsonObject Parameters = getParameters();
            AtomicBoolean returnValue = new AtomicBoolean(false);

            ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(playerName);
            if (player == null) return false;
            if(!Parameters.has("value")) return  false;
            if(!Parameters.has("name")) return  false;

            int StatisticValue = Parameters.get("value").getAsInt();
            String StatisticName = Parameters.get("name").getAsString();

            if (StatisticValue == -100) return false;
            if (StatisticName == null) return false;
            Registry.CUSTOM_STAT.entrySet().forEach((k) -> {
    //                        if(k.getValue().getPath().equals(StatisticName)) System.out.println(player.getStats().getValue(Stats.CUSTOM.get(k.getValue())));
                if (k.getValue().getPath().equals(StatisticName))
                    returnValue.set(player.getStats().getValue(Stats.CUSTOM.get(k.getValue())) > StatisticValue);
            });
            return returnValue.get();
        }
    }
    public static final RegistryObject<RequirementRegistryEntry> Statistics=
            DatRequirementRegistry.REQUIREMENTS.register("statistics", () -> new RequirementRegistryEntry(new Requirements.Statistics()));

//    public static final DeferredRegister<RequirementRegistryEntry> Reqs = DeferredRegister.create(DatRequirementRegistry.REQUIREMENTS, Main.MODID);


}
