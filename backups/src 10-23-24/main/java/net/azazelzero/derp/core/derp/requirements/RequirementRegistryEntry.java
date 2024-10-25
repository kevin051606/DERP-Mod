package net.azazelzero.derp.core.derp.requirements;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public abstract class RequirementRegistryEntry  implements IForgeRegistryEntry<RequirementRegistryEntry> {
    private ResourceLocation registryName = null;


    @Override
    public RequirementRegistryEntry setRegistryName(ResourceLocation name) {
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
    public Class<RequirementRegistryEntry> getRegistryType() {
        return RequirementRegistryEntry.class;
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
    public JsonObject Parameters;
    public void setParameters(JsonObject fieldsAndKeys){
        this.Parameters=fieldsAndKeys;
    }
    public abstract boolean check(String playerName);
//    public static DatRequirement giveEntry(String name){
//        NAME=name;
//        return this;
//    }
//    DatRequirement setEntry(){}



}
