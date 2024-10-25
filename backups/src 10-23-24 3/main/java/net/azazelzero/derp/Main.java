package net.azazelzero.derp;

import com.mojang.logging.LogUtils;
import net.azazelzero.derp.core.derp.requirements.DatRequirementRegistry;
import net.azazelzero.derp.core.derp.skillactions.SkillActionRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("derp")
public class Main
{
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "derp";


    public Main()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        DatRequirementRegistry.register(eventBus);
        SkillActionRegistry.register(eventBus);
//        eventBus.addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
//        eventBus.register(this);
        MinecraftForge.EVENT_BUS.register(this);

    }

    // Event is on the mod event bus only on the physical clie




}
