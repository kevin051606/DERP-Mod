package net.azazelzero.derp.core.derp.requirements;

import net.azazelzero.derp.Main;
import net.azazelzero.derp.core.derp.requirements.RequirementRegistryEntry;
import net.azazelzero.derp.core.derp.requirements.Requirements;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public  class DatRequirementRegistry{

    private static boolean isInitialized = false;
    public static final ResourceLocation location = new ResourceLocation("derp", "requirements");
    public static final DeferredRegister<net.azazelzero.derp.core.derp.requirements.RequirementRegistryEntry> REQUIREMENTS;

    public DatRequirementRegistry() {
    }

    public static void register(IEventBus modEventBus) {
        if (isInitialized) {
            throw new IllegalStateException("Ingredients already initialized");
        } else {
            new Requirements();
            REQUIREMENTS.makeRegistry(RequirementRegistryEntry.class, RegistryBuilder::new);
            REQUIREMENTS.register(modEventBus);
            isInitialized = true;
        }
    }

    static {
        REQUIREMENTS = DeferredRegister.create(location, "derp");
    }

}
