package net.azazelzero.derp.core.derp.requirements;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.concurrent.atomic.AtomicBoolean;

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
    public static class Statistics extends net.azazelzero.derp.core.derp.requirements.RequirementRegistryEntry {
        public Statistics(){}
        @Override
        public boolean check(String playerName) {
            AtomicBoolean returnValue = new AtomicBoolean(false);


            ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(playerName);
            if (player == null) return false;

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
    public static final RegistryObject<RequirementRegistryEntry> Statistics= DatRequirementRegistry.REQUIREMENTS.register("statistics", Requirements.Statistics::new);

//    public static final DeferredRegister<RequirementRegistryEntry> Reqs = DeferredRegister.create(DatRequirementRegistry.REQUIREMENTS, Main.MODID);


}
