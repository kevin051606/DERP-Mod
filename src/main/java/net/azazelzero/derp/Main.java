package net.azazelzero.derp;

import com.mojang.logging.LogUtils;
import net.azazelzero.derp.core.data.DerpPlayerData;
import net.azazelzero.derp.core.derp.requirements.DatRequirementRegistry;
import net.azazelzero.derp.core.derp.skillactions.SkillActionRegistry;
import net.azazelzero.derp.core.event.Capabilities;
import net.azazelzero.derp.core.event.ModEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
        eventBus.addListener(this::setup);

        eventBus.addListener(this::onRegisterCapabilities);
//        eventBus.addListener(this::setup);

//        ModEvents.setup();

        MinecraftForge.EVENT_BUS.register(this);

    }

    // Event is on the mod event bus only on the physical clie

    private void setup(final FMLCommonSetupEvent event){
        MinecraftForge.EVENT_BUS.register(new Capabilities());
    }
    public void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(DerpPlayerData.class);
    }


}
