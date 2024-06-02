package net.azazelzero.erc.client.event;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.ScriptableObject;
import net.azazelzero.erc.Main;
import net.azazelzero.erc.client.gui.SkillTree;
import net.azazelzero.erc.client.keybinds.KeyInit;
import net.azazelzero.erc.core.scripting.Storage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jline.utils.Log;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static com.ibm.icu.lang.UCharacter.GraphemeClusterBreak.T;

@Mod.EventBusSubscriber(modid = Main.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {
    private ClientForgeEvents(){}

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){
        if(KeyInit.ercKeyOpenUI.isDown()){
            System.out.println("Info: ");
            Minecraft.getInstance().setScreen(new SkillTree());

        }
    }

}
