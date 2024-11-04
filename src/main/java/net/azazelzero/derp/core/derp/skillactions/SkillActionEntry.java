package net.azazelzero.derp.core.derp.skillactions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class SkillActionEntry implements IForgeRegistryEntry<SkillActionEntry>, Serializable {
    private ResourceLocation registryName = null;


    @Override
    public SkillActionEntry setRegistryName(ResourceLocation name) {
        if (getRegistryName() != null)
            throw new IllegalStateException("Attempted to set registry name with existing registry name! New: " + name + " Old: " + getRegistryName());

        this.registryName = checkRegistryName(name.toString());
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public Class<SkillActionEntry> getRegistryType() {
        return SkillActionEntry.class;
    }

    ResourceLocation checkRegistryName(String name)
    {
        return GameData.checkPrefix(name, true);
    }
//    public boolean check(boolean invert){
//        return !invert;
//    }

    //    String name;
//    DatRequirement(String nameHere);
//    String NAME = null;

//    public static DatRequirement giveEntry(String name){
//        NAME=name;
//        return this;
//    }
//    DatRequirement setEntry(){}
    public SkillAction skillAction;
    public SkillAction skillAction(){
        try {
            return skillAction.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    };

    public SkillActionEntry(SkillAction skillAction){
        this.skillAction=skillAction;
    }

}
