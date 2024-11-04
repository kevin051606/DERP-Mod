package net.azazelzero.derp.core.derp.requirements;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.azazelzero.derp.core.net.SerializableResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;
import org.openjdk.nashorn.internal.parser.JSONParser;

import java.io.Serializable;

public class RequirementRegistryEntry  implements IForgeRegistryEntry<RequirementRegistryEntry> {
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
    public DatRequirement datRequirement;
    public DatRequirement DatFunction(){
        if (datRequirement!=null) {
            try {
                return datRequirement.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return new Requirements.Statistics();
            }
        }
        return new Requirements.Statistics();
    };

    public RequirementRegistryEntry(DatRequirement datRequirement){
        this.datRequirement=datRequirement;
    }

}
