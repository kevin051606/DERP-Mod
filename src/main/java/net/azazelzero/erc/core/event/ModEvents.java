package net.azazelzero.erc.core.event;

import net.azazelzero.erc.Main;
import net.azazelzero.erc.client.keybinds.KeyInit;
import net.azazelzero.erc.reader.DatapackLoader;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber(modid = Main.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {


    private ModEvents(){}

    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event){
        System.out.println("info +: ");
        event.addListener(new DatapackLoader(event));
    }


}
