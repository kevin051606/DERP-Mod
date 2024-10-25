package net.azazelzero.derp.core.derp.skillactions;

import net.azazelzero.derp.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;

public  class SkillActionRegistry {

    private static boolean isInitialized = false;
    public static final ResourceLocation location=new ResourceLocation(net.azazelzero.derp.Main.MODID, "skillactions");
    public static final DeferredRegister<net.azazelzero.derp.core.derp.skillactions.SkillActionEntry> SKILL_ACTIONS = DeferredRegister.create(location, Main.MODID);

    public static void register(final IEventBus modEventBus){
        if (isInitialized) {
            throw new IllegalStateException("Ingredients already initialized");
        }
        
        new SkillActions();
        SKILL_ACTIONS.makeRegistry(SkillActionEntry.class, RegistryBuilder::new);
        SKILL_ACTIONS.register(modEventBus);
        isInitialized = true;
    }


}
